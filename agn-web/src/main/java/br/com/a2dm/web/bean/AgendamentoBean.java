package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.cmn.util.others.Utilitarios;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.Convenio;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.AgendamentoService;
import br.com.a2dm.ngc.service.ConvenioService;
import br.com.a2dm.ngc.service.PacienteService;
import br.com.a2dm.ngc.service.ServicoService;


@RequestScoped
@ManagedBean
public class AgendamentoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	private BigInteger idAgendamento;
	private String inicio;
	private String fim;
	private String dataCalendario;
	
	private List<Servico> listaServico;
	private List<Convenio> listaConvenio;
	private List<Paciente> listaPaciente;
	
	private String jsonAgendamento;
	
	private String value;
    
    private String mensagem;
    
    private JSFUtil util = new JSFUtil();
	
	public AgendamentoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "agendamento";
		this.pageTitle = "Agendamento";
		
		MenuControl.ativarMenu("flgMenuAgn");
	}
	
	@Override
	@SuppressWarnings("deprecation")
	protected void setDefaultInserir() throws Exception 
	{
		Date dataInicio = new Date(inicio);		
		
		this.getEntity().setDatAgendamento(dataInicio);
		this.getEntity().setHorInicio(new SimpleDateFormat("HH:mm").format(dataInicio));
		
		
		this.popularServicos();
		this.popularConvenios();
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		Date data = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.setDataCalendario(sdf.format(data));
	}

	private void popularServicos() throws Exception
	{
		List<Servico> listaAll = new ArrayList<Servico>();
		
		Servico sIni = new Servico();
		sIni.setDesServico("-- Selecione um Serviço --");
		listaAll.add(sIni);
		
		Servico servico = new Servico();
		servico.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		servico.setFlgAtivo("S");
		
		List<Servico> lista = ServicoService.getInstancia().pesquisar(servico, 0);
		listaAll.addAll(lista);
		
		this.setListaServico(listaAll);
	}
	
	private void popularConvenios() throws Exception
	{
		List<Convenio> listaAll = new ArrayList<Convenio>();
		
		Convenio cIni = new Convenio();
		cIni.setDesConvenio("-- Selecione um Convênio --");
		listaAll.add(cIni);
		
		Convenio convenio = new Convenio();
		convenio.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		convenio.setFlgAtivo("S");
		
		List<Convenio> lista = ConvenioService.getInstancia().pesquisar(convenio, 0);
		listaAll.addAll(lista);
		
		this.setListaConvenio(listaAll);
	}
	
	public void verificarPacienteExistente()
	{
		try
		{
			if(this.getEntity().getCpfPaciente() != null
					&& !this.getEntity().getCpfPaciente().trim().equals(""))
			{
				Paciente paciente = new Paciente();
				paciente.setCpfPaciente(this.getEntity().getCpfPaciente());
				paciente.setIdProfissional(UtilFuncions.getClinicaProfissionalSession().getIdUsuario());			
				paciente = PacienteService.getInstancia().get(paciente, 0);
				
				this.getEntity().setIdPaciente(paciente != null ? paciente.getIdPaciente() : null);
				this.getEntity().setNomPaciente(paciente != null ? paciente.getNomPaciente() : null);
				this.getEntity().setTelPaciente(paciente != null ? paciente.getTelPaciente() : null);
				this.getEntity().setEmlPaciente(paciente != null ? paciente.getEmlPaciente() : null);
			}
			else
			{
				this.getEntity().setIdPaciente(null);
				this.getEntity().setNomPaciente(null);
				this.getEntity().setTelPaciente(null);
				this.getEntity().setEmlPaciente(null);
			}
		}
		catch (Exception e)
	    {
			FacesMessage message = new FacesMessage(e.getMessage());
		    message.setSeverity(FacesMessage.SEVERITY_ERROR);
		    FacesContext.getCurrentInstance().addMessage(null, message);
	    }
	}
	
	public void selecionarHoraInicio()
	{
		try
		{
			//VERIFICAR FORMATACAO HORA
			if(this.getEntity() != null
					&& this.getEntity().getHorInicio() != null
					&& !this.getEntity().getHorInicio().equals("")
					&& this.getEntity().getHorInicio().trim().length() < 5)
			{
				throw new Exception("Favor informar a Hora Limite no formato correto. Ex: 09:00.");
			}
			
			//CALCULO DE HORA FIM
			if(this.getEntity() != null
					&& this.getEntity().getIdServico() != null
					&& this.getEntity().getIdServico().intValue() > 0)
			{
				this.atualizarHoraFim();
			}
			else
			{
				this.getEntity().setHorFim(null);
			}
		}
		catch (Exception e)
	    {
			this.setMensagem(e.getMessage());
	    }
	}
	
	public void atualizarHoraFim()
	{
		try
		{
			if(this.getEntity() != null
					&& this.getEntity().getHorInicio() != null
					&& !this.getEntity().getHorInicio().equals(""))
			{
				Servico servico = new Servico();
				servico.setIdServico(this.getEntity().getIdServico());
				
				servico = ServicoService.getInstancia().get(servico, 0);
				
				String horaFim = Utilitarios.somarHoras(this.getEntity().getHorInicio(), servico.getDrcServico());
				
				this.getEntity().setHorFim(horaFim);
			}
			else
			{
				this.getEntity().setHorFim(null);
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
	public void inserir(ActionEvent event) 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_INSERIR))
			{
				this.setMensagem(null);
				validarInserir();
				completarInserir();
				
				AgendamentoService.getInstancia().inserir(this.getEntity());	    		  
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro inserido com sucesso.", null));	    		  
				this.clean(event);
			}
		}
		catch (Exception e)
		{
			this.setMensagem(e.getMessage());
		}
	}
	
	@Override
	public void preparaAlterar() 
	{	
		try
	      {
	    	  if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
	    	  {	    		
	    		  setCurrentState(STATE_EDIT);
	    		  setListaAlterar();
	    		  
	    		  this.popularServicos();
	    			this.popularConvenios();
	    		  
	    		  Agendamento agendamento = new Agendamento();
	    		  agendamento.setIdAgendamento(this.getIdAgendamento());	    		  
	    		  agendamento = AgendamentoService.getInstancia().get(agendamento, 0);	    	
	    		  
	    		  this.setEntity(agendamento);	    		  
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
	protected void completarAlterar() throws Exception 
	{
		this.validarInserir();
		this.getEntity().setDatAlteracao(new Date());
		this.getEntity().setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
	}
	
	public void inativar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_INATIVAR))
				{
					this.setMensagem(null);
					
					AgendamentoService.getInstancia().inativar(this.getEntity());
					
					FacesMessage message = new FacesMessage("Agendamento inativado com sucesso!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			}
		}
		catch (Exception e) 
		{
			this.setMensagem(e.getMessage());
		}		
	}
	
	protected void completarInserir() throws Exception
	{
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setFlgAtivo("S");
		this.getEntity().setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_AGENDADA)));
		this.getEntity().setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		this.getEntity().setFlgConfirmado("N");
	}
	
	@Override
	protected void validarInserir() throws Exception
	{
		if(this.getEntity() == null
				|| this.getEntity().getDatAgendamento() == null
				|| this.getEntity().getDatAgendamento().toString().trim().equals(""))
		{
			throw new Exception("O campo Data do Agendamento é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getHorInicio() == null
				|| this.getEntity().getHorInicio().trim().equals(""))
		{
			throw new Exception("O campo Hora Inicial é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getHorFim() == null
				|| this.getEntity().getHorFim().trim().equals(""))
		{
			throw new Exception("O campo Hora Final é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getNomPaciente() == null
				|| this.getEntity().getNomPaciente().trim().equals(""))
		{
			throw new Exception("O campo Nome do Paciente é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getIdServico() == null
				|| this.getEntity().getIdServico().intValue() <= 0)
		{
			throw new Exception("O campo Serviço é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getTpAgendamento() == null
				|| this.getEntity().getTpAgendamento().equals(""))
		{
			throw new Exception("O campo Tipo do Agendamento é obrigatório!");
		}
		
		if(this.getEntity() != null
				&& this.getEntity().getTpAgendamento() != null
				&& this.getEntity().getTpAgendamento().equals("C")
				&& (this.getEntity().getIdConvenio() == null 
					|| this.getEntity().getIdConvenio().equals("")))
		{
			throw new Exception("O campo Convênio é obrigatório!");
		}
				
		//AGENDAMENTO A PARTIR DA DATA DE HOJE
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		
		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
		
		Date dataAtual = gc.getTime();
		
		if(this.getEntity().getDatAgendamento().before(dataAtual))
		{
			throw new Exception("O campo Data do Agendamento deve ser maior ou igual a Data de Hoje!");
		}
		
		int horaInicio = Integer.parseInt(this.getEntity().getHorInicio().replace(":", ""));
		int horaFim = Integer.parseInt(this.getEntity().getHorFim().replace(":", ""));
		
		if(horaInicio >= horaFim)
		{
			throw new Exception("O campo Hora Fim deve ser maior que o campo Hora Início!");
		}
	}
	
	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public List<Servico> getListaServico() {
		return listaServico;
	}

	public void setListaServico(List<Servico> listaServico) {
		this.listaServico = listaServico;
	}

	public List<Convenio> getListaConvenio() {
		return listaConvenio;
	}

	public void setListaConvenio(List<Convenio> listaConvenio) {
		this.listaConvenio = listaConvenio;
	}

	public List<Paciente> getListaPaciente() {
		return listaPaciente;
	}

	public void setListaPaciente(List<Paciente> listaPaciente) {
		this.listaPaciente = listaPaciente;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getJsonAgendamento() {
		return jsonAgendamento;
	}

	public void setJsonAgendamento(String jsonAgendamento) {
		this.jsonAgendamento = jsonAgendamento;
	}

	public BigInteger getIdAgendamento() {
		return idAgendamento;
	}

	public void setIdAgendamento(BigInteger idAgendamento) {
		this.idAgendamento = idAgendamento;
	}

	public String getDataCalendario() {
		return dataCalendario;
	}

	public void setDataCalendario(String dataCalendario) {
		this.dataCalendario = dataCalendario;
	}
}