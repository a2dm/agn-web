package br.com.a2dm.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Convenio;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.ConvenioService;


@RequestScoped
@ManagedBean
public class AtendimentoPlanoBean extends AbstractBean<Convenio, ConvenioService>
{	
	private Date dataInicio;
	
	private Date dataFim;
	
	private Long total;
	
	public AtendimentoPlanoBean()
	{
		super(ConvenioService.getInstancia());
		this.ACTION_SEARCH = "atendimentoPlano";
		this.pageTitle = "Atendimentos por Plano";
		
		MenuControl.ativarMenu("flgMenuRlt");
	}
	
	@Override
	protected void validarPesquisar() throws Exception
	{
		if(this.getDataInicio() == null
				|| this.getDataInicio().toString().trim().equals(""))
		{
			throw new Exception("O campo Período Inicial é obrigatório!");
		}
		
		if(this.getDataFim() == null
				|| this.getDataFim().toString().trim().equals(""))
		{
			throw new Exception("O campo Período Final é obrigatório!");
		}
	}

	@Override
	public void pesquisar(ActionEvent event)
	{
		try
        {
			if(validarAcesso(Variaveis.ACAO_PESQUISAR))
       	 	{
				this.validarPesquisar();
				
				Convenio convenio = new Convenio();
				convenio.setFiltroMap(new HashMap<String, Object>());
				convenio.getFiltroMap().put("datAgendamentoInicio", dataInicio);
				convenio.getFiltroMap().put("datAgendamentoFim", dataFim);
				convenio.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				convenio.setFlgAtivo("S");
				
				List<Convenio> lista = ConvenioService.getInstancia().pesquisarAtendimentoPlano(convenio);
				
				this.setTotal(new Long(0));
				
				for (Convenio obj : lista)
				{
					this.total += obj.getCountAgendamento().longValue();
				}
				
				this.setSearchResult(lista);
       	 	}
        }
		catch (Exception e)
	    {
           	FacesMessage message = new FacesMessage(e.getMessage());
           	message.setSeverity(FacesMessage.SEVERITY_ERROR);
            if(e.getMessage() == null)
            	FacesContext.getCurrentInstance().addMessage("", message);
            else
            	FacesContext.getCurrentInstance().addMessage(null, message);
            this.setSearchResult(new ArrayList<Convenio>());
	    }
	}
	
//	@Override
//	protected boolean validarAcesso(String acao)
//	{
//		boolean temAcesso = true;
//
//		if (!ValidaPermissao.getInstancia().verificaPermissao("atendimentoPlano", acao))
//		{
//			temAcesso = false;
//			HttpServletResponse rp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//			try
//			{
//				rp.sendRedirect("/agn-web/pages/acessoNegado.jsf");
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		
//		return temAcesso;
//	}
	
	@Override
	public void clean(ActionEvent event)
	{
		super.clean(event);
		this.setDataInicio(null);
		this.setDataFim(null);
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
