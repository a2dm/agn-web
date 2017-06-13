package br.com.a2dm.ngc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.sql.JoinType;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Convenio;
import br.com.a2dm.ngc.entity.ConvenioServico;
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.functions.UtilFuncions;

public class ConvenioService extends A2DMHbNgc<Convenio>
{
	private JSFUtil util = new JSFUtil();
	
	private static ConvenioService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ConvenioService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ConvenioService();
		}
		return instancia;
	}
	
	public ConvenioService()
	{
		adicionarFiltro("idConvenio", RestritorHb.RESTRITOR_EQ,"idConvenio");
		adicionarFiltro("idConvenio", RestritorHb.RESTRITOR_NE, "filtroMap.idConvenioNotEq");
		adicionarFiltro("idUsuario", RestritorHb.RESTRITOR_EQ,"idUsuario");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("desConvenio", RestritorHb.RESTRITOR_LIKE, "desConvenio");
		adicionarFiltro("desConvenio", RestritorHb.RESTRITOR_EQ, "filtroMap.desConvenio");
	}
	
	@Override
	protected void validarInserir(Session sessao, Convenio vo) throws Exception
	{
		Convenio convenio = new Convenio();
		convenio.setFlgAtivo("S");
		convenio.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		convenio.setFiltroMap(new HashMap<String, Object>());
		convenio.getFiltroMap().put("desConvenio", vo.getDesConvenio().trim());
		
		convenio = this.get(sessao, convenio, 0);
		
		if(convenio != null)
		{
			throw new Exception("Este convênio já está cadastrado na sua base de dados!");
		}
	}
	
	@Override
	public Convenio inserir(Session sessao, Convenio vo) throws Exception
	{
		vo = super.inserir(sessao, vo);
		sessao.flush();
		
		//CADASTRO TB_CONVENIO_SERVICO
		List<Servico> lista = new ArrayList<Servico>();
		lista.addAll(vo.getListaServico());	
		
		for (Servico obj : lista)
		{
			if(obj.getFlgAtivo() != null
					&& obj.getFlgAtivo().equals("S"))
			{
				ConvenioServico convenioServico = new ConvenioServico();
				convenioServico.setIdClinicaProfissional(vo.getIdClinicaProfissional());
				convenioServico.setIdConvenio(vo.getIdConvenio());
				convenioServico.setIdServico(obj.getIdServico());
				convenioServico.setVlrReceber(new Double(obj.getVlrServicoFormatado().toString().replace(".", "").replace(",", ".")));
				convenioServico.setFlgAtivo(obj.getFlgAtivo());
				convenioServico.setDatCadastro(new Date());
				convenioServico.setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
				
				ConvenioServicoService.getInstancia().inserir(sessao, convenioServico);
			}
		}
		
		return vo;
	}
	
	@Override
	protected void validarAlterar(Session sessao, Convenio vo) throws Exception
	{
		Convenio convenio = new Convenio();
		convenio.setFiltroMap(new HashMap<String, Object>());
		convenio.getFiltroMap().put("idConvenioNotEq", vo.getIdConvenio());		
		convenio.getFiltroMap().put("desConvenio", vo.getDesConvenio().trim());
		convenio.setFlgAtivo(vo.getFlgAtivo());
		convenio.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		
		convenio = this.get(sessao, convenio, 0);
		
		if(convenio != null)
		{
			throw new Exception("Este convênio já está cadastrado na sua base de dados!");
		}
	}
	
	@Override
	public Convenio alterar(Session sessao, Convenio vo) throws Exception
	{
		vo = super.alterar(sessao, vo);
		sessao.flush();
		
		//ALTERACAO TB_CONVENIO_SERVICO
		List<Servico> lista = new ArrayList<Servico>();
		lista.addAll(vo.getListaServico());	
				
		for (Servico obj : lista)
		{
			ConvenioServico convenioServico = new ConvenioServico();			
			convenioServico.setIdConvenio(vo.getIdConvenio());
			convenioServico.setIdServico(obj.getIdServico());
			
			convenioServico = ConvenioServicoService.getInstancia().get(sessao, convenioServico, 0);
			
			if(convenioServico != null)
			{
				//atualizar
				convenioServico.setFlgAtivo(obj.getFlgAtivo());
				convenioServico.setDatAlteracao(new Date());
				convenioServico.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
				
				if(obj.getVlrServicoFormatado() != null
						&& !obj.getVlrServicoFormatado().trim().equals(""))
				{
					convenioServico.setVlrReceber(new Double(obj.getVlrServicoFormatado().toString().replace(".", "").replace(",", ".")));
				}

				ConvenioServicoService.getInstancia().alterar(sessao, convenioServico);
			}
			else
			{
				if(obj.getFlgAtivo().equals("S"))
				{
					//inserir
					ConvenioServico objInserir = new ConvenioServico();
					objInserir.setIdClinicaProfissional(vo.getIdClinicaProfissional());
					objInserir.setIdConvenio(vo.getIdConvenio());
					objInserir.setIdServico(obj.getIdServico());
					objInserir.setVlrReceber(new Double(obj.getVlrServicoFormatado().toString().replace(".", "").replace(",", ".")));
					objInserir.setFlgAtivo(obj.getFlgAtivo());
					objInserir.setDatCadastro(new Date());
					objInserir.setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
					
					ConvenioServicoService.getInstancia().inserir(sessao, objInserir);
				}
			}
		}
		
		return vo;
	}
	
	public Convenio inativar(Convenio vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = inativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("S");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}

	public Convenio inativar(Session sessao, Convenio vo) throws Exception
	{
		Convenio convenio = new Convenio();
		convenio.setIdConvenio(vo.getIdConvenio());
		convenio = this.get(sessao, convenio, 0);
				
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		sessao.merge(vo);
		
		return vo;
	}
	
	public Convenio ativar(Convenio vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = ativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("N");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Convenio ativar(Session sessao, Convenio vo) throws Exception
	{
		Convenio convenio = new Convenio();
		convenio.setIdConvenio(vo.getIdConvenio());
		convenio = this.get(sessao, convenio, 0);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDatAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Convenio.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
			criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Convenio vo, int join)
	{
		criteria.addOrder(Order.asc("desConvenio"));
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
