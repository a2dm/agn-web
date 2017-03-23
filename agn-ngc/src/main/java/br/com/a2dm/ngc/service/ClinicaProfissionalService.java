package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.ClinicaProfissional;

public class ClinicaProfissionalService extends A2DMHbNgc<ClinicaProfissional>
{	
	private static ClinicaProfissionalService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_CLINICA = 2;
	
	public static final int JOIN_PROFISSIONAL = 4;
	
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ClinicaProfissionalService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ClinicaProfissionalService();
		}
		return instancia;
	}
	
	public ClinicaProfissionalService()
	{
		adicionarFiltro("idClinicaProfissional", RestritorHb.RESTRITOR_EQ,"idClinicaProfissional");
		adicionarFiltro("idClinica", RestritorHb.RESTRITOR_EQ, "idClinica");
		adicionarFiltro("idUsuario", RestritorHb.RESTRITOR_EQ,"idUsuario");				
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(ClinicaProfissional.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_CLINICA) != 0)
	    {
			criteria.createAlias("clinica", "clinica");
	    }
		
		if ((join & JOIN_PROFISSIONAL) != 0)
	    {
			criteria.createAlias("usuario", "usuario");
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, ClinicaProfissional vo, int join)
	{
		if(JOIN_CLINICA != 0)
		{
			criteria.addOrder(Order.asc("clinica.desClinica"));
		}
		
		criteria.addOrder(Order.asc("idUsuario"));
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
