package br.com.a2dm.web.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.ngc.configuracao.MenuControl;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.service.AgendamentoService;


@RequestScoped
@ManagedBean
public class AgendamentoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	public AgendamentoBean()
	{
		super(AgendamentoService.getInstancia());
		this.ACTION_SEARCH = "agendamento";
		this.pageTitle = "Agendamento";
		
		MenuControl.ativarMenu("flgMenuAgn");
	}
}
