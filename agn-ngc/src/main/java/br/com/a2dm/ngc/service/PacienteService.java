package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Paciente;

public class PacienteService extends A2DMHbNgc<Paciente>
{
	private JSFUtil util = new JSFUtil();
	
	private static PacienteService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static PacienteService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new PacienteService();
		}
		return instancia;
	}
	
	public PacienteService()
	{
		adicionarFiltro("idPaciente", RestritorHb.RESTRITOR_EQ,"idPaciente");		
	}
		
	@Override
	protected void validarInserir(Session sessao, Paciente vo) throws Exception
	{
		Criteria criteria = sessao.createCriteria(Paciente.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.eq("nomPaciente", vo.getNomPaciente()).ignoreCase());
		or.add(Restrictions.eq("emlPaciente",vo.getEmlPaciente()).ignoreCase());		
		or.add(Restrictions.eq("cpfPaciente",vo.getCpfPaciente()).ignoreCase());
		criteria.add(or)
				.add(Restrictions.eq("idProfissional", util.getUsuarioLogado().getIdUsuario()));
		
		
		Paciente paciente = (Paciente) criteria.uniqueResult();
		
		if(paciente != null)
		{
			if(paciente.getNomPaciente() != null
					&& paciente.getNomPaciente().trim().equals(vo.getNomPaciente().trim()))
			{
				throw new Exception("Já existe um paciente cadastrado com este Nome.");
			}
			
			if(paciente.getCpfPaciente() != null
					&& paciente.getCpfPaciente().trim().equals(vo.getCpfPaciente().trim()))
			{
				throw new Exception("Já existe um Paciente cadastrado com este Cpf.");
			}
			
			if(paciente.getEmlPaciente() != null
					&& paciente.getEmlPaciente().trim().equals(vo.getEmlPaciente().trim()))
			{
				throw new Exception("Já existe um Paciente cadastrado com este E-mail.");
			}
		}
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Paciente.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
			criteria.createAlias("usuarioAlt", "usuarioAlt");
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Paciente vo, int join)
	{
		criteria.addOrder(Order.asc("nomPaciente"));
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
