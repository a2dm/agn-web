package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.Dominio;

public class DominioService extends A2DMHbNgc<Dominio>
{	
	private static DominioService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static DominioService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new DominioService();
		}
		return instancia;
	}
	
	public DominioService()
	{
		adicionarFiltro("idDominio", RestritorHb.RESTRITOR_EQ,"idDominio");
		adicionarFiltro("desDominio", RestritorHb.RESTRITOR_EQ,"desDominio");
		adicionarFiltro("refDominio", RestritorHb.RESTRITOR_EQ,"refDominio");
	}
	
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Dominio.class);
		
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
	protected void setarOrdenacao(Criteria criteria, Dominio vo, int join)
	{
		criteria.addOrder(Order.asc("vlrDominio"));
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
