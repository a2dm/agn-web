package br.com.a2dm.cmn.service.log;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.a2dm.cmn.entity.log.UsuarioLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;

public class UsuarioServiceLog extends A2DMHbNgc<UsuarioLog>
{
	private static UsuarioServiceLog instancia = null;

	public static final int JOIN_USUARIO_CAD = 1;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static UsuarioServiceLog getInstancia()
	{
		if (instancia == null)
		{
			instancia = new UsuarioServiceLog();
		}
		return instancia;
	}
	
	public UsuarioServiceLog(){}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(UsuarioLog.class);
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
