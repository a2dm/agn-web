package br.com.a2dm.cmn.service.log;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.a2dm.cmn.entity.log.GrupoTelaAcaoLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;

public class GrupoTelaAcaoServiceLog extends A2DMHbNgc<GrupoTelaAcaoLog>
{
	private static GrupoTelaAcaoServiceLog instancia = null;
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static GrupoTelaAcaoServiceLog getInstancia()
	{
		if (instancia == null)
		{
			instancia = new GrupoTelaAcaoServiceLog();
		}
		return instancia;
	}
	
	public GrupoTelaAcaoServiceLog(){}
				
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

	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(GrupoTelaAcaoLog.class);
		return criteria;
	}
}
