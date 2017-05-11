package br.com.a2dm.web.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.ngc.configuracao.MenuControl;
import br.com.a2dm.ngc.configuracao.UtilFuncions;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.Convenio;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.service.AgendamentoService;
import br.com.a2dm.ngc.service.ConvenioService;
import br.com.a2dm.ngc.service.ServicoService;


@RequestScoped
@ManagedBean
public class AgendamentoBean extends AbstractBean<Agendamento, AgendamentoService>
{	
	private String inicio;
	private String fim;
	
	private List<Servico> listaServico;
	private List<Convenio> listaConvenio;
	
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
		this.getEntity().setPaciente(new Paciente());
		
		Date dataInicio = new Date(inicio);		
		
		this.getEntity().setDatAgendamento(dataInicio);
		this.getEntity().setHorInicio(new SimpleDateFormat("HH:mm").format(dataInicio));
		
		
		this.popularServicos();
		this.popularConvenios();
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		this.getEntity().setPaciente(new Paciente());
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
}
