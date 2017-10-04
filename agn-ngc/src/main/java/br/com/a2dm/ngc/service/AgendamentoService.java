package br.com.a2dm.ngc.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.AgendamentoExame;
import br.com.a2dm.ngc.entity.Dominio;
import br.com.a2dm.ngc.entity.Exame;
import br.com.a2dm.ngc.entity.Horario;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.entity.log.AgendamentoLog;
import br.com.a2dm.ngc.entity.vo.RetornoVo;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.log.AgendamentoLogService;

public class AgendamentoService extends A2DMHbNgc<Agendamento>
{
	private static AgendamentoService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	public static final int JOIN_SERVICO = 4;
	
	public static final int JOIN_CONVENIO = 8;
	
	public static final int JOIN_PACIENTE = 16;
	
	public static final int JOIN_PACIENTE_ESTADO = 32;
	
	public static final int JOIN_AGENDAMENTO_EXAME = 64;
	
	public static final int JOIN_EXAME = 128;
	
	private JSFUtil util = new JSFUtil();	
	
	//SITUACOES DO AGENDAMENTO
	public static final int SITUACAO_AGENDADA       = 1; //AZUL     #3187bf
	public static final int SITUACAO_PRESENTE       = 2; //ROXO     #bf00ff
	public static final int SITUACAO_EM_ATENDIMENTO = 3; //LARANJA  #ffbf00
	public static final int SITUACAO_CONCLUIDA      = 4; //VERDE    #378006
	public static final int SITUACAO_CANCELADA      = 5;
	

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static AgendamentoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new AgendamentoService();
		}
		return instancia;
	}
	
	public AgendamentoService()
	{
		adicionarFiltro("idAgendamento", RestritorHb.RESTRITOR_EQ,"idAgendamento");
		adicionarFiltro("idClinicaProfissional", RestritorHb.RESTRITOR_EQ,"idClinicaProfissional");
		adicionarFiltro("idSituacao", RestritorHb.RESTRITOR_EQ,"idSituacao");
		adicionarFiltro("datAgendamento", RestritorHb.RESTRITOR_EQ,"datAgendamento");
		adicionarFiltro("datAgendamento", RestritorHb.RESTRITOR_DATA_INICIAL,"filtroMap.datAgendamentoInicio");
		adicionarFiltro("datAgendamento", RestritorHb.RESTRITOR_DATA_FINAL,"filtroMap.datAgendamentoFim");		
		adicionarFiltro("idPaciente", RestritorHb.RESTRITOR_EQ,"idPaciente");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("flgConfirmado", RestritorHb.RESTRITOR_EQ, "flgConfirmado");
		adicionarFiltro("cpfPaciente", RestritorHb.RESTRITOR_EQ, "cpfPaciente");
		adicionarFiltro("nomPaciente", RestritorHb.RESTRITOR_LIKE, "nomPaciente");
	}
	
	@Override
	protected void validarInserir(Session sessao, Agendamento vo) throws Exception
	{
		//VERIFICA SE O HORARIO INFORMADO ESTA DENTRO DO INTERVALO PERMITIDO DE AGENDAMENTO - CONFIGURACOES / HORARIO
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(vo.getDatAgendamento());
		Integer diaAgendamento = calendar.get(Calendar.DAY_OF_WEEK);
		
		Horario horario = new Horario();
		horario.setIdClinicaProfissional(vo.getIdClinicaProfissional());
		horario.setNumHorario(new BigInteger(Integer.toString(diaAgendamento)));
		horario = HorarioService.getInstancia().get(sessao, horario, 0);
		
		int hr1 = Integer.parseInt(vo.getHorInicio().replace(":", ""));
		int hr2 = Integer.parseInt(vo.getHorFim().replace(":", ""));
		
		int hrBd1 = Integer.parseInt(horario.getHorInicio().replace(":", ""));
		int hrBd2 = Integer.parseInt(horario.getHorFim().replace(":", ""));
		
		if(hr1 < hrBd1 || hr2 > hrBd2)
		{
			throw new Exception("O agendamento está fora do horário permitido! O horário de agendamento para este dia é: " + horario.getHorInicio() + " a " +horario.getHorFim());
		}
		
		//LISTA OS AGENDAMENTOS ATIVOS DO DIA E VERIFICA SE EXISTE ALGUM AGENDAMENTO CADASTRADO NO INTERVALO DE HORA INFORMADO
		Agendamento agendamento = new Agendamento();
		agendamento.setIdClinicaProfissional(vo.getIdClinicaProfissional());
		agendamento.setDatAgendamento(vo.getDatAgendamento());
		agendamento.setFlgAtivo("S");
		
		List<Agendamento> lista = this.pesquisar(sessao, agendamento, 0);
				
		RetornoVo retorno = UtilFuncions.checkHoraAgendamento(lista, vo.getHorInicio(), vo.getHorFim());
		
		if(retorno.isFlgRetorno())
		{
			throw new Exception(retorno.getMensagem());
		}
	}
	
	@Override
	protected void validarAlterar(Session sessao, Agendamento vo) throws Exception
	{
		//LISTA TODOS OS AGENDAMENTOS DO DIA
		Agendamento agendamento = new Agendamento();
		agendamento.setIdClinicaProfissional(vo.getIdClinicaProfissional());
		agendamento.setDatAgendamento(vo.getDatAgendamento());	
		
		List<Agendamento> lista = this.pesquisar(sessao, agendamento, 0);
		List<Agendamento> result = null;
		
		if(lista != null)
		{
			result = new ArrayList<Agendamento>();
			
			for (Agendamento obj : lista)
			{
				if(obj.getIdAgendamento().intValue() != vo.getIdAgendamento().intValue())
				{
					result.add(obj);
				}
			}
		}
		
		RetornoVo retorno = UtilFuncions.checkHoraAgendamento(result, vo.getHorInicio(), vo.getHorFim());
		
		if(retorno.isFlgRetorno())
		{
			throw new Exception(retorno.getMensagem());
		}
	}
	
	public Agendamento concluirAtendimento(Agendamento vo, List<Exame> lista) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = concluirAtendimento(sessao, vo, lista);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}

	public Agendamento concluirAtendimento(Session sessao, Agendamento vo, List<Exame> lista) throws Exception
	{
		//ALTERANDO O AGENDAMENTO				
		Agendamento agendamento = new Agendamento();
		agendamento.setIdAgendamento(vo.getIdAgendamento());
		agendamento = this.get(agendamento, 0);
		
		agendamento.setVlrAgendamento(new Double(vo.getVlrAgendamentoFormatado().replace(".", "").replace(",", ".")));
		agendamento.setVlrDesconto(new Double(vo.getVlrDescontoFormatado().replace(".", "").replace(",", ".")));
		agendamento.setDesAnamnese(vo.getDesAnamnese());
		agendamento.setDesPrescricao(vo.getDesPrescricao());
		
		agendamento.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_CONCLUIDA)));
		
		sessao.merge(agendamento);
		
		//INSERINDO OS EXAMES
		if(lista != null)
		{
			for (Exame exame : lista) 
			{
				AgendamentoExame agendamentoExame = new AgendamentoExame();
				agendamentoExame.setIdExame(exame.getIdExame());
				agendamentoExame.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				agendamentoExame.setIdAgendamento(agendamento.getIdAgendamento());
				
				AgendamentoExameService.getInstancia().inserir(sessao, agendamentoExame);
			}
		}
		
		//INSERINDO O LOG
		AgendamentoLog log = new AgendamentoLog();
		log.setIdAgendamento(vo.getIdAgendamento());
		log.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		log.setDatCadastro(new Date());
		log.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_CONCLUIDA)));
		log.setDesOperacao("MACANDO O AGENDAMENTO COMO CONCLUÍDO");
		
		AgendamentoLogService.getInstancia().inserir(sessao, log);
		
		return vo;
	}
	
	public Agendamento inativar(Agendamento vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = inativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("S");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}

	public Agendamento inativar(Session sessao, Agendamento vo) throws Exception
	{
		this.validarInativar(sessao, vo);
		
		Agendamento agendamento = new Agendamento();
		agendamento.setIdAgendamento(vo.getIdAgendamento());
		agendamento = this.get(sessao, agendamento, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	private void validarInativar(Session sessao, Agendamento vo) throws Exception
	{
		Agendamento agendamento = new Agendamento();
		agendamento.setIdAgendamento(vo.getIdAgendamento());		
		agendamento = this.get(sessao, agendamento, 0);
		
		if(agendamento != null
				
				&& agendamento.getIdSituacao().intValue() == SITUACAO_CONCLUIDA)
		{
			throw new Exception("Não foi possível inativar o agendamento pois o mesmo já foi concluído!");
		}	
	}
	
	public Agendamento confirmar(Agendamento vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = confirmar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgConfirmado("N");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Agendamento confirmar(Session sessao, Agendamento vo) throws Exception
	{
		vo.setFlgConfirmado("S");
		vo.setIdUsuarioConfirm(util.getUsuarioLogado().getIdUsuario());
		vo.setDatConfirmacao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	public Agendamento marcarPresenca(Agendamento vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = marcarPresenca(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_AGENDADA)));
			vo.setHorPresenca(null);
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Agendamento marcarPresenca(Session sessao, Agendamento vo) throws Exception
	{
		//VALIDACAO DA SITUACAO DO AGENDAMENTO
		Agendamento agendamento = new Agendamento();
		agendamento.setIdAgendamento(vo.getIdAgendamento());
		agendamento = this.get(agendamento, 0);
		
		if(agendamento != null
				&& agendamento.getIdSituacao() != null
				&& agendamento.getIdSituacao().intValue() != SITUACAO_AGENDADA)
		{
			throw new Exception("Só podem ser marcados como presente os agendamentos com situação agendada! Situação do agendamento: " + agendamento.getDesSituacao());
		}
		
		//ALTERAR SITUACAO DO AGENDAMENTO PARA PRESENTE
		vo.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_PRESENTE)));
		vo.setHorPresenca(new SimpleDateFormat("HH:mm").format(new Date()));
		
		sessao.merge(vo);
		
		//INSERIR REGISTRO DE LOG DO AGENDAMENTO
		AgendamentoLog log = new AgendamentoLog();
		log.setIdAgendamento(vo.getIdAgendamento());
		log.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		log.setDatCadastro(new Date());
		log.setDesOperacao("MACANDO O AGENDAMENTO COMO PRESENTE");
		log.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_PRESENTE)));
		
		AgendamentoLogService.getInstancia().inserir(sessao, log);
		
		return vo;
	}
	
	public Agendamento atenderPaciente(Agendamento vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = atenderPaciente(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_PRESENTE)));
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Agendamento atenderPaciente(Session sessao, Agendamento vo) throws Exception
	{
		//VALIDACAO DA SITUACAO DO AGENDAMENTO
		Agendamento agendamento = new Agendamento();
		agendamento.setIdAgendamento(vo.getIdAgendamento());
		agendamento = this.get(agendamento, 0);
		
		if(agendamento != null
				&& agendamento.getIdSituacao() != null
				&& agendamento.getIdSituacao().intValue() != SITUACAO_PRESENTE)
		{
			throw new Exception("Só podem ser colocado em atendimento os agendamentos com situação presente! Situação do agendamento: " + agendamento.getDesSituacao());
		}
		
		//NAO PODE COLOCAR EM ATENDIMENTO SE JA POSSUIR PACIENTE EM ATENDIMENTO
		Agendamento agendamentoAtendimento = new Agendamento();
		agendamentoAtendimento.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		agendamentoAtendimento.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)));		
		
		List<Agendamento> listaAgendamentosAtendimento = this.pesquisar(sessao, agendamentoAtendimento, 0);
		
		if(listaAgendamentosAtendimento != null
				&& listaAgendamentosAtendimento.size() > 0)
		{
			throw new Exception("Não foi possível colocar o paciente em atendimento pois já existe um atendimento para o profissional!");
		}		
		
		//ALTERAR SITUACAO DO AGENDAMENTO PARA EM ATENDIMENTO
		vo.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)));
		sessao.merge(vo);
		
		//INSERIR REGISTRO DE LOG DO AGENDAMENTO
		AgendamentoLog log = new AgendamentoLog();
		log.setIdAgendamento(vo.getIdAgendamento());
		log.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		log.setDatCadastro(new Date());
		log.setDesOperacao("MACANDO O AGENDAMENTO COMO EM ATENDIMENTO");
		log.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)));
		
		AgendamentoLogService.getInstancia().inserir(sessao, log);
		
		return vo;
	}
	
	public Agendamento vincularPaciente(Agendamento vo, Paciente paciente) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = vincularPaciente(sessao, vo, paciente);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Agendamento vincularPaciente(Session sessao, Agendamento vo, Paciente paciente) throws Exception
	{
		//VALIDACAO DA SITUACAO DO AGENDAMENTO
		Agendamento agendamento = new Agendamento();
		agendamento.setIdAgendamento(vo.getIdAgendamento());
		agendamento = this.get(agendamento, 0);
		
		if(agendamento != null
				&& agendamento.getIdSituacao() != null
				&& agendamento.getIdSituacao().intValue() != SITUACAO_PRESENTE)
		{
			throw new Exception("Só pode vincular pacientes para agendamentos com situação presente! Situação do agendamento: " + agendamento.getDesSituacao());
		}
		
		vo.setIdPaciente(paciente.getIdPaciente());
		vo.setNomPaciente(paciente.getNomPaciente());
		vo.setCpfPaciente(paciente.getCpfPaciente());
		vo.setTelPaciente(paciente.getTelPaciente());
		vo.setEmlPaciente(paciente.getEmlPaciente());
		sessao.merge(vo);
		
		//INSERIR REGISTRO DE LOG DO AGENDAMENTO
		AgendamentoLog log = new AgendamentoLog();
		log.setIdAgendamento(vo.getIdAgendamento());
		log.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		log.setDatCadastro(new Date());
		log.setDesOperacao("VINCULANDO O PACIENTE PARA O AGENDAMENTO");
		
		AgendamentoLogService.getInstancia().inserir(sessao, log);
		
		return vo;
	}
	
	public HashMap<BigInteger, Long> countAgendamentoSituacao(Agendamento vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			HashMap<BigInteger, Long> map = countAgendamentoSituacao(sessao, vo);
			tx.commit();
			return map;
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<BigInteger, Long> countAgendamentoSituacao(Session sessao, Agendamento vo) throws Exception
	{
		Dominio dominio = new Dominio();
		dominio.setRefDominio("SITUACAO_AGENDAMENTO");
		List<Dominio> listaSituacao = DominioService.getInstancia().pesquisar(sessao, dominio, 0);		
		
		HashMap<BigInteger, Long> map = new HashMap<BigInteger, Long>();
		
		//SETAR TODAS AS SITUACOES NO MAP
		if(listaSituacao != null)
		{
			for (Dominio objSituacao : listaSituacao)
			{
				map.put(objSituacao.getVlrDominio(), new Long(0));
			}
		}
				
		Criteria criteria = sessao.createCriteria(Agendamento.class);
		
		ProjectionList projection = Projections.projectionList();
		projection.add(Projections.groupProperty("idSituacao"));
		projection.add(Projections.count("idSituacao"));
		
		criteria.add(Restrictions.eq("flgAtivo", vo.getFlgAtivo()));
		criteria.add(Restrictions.eq("idClinicaProfissional", vo.getIdClinicaProfissional()));
		
		criteria.addOrder(Order.asc("idSituacao"));
		
		criteria.setProjection(projection);
		List<Object[]> resultado = criteria.list();
		
		if (resultado != null && resultado.size() > 0)
	    {
	    	int j = 0;
	    	for (int i = 0; i < resultado.size(); i++)
	    	{
	    		j = 0;
	    		
	    		BigInteger situacao = (BigInteger) resultado.get(i)[j++];
	    		Long quantidade = (Long) resultado.get(i)[j++];
	    		
	    		map.put(situacao, quantidade);
	    	}
	    }
		
		return map;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Agendamento.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
			criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }
		
		if ((join & JOIN_SERVICO) != 0)
	    {
			criteria.createAlias("servico", "servico");
	    }
		
		if ((join & JOIN_CONVENIO) != 0)
	    {
			criteria.createAlias("convenio", "convenio", JoinType.LEFT_OUTER_JOIN);
	    }
		
		if ((join & JOIN_PACIENTE) != 0)
	    {
			criteria.createAlias("paciente", "paciente", JoinType.INNER_JOIN);
			
			if ((join & JOIN_PACIENTE_ESTADO) != 0)
		    {
				criteria.createAlias("paciente.estado", "estado", JoinType.INNER_JOIN);
		    }
	    }
		
		if ((join & JOIN_AGENDAMENTO_EXAME) != 0)
	    {
			criteria.createAlias("listaAgendamentoExame", "listaAgendamentoExame", JoinType.LEFT_OUTER_JOIN);
			
			if ((join & JOIN_EXAME) != 0)
		    {
				criteria.createAlias("listaAgendamentoExame.exame", "exame", JoinType.LEFT_OUTER_JOIN);
		    }
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Agendamento vo, int join)
	{
		criteria.addOrder(Order.asc("datAgendamento"));
		criteria.addOrder(Order.asc("horInicio"));
		criteria.addOrder(Order.asc("nomPaciente"));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected Map restritores() 
	{
		return restritores;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Map filtroPropriedade() 
	{
		return filtroPropriedade;
	}
}
