package br.com.a2dm.cmn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.entity.Grupo;
import br.com.a2dm.cmn.entity.log.GrupoLog;
import br.com.a2dm.cmn.service.log.GrupoServiceLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;

public class GrupoService extends A2DMHbNgc<Grupo>
{
	private static GrupoService instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int GRUPO_ADMINISTRADOR = 1;
	public static final int GRUPO_PROFISSIONAL = 2;
	public static final int GRUPO_RECEPCIONISTA = 3;
	
	private JSFUtil util = new JSFUtil();
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static GrupoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new GrupoService();
		}
		return instancia;
	}
	
	public GrupoService()
	{
		adicionarFiltro("idGrupo", RestritorHb.RESTRITOR_EQ,"idGrupo");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_LIKE, "descricao");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_EQ, "filtroMap.descricao");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Grupo.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
	         criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		return criteria;
	}
	
	@Override
	public Grupo alterar(Session sessao, Grupo vo) throws Exception
	{
		Grupo grupo = new Grupo();
		grupo.setIdGrupo(vo.getIdGrupo());
		grupo = this.get(sessao, grupo, 0);
		
		GrupoLog grupoLog = new GrupoLog();
		JSFUtil.copiarPropriedades(grupo, grupoLog);
		grupoLog.setLogMapping("ALTERADO POR TELA: GRUPO - ALTERAR");
		GrupoServiceLog.getInstancia().inserir(sessao, grupoLog);
		
		grupo.setDescricao(vo.getDescricao());
		grupo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		grupo.setDataAlteracao(new Date());
		
		return super.alterar(sessao, grupo);
	}
	
	@Override
	protected void validarInserir(Session sessao, Grupo vo) throws Exception
	{
		Grupo grupo = new Grupo();
		grupo.setFiltroMap(new HashMap<String, Object>());
		grupo.getFiltroMap().put("descricao", vo.getDescricao());
		grupo = this.get(sessao, grupo, 0);
		
		if(grupo != null)
		{
			String mensagem = grupo.getFlgAtivo().equals("S") ? "ativo" : "inativo";
			throw new Exception("Não foi possível inserir o registro, já existe um registro "+ mensagem +" com esta descrição!");
		}
	}
	
	public Grupo inativar(Grupo vo) throws Exception
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

	public Grupo inativar(Session sessao, Grupo vo) throws Exception
	{
		Grupo grupo = new Grupo();
		grupo.setIdGrupo(vo.getIdGrupo());
		grupo = this.get(sessao, grupo, 0);
		
		GrupoLog grupoLog = new GrupoLog();
		JSFUtil.copiarPropriedades(grupo, grupoLog);
		grupoLog.setLogMapping("REGISTRO INATIVADO");
		GrupoServiceLog.getInstancia().inserir(sessao, grupoLog);
		
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDataAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	public Grupo ativar(Grupo vo) throws Exception
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
	
	public Grupo ativar(Session sessao, Grupo vo) throws Exception
	{
		Grupo grupo = new Grupo();
		grupo.setIdGrupo(vo.getIdGrupo());
		grupo = this.get(sessao, grupo, 0);
		
		GrupoLog grupoLog = new GrupoLog();
		JSFUtil.copiarPropriedades(grupo, grupoLog);
		grupoLog.setLogMapping("REGISTRO ATIVADO");
		GrupoServiceLog.getInstancia().inserir(sessao, grupoLog);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDataAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Grupo vo, int join)
	{
		criteria.addOrder(Order.asc("descricao"));
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
