package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Clinica;
import br.com.a2dm.ngc.entity.ValidaLimite;

public class ClinicaService extends A2DMHbNgc<Clinica>
{
	private JSFUtil util = new JSFUtil();
	
	private static ClinicaService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ClinicaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ClinicaService();
		}
		return instancia;
	}
	
	public ClinicaService()
	{
		adicionarFiltro("idClinica", RestritorHb.RESTRITOR_EQ,"idClinica");
		adicionarFiltro("idClinica", RestritorHb.RESTRITOR_NE, "filtroMap.idClinicaNotEq");
		adicionarFiltro("idUsuario", RestritorHb.RESTRITOR_EQ,"idUsuario");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("desClinica", RestritorHb.RESTRITOR_LIKE, "desClinica");
		adicionarFiltro("desClinica", RestritorHb.RESTRITOR_EQ, "filtroMap.desClinica");
	}
	
	@Override
	protected void validarInserir(Session sessao, Clinica vo) throws Exception
	{
		Clinica clinica = new Clinica();
		clinica.setFlgAtivo("S");
		clinica.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		clinica.setFiltroMap(new HashMap<String, Object>());
		clinica.getFiltroMap().put("desClinica", vo.getDesClinica().trim());
		
		clinica = this.get(sessao, clinica, 0);
		
		if(clinica != null)
		{
			throw new Exception("Esta clínica já está cadastrada na sua base de dados!");
		}
		
		ValidaLimite validaLimite = new ValidaLimite();
		validaLimite.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		
		validaLimite = ValidaLimiteService.getInstancia().get(sessao, validaLimite, 0);
		
		if(validaLimite.getQtdTotalClinica().intValue() >= validaLimite.getQtdClinicaLimite().intValue())
		{
			throw new Exception("Você chegou ao limite de clínicas. Altere seu plano para poder adicionar mais clínicas.");
		}
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Clinica.class);
		
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
	protected void setarOrdenacao(Criteria criteria, Clinica vo, int join)
	{
		criteria.addOrder(Order.asc("desClinica"));
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
