package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.AgendamentoExame;

public class AgendamentoExameService extends A2DMHbNgc<AgendamentoExame>
{
	private static AgendamentoExameService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static AgendamentoExameService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new AgendamentoExameService();
		}
		return instancia;
	}
	
	public AgendamentoExameService()
	{
		adicionarFiltro("idAgendamentoExame", RestritorHb.RESTRITOR_EQ,"idAgendamentoExame");
		adicionarFiltro("idExame", RestritorHb.RESTRITOR_EQ,"idExame");
		adicionarFiltro("idAgendamento", RestritorHb.RESTRITOR_EQ,"idAgendamento");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(AgendamentoExame.class);
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, AgendamentoExame vo, int join)
	{
		criteria.addOrder(Order.asc("desClinica"));
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
