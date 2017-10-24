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
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.ServicoService;


@RequestScoped
@ManagedBean
public class AtendimentoServicoBean extends AbstractBean<Servico, ServicoService>
{	
	private Date dataInicio;
	
	private Date dataFim;
	
	private Long total;
	
	public AtendimentoServicoBean()
	{
		super(ServicoService.getInstancia());
		this.ACTION_SEARCH = "atendimentoServico";
		this.pageTitle = "Atendimentos por Serviço";
		
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
				
				Servico servico = new Servico();
				servico.setFiltroMap(new HashMap<String, Object>());
				servico.getFiltroMap().put("datAgendamentoInicio", dataInicio);
				servico.getFiltroMap().put("datAgendamentoFim", dataFim);
				servico.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				servico.setFlgAtivo("S");
				
				List<Servico> lista = ServicoService.getInstancia().pesquisarAtendimentoServico(servico);
				
				this.setTotal(new Long(0));
				
				for (Servico obj : lista)
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
            this.setSearchResult(new ArrayList<Servico>());
	    }
	}
	
//	@Override
//	protected boolean validarAcesso(String acao)
//	{
//		boolean temAcesso = true;
//
//		if (!ValidaPermissao.getInstancia().verificaPermissao("atendimentoServico", acao))
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
