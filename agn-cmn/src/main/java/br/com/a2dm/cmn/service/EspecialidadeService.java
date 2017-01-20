package br.com.a2dm.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.entity.Especialidade;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;

public class EspecialidadeService extends A2DMHbNgc<Especialidade>
{
	private static EspecialidadeService instancia = null;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static EspecialidadeService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new EspecialidadeService();
		}
		return instancia;
	}
	
	public EspecialidadeService()
	{
		adicionarFiltro("idEspecialidade", RestritorHb.RESTRITOR_EQ,"idEspecialidade");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_LIKE, "descricao");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Especialidade.class);
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Especialidade vo, int join)
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
