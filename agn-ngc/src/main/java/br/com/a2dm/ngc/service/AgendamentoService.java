package br.com.a2dm.ngc.service;

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

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.vo.RetornoVo;
import br.com.a2dm.ngc.functions.UtilFuncions;

public class AgendamentoService extends A2DMHbNgc<Agendamento>
{
	private static AgendamentoService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
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
		adicionarFiltro("datAgendamento", RestritorHb.RESTRITOR_EQ,"datAgendamento");
		adicionarFiltro("idPaciente", RestritorHb.RESTRITOR_EQ,"idPaciente");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
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
			criteria.createAlias("usuarioAlt", "usuarioAlt");
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Agendamento vo, int join)
	{
		criteria.addOrder(Order.asc("datAgendamento"));
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
