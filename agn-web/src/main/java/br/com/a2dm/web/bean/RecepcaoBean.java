package br.com.a2dm.web.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.richfaces.component.SortOrder;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.AgendamentoService;
import br.com.a2dm.ngc.service.PacienteService;

@RequestScoped
@ManagedBean
public class RecepcaoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	private Date dataInicio;	
	private Date dataFim;
	
	private Integer ctrMensagem;
	private String mensagem;
		
	private SortOrder nomeOrder;	
	private SortOrder inicioOrder;
	private SortOrder fimOrder;
	private SortOrder datAgendamentoOrder;
	private SortOrder situacaoOrder;	
	private SortOrder presencaOrder;
	
	private Integer countAgendada;
	private Integer countPresente;
	private Integer countAtendimento;
	private Integer countConcluida;
	
	private List<Paciente> listaPaciente;
	private Paciente paciente;
	
	public RecepcaoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "recepcao";
		this.pageTitle = "Recepção";
		
		MenuControl.ativarMenu("flgMenuRcp");
	}
	
	@Override
	public String preparaPesquisar() 
	{
		try
	      {
	    	  if(validarAcesso(Variaveis.ACAO_PREPARA_PESQUISAR))
	    	  {
	    		  setSearchObject(getNewEntityInstance());
	    		  setValoresDefault();
	    		  setCurrentState(STATE_SEARCH);
	    		  setListaPesquisa();
	    		  
	    		  this.popularResultInicio();
	    		  this.completarPosPesquisar();	    		  
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
	      return ACTION_SEARCH;
	}
	
	private void popularResultInicio() throws Exception
	{
		Agendamento agendamento = new Agendamento();
		agendamento.setFiltroMap(new HashMap<String, Object>());
		agendamento.getFiltroMap().put("datAgendamentoInicio", this.getDataInicio());
		agendamento.getFiltroMap().put("datAgendamentoFim", this.getDataFim());
		agendamento.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		agendamento.setFlgAtivo("S");
		
		List<Agendamento> lista = AgendamentoService.getInstancia().pesquisar(agendamento, getJoinPesquisar());
		this.setSearchResult(lista);
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
					
					this.completarPosPesquisar();
					
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
					
					this.completarPosPesquisar();
					
					FacesMessage message = new FacesMessage("O paciente foi colocado em atendimento!");
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
	protected void completarPosPesquisar() throws Exception
	{
		this.setCountAgendada(0);
		this.setCountPresente(0);
		this.setCountAtendimento(0);
		this.setCountConcluida(0);
		
		
		if(this.getSearchResult() != null
				&& this.getSearchResult().size() > 0)
		{
			for (Agendamento obj : this.getSearchResult())
			{
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_AGENDADA)
				{
					this.setCountAgendada(this.getCountAgendada().intValue() + 1);
				}
				
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_PRESENTE)
				{
					this.setCountPresente(this.getCountPresente().intValue() + 1);
				}
				
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_EM_ATENDIMENTO)
				{
					this.setCountAtendimento(this.getCountAtendimento().intValue() + 1);
				}
				
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_CONCLUIDA)
				{
					this.setCountConcluida(this.getCountConcluida().intValue() + 1);
				}
			}
		}
	}
	
	public void preparaVincularPaciente()
	{
		this.pesquisarPaciente();
	}
	
	public void vincularPaciente()
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_VINCULAR_PACIENTE))
			{
				AgendamentoService.getInstancia().vincularPaciente(this.getEntity(), paciente);
				
				FacesMessage message = new FacesMessage("O paciente foi vinculado para o agendamento!");
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(null, message);
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
	
	public void pesquisarPaciente()
	{
		try
		{
			this.setMensagem(null);			
			this.validarPesquisarPaciente();
			
			Paciente paciente = new Paciente();
			paciente.setNomPaciente(this.getEntity().getNomPaciente());
			paciente.setCpfPaciente(this.getEntity().getCpfPaciente());
			paciente.setIdProfissional(UtilFuncions.getClinicaProfissionalSession().getIdUsuario());
			
			List<Paciente> lista = PacienteService.getInstancia().pesquisar(paciente, 0);
			this.setListaPaciente(lista);
			this.setCtrMensagem(1);
		}
		catch (Exception e)
		{
			this.setMensagem(e.getMessage());
			this.setCtrMensagem(2);
		}
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
	
	public void validarPesquisarPaciente() throws Exception
	{
		if((this.getEntity().getCpfPaciente() == null
				|| this.getEntity().getCpfPaciente().equals(""))
				&& (this.getEntity().getNomPaciente() == null
				|| this.getEntity().getNomPaciente().equals("")))
		{
			throw new Exception("Pelo menos um dos campos com * são obrigatórios!");
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
	
	@Override
	public String getFullTitle()
	{
		return this.pageTitle;
	}
	
//	@Override
//	protected boolean validarAcesso(String acao)
//	{
//		boolean temAcesso = true;
//
//		if (!ValidaPermissao.getInstancia().verificaPermissao("recepcao", acao))
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

	public List<Paciente> getListaPaciente() {
		return listaPaciente;
	}

	public void setListaPaciente(List<Paciente> listaPaciente) {
		this.listaPaciente = listaPaciente;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Integer getCountAgendada() {
		return countAgendada;
	}

	public void setCountAgendada(Integer countAgendada) {
		this.countAgendada = countAgendada;
	}

	public Integer getCountPresente() {
		return countPresente;
	}

	public void setCountPresente(Integer countPresente) {
		this.countPresente = countPresente;
	}

	public Integer getCountAtendimento() {
		return countAtendimento;
	}

	public void setCountAtendimento(Integer countAtendimento) {
		this.countAtendimento = countAtendimento;
	}

	public Integer getCountConcluida() {
		return countConcluida;
	}

	public void setCountConcluida(Integer countConcluida) {
		this.countConcluida = countConcluida;
	}

	public Integer getCtrMensagem() {
		return ctrMensagem;
	}

	public void setCtrMensagem(Integer ctrMensagem) {
		this.ctrMensagem = ctrMensagem;
	}
}
