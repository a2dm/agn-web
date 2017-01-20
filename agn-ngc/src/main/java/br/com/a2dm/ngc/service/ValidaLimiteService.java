package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.ValidaLimite;

public class ValidaLimiteService extends A2DMHbNgc<ValidaLimite>
{	
	private static ValidaLimiteService instancia = null;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ValidaLimiteService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ValidaLimiteService();
		}
		return instancia;
	}
	
	public ValidaLimiteService()
	{		
		adicionarFiltro("idUsuario", RestritorHb.RESTRITOR_EQ,"idUsuario");
	}
		
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(ValidaLimite.class);		
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
