package br.com.a2dm.ngc.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.ngc.entity.ConvenioServico;

public class ConvenioServicoService extends A2DMHbNgc<ConvenioServico>
{	
	private static ConvenioServicoService instancia = null;
	
	public static final int JOIN_USUARIO_CAD = 1;
	
	public static final int JOIN_USUARIO_ALT = 2;
	
	public static final int JOIN_SERVICO = 4;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static ConvenioServicoService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ConvenioServicoService();
		}
		return instancia;
	}
	
	public ConvenioServicoService()
	{
		adicionarFiltro("idConvenio", RestritorHb.RESTRITOR_EQ, "idConvenio");
		adicionarFiltro("idServico", RestritorHb.RESTRITOR_EQ, "idServico");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("servico.flgAtivo", RestritorHb.RESTRITOR_EQ, "servico.flgAtivo");
	}
	
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(ConvenioServico.class);
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
			criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
			criteria.createAlias("usuarioAlt", "usuarioAlt");
	    }
		
		if ((join & JOIN_SERVICO) != 0)
	    {
			criteria.createAlias("servico", "servico");
	    }
		
		return criteria;
	}
		
	@Override
	protected void setarOrdenacao(Criteria criteria, ConvenioServico vo, int join)
	{
		criteria.addOrder(Order.asc("servico.desServico"));
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
