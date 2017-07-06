package br.com.a2dm.ngc.service.log;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.ngc.entity.log.AgendamentoLog;

public class AgendamentoLogService extends A2DMHbNgc<AgendamentoLog>
{
	private static AgendamentoLogService instancia = null;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static AgendamentoLogService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new AgendamentoLogService();
		}
		return instancia;
	}
	
	public AgendamentoLogService(){}

	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(AgendamentoLog.class);
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, AgendamentoLog vo, int join)
	{
		criteria.addOrder(Order.asc("idAgendamento"));
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
