package br.com.a2dm.cmn.service.log;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.a2dm.cmn.entity.log.GrupoLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;

public class GrupoServiceLog extends A2DMHbNgc<GrupoLog>
{
	private static GrupoServiceLog instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static GrupoServiceLog getInstancia()
	{
		if (instancia == null)
		{
			instancia = new GrupoServiceLog();
		}
		return instancia;
	}
	
	public GrupoServiceLog(){}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(GrupoLog.class);
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
