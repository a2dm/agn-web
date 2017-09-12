package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.Sugestao;

public class SugestaoService extends A2DMHbNgc<Sugestao>
{
	private static SugestaoService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int SITUACAO_ABERTA       		= 1;
	public static final int SITUACAO_EM_ANALISE       	= 2;
	public static final int SITUACAO_EM_DESENVOLVIMENTO = 3;
	public static final int SITUACAO_REJEITADA      	= 4;
	public static final int SITUACAO_REALIZADA      	= 5;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static SugestaoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new SugestaoService();
		}
		return instancia;
	}
	
	public SugestaoService()
	{
		adicionarFiltro("idSugestao", RestritorHb.RESTRITOR_EQ,"idSugestao");
		adicionarFiltro("idClinicaProfissional", RestritorHb.RESTRITOR_EQ,"idClinicaProfissional");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Sugestao.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Sugestao vo, int join)
	{
		criteria.addOrder(Order.desc("datCadastro"));
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
