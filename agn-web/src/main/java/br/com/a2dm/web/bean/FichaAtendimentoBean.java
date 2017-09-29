package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;

import com.ibm.icu.text.SimpleDateFormat;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.AgendamentoExame;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.AgendamentoService;


@RequestScoped
@ManagedBean
public class FichaAtendimentoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	public FichaAtendimentoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "fichaAtendimento";
		this.pageTitle = "Ficha de Atendimento";
		
		MenuControl.ativarMenu("flgMenuRlt");
	}
	
	@Override
	protected void validarPesquisar() throws Exception
	{
		if((this.getSearchObject().getCpfPaciente() == null
				|| this.getSearchObject().getCpfPaciente().trim().equals(""))
				&& (this.getSearchObject().getNomPaciente() == null
				 	 || this.getSearchObject().getNomPaciente().trim().equals("")))
		{
			throw new Exception("Pelo menos um campo com * é obrigatório!");
		}
	}
	
	@Override
	protected void completarPesquisar() throws Exception
	{
		this.getSearchObject().setFlgAtivo("S");
		this.getSearchObject().setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		this.getSearchObject().setIdSituacao(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_CONCLUIDA)));
	}
	
	@Override
	protected int getJoinPesquisar()
	{
		return AgendamentoService.JOIN_SERVICO
			 | AgendamentoService.JOIN_PACIENTE
			 | AgendamentoService.JOIN_AGENDAMENTO_EXAME
			 | AgendamentoService.JOIN_EXAME;
	}
	
	public void pesquisar(ActionEvent event)
	{	
		try
		{
			if(validarAcesso(Variaveis.ACAO_PESQUISAR))
			{
				validarPesquisar();
				completarPesquisar();
				validarCampoTexto();
				List<Agendamento> lista = AgendamentoService.getInstancia().pesquisar(this.getSearchObject(),
						getJoinPesquisar(), Criteria.DISTINCT_ROOT_ENTITY);
				this.setSearchResult(lista);
				completarPosPesquisar();
				setCurrentState(STATE_SEARCH);
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
			this.setSearchResult(new ArrayList<Agendamento>());
		}
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void configuraRelatorio(Map parameters, HttpServletRequest request) throws Exception
	{
		this.REPORT_NAME = "fichaAtendimentoReport";
		
		parameters.put("NOME_PACIENTE", this.getEntity().getPaciente().getNomPaciente() == null ? "" : this.getEntity().getPaciente().getNomPaciente());
		parameters.put("CPF_PACIENTE", this.getEntity().getPaciente().getCpfPaciente() == null ? "" : this.getEntity().getPaciente().getCpfPaciente());
		parameters.put("DATA_NASCIMENTO", this.getEntity().getPaciente().getDatNascimento() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(this.getEntity().getPaciente().getDatNascimento()));
		parameters.put("SEX_PACIENTE", this.getEntity().getPaciente().getSexPaciente() == "M" ? "Masculino" : "Feminino");
		parameters.put("ANAMNESE", this.getEntity().getDesAnamnese() == null ? "" : this.getEntity().getDesAnamnese());
		parameters.put("PRESCRICAO", this.getEntity().getDesPrescricao() == null ? "" : this.getEntity().getDesPrescricao());
		
		String strExame = "";
		
		for (AgendamentoExame agendamentoExame : this.getEntity().getListaAgendamentoExame())
		{
			strExame += agendamentoExame.getExame().getDesExame() + " ; ";
		}
		
		parameters.put("EXAME", strExame);
	}
}
