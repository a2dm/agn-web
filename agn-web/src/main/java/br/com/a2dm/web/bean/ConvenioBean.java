package br.com.a2dm.web.bean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.cmn.util.validators.ValidaPermissao;
import br.com.a2dm.ngc.entity.Convenio;
import br.com.a2dm.ngc.entity.ConvenioServico;
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.service.ConvenioService;
import br.com.a2dm.ngc.service.ConvenioServicoService;
import br.com.a2dm.ngc.service.ServicoService;
import br.com.a2dm.web.configuracao.MenuControl;


@RequestScoped
@ManagedBean
public class ConvenioBean extends AbstractBean<Convenio, ConvenioService>
{	
	private JSFUtil util = new JSFUtil();
	
	private List<Servico> listaServico;
	
	private Servico servico;
	
	public ConvenioBean()
	{
		super(ConvenioService.getInstancia());
		this.ACTION_SEARCH = "convenio";
		this.pageTitle = "Configuração / Convenio";
		
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
		Servico servico = new Servico();
		servico.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		servico.setFlgAtivo("S");
		
		List<Servico> listaServico = ServicoService.getInstancia().pesquisar(servico, 0);
		
		//SETANDO FLG_ATIVO = N PARA MOSTRAR OS SERVICOS COMO INATIVOS NA TELA, REFERENCIANDO TB_CONVENIO_SERVICO
		for (Servico obj : listaServico)
		{
			obj.setFlgAtivo("N");
		}
		
		this.setListaServico(listaServico);
	}
	
	@Override
	protected void validarInserir() throws Exception
	{
		if(this.getEntity() == null
				|| this.getEntity().getDesConvenio() == null
				|| this.getEntity().getDesConvenio().trim().equals(""))
		{
			throw new Exception("O campo Descrição é obrigatório!");
		}
		
		if(this.getListaServico() != null
				&& this.getListaServico().size() > 0)
		{
			for (Servico obj : listaServico)
			{
				if(obj.getFlgAtivo() != null
						&& obj.getFlgAtivo().equals("S"))
				{
					if(obj.getVlrServicoFormatado() == null
							|| obj.getVlrServicoFormatado().trim().equals(""))
					{
						throw new Exception("O campo R$ Convênio do serviço " + obj.getIdServico() + " - " + obj.getDesServico() + " é obrigatório!");
					}
				}
			}
		}
	}
	
	@Override
	protected void completarInserir() throws Exception
	{		
		this.getEntity().setFlgAtivo("S");
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setListaServico(this.getListaServico());
	}
	
	@Override
	public void preparaAlterar() 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
			{
				super.preparaAlterar();

				Convenio convenio = new Convenio();
				convenio.setIdConvenio(this.getEntity().getIdConvenio());
				convenio = ConvenioService.getInstancia().get(convenio, 0);				
				
				this.setEntity(convenio);
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
	protected void setListaAlterar() throws Exception
	{
		this.setListaServico(null);
		
		Servico servico = new Servico();
		servico.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
		servico.setFlgAtivo("S");
		
		List<Servico> listaServico = ServicoService.getInstancia().pesquisar(servico, 0);
		
		ConvenioServico convenioServico = new ConvenioServico();
		convenioServico.setIdConvenio(this.getEntity().getIdConvenio());
		
		List<ConvenioServico> listaConvenioServico = ConvenioServicoService.getInstancia().pesquisar(convenioServico, 0);
		
		for (Servico objServico : listaServico)
		{
			objServico.setFlgAtivo("N");
			
			for (ConvenioServico objConvenioServico : listaConvenioServico)
			{
				if(objServico.getIdServico().intValue() == objConvenioServico.getIdServico().intValue())
				{
					objServico.setFlgAtivo(objConvenioServico.getFlgAtivo());
					objServico.setVlrServicoFormatado(new DecimalFormat("#,##0.00").format(objConvenioServico.getVlrReceber()));
				}
			}
		}
		
		this.setListaServico(listaServico);
	}
	
	@Override
	protected void completarAlterar() throws Exception 
	{
		this.validarInserir();
		this.getEntity().setDatAlteracao(new Date());
		this.getEntity().setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setListaServico(this.getListaServico());
	}
	
	public void inativar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_INATIVAR))
				{
					ConvenioService.getInstancia().inativar(this.getEntity());
					
					FacesMessage message = new FacesMessage("Registro inativado com sucesso!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
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
	
	public void ativar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_ATIVAR))
				{
					ConvenioService.getInstancia().ativar(this.getEntity());
					
					FacesMessage message = new FacesMessage("Registro ativado com sucesso!");
					message.setSeverity(FacesMessage.SEVERITY_INFO);
					FacesContext.getCurrentInstance().addMessage(null, message);
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
	
	public void inativarServico() 
	{	
		this.getServico().setFlgAtivo("N");
		this.getServico().setVlrServicoFormatado(null);
	}
	
	public void ativarServico() 
	{
		this.getServico().setFlgAtivo("S");
	}
	
	public void visualizar()
	{
		try
		{
			ConvenioServico convenioServico = new ConvenioServico();
			convenioServico.setFlgAtivo("S");
			convenioServico.setIdConvenio(this.getEntity().getIdConvenio());
			convenioServico.setServico(new Servico());
			convenioServico.getServico().setFlgAtivo("S");			
			
			List<ConvenioServico> listaConvenioServico = ConvenioServicoService.getInstancia().pesquisar(convenioServico, ConvenioServicoService.JOIN_SERVICO);

			this.setListaServico(new ArrayList<Servico>());
			
			for (ConvenioServico obj : listaConvenioServico)
			{
				obj.getServico().setVlrServicoFormatado(new DecimalFormat("#,##0.00").format(obj.getVlrReceber()));
				this.getListaServico().add(obj.getServico());
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

	@Override
	protected boolean validarAcesso(String acao)
	{
		boolean temAcesso = true;

		if (!ValidaPermissao.getInstancia().verificaPermissao("convenio", acao))
		{
			temAcesso = false;
			HttpServletResponse rp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			try
			{
				rp.sendRedirect("/brcli/pages/acessoNegado.jsf");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return temAcesso;
	}
	
	public List<Servico> getListaServico() {
		return listaServico;
	}

	public void setListaServico(List<Servico> listaServico) {
		this.listaServico = listaServico;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}
}
