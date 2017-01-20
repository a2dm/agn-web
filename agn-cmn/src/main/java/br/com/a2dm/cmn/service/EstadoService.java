package br.com.a2dm.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.entity.Estado;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;

public class EstadoService extends A2DMHbNgc<Estado>
{
	private static EstadoService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static EstadoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new EstadoService();
		}
		return instancia;
	}
	
	public EstadoService()
	{
		adicionarFiltro("idEstado", RestritorHb.RESTRITOR_EQ,"idEstado");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_LIKE, "descricao");
		adicionarFiltro("sigla", RestritorHb.RESTRITOR_EQ, "sigla");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Estado.class);
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Estado vo, int join)
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
