package br.com.a2dm.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.a2dm.cmn.entity.Estado;
import br.com.a2dm.cmn.service.EstadoService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.ws.WebServiceCep;
import br.com.a2dm.ngc.entity.Clinica;
import br.com.a2dm.ngc.service.ClinicaService;
import br.com.a2dm.web.configuracao.MenuControl;


@RequestScoped
@ManagedBean
public class ClinicaBean extends AbstractBean<Clinica, ClinicaService>
{	
	private JSFUtil util = new JSFUtil();
	
	private List<Estado> listaEstado;
	
	public ClinicaBean()
	{
		super(ClinicaService.getInstancia());
		this.ACTION_SEARCH = "clinica";
		this.pageTitle = "Configuração / Clinica";
		
		MenuControl.ativarMenu("flgMenuCfg");
	}
	
	@Override
	protected void completarPesquisar() throws Exception
	{
		if(this.getSearchObject().getFlgAtivo() != null
				&& this.getSearchObject().getFlgAtivo().equals("T"))
		{
			this.getSearchObject().setFlgAtivo(null);
		}
		
		//RECUPERAR SOMENTE OS REGISTROS DO PROFISSIONAL LOGADO
		this.getSearchObject().setIdUsuario(util.getUsuarioLogado().getIdUsuario());
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		this.getSearchObject().setFlgAtivo("T");		
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

	@Override
	protected void validarInserir() throws Exception
	{
		if(this.getEntity() == null
				|| this.getEntity().getDesClinica() == null
				|| this.getEntity().getDesClinica().trim().equals(""))
		{
			throw new Exception("O campo Descrição é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getTlfClinica() == null
				|| this.getEntity().getTlfClinica().trim().equals(""))
		{
			throw new Exception("O campo Telefone é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getCepClinica() == null
				|| this.getEntity().getCepClinica().trim().equals(""))
		{
			throw new Exception("O campo Cep é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getLgdClinica() == null
				|| this.getEntity().getLgdClinica().trim().equals(""))
		{
			throw new Exception("O campo Logradouro é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getNumEndClinica() == null
				|| this.getEntity().getNumEndClinica().intValue() <= 0)
		{
			throw new Exception("O campo Número é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getBroClinica() == null
				|| this.getEntity().getBroClinica().trim().equals(""))
		{
			throw new Exception("O campo Bairro é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getCidClinica() == null
				|| this.getEntity().getCidClinica().trim().equals(""))
		{
			throw new Exception("O campo Cidade é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getIdEstado() == null
				|| this.getEntity().getIdEstado().intValue() <= 0)
		{
			throw new Exception("O campo Estado é obrigatório!");
		}
	}

	@Override
	protected void completarInserir() throws Exception
	{
		this.getEntity().setFlgAtivo("S");
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setIdUsuario(util.getUsuarioLogado().getIdUsuario());
	}
	
	public void buscarCep()
	{
		try
		{
			if(this.getEntity().getCepClinica() != null
					&& !this.getEntity().getCepClinica().equals(""))
			{
				String cep = this.getEntity().getCepClinica().replace("-", "");
				
				WebServiceCep webServiceCep = WebServiceCep.searchCep(cep);
				
				this.getEntity().setLgdClinica(webServiceCep.getLogradouroFull().toUpperCase().trim().equals("") ? null : webServiceCep.getLogradouroFull().toUpperCase());
				this.getEntity().setBroClinica(webServiceCep.getBairro().toUpperCase());
				this.getEntity().setCidClinica(webServiceCep.getCidade().toUpperCase());

				if(webServiceCep.getUf() != null
						&& !webServiceCep.getUf().trim().equals("")) 
				{
					Estado estado = new Estado();
					estado.setSigla(webServiceCep.getUf().toUpperCase());
					estado = EstadoService.getInstancia().get(estado, 0);
					
					
					if(estado != null)
					{
						this.getEntity().setIdEstado(estado.getIdEstado());
					}
					else
					{
						this.getEntity().setIdEstado(null);
					}
				}
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
	public void cancelar(ActionEvent event)
	{
		super.cancelar(event);
		this.getSearchObject().setFlgAtivo("T");
	}
	
	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}
}
