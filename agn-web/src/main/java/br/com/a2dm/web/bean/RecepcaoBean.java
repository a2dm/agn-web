package br.com.a2dm.web.bean;

import java.util.Date;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.richfaces.component.SortOrder;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.AgendamentoService;

@RequestScoped
@ManagedBean
public class RecepcaoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	private Date dataInicio;
	
	private Date dataFim;
		
	private SortOrder nomeOrder;	
	private SortOrder inicioOrder;
	private SortOrder fimOrder;
	private SortOrder datAgendamentoOrder;
	private SortOrder situacaoOrder;	
	private SortOrder presencaOrder;
	
	public RecepcaoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "recepcao";
		this.pageTitle = "Recepção";
		
		MenuControl.ativarMenu("flgMenuRcp");
	}
	
	@Override
	protected int getJoinPesquisar() 
	{
		return AgendamentoService.JOIN_CONVENIO
			 | AgendamentoService.JOIN_SERVICO;
	}
	
	public void marcarPresenca()
	{
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_PRESENCA))
				{
					AgendamentoService.getInstancia().marcarPresenca(this.getEntity());
					
					FacesMessage message = new FacesMessage("O agendamento foi marcado como presente!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
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
		}		
	}
	
	public void atenderPaciente()
	{
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_ATENDER))
				{
					AgendamentoService.getInstancia().atenderPaciente(this.getEntity());
					
					FacesMessage message = new FacesMessage("O agendamento foi marcado como presente!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
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
		}		
	}
		
	
	@Override
	protected void completarPesquisar() throws Exception
	{
		this.getSearchObject().setFlgAtivo("S");
		this.getSearchObject().setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		this.getSearchObject().setFiltroMap(new HashMap<String, Object>());
		this.getSearchObject().getFiltroMap().put("datAgendamentoInicio", this.getDataInicio());
		this.getSearchObject().getFiltroMap().put("datAgendamentoFim", this.getDataFim());
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		this.setDataInicio(new Date());
		this.setDataFim(new Date());
		
		this.setInicioOrder(SortOrder.unsorted);
		this.setFimOrder(SortOrder.unsorted);
		this.setNomeOrder(SortOrder.unsorted);
		this.setDatAgendamentoOrder(SortOrder.unsorted);
		this.setSituacaoOrder(SortOrder.unsorted);
		this.setPresencaOrder(SortOrder.unsorted);
	}

	@Override
	protected void validarPesquisar() throws Exception
	{
		if(this.getDataInicio() == null
				|| this.getDataInicio().toString().equals(""))
		{
			throw new Exception("O campo Período Inicio é obrigatório!");
		}
		
		if(this.getDataFim() == null
				|| this.getDataFim().toString().equals(""))
		{
			throw new Exception("O campo Período Fim é obrigatório!");
		}
	}
	
	public void orderNome()
	{
		this.setSituacaoOrder(SortOrder.unsorted);
		setInicioOrder(SortOrder.unsorted);
		setFimOrder(SortOrder.unsorted);
		setDatAgendamentoOrder(SortOrder.unsorted);
		setPresencaOrder(SortOrder.unsorted);
		setNomeOrder(nomeOrder.equals(SortOrder.ascending) ? SortOrder.descending : SortOrder.ascending);		
	}
	
	public void orderInicio()
	{
		this.setSituacaoOrder(SortOrder.unsorted);
		setNomeOrder(SortOrder.unsorted);
		setFimOrder(SortOrder.unsorted);
		setDatAgendamentoOrder(SortOrder.unsorted);
		setPresencaOrder(SortOrder.unsorted);
		setInicioOrder(inicioOrder.equals(SortOrder.ascending) ? SortOrder.descending : SortOrder.ascending);		
	}
	
	public void orderFim()
	{
		this.setSituacaoOrder(SortOrder.unsorted);
		setInicioOrder(SortOrder.unsorted);
		setNomeOrder(SortOrder.unsorted);
		setDatAgendamentoOrder(SortOrder.unsorted);
		setPresencaOrder(SortOrder.unsorted);
		setFimOrder(fimOrder.equals(SortOrder.ascending) ? SortOrder.descending : SortOrder.ascending);		
	}
	
	public void orderDatAgendamento()
	{
		this.setSituacaoOrder(SortOrder.unsorted);
		setInicioOrder(SortOrder.unsorted);
		setFimOrder(SortOrder.unsorted);
		setNomeOrder(SortOrder.unsorted);
		setPresencaOrder(SortOrder.unsorted);
		setDatAgendamentoOrder(datAgendamentoOrder.equals(SortOrder.ascending) ? SortOrder.descending : SortOrder.ascending);		
	}
	
	public void orderSituacao()
	{
		setInicioOrder(SortOrder.unsorted);
		setFimOrder(SortOrder.unsorted);
		setNomeOrder(SortOrder.unsorted);
		setDatAgendamentoOrder(SortOrder.unsorted);	
		setPresencaOrder(SortOrder.unsorted);
		this.setSituacaoOrder(situacaoOrder.equals(SortOrder.ascending) ? SortOrder.descending : SortOrder.ascending);
	}
	
	public void orderPresenca()
	{
		setInicioOrder(SortOrder.unsorted);
		setFimOrder(SortOrder.unsorted);
		setNomeOrder(SortOrder.unsorted);
		setDatAgendamentoOrder(SortOrder.unsorted);		
		setSituacaoOrder(SortOrder.unsorted);
		this.setPresencaOrder(presencaOrder.equals(SortOrder.ascending) ? SortOrder.descending : SortOrder.ascending);
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

	public SortOrder getNomeOrder() {
		return nomeOrder;
	}

	public void setNomeOrder(SortOrder nomeOrder) {
		this.nomeOrder = nomeOrder;
	}

	public SortOrder getInicioOrder() {
		return inicioOrder;
	}

	public void setInicioOrder(SortOrder inicioOrder) {
		this.inicioOrder = inicioOrder;
	}

	public SortOrder getFimOrder() {
		return fimOrder;
	}

	public void setFimOrder(SortOrder fimOrder) {
		this.fimOrder = fimOrder;
	}

	public SortOrder getDatAgendamentoOrder() {
		return datAgendamentoOrder;
	}

	public void setDatAgendamentoOrder(SortOrder datAgendamentoOrder) {
		this.datAgendamentoOrder = datAgendamentoOrder;
	}

	public SortOrder getSituacaoOrder() {
		return situacaoOrder;
	}

	public void setSituacaoOrder(SortOrder situacaoOrder) {
		this.situacaoOrder = situacaoOrder;
	}

	public SortOrder getPresencaOrder() {
		return presencaOrder;
	}

	public void setPresencaOrder(SortOrder presencaOrder) {
		this.presencaOrder = presencaOrder;
	}
}
