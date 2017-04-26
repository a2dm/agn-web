package br.com.a2dm.ngc.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.configuracao.UtilFuncions;
import br.com.a2dm.ngc.entity.Horario;

public class HorarioService extends A2DMHbNgc<Horario>
{
	private JSFUtil util = new JSFUtil();
	
	private static HorarioService instancia = null;
	
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static HorarioService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new HorarioService();
		}
		return instancia;
	}
	
	public HorarioService()
	{
		adicionarFiltro("idClinicaProfissional", RestritorHb.RESTRITOR_EQ,"idClinicaProfissional");	
		adicionarFiltro("idHorario", RestritorHb.RESTRITOR_EQ,"idHorario");
		adicionarFiltro("numHorario", RestritorHb.RESTRITOR_EQ,"numHorario");
	}
	
	public void salvar(List<Horario> lista) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			this.salvar(sessao, lista);
			tx.commit();
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public void salvar(Session sessao, List<Horario> lista) throws Exception
	{
		if(lista != null)
		{
			for (Horario horario : lista)
			{
				Horario objPersist = new Horario();
				objPersist.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				objPersist.setNumHorario(horario.getNumHorario());
				
				objPersist = this.get(sessao, objPersist, 0);
				
				if(objPersist != null)
				{
					objPersist.setHorInicio(horario.getHorInicio());
					objPersist.setHorFim(horario.getHorFim());
					objPersist.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
					objPersist.setDatAlteracao(new Date());
					objPersist.setFlgAtivo(horario.isFlgAtivo());
					
					this.alterar(sessao, objPersist);
				}
				else
				{
					objPersist = new Horario();
					
					JSFUtil.copiarPropriedades(horario, objPersist);
					objPersist.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
					objPersist.setDatAlteracao(new Date());
					objPersist.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
					this.inserir(sessao, objPersist);
				}
			}
		}
	}
		
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Horario.class);		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Horario vo, int join)
	{
		criteria.addOrder(Order.asc("numHorario"));
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
