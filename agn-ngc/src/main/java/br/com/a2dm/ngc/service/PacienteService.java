package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.configuracao.UtilFuncions;
import br.com.a2dm.ngc.entity.Paciente;

public class PacienteService extends A2DMHbNgc<Paciente>
{	
	private static PacienteService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	public static final int JOIN_ESTADO = 4;

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
		adicionarFiltro("nomPaciente", RestritorHb.RESTRITOR_LIKE,"nomPaciente");
		adicionarFiltro("cpfPaciente", RestritorHb.RESTRITOR_EQ,"cpfPaciente");
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
				.add(Restrictions.eq("idProfissional", UtilFuncions.getClinicaProfissionalSession().getIdUsuario()));
		
		
		Paciente paciente = (Paciente) criteria.uniqueResult();
		
		if(paciente != null)
		{
			if(paciente.getNomPaciente() != null
					&& paciente.getNomPaciente().toLowerCase().trim().equals(vo.getNomPaciente().toLowerCase().trim()))
			{
				throw new Exception("Já existe um paciente cadastrado com este Nome.");
			}
			
			if(paciente.getCpfPaciente() != null
					&& paciente.getCpfPaciente().trim().equals(vo.getCpfPaciente().trim()))
			{
				throw new Exception("Já existe um Paciente cadastrado com este Cpf.");
			}
			
			if(paciente.getEmlPaciente() != null
					&& paciente.getEmlPaciente().toLowerCase().trim().equals(vo.getEmlPaciente().toLowerCase().trim()))
			{
				throw new Exception("Já existe um Paciente cadastrado com este E-mail.");
			}
		}
	}
	
	@Override
	protected void validarAlterar(Session sessao, Paciente vo) throws Exception
	{
		Criteria criteria = sessao.createCriteria(Paciente.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.eq("nomPaciente", vo.getNomPaciente()).ignoreCase());
		or.add(Restrictions.eq("emlPaciente",vo.getEmlPaciente()).ignoreCase());		
		or.add(Restrictions.eq("cpfPaciente",vo.getCpfPaciente()).ignoreCase());
		criteria.add(or)
				.add(Restrictions.ne("idPaciente", vo.getIdPaciente()))
				.add(Restrictions.eq("idProfissional", UtilFuncions.getClinicaProfissionalSession().getIdUsuario()));
		
		
		Paciente paciente = (Paciente) criteria.uniqueResult();
		
		if(paciente != null)
		{
			if(paciente.getNomPaciente() != null
					&& paciente.getNomPaciente().toLowerCase().trim().equals(vo.getNomPaciente().toLowerCase().trim()))
			{
				throw new Exception("Já existe um paciente cadastrado com este Nome.");
			}
			
			if(paciente.getCpfPaciente() != null
					&& paciente.getCpfPaciente().trim().equals(vo.getCpfPaciente().trim()))
			{
				throw new Exception("Já existe um Paciente cadastrado com este Cpf.");
			}
			
			if(paciente.getEmlPaciente() != null
					&& paciente.getEmlPaciente().toLowerCase().trim().equals(vo.getEmlPaciente().toLowerCase().trim()))
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
			criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }
		
		if ((join & JOIN_ESTADO) != 0)
	    {
			criteria.createAlias("estado", "estado");
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
