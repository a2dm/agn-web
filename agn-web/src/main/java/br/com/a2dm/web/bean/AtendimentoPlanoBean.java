package br.com.a2dm.web.bean;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.service.AgendamentoService;


@RequestScoped
@ManagedBean
public class AtendimentoPlanoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	private Date dataInicio;
	
	private Date dataFim;
	
	public AtendimentoPlanoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "atendimentoPlano";
		this.pageTitle = "Atendimentos por Plano";
		
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
}
