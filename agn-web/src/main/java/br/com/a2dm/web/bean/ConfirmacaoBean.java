package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.service.AgendamentoService;


@RequestScoped
@ManagedBean
public class ConfirmacaoBean extends AbstractBean<Agendamento, AgendamentoService>
{
	private Date dataAgendamentoInicio;
	
	private Date dataAgendamentoFim;
	
	//private JSFUtil util = new JSFUtil();
	
	public ConfirmacaoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "confirmacao";
		this.pageTitle = "Confirmações";
		
		MenuControl.ativarMenu("flgMenuCfm");
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		this.getSearchObject().setFlgConfirmado("T");		
		this.setDataAgendamentoInicio(new Date());
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		gc.add(Calendar.DAY_OF_MONTH, 3);
		
		this.setDataAgendamentoFim(gc.getTime());
	}
	
	@Override
	protected void validarPesquisar() throws Exception
	{
		if(this.getDataAgendamentoInicio() == null
				|| this.getDataAgendamentoInicio().toString().equals(""))
		{
			throw new Exception("O campo Período Inicio é obrigatório!");
		}
		
		if(this.getDataAgendamentoFim() == null
				|| this.getDataAgendamentoFim().toString().equals(""))
		{
			throw new Exception("O campo Período Fim é obrigatório!");
		}
	}
	
	private void validarConfirmar() throws Exception
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		
		if(this.getEntity().getDatAgendamento().before(gc.getTime()))
		{
			throw new Exception("Só é permitido a confirmação de agendamentos com data maior ou igual a data de hoje!");
		}
	}

	public void confirmar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_CONFIRMAR))
				{
					this.validarConfirmar();					
					AgendamentoService.getInstancia().confirmar(this.getEntity());
					
					FacesMessage message = new FacesMessage("Paciente confirmado com sucesso!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			}
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}		
	}
	
	@Override
	protected int getJoinPesquisar()
	{
		return AgendamentoService.JOIN_SERVICO
			 | AgendamentoService.JOIN_CONVENIO;
	}

	@Override
	protected void completarPesquisar() throws Exception
	{
		this.getSearchObject().setFiltroMap(new HashMap<String, Object>());
		this.getSearchObject().getFiltroMap().put("datAgendamentoInicio", this.getDataAgendamentoInicio());
		this.getSearchObject().getFiltroMap().put("datAgendamentoFim", this.getDataAgendamentoFim());
		this.getSearchObject().setFlgConfirmado( this.getSearchObject().getFlgConfirmado().equals("T") ? null : this.getSearchObject().getFlgConfirmado() );		
		this.getSearchObject().setFlgAtivo("S");
		this.getSearchObject().setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_AGENDADA)));
	}
	
	public Date getDataAgendamentoInicio() {
		return dataAgendamentoInicio;
	}

	public void setDataAgendamentoInicio(Date dataAgendamentoInicio) {
		this.dataAgendamentoInicio = dataAgendamentoInicio;
	}

	public Date getDataAgendamentoFim() {
		return dataAgendamentoFim;
	}

	public void setDataAgendamentoFim(Date dataAgendamentoFim) {
		this.dataAgendamentoFim = dataAgendamentoFim;
	}
}
