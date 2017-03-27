package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.ClinicaProfissionalRec;

public class ClinicaProfissionalRecService extends A2DMHbNgc<ClinicaProfissionalRec>
{	
	private static ClinicaProfissionalRecService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_CLINICA_PROFISSIONAL = 2;
	
	public static final int JOIN_CLINICA_PROFISSIONAL_CLINICA = 4;
	
	public static final int JOIN_CLINICA_PROFISSIONAL_PROFISSIONAL = 8;	
	
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ClinicaProfissionalRecService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ClinicaProfissionalRecService();
		}
		return instancia;
	}
	
	public ClinicaProfissionalRecService()
	{
		adicionarFiltro("idClinicaProfissionalRec", RestritorHb.RESTRITOR_EQ,"idClinicaProfissionalRec");
		adicionarFiltro("idClinicaProfissional", RestritorHb.RESTRITOR_EQ, "idClinicaProfissional");
		adicionarFiltro("idUsuarioRec", RestritorHb.RESTRITOR_EQ, "idUsuarioRec");
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(ClinicaProfissionalRec.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_CLINICA_PROFISSIONAL) != 0)
	    {
			criteria.createAlias("clinicaProfissional", "clinicaProfissional");
			
			if ((join & JOIN_CLINICA_PROFISSIONAL_CLINICA) != 0)
		    {
				criteria.createAlias("clinicaProfissional.clinica", "clinica");
		    }
			
			if ((join & JOIN_CLINICA_PROFISSIONAL_PROFISSIONAL) != 0)
		    {
				criteria.createAlias("clinicaProfissional.usuario", "usuario");
		    }
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, ClinicaProfissionalRec vo, int join)
	{
		criteria.addOrder(Order.asc("idClinicaProfissionalRec"));
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
