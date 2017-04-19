package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.Horario;

public class HorarioService extends A2DMHbNgc<Horario>
{
	private static HorarioService instancia = null;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static HorarioService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new HorarioService();
		}
		return instancia;
	}
	
	public HorarioService()
	{
		adicionarFiltro("idHorario", RestritorHb.RESTRITOR_EQ,"idHorario");		
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Horario.class);		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Horario vo, int join)
	{
		criteria.addOrder(Order.asc("desHorario"));
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
