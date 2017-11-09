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
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import br.com.a2dm.cmn.service.GrupoService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Servico;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.ServicoService;


@RequestScoped
@ManagedBean
public class ServicoBean extends AbstractBean<Servico, ServicoService>
{
	private List<SelectItem> listaDuracao;
	
	private JSFUtil util = new JSFUtil();
	
	public ServicoBean()
	{
		super(ServicoService.getInstancia());
		this.ACTION_SEARCH = "servico";
		this.pageTitle = "Configuração / Serviço";
		
		MenuControl.ativarMenu("flgMenuCfg");
	}
	
	@Override
	protected void setValoresDefault() throws Exception
	{
		this.getSearchObject().setFlgAtivo("T");		
	}
	
	@Override
	protected void setListaInserir() throws Exception
	{
		List<SelectItem> lista = new ArrayList<SelectItem>();
		
		SelectItem si0 = new SelectItem(new Integer(0), "Escolha a Duração");
		SelectItem si1 = new SelectItem(new Integer(15), "15 minutos");
		SelectItem si2 = new SelectItem(new Integer(30), "30 minutos");
		SelectItem si3 = new SelectItem(new Integer(45), "45 minutos");
		SelectItem si4 = new SelectItem(new Integer(60), "60 minutos");
		
		lista.add(si0);
		lista.add(si1);
		lista.add(si2);
		lista.add(si3);
		lista.add(si4);
		
		this.setListaDuracao(lista);		
	}
	
	@Override
	protected void completarPesquisar() throws Exception
	{
		if(this.getSearchObject().getFlgAtivo() != null
				&& this.getSearchObject().getFlgAtivo().equals("T"))
		{
			this.getSearchObject().setFlgAtivo(null);
		}
		
		//RECUPERAR SOMENTE OS REGISTROS DO PROFISSIONAL RESPONSAVEL
		this.getSearchObject().setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
	}
	
	@Override
	protected void validarInserir() throws Exception
	{
		if(this.getEntity() == null
				|| this.getEntity().getDesServico() == null
				|| this.getEntity().getDesServico().trim().equals(""))
		{
			throw new Exception("O campo Descrição é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getVlrServicoFormatado() == null
				|| this.getEntity().getVlrServicoFormatado().trim().equals(""))
		{
			throw new Exception("O campo Valor é obrigatório!");
		}
		
		if(this.getEntity() == null
				|| this.getEntity().getDrcServico() == null
				|| this.getEntity().getDrcServico().intValue() <= 0)
		{
			throw new Exception("O campo Duração é obrigatório!");
		}
	}
	
	@Override
	protected void completarInserir() throws Exception
	{
		this.getEntity().setVlrServico(new Double(this.getEntity().getVlrServicoFormatado().toString().replace(".", "").replace(",", ".")));
		this.getEntity().setFlgAtivo("S");
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
	}
	
	@Override
	public void preparaAlterar() 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
			{
				super.preparaAlterar();

				Servico servico = new Servico();
				servico.setIdServico(this.getEntity().getIdServico());
				servico = ServicoService.getInstancia().get(servico, 0);				
				servico.setVlrServicoFormatado(new DecimalFormat("#,##0.00").format(servico.getVlrServico()));
				
				this.setEntity(servico);
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
	protected void completarAlterar() throws Exception 
	{
		this.validarInserir();
		this.getEntity().setDatAlteracao(new Date());
		this.getEntity().setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setVlrServico(new Double(this.getEntity().getVlrServicoFormatado().toString().replace(".", "").replace(",", ".")));
	}
	
	public void inativar() 
	{		
		try
		{
			if(this.getEntity() != null)
			{
				if(validarAcesso(Variaveis.ACAO_INATIVAR))
				{
					ServicoService.getInstancia().inativar(this.getEntity());
					
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
					ServicoService.getInstancia().ativar(this.getEntity());
					
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
	
	@Override
	protected boolean validarAcesso(String acao)
	{
		boolean temAcesso = true;

		try
		{
			if (util.getUsuarioLogado().getIdGrupo().intValue() != GrupoService.GRUPO_ADMINISTRADOR
					&& util.getUsuarioLogado().getIdGrupo().intValue() != GrupoService.GRUPO_PROFISSIONAL)
			{
				temAcesso = false;
				HttpServletResponse rp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();				
				rp.sendRedirect("/agn-web/pages/acessoNegado.jsf");				
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return temAcesso;
	}
	
	@Override
	public void cancelar(ActionEvent event)
	{
		super.cancelar(event);
		this.getSearchObject().setFlgAtivo("T");
	}

	public List<SelectItem> getListaDuracao() {
		return listaDuracao;
	}

	public void setListaDuracao(List<SelectItem> listaDuracao) {
		this.listaDuracao = listaDuracao;
	}
}
