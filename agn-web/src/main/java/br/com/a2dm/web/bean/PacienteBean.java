package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.a2dm.cmn.entity.Estado;
import br.com.a2dm.cmn.service.EstadoService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.AgendamentoExame;
import br.com.a2dm.ngc.entity.Exame;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.entity.log.AgendamentoLog;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.AgendamentoExameService;
import br.com.a2dm.ngc.service.AgendamentoService;
import br.com.a2dm.ngc.service.PacienteService;
import br.com.a2dm.ngc.service.log.AgendamentoLogService;


@RequestScoped
@ManagedBean
public class PacienteBean extends AbstractBean<Paciente, PacienteService>
{
	private List<Agendamento> listaPacienteAtendimento;
	private List<Estado> listaEstado;
	private String siglaEstado;
	private String activeTab;
	private Date datInicioAtendimento;
	private List<Exame> listaExamesPaciente;
	
	private Agendamento agendamento;
	
	
	private JSFUtil util = new JSFUtil();
	
	public PacienteBean()
	{
		super(PacienteService.getInstancia());
		this.ACTION_SEARCH = "paciente";
		this.pageTitle = "Paciente";
		
		MenuControl.ativarMenu("flgMenuPct");
	}
		
	@Override
	protected void setDefaultInserir() throws Exception 
	{
		this.getEntity().setSexPaciente("M");
		this.setActiveTab("Dados Pessoais");
	}
	
	@Override
	protected void setListaInserir() throws Exception
	{
		//LISTA DE ESTADOS
		List<Estado> resultEst = EstadoService.getInstancia().pesquisar(new Estado(), 0);
		
		Estado est = new Estado();
		est.setDescricao("Escolha o Estado");
		
		List<Estado> listaEstado = new ArrayList<Estado>();
		listaEstado.add(est);
		listaEstado.addAll(resultEst);
		
		this.setListaEstado(listaEstado);
	}
	
