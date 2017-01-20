package br.com.a2dm.web.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.service.UsuarioService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;


@RequestScoped
@ManagedBean
public class PrincipalBean extends AbstractBean<Usuario, UsuarioService>
{
	public PrincipalBean()
	{
		super(UsuarioService.getInstancia());
		this.ACTION_SEARCH = "principal";
		this.pageTitle = "Dashboard"; 
	}
}
