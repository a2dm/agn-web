package br.com.a2dm.cmn.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.a2dm.cmn.entity.Parametro;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;

public class ParametroService extends A2DMHbNgc<Parametro>
{
	private static ParametroService instancia = null;
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ParametroService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ParametroService();
		}
		return instancia;
	}
	
	public ParametroService()
	{
		adicionarFiltro("idParametro", RestritorHb.RESTRITOR_EQ, "idParametro");
		adicionarFiltro("descricao", RestritorHb.RESTRITOR_EQ, "descricao");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Parametro.class);
		return criteria;
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