	   public String preparaCompletarInserir()
	   {
	      try
	      {
	    	  if(validarAcesso(Variaveis.ACAO_PREPARA_INSERIR))
	    	  {
	    		  setCurrentState(STATE_INSERT);
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
	
	@Override
	public void preparaAlterar()
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
			{
				super.preparaAlterar();
				
				Estado estado = new Estado();
				estado.setIdEstado(this.getEntity().getIdEstado());
				estado = EstadoService.getInstancia().get(estado, 0);
				
				this.setSiglaEstado(estado.getSigla());
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
	public void preparaVisualizar()
	{
		try
		{
			super.preparaVisualizar();
			
			Estado estado = new Estado();
			estado.setIdEstado(this.getEntity().getIdEstado());
			estado = EstadoService.getInstancia().get(estado, 0);
			
			this.setSiglaEstado(estado.getSigla());
			
			//HISTORICO DE ATENDIMENTOS
			Agendamento agendamento = new Agendamento();
			agendamento.setFiltroMap(new HashMap<String, Object>());
			agendamento.getFiltroMap().put("orderHistorico", true);
			agendamento.setFlgAtivo("S");
			agendamento.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
			agendamento.setIdPaciente(this.getEntity().getIdPaciente());
			
			List<Agendamento> lista = AgendamentoService.getInstancia().pesquisar(agendamento, AgendamentoService.JOIN_SERVICO
																							 | AgendamentoService.JOIN_CONVENIO
																							 | AgendamentoService.JOIN_PACIENTE);
			this.setListaPacienteAtendimento(lista);
		}
		catch (Exception e) 
		{	
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}	

	public void visualizarAtendimento()
	{
		try
		{
			if(this.getAgendamento() != null)
			{
				this.getAgendamento().setVlrAgendamentoFormatado(new DecimalFormat("#,##0.00").format(this.getAgendamento().getVlrAgendamento() == null ? this.getAgendamento().getServico().getVlrServico() : this.getAgendamento().getVlrAgendamento()));
				this.getAgendamento().setVlrDescontoFormatado(new DecimalFormat("#,##0.00").format(this.getAgendamento().getVlrDesconto() == null ? 0 : this.getAgendamento().getVlrDesconto()));
				
				AgendamentoLog agendamentoLog = new AgendamentoLog();
				agendamentoLog.setIdAgendamento(this.getAgendamento().getIdAgendamento());
				agendamentoLog.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)));
				
				agendamentoLog = AgendamentoLogService.getInstancia().get(agendamentoLog, 0);
				
				if(agendamentoLog != null)
				{
					this.setDatInicioAtendimento(agendamentoLog.getDatCadastro());
				}
				
				//POPULANDO LISTA DE EXAMES
				AgendamentoExame agendamentoExame = new AgendamentoExame();
				agendamentoExame.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				agendamentoExame.setIdAgendamento(this.getAgendamento().getIdAgendamento());
				
				List<AgendamentoExame> lista = AgendamentoExameService.getInstancia().pesquisar(agendamentoExame, AgendamentoExameService.JOIN_EXAME);
				List<Exame> listaExamesPaciente = null;
				
				if(lista != null
						&& lista.size() > 0)
				{
					listaExamesPaciente = new ArrayList<Exame>();
					
					for (AgendamentoExame obj : lista)
					{
						Exame exame = new Exame();
						JSFUtil.copiarPropriedades(obj.getExame(), exame);
						listaExamesPaciente.add(exame);
					}
				}
				
				this.setListaExamesPaciente(listaExamesPaciente);
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
	protected void completarInserir() throws Exception
	{
		Estado estado = new Estado();
		estado.setSigla(this.getSiglaEstado());
		estado = EstadoService.getInstancia().get(estado, 0);
		
		this.getEntity().setIdEstado(estado.getIdEstado());
		this.getEntity().setIdProfissional(UtilFuncions.getClinicaProfissionalSession().getIdUsuario());
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setFlgCompleto("S");
	}
	
	@Override
	protected void completarAlterar() throws Exception
	{
		this.validarInserir();
		
		Estado estado = new Estado();
		estado.setSigla(this.getSiglaEstado());
		estado = EstadoService.getInstancia().get(estado, 0);
		
		this.getEntity().setIdEstado(estado.getIdEstado());		
		this.getEntity().setDatAlteracao(new Date());
		this.getEntity().setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setFlgCompleto("S");
	}
	
	@Override
	protected void completarPesquisar() throws Exception
	{
		this.getSearchObject().setIdProfissional(UtilFuncions.getClinicaProfissionalSession().getIdUsuario());
	}
	
	@Override
	protected int getJoinPesquisar()
	{
		return PacienteService.JOIN_USUARIO_CAD
			 | PacienteService.JOIN_USUARIO_ALT
			 | PacienteService.JOIN_ESTADO;
	}
	
//	@Override
//	protected boolean validarAcesso(String acao)
//	{
//		boolean temAcesso = true;
//
//		if (!ValidaPermissao.getInstancia().verificaPermissao("paciente", acao))
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
	protected void validarInserir() throws Exception {
		
		if(this.getEntity().getNomPaciente() == null || this.getEntity().getNomPaciente().trim().equals(""))
		{
			throw new Exception("O campo Nome é obrigatório.");
		}
		
		if(this.getEntity().getEmlPaciente() == null || this.getEntity().getEmlPaciente().trim().equals(""))
		{
			throw new Exception("O campo E-mail é obrigatório.");
		}
		
		if(this.getEntity().getCpfPaciente() == null || this.getEntity().getCpfPaciente().trim().equals(""))
		{
			throw new Exception("O campo Cpf é obrigatório.");
		}
		
		if(this.getEntity().getDatNascimento() == null || this.getEntity().getDatNascimento().toString().equals(""))
		{
			throw new Exception("O campo Data de Nascimento é obrigatório.");
		}
		
		if(this.getEntity().getCelPaciente() == null || this.getEntity().getCelPaciente().trim().equals(""))
		{
			throw new Exception("O campo Celular é obrigatório.");
		}
		
		if(this.getEntity().getCepPaciente() == null || this.getEntity().getCepPaciente().trim().equals(""))
		{
			throw new Exception("O campo Cep é obrigatório.");
		}
		
		if(this.getEntity().getLgdPaciente() == null || this.getEntity().getLgdPaciente().trim().equals(""))
		{
			throw new Exception("O campo Logradouro é obrigatório.");
		}
		
		if(this.getEntity().getNumEndereco() == null || this.getEntity().getNumEndereco().longValue() <= 0)
		{
			throw new Exception("O campo Número da Residência é obrigatório.");
		}
		
		if(this.getEntity().getBrrPaciente() == null || this.getEntity().getBrrPaciente().trim().equals(""))
		{
			throw new Exception("O campo Bairro é obrigatório.");
		}
		
		if(this.getEntity().getCidPaciente() == null || this.getEntity().getCidPaciente().trim().equals(""))
		{
			throw new Exception("O campo Cidade é obrigatório.");
		}
		
		if(this.getSiglaEstado() == null || this.getSiglaEstado().trim().equals(""))
		{
			throw new Exception("O campo Estado é obrigatório.");
		}
		
		if(this.getEntity().getDatNascimento() != null
				&& this.getEntity().getDatNascimento().after(new Date())){
			throw new Exception("O campo Data de Nascimento deve ser anterior a Data de Hoje.");
		}
		
	}

	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public String getSiglaEstado() {
		return siglaEstado;
	}

	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public List<Agendamento> getListaPacienteAtendimento() {
		return listaPacienteAtendimento;
	}

	public void setListaPacienteAtendimento(List<Agendamento> listaPacienteAtendimento) {
		this.listaPacienteAtendimento = listaPacienteAtendimento;
	}

	public Date getDatInicioAtendimento() {
		return datInicioAtendimento;
	}

	public void setDatInicioAtendimento(Date datInicioAtendimento) {
		this.datInicioAtendimento = datInicioAtendimento;
	}

	public List<Exame> getListaExamesPaciente() {
		return listaExamesPaciente;
	}

	public void setListaExamesPaciente(List<Exame> listaExamesPaciente) {
		this.listaExamesPaciente = listaExamesPaciente;
	}
}
