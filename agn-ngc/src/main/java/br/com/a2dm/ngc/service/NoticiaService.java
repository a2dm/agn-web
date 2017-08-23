package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.Noticia;

public class NoticiaService extends A2DMHbNgc<Noticia>
{
	private static NoticiaService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static NoticiaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new NoticiaService();
		}
		return instancia;
	}
	
	public NoticiaService()
	{
		adicionarFiltro("idNoticia", RestritorHb.RESTRITOR_EQ,"idNoticia");
		adicionarFiltro("desTitulo", RestritorHb.RESTRITOR_EQ,"desTitulo");
		adicionarFiltro("desNoticia", RestritorHb.RESTRITOR_EQ,"desNoticia");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Noticia.class);
		return criteria;
	}
	
	@Override
	protected void setNumeroMaximoRegistros(Session session, Criteria criteria, Noticia filter, int join)
	{
		criteria.setMaxResults(10);
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Noticia vo, int join)
	{
		criteria.addOrder(Order.asc("datCadastro"));
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
