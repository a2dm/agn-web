package br.com.a2dm.cmn.service.log;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;

import br.com.a2dm.cmn.entity.log.TelaAcaoLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;

public class TelaAcaoServiceLog extends A2DMHbNgc<TelaAcaoLog>
{
	private static TelaAcaoServiceLog instancia = null;

	public static final int JOIN_SISTEMA = 1;
	
	public static final int JOIN_ACAO = 2;
	
	public static final int JOIN_USUARIO_CAD = 4;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static TelaAcaoServiceLog getInstancia()
	{
		if (instancia == null)
		{
			instancia = new TelaAcaoServiceLog();
		}
		return instancia;
	}
	
	public TelaAcaoServiceLog()
	{
		adicionarFiltro("idTelaAcaoLog", RestritorHb.RESTRITOR_EQ,"idTelaAcaoLog");
	}

	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(TelaAcaoLog.class);
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
