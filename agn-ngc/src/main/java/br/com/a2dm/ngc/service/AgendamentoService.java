package br.com.a2dm.ngc.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Agendamento;
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
	}
	
	@Override
	protected void validarInserir(Session sessao, Agendamento vo) throws Exception
	{
		//LISTA TODOS OS AGENDAMENTOS DO DIA
		Agendamento agendamento = new Agendamento();
		agendamento.setIdClinicaProfissional(vo.getIdClinicaProfissional());
		agendamento.setDatAgendamento(vo.getDatAgendamento());	
		
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
		Agendamento agendamento = new Agendamento();
		agendamento.setIdAgendamento(vo.getIdAgendamento());
		agendamento = this.get(sessao, agendamento, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
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
		
		super.alterar(sessao, vo);
		
		//INSERIR REGISTRO DE LOG DO AGENDAMENTO
		AgendamentoLog log = new AgendamentoLog();
		log.setIdAgendamento(vo.getIdAgendamento());
		log.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		log.setDatCadastro(new Date());
		log.setDesOperacao("MACANDO O AGENDAMENTO COMO PRESENTE");
		
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
		
		//ALTERAR SITUACAO DO AGENDAMENTO PARA PRESENTE
		vo.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)));
		super.alterar(sessao, vo);
		
		//INSERIR REGISTRO DE LOG DO AGENDAMENTO
		AgendamentoLog log = new AgendamentoLog();
		log.setIdAgendamento(vo.getIdAgendamento());
		log.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		log.setDatCadastro(new Date());
		log.setDesOperacao("MACANDO O AGENDAMENTO COMO EM ATENDIMENTO");
		
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
		super.alterar(vo);
		
		//INSERIR REGISTRO DE LOG DO AGENDAMENTO
		AgendamentoLog log = new AgendamentoLog();
		log.setIdAgendamento(vo.getIdAgendamento());
		log.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		log.setDatCadastro(new Date());
		log.setDesOperacao("VINCULANDO O PACIENTE PARA O AGENDAMENTO");
		
		AgendamentoLogService.getInstancia().inserir(sessao, log);
		
		return vo;
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
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Agendamento vo, int join)
	{
		criteria.addOrder(Order.asc("datAgendamento"));
		criteria.addOrder(Order.asc("horInicio"));
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
