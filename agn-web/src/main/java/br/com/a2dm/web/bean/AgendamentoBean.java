package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import br.com.a2dm.ngc.configuracao.MenuControl;
import br.com.a2dm.ngc.configuracao.UtilFuncions;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.Convenio;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.service.AgendamentoService;
import br.com.a2dm.ngc.service.ConvenioService;
import br.com.a2dm.ngc.service.PacienteService;
import br.com.a2dm.ngc.service.ServicoService;


@RequestScoped
@ManagedBean
public class AgendamentoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	private String inicio;
	private String fim;
	
	private List<Servico> listaServico;
	private List<Convenio> listaConvenio;
	private List<Paciente> listaPaciente;
	
	private String value;
    private List<String> autocompleteList;
    
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
		this.setListaPaciente(PacienteService.getInstancia().pesquisar(new Paciente(), 0));
		
		Date dataInicio = new Date(inicio);		
		
		this.getEntity().setDatAgendamento(dataInicio);
		this.getEntity().setHorInicio(new SimpleDateFormat("HH:mm").format(dataInicio));
		
		
		this.popularServicos();
		this.popularConvenios();
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
			}
			else
			{
				this.getEntity().setIdPaciente(null);
				this.getEntity().setNomPaciente(null);
			}
		}
		catch (Exception e)
	    {
			FacesMessage message = new FacesMessage(e.getMessage());
		    message.setSeverity(FacesMessage.SEVERITY_ERROR);
		    FacesContext.getCurrentInstance().addMessage(null, message);
	    }
	}
	
	public void atualizarHoraFim()
	{
		try
		{
			Servico servico = new Servico();
			servico.setIdServico(this.getEntity().getIdServico());
			
			servico = ServicoService.getInstancia().get(servico, 0);
			
			String horaFim = Utilitarios.somarHoras(this.getEntity().getHorInicio(), servico.getDrcServico());
			
			this.getEntity().setHorFim(horaFim);
			
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

	public List<String> getAutocompleteList() {
		return autocompleteList;
	}

	public void setAutocompleteList(List<String> autocompleteList) {
		this.autocompleteList = autocompleteList;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
