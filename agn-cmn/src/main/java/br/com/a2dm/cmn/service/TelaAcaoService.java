package br.com.a2dm.cmn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.entity.TelaAcao;
import br.com.a2dm.cmn.entity.log.TelaAcaoLog;
import br.com.a2dm.cmn.service.log.TelaAcaoServiceLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;

public class TelaAcaoService extends A2DMHbNgc<TelaAcao>
{
	private static TelaAcaoService instancia = null;

	public static final int JOIN_SISTEMA = 1;
	
	public static final int JOIN_ACAO = 2;
	
	public static final int JOIN_USUARIO_CAD = 4;
	
	public static final int JOIN_GRUPO_TELA_ACAO = 8;
	
	private JSFUtil util = new JSFUtil();
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static TelaAcaoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new TelaAcaoService();
		}
		return instancia;
	}
	
	public TelaAcaoService()
	{
		adicionarFiltro("idTelaAcao", RestritorHb.RESTRITOR_EQ,"idTelaAcao");
		adicionarFiltro("idTelaAcao", RestritorHb.RESTRITOR_NE, "filtroMap.idTelaAcaoNotEq");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_LIKE, "descricao");
		adicionarFiltro("pagina", RestritorHb.RESTRITOR_LIKE, "pagina");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("idSistema", RestritorHb.RESTRITOR_EQ, "idSistema");
		adicionarFiltro("idAcao", RestritorHb.RESTRITOR_EQ, "idAcao");
		adicionarFiltro("pagina", RestritorHb.RESTRITOR_EQ, "filtroMap.pagina");
		adicionarFiltro("listaGrupoTelaAcao.idGrupo", RestritorHb.RESTRITOR_EQ, "filtroMap.idGrupo");
		adicionarFiltro("listaGrupoTelaAcao.flgAtivo", RestritorHb.RESTRITOR_EQ, "filtroMap.flgAtivoGrupoTelaAcao");
	}

	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(TelaAcao.class);
		
		if ((join & JOIN_SISTEMA) != 0)
	    {
	         criteria.createAlias("sistema", "sistema");
	    }
		
		if ((join & JOIN_ACAO) != 0)
	    {
	         criteria.createAlias("acao", "acao");
	    }
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
	         criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_GRUPO_TELA_ACAO) != 0)
	    {
	         criteria.createAlias("listaGrupoTelaAcao", "listaGrupoTelaAcao");
	    }
		
		return criteria;
	}
	
	@Override
	protected void validarInserir(Session sessao, TelaAcao vo) throws Exception
	{
		TelaAcao telaAcao = new TelaAcao();
		telaAcao.setFiltroMap(new HashMap<String, Object>());
		telaAcao.getFiltroMap().put("pagina", vo.getPagina());
		telaAcao.setIdSistema(vo.getIdSistema());
		telaAcao.setIdAcao(vo.getIdAcao());
		
		telaAcao = this.get(sessao, telaAcao, 0);
		
		if(telaAcao != null)
		{
			throw new Exception("Esta ação já está cadastrada na base de dados.");
		}
	}
	
	@Override
	protected void validarAlterar(Session sessao, TelaAcao vo) throws Exception
	{
		TelaAcao telaAcao = new TelaAcao();
		telaAcao.setFiltroMap(new HashMap<String, Object>());
		telaAcao.getFiltroMap().put("idTelaAcaoNotEq", vo.getIdTelaAcao());
		telaAcao.getFiltroMap().put("pagina", vo.getPagina());
		telaAcao.setIdSistema(vo.getIdSistema());
		telaAcao.setIdAcao(vo.getIdAcao());
		
		telaAcao = this.get(sessao, telaAcao, 0);
		
		if(telaAcao != null)
		{
			throw new Exception("Esta ação já está cadastrada na base de dados.");
		}
	}
	
	@Override
	public TelaAcao alterar(Session sessao, TelaAcao vo) throws Exception
	{
		TelaAcao telaAcao = new TelaAcao();
		telaAcao.setIdTelaAcao(vo.getIdTelaAcao());
		telaAcao = this.get(sessao, telaAcao, 0);
		
		TelaAcaoLog telaAcaoLog = new TelaAcaoLog();
		JSFUtil.copiarPropriedades(telaAcao, telaAcaoLog);
		telaAcaoLog.setLogMapping("ALTERADO POR TELA: TELA AÇÃO - ALTERAR");
		TelaAcaoServiceLog.getInstancia().inserir(sessao, telaAcaoLog);
		
		telaAcao.setDescricao(vo.getDescricao());
		telaAcao.setPagina(vo.getPagina());
		telaAcao.setIdSistema(vo.getIdSistema());
		telaAcao.setIdAcao(vo.getIdAcao());
		telaAcao.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		telaAcao.setDataAlteracao(new Date());
		
		return super.alterar(sessao, telaAcao);
	}
	
	public TelaAcao inativar(TelaAcao vo) throws Exception
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
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}

	public TelaAcao inativar(Session sessao, TelaAcao vo) throws Exception
	{
		TelaAcao telaAcao = new TelaAcao();
		telaAcao.setIdTelaAcao(vo.getIdTelaAcao());
		telaAcao = this.get(sessao, telaAcao, 0);
		
		TelaAcaoLog telaAcaoLog = new TelaAcaoLog();
		JSFUtil.copiarPropriedades(telaAcao, telaAcaoLog);
		telaAcaoLog.setLogMapping("REGISTRO INATIVADO");
		TelaAcaoServiceLog.getInstancia().inserir(sessao, telaAcaoLog);
		
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDataAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	public TelaAcao ativar(TelaAcao vo) throws Exception
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
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public TelaAcao ativar(Session sessao, TelaAcao vo) throws Exception
	{
		TelaAcao telaAcao = new TelaAcao();
		telaAcao.setIdTelaAcao(vo.getIdTelaAcao());
		telaAcao = this.get(sessao, telaAcao, 0);
		
		TelaAcaoLog telaAcaoLog = new TelaAcaoLog();
		JSFUtil.copiarPropriedades(telaAcao, telaAcaoLog);
		telaAcaoLog.setLogMapping("REGISTRO ATIVADO");
		TelaAcaoServiceLog.getInstancia().inserir(sessao, telaAcaoLog);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDataAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, TelaAcao vo, int join)
	{
		criteria.addOrder(Order.asc("descricao"));
		criteria.addOrder(Order.asc("pagina"));
		
		if ((join & JOIN_SISTEMA) != 0)
	    {
			criteria.addOrder(Order.asc("sistema.descricao"));
	    }
		
		if ((join & JOIN_ACAO) != 0)
	    {
			criteria.addOrder(Order.asc("acao.descricao"));
	    }
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
