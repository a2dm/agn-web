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

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.AgendamentoExame;
import br.com.a2dm.ngc.entity.Exame;
import br.com.a2dm.ngc.entity.log.AgendamentoLog;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.AgendamentoExameService;
import br.com.a2dm.ngc.service.AgendamentoService;
import br.com.a2dm.ngc.service.ExameService;
import br.com.a2dm.ngc.service.log.AgendamentoLogService;


@RequestScoped
@ManagedBean
public class AtendimentoBean extends AbstractBean<Agendamento, AgendamentoService>
{
	private String activeTab;
	
	private Date datInicioAtendimento;
	
	private String desPrescricao;
	
	private List<Exame> listaExames;
	
	private List<Exame> listaExamesSelecionados;
	
	private Exame exame;
	
	private String desExame;
	
	private String mensagem;
	
	private Integer ctrMensagem;
	
	private List<Agendamento> listaHistoricoAtendimento;
	
	private Agendamento agendamento;
	
	private List<Exame> listaExamesPaciente;
	
	private JSFUtil util = new JSFUtil();
	
	
	public AtendimentoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "atendimento";
		this.pageTitle = "Atendimento";
		
		MenuControl.ativarMenu("flgMenuAtn");
	}
	
	public String preparaAtendimento()
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_INSERIR))
			{
				this.setActiveTab("Paciente");
				
				Agendamento agendamento = new Agendamento();
				agendamento.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				agendamento.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)));
				agendamento.setFlgAtivo("S");
				
				agendamento = AgendamentoService.getInstancia().get(agendamento, AgendamentoService.JOIN_CONVENIO
																			   | AgendamentoService.JOIN_SERVICO
																			   | AgendamentoService.JOIN_PACIENTE
																			   | AgendamentoService.JOIN_PACIENTE_ESTADO);
				
				if(agendamento != null)
				{
					this.setEntity(agendamento);
					this.getEntity().setVlrAgendamentoFormatado(new DecimalFormat("#,##0.00").format(this.getEntity().getServico().getVlrServico()));
					this.getEntity().setVlrDescontoFormatado(new DecimalFormat("#,##0.00").format(new Double(0)));
					
					AgendamentoLog agendamentoLog = new AgendamentoLog();
					agendamentoLog.setIdAgendamento(this.getEntity().getIdAgendamento());
					agendamentoLog.setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)));
					
					agendamentoLog = AgendamentoLogService.getInstancia().get(agendamentoLog, 0);
					this.setDatInicioAtendimento(agendamentoLog.getDatCadastro());
					
					this.setListaExamesSelecionados(new ArrayList<Exame>());
					this.popularExames();
					this.popularHistoricoAtendimento();
					
					setCurrentState(STATE_INSERT);
				}
				else
				{
					this.setEntity(null);					
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
		
		return ACTION_SEARCH;
	}
	
	private void popularExames() throws Exception
	{
		List<Exame> listaAll = new ArrayList<Exame>();
		
		Exame eIni = new Exame();
		eIni.setDesExame("-- Selecione um Exame --");
		listaAll.add(eIni);
		
		Exame exame = new Exame();
		exame.setFlgAtivo("S");
		exame.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		
		List<Exame> lista = ExameService.getInstancia().pesquisar(exame, 0);
		listaAll.addAll(lista);
		
		this.setListaExames(listaAll);
	}
	
	private void popularHistoricoAtendimento() throws Exception
	{
		Agendamento agendamento = new Agendamento();
		agendamento.setFiltroMap(new HashMap<String, Object>());
		agendamento.getFiltroMap().put("orderHistorico", true);
		agendamento.setFlgAtivo("S");
		agendamento.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		agendamento.setIdPaciente(this.getEntity().getIdPaciente());
		
		List<Agendamento> lista = AgendamentoService.getInstancia().pesquisar(agendamento, AgendamentoService.JOIN_SERVICO
																						 | AgendamentoService.JOIN_CONVENIO
																						 | AgendamentoService.JOIN_PACIENTE);
		
		this.setListaHistoricoAtendimento(lista);
	}
	
	public void preparaInserirExame()
	{
		this.setDesExame(null);
	}
	
	public void inserirExame()
	{
		try
		{
			this.setMensagem(null);
			this.validarInserirExame();
			
			Exame exame = new Exame();
			exame.setDesExame(this.getDesExame());
			exame.setDatCadastro(new Date());
			exame.setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
			exame.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
			exame.setFlgAtivo("S");
			
			ExameService.getInstancia().inserir(exame);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exame cadastrado com sucesso.", null));
			
			this.setCtrMensagem(1);
			this.popularExames();
		}
		catch (Exception e)
		{
			this.setCtrMensagem(2);
			this.setMensagem(e.getMessage());
		}
	}
	
	public void adicionarExame()
	{
		try
		{
			if(this.getListaExamesSelecionados() != null
					&& this.getListaExamesSelecionados().size() > 0)
			{
				for (Exame exame : listaExamesSelecionados)
				{
					if(exame.getIdExame().intValue() == this.getExame().getIdExame().intValue())
					{
						throw new Exception("Este exame já foi adicionado!");
					}
				}
			}
			
			this.getListaExamesSelecionados().add(0, this.getExame());	
	
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
	
	public void removerExame()
	{
		this.getListaExamesSelecionados().remove(this.getExame());
	}
	
	private void validarInserirExame() throws Exception
	{
		if(this.getDesExame() == null
				|| this.getDesExame().trim().equals(""))
		{
			throw new Exception("O campo Nome do Exame é obrigatório!");
		}
	}
	
	public void concluirAtendimento()
	{
		try
		{
			this.validarAtendimento();			
			AgendamentoService.getInstancia().concluirAtendimento(this.getEntity(), this.getListaExamesSelecionados());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agendamento concluído com sucesso.", null));
			
			this.preparaAtendimento();
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
	
	private void validarAtendimento() throws Exception
	{
		if(this.getEntity().getTpAgendamento().equals("P") 
				&& (this.getEntity().getVlrAgendamentoFormatado() == null
					|| this.getEntity().getVlrAgendamentoFormatado().trim().equals("")))
		{
			throw new Exception("O campo Valor é obrigatório!");
		}
		
		if(this.getEntity().getTpAgendamento().equals("P") 
				&& (this.getEntity().getVlrDescontoFormatado() == null
					|| this.getEntity().getVlrDescontoFormatado().trim().equals("")))
		{
			throw new Exception("O campo Desconto é obrigatório!");
		}
		
		if(this.getEntity().getDesAnamnese() == null
				|| this.getEntity().getDesAnamnese().trim().equals(""))
		{
			throw new Exception("É necessário informar a Anamnese do paciente para concluir o atendimento!");
		}
		
		if(this.getEntity().getDesPrescricao() == null
				|| this.getEntity().getDesPrescricao().trim().equals(""))
		{
			throw new Exception("É necessário informar a Prescrição do paciente para concluir o atendimento!");
		}
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public Date getDatInicioAtendimento() {
		return datInicioAtendimento;
	}

	public void setDatInicioAtendimento(Date datInicioAtendimento) {
		this.datInicioAtendimento = datInicioAtendimento;
	}

	public String getDesPrescricao() {
		return desPrescricao;
	}

	public void setDesPrescricao(String desPrescricao) {
		this.desPrescricao = desPrescricao;
	}

	public List<Exame> getListaExames() {
		return listaExames;
	}

	public void setListaExames(List<Exame> listaExames) {
		this.listaExames = listaExames;
	}

	public Exame getExame() {
		return exame;
	}

	public void setExame(Exame exame) {
		this.exame = exame;
	}

	public String getDesExame() {
		return desExame;
	}

	public void setDesExame(String desExame) {
		this.desExame = desExame;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public List<Exame> getListaExamesSelecionados() {
		return listaExamesSelecionados;
	}

	public void setListaExamesSelecionados(List<Exame> listaExamesSelecionados) {
		this.listaExamesSelecionados = listaExamesSelecionados;
	}

	public Integer getCtrMensagem() {
		return ctrMensagem;
	}

	public void setCtrMensagem(Integer ctrMensagem) {
		this.ctrMensagem = ctrMensagem;
	}

	public List<Agendamento> getListaHistoricoAtendimento() {
		return listaHistoricoAtendimento;
	}

	public void setListaHistoricoAtendimento(List<Agendamento> listaHistoricoAtendimento) {
		this.listaHistoricoAtendimento = listaHistoricoAtendimento;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public List<Exame> getListaExamesPaciente() {
		return listaExamesPaciente;
	}

	public void setListaExamesPaciente(List<Exame> listaExamesPaciente) {
		this.listaExamesPaciente = listaExamesPaciente;
	}
}