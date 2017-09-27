package br.com.a2dm.web.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.ngc.entity.Agendamento;
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
	}
	
	@Override
	protected int getJoinPesquisar()
	{	
		return AgendamentoService.JOIN_SERVICO;
	}
}
