package br.com.a2dm.cmn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.entity.GrupoTelaAcao;
import br.com.a2dm.cmn.entity.log.GrupoTelaAcaoLog;
import br.com.a2dm.cmn.service.log.GrupoTelaAcaoServiceLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;

public class GrupoTelaAcaoService extends A2DMHbNgc<GrupoTelaAcao>
{
	private static GrupoTelaAcaoService instancia = null;
		
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_TELA_ACAO = 2;
	
	public static final int JOIN_SISTEMA = 4;
	
	public static final int JOIN_ACAO = 8;
	
	
	private JSFUtil util = new JSFUtil();
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static GrupoTelaAcaoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new GrupoTelaAcaoService();
		}
		return instancia;
	}
	
	public GrupoTelaAcaoService()
	{
		adicionarFiltro("idGrupoTelaAcao", RestritorHb.RESTRITOR_EQ, "idGrupoTelaAcao");
		adicionarFiltro("idGrupo", RestritorHb.RESTRITOR_EQ, "idGrupo");
		adicionarFiltro("idTelaAcao", RestritorHb.RESTRITOR_EQ, "idTelaAcao");
		adicionarFiltro("sistema.idSistema", RestritorHb.RESTRITOR_EQ, "filtroMap.idSistema");
		adicionarFiltro("acao.descricao", RestritorHb.RESTRITOR_EQ, "filtroMap.descricaoAcaoAcesso");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
	}

	public void inserirPermissoes(List<GrupoTelaAcao> lista) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			this.inserirPermissoes(sessao, lista);
			tx.commit();
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
	
	public void inserirPermissoes(Session sessao, List<GrupoTelaAcao> lista) throws Exception
	{
		for (GrupoTelaAcao obj : lista) 
		{
			obj.setFlgAtivo("S");
			GrupoTelaAcao gta = new GrupoTelaAcao();
			gta = this.get(obj, JOIN_TELA_ACAO | JOIN_SISTEMA);
			
			if(gta != null)
			{
				GrupoTelaAcaoLog gtaLog = new GrupoTelaAcaoLog();
				JSFUtil.copiarPropriedades(gta, gtaLog);
				gtaLog.setLogMapping("REMOVIDO DAS PERMISSÕES POR: Grupo - Configurar");
				GrupoTelaAcaoServiceLog.getInstancia().inserir(sessao, gtaLog);
				
				gta.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
				gta.setDataAlteracao(new Date());
				gta.setFlgAtivo("N");
				
				this.alterar(sessao, gta);
			}
			else
			{
				obj.setDataCadastro(new Date());				
				this.inserir(sessao, obj);
			}
		}
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(GrupoTelaAcao.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
	         criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_TELA_ACAO) != 0)
	    {
	         criteria.createAlias("telaAcao", "telaAcao");
	         
	         if ((join & JOIN_SISTEMA) != 0)
	 	     {
	        	 criteria.createAlias("telaAcao.sistema", "sistema");
	 	     }
	         
	         if ((join & JOIN_ACAO) != 0)
	 	     {
	        	 criteria.createAlias("telaAcao.acao", "acao");
	 	     }
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, GrupoTelaAcao vo, int join)
	{
		criteria.addOrder(Order.asc("idGrupoTelaAcao"));
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
