package br.com.a2dm.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.entity.Sistema;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;

public class SistemaService extends A2DMHbNgc<Sistema>
{
	private static SistemaService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static SistemaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new SistemaService();
		}
		return instancia;
	}
	
	public SistemaService()
	{
		adicionarFiltro("idSistema", RestritorHb.RESTRITOR_EQ,"idSistema");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_LIKE, "descricao");
	}

	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Sistema.class);
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Sistema vo, int join)
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
