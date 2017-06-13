package br.com.a2dm.ngc.service;

import java.util.Date;
import java.util.HashMap;
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
			criteria.createAlias("usuarioAlt", "usuarioAlt");
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
