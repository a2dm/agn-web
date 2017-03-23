package br.com.a2dm.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.service.GrupoService;
import br.com.a2dm.cmn.service.UsuarioService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Clinica;
import br.com.a2dm.ngc.entity.ClinicaProfissional;
import br.com.a2dm.ngc.service.ClinicaProfissionalService;


@RequestScoped
@ManagedBean
public class PrincipalBean extends AbstractBean<Usuario, UsuarioService>
{
	private List<Usuario> listaProfissional;
	private List<Clinica> listaClinica;
	private List<ClinicaProfissional> listaClinicaProfissional;
	
	private Usuario profissional;
	private Clinica clinica;
	
	private String msgConfig;
	
	private JSFUtil util = new JSFUtil();
	
	public PrincipalBean()
	{
		util.getSession().removeAttribute("clinicaProfissional");
		this.configuracaoInicio();
	}
	
	public void configuracaoInicio()
	{
		try
		{
			this.setListaProfissional(new ArrayList<Usuario>());
			this.setListaClinica(new ArrayList<Clinica>());
			
			//USUARIO PROFISSIONAL
			if(util.getUsuarioLogado().getIdGrupo().intValue() == GrupoService.GRUPO_PROFISSIONAL)
			{
				this.getListaProfissional().add(util.getUsuarioLogado());
				
				ClinicaProfissional clinicaProfissional = new ClinicaProfissional();
				clinicaProfissional.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
				
				List<ClinicaProfissional> lista = ClinicaProfissionalService.getInstancia().pesquisar(clinicaProfissional, ClinicaProfissionalService.JOIN_CLINICA
																														 | ClinicaProfissionalService.JOIN_PROFISSIONAL);
				
				if(lista != null
						&& lista.size() == 1)
				{
					util.getSession().setAttribute("clinicaProfissional", lista.get(0));					
				}
				else
				{
					if(lista != null
							&& lista.size() > 1)
					{
						this.setListaClinicaProfissional(lista);
						this.setProfissional(this.getListaClinicaProfissional().get(0).getUsuario());
									
						Clinica cli = new Clinica();
						cli.setDesClinica("Selecione uma Clinica");
						this.getListaClinica().add(cli);
						
						for (ClinicaProfissional obj : lista)
						{
							Clinica clinica = new Clinica();
							JSFUtil.copiarPropriedades(obj.getClinica(), clinica);
							
							this.getListaClinica().add(clinica);
						}				        
					}
					else
					{
						//NAO EXISTE CLINICA VINCULADA, REDIRECIONA PARA LOGIN
						this.redirectLogout();
					}
				}
			}
			else
			{
				//USUARIO RECEPCIONISTA
				if(util.getUsuarioLogado().getIdGrupo().intValue() == GrupoService.GRUPO_RECEPCIONISTA)
				{
					
				}
				else
				{
					//USUARIO ADMINISTRADOR
					if(util.getUsuarioLogado().getIdGrupo().intValue() == GrupoService.GRUPO_ADMINISTRADOR)
					{
						this.redirectLogout();
					}
					else
					{
						//USUARIO NAO PERTENCE A NENHUM GRUPO, REDIRECIONA PARA LOGIN
						this.redirectLogout();
					}
				}
			}
			
			
		}	
		catch(Exception e)
		{
			
			///mensagem
			
		}
	}

	public void iniciar()
	{
		try
		{
			this.setMsgConfig(null);
			this.validarIniciar();
			
			ClinicaProfissional clinicaProfissional = new ClinicaProfissional();
			clinicaProfissional.setIdUsuario(this.getProfissional().getIdUsuario());
			clinicaProfissional.setIdClinica(this.getClinica().getIdClinica());
			
			clinicaProfissional = ClinicaProfissionalService.getInstancia().get(clinicaProfissional, ClinicaProfissionalService.JOIN_CLINICA
																								   | ClinicaProfissionalService.JOIN_PROFISSIONAL);
			
			util.getSession().setAttribute("clinicaProfissional", clinicaProfissional);
			
		}
		catch (Exception e)
	    {
	       this.setMsgConfig(e.getMessage());
	    }
	}
	
	private void validarIniciar() throws Exception
	{
		if(this.getProfissional() == null)
		{
			throw new Exception("O campo Profissional é obrigatório.");
		}
		
		if(this.getClinica() == null
				|| this.getClinica().getIdClinica() == null)
		{
			throw new Exception("O campo Clínica é obrigatório.");
		}
	}
	
	private void redirectLogout() throws Exception
	{
		//util.getSession().invalidate();
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		((HttpServletResponse) response).sendRedirect(request.getContextPath() + "/pages/login.jsf");
	}
	
	public List<ClinicaProfissional> getListaClinicaProfissional() {
		return listaClinicaProfissional;
	}

	public void setListaClinicaProfissional(List<ClinicaProfissional> listaClinicaProfissional) {
		this.listaClinicaProfissional = listaClinicaProfissional;
	}

	public List<Usuario> getListaProfissional() {
		return listaProfissional;
	}

	public void setListaProfissional(List<Usuario> listaProfissional) {
		this.listaProfissional = listaProfissional;
	}

	public List<Clinica> getListaClinica() {
		return listaClinica;
	}

	public void setListaClinica(List<Clinica> listaClinica) {
		this.listaClinica = listaClinica;
	}

	public Usuario getProfissional() {
		return profissional;
	}

	public void setProfissional(Usuario profissional) {
		this.profissional = profissional;
	}

	public Clinica getClinica() {
		return clinica;
	}

	public void setClinica(Clinica clinica) {
		this.clinica = clinica;
	}

	public String getMsgConfig() {
		return msgConfig;
	}

	public void setMsgConfig(String msgConfig) {
		this.msgConfig = msgConfig;
	}
}
