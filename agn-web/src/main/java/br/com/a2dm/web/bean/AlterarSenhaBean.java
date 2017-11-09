package br.com.a2dm.web.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.service.UsuarioService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.functions.MenuControl;


@RequestScoped
@ManagedBean
public class AlterarSenhaBean extends AbstractBean<Usuario, UsuarioService>
{
	private String senhaAtual;
	private String novaSenha;
	private String confirmaNovaSenha;
	
	private JSFUtil util = new JSFUtil();
	
	
	public AlterarSenhaBean()
	{
		super(UsuarioService.getInstancia());
		this.ACTION_SEARCH = "alterarSenha";
		this.pageTitle = "Alterar Senha";
		
		MenuControl.ativarMenu("");
	}
	
	public void salvar()
	{
		try
		{
			this.validarAlterarSenha();
			
			Usuario usuario = util.getUsuarioLogado();
			UsuarioService.getInstancia().alterarSenha(usuario, senhaAtual, novaSenha);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sua senha foi alterada com sucesso.", null));
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	private void validarAlterarSenha() throws Exception
	{
		if(this.getSenhaAtual() == null
				|| this.getSenhaAtual().equals(""))
		{
			throw new Exception("O campo Senha Atual é obrigatório.");
		}
		
		if(this.getNovaSenha() == null
				|| this.getNovaSenha().equals(""))
		{
			throw new Exception("O campo Nova Senha é obrigatório.");
		}
		
		if(this.getConfirmaNovaSenha() == null
				|| this.getConfirmaNovaSenha().equals(""))
		{
			throw new Exception("O campo Confirma Nova Senha é obrigatório.");
		}
		
		if(!this.getNovaSenha().equals(this.getConfirmaNovaSenha()))
		{
			throw new Exception("O campo Nova Senha deve ser igual ao campo Confirma Nova Senha.");
		}
	}
	
	@Override
	public String getFullTitle()
	{
		return this.pageTitle;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaNovaSenha() {
		return confirmaNovaSenha;
	}

	public void setConfirmaNovaSenha(String confirmaNovaSenha) {
		this.confirmaNovaSenha = confirmaNovaSenha;
	}
}
