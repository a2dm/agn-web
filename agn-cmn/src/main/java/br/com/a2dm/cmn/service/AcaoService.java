package br.com.a2dm.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.entity.Acao;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;

public class AcaoService extends A2DMHbNgc<Acao>
{
	private static AcaoService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static AcaoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new AcaoService();
		}
		return instancia;
	}
	
	public AcaoService()
	{
		adicionarFiltro("idAcao", RestritorHb.RESTRITOR_EQ,"idAcao");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_LIKE, "descricao");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Acao.class);
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Acao vo, int join)
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
