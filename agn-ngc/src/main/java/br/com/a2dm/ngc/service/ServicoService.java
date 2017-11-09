package br.com.a2dm.ngc.service;

import java.math.BigInteger;
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
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.functions.UtilFuncions;

public class ServicoService extends A2DMHbNgc<Servico>
{
	private JSFUtil util = new JSFUtil();
	
	private static ServicoService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ServicoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ServicoService();
		}
		return instancia;
	}
	
	public ServicoService()
	{
		adicionarFiltro("idServico", RestritorHb.RESTRITOR_EQ,"idServico");
		adicionarFiltro("idClinicaProfissional", RestritorHb.RESTRITOR_EQ, "idClinicaProfissional");		
		adicionarFiltro("idServico", RestritorHb.RESTRITOR_NE, "filtroMap.idServicoNotEq");
		adicionarFiltro("desServico", RestritorHb.RESTRITOR_LIKE, "desServico");
		adicionarFiltro("desServico", RestritorHb.RESTRITOR_EQ, "filtroMap.desServico");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");		
	}
	
	@Override
	protected void validarInserir(Session sessao, Servico vo) throws Exception
	{
		Servico servico = new Servico();
		servico.setFlgAtivo("S");
		servico.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		servico.setFiltroMap(new HashMap<String, Object>());
		servico.getFiltroMap().put("desServico", vo.getDesServico().trim());		
		
		servico = this.get(sessao, servico, 0);
		
		if(servico != null)
		{
			throw new Exception("Este serviço já está cadastrado na sua base de dados!");
		}
	}
	
	@Override
	protected void validarAlterar(Session sessao, Servico vo) throws Exception
	{
		Servico servico = new Servico();
		servico.setFiltroMap(new HashMap<String, Object>());
		servico.getFiltroMap().put("idServicoNotEq", vo.getIdServico());
		servico.getFiltroMap().put("desServico", vo.getDesServico().trim());
		servico.setFlgAtivo(vo.getFlgAtivo());
		servico.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		
		servico = this.get(sessao, servico, 0);
		
		if(servico != null)
		{
			throw new Exception("Este serviço já está cadastrado na sua base de dados!");
		}
	}
	
	public Servico inativar(Servico vo) throws Exception
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

	public Servico inativar(Session sessao, Servico vo) throws Exception
	{
		Servico servico = new Servico();
		servico.setIdServico(vo.getIdServico());
		servico = this.get(sessao, servico, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	public Servico ativar(Servico vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = ativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("N");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Servico ativar(Session sessao, Servico vo) throws Exception
	{
		Servico servico = new Servico();
		servico.setIdServico(vo.getIdServico());
		servico = this.get(sessao, servico, 0);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	public List<Servico> pesquisarAtendimentoServico(Servico vo) throws Exception 
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		try
		{
			return this.pesquisarAtendimentoServico(sessao, vo);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Servico> pesquisarAtendimentoServico(Session sessao, Servico vo) throws Exception
	{		
		Criteria  criteria = sessao.createCriteria(Servico.class);
		
		//AGRUPAMENTOS
		ProjectionList projection = Projections.projectionList();
		projection.add(Projections.groupProperty("idServico"));
		projection.add(Projections.groupProperty("desServico"));
		projection.add(Projections.count("idServico"));
		
		//JOINS
		criteria.createAlias("listaAgendamento", "listaAgendamento", JoinType.INNER_JOIN);
		
		//WHERES
		criteria.add(Restrictions.eq("idClinicaProfissional", UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional()));
		criteria.add(Restrictions.eq("listaAgendamento.idClinicaProfissional", UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional()));
		criteria.add(Restrictions.eq("flgAtivo", "S"));
		criteria.add(Restrictions.eq("listaAgendamento.flgAtivo", "S"));
		criteria.add(Restrictions.ge("listaAgendamento.datAgendamento", vo.getFiltroMap().get("datAgendamentoInicio")));
		criteria.add(Restrictions.lt("listaAgendamento.datAgendamento", vo.getFiltroMap().get("datAgendamentoFim")));
		
		criteria.setProjection(projection);
		criteria.addOrder(Order.asc("desServico"));
		
		List<Object[]> resultado = criteria.list();
		List<Servico> retorno = new ArrayList<Servico>(3);
		
		if (resultado != null && resultado.size() > 0)
		{
			int j = 0;
			for (int i = 0; i < resultado.size(); i++)
			{
				j = 0;
				Servico servico = new Servico();				
				servico.setIdServico((BigInteger) resultado.get(i)[j++]);
				servico.setDesServico((String) resultado.get(i)[j++]);
				servico.setCountAgendamento((Long) resultado.get(i)[j++]);
				retorno.add(servico);
			}
		}
		
		//QUANTIDADE DE ATENDIMENTOS PARTICULARES
		Servico objParticular = this.getAtendimentoParticular(sessao, vo);
		retorno.add(objParticular);
		
		return retorno;
	}	
	
	@SuppressWarnings("unchecked")
	public Servico getAtendimentoParticular(Session sessao, Servico vo) throws Exception
	{
		Criteria  criteria = sessao.createCriteria(Agendamento.class);
		
		//AGRUPAMENTOS
		ProjectionList projection = Projections.projectionList();
		projection.add(Projections.count("idAgendamento"));
		projection.add(Projections.groupProperty("tpAgendamento"));
		
		//WHERES
		criteria.add(Restrictions.eq("flgAtivo", "S"));
		criteria.add(Restrictions.eq("tpAgendamento", "P"));
		criteria.add(Restrictions.eq("idClinicaProfissional",  UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional()));
		criteria.add(Restrictions.ge("datAgendamento", vo.getFiltroMap().get("datAgendamentoInicio")));
		criteria.add(Restrictions.lt("datAgendamento", vo.getFiltroMap().get("datAgendamentoFim")));
		
		criteria.setProjection(projection);
		
		List<Object[]> resultado = criteria.list();
		
		Servico servico = new Servico();				
		
		if (resultado != null && resultado.size() > 0)
		{
			int j = 0;
			for (int i = 0; i < resultado.size(); i++)
			{
				j = 0;
				servico.setDesServico("Particular");
				servico.setCountAgendamento((Long) resultado.get(i)[j++]);			
			}
		}
				
		return servico;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Servico.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
			criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }		
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Servico vo, int join)
	{
		criteria.addOrder(Order.asc("desServico"));
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
