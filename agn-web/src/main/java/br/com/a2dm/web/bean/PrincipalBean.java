package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.service.GrupoService;
import br.com.a2dm.cmn.service.UsuarioService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Clinica;
import br.com.a2dm.ngc.entity.ClinicaProfissional;
import br.com.a2dm.ngc.entity.ClinicaProfissionalRec;
import br.com.a2dm.ngc.service.ClinicaProfissionalRecService;
import br.com.a2dm.ngc.service.ClinicaProfissionalService;


@RequestScoped
@ManagedBean
public class PrincipalBean extends AbstractBean<Usuario, UsuarioService>
{
	private static final String ACAO_LOGOUT  = "login";
	
	private List<Usuario> listaProfissional;
	private List<Clinica> listaClinica;
	private List<ClinicaProfissional> listaClinicaProfissional;
	
	private Usuario profissional;
	private Clinica clinica;
	private String msgConfig;
	
	private JSFUtil util = new JSFUtil();
	
	private int controle;	
	
	public PrincipalBean()
	{
		this.setControle(0);
		
		if( util.getSession().getAttribute("startConfig") == null )
		{
			this.configuracaoInicio();
			util.getSession().setAttribute("startConfig", Integer.MIN_VALUE);
		}
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
						this.setControle(1);
					}
				}
			}
			else
			{
				//USUARIO RECEPCIONISTA
				if(util.getUsuarioLogado().getIdGrupo().intValue() == GrupoService.GRUPO_RECEPCIONISTA)
				{
					ClinicaProfissionalRec clinicaProfissionalRec = new ClinicaProfissionalRec();
					clinicaProfissionalRec.setIdUsuarioRec(util.getUsuarioLogado().getIdUsuario());
					
					List<ClinicaProfissionalRec> lista = ClinicaProfissionalRecService.getInstancia().pesquisar(clinicaProfissionalRec, ClinicaProfissionalRecService.JOIN_CLINICA_PROFISSIONAL
																																	  | ClinicaProfissionalRecService.JOIN_CLINICA_PROFISSIONAL_CLINICA
																																	  | ClinicaProfissionalRecService.JOIN_CLINICA_PROFISSIONAL_PROFISSIONAL);
					
					if(lista != null
							&& lista.size() == 1)
					{
						util.getSession().setAttribute("clinicaProfissional", lista.get(0).getClinicaProfissional());
					}
					else
					{
						if(lista != null
								&& lista.size() > 1)
						{
							List<ClinicaProfissional> listaClinicaProfissional = new ArrayList<ClinicaProfissional>();
							
							List<Clinica> listaClinica = new ArrayList<Clinica>();
							Clinica cIni = new Clinica();
							cIni.setDesClinica("Selecione uma Clinica");
							listaClinica.add(cIni);
							
							List<Usuario> listaProfissional = new ArrayList<Usuario>();
							Usuario pIni = new Usuario();
							pIni.setNome("Selecione um Profissional");
							listaProfissional.add(pIni);
							
							for (ClinicaProfissionalRec cpr : lista)
							{
								listaClinicaProfissional.add(cpr.getClinicaProfissional());
								listaProfissional.add(cpr.getClinicaProfissional().getUsuario());
								listaClinica.add(cpr.getClinicaProfissional().getClinica());
							}
							
							//ELIMINANDO REPETIDOS DAS LISTAS DE CLINICA E PROFISSIONAL
							HashMap<BigInteger, Clinica> mapClinica = new HashMap<BigInteger, Clinica>();
							HashMap<BigInteger, Usuario> mapProfissional = new HashMap<BigInteger, Usuario>();
							
							for (Usuario objU : listaProfissional)
							{
								mapProfissional.put(objU.getIdUsuario(), objU);
							}
							
							for (Clinica objC : listaClinica)
							{
								mapClinica.put(objC.getIdClinica(), objC);
							}
							
							this.setListaClinicaProfissional(listaClinicaProfissional);
							
							this.setListaClinica(new ArrayList<Clinica>(mapClinica.values()));
							this.setListaProfissional(new ArrayList<Usuario>(mapProfissional.values()));							
						}
						else
						{
							this.setControle(1);
						}
					}
				}
				else
				{
					//USUARIO ADMINISTRADOR
					if(util.getUsuarioLogado().getIdGrupo().intValue() == GrupoService.GRUPO_ADMINISTRADOR)
					{
						this.setControle(1);
					}
					else
					{
						this.setControle(1);
					}
				}
			}
		}
		catch(Exception e)
		{
			this.setControle(1);
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
		if(this.getProfissional() == null
				|| this.getProfissional().getIdUsuario() == null)
		{
			throw new Exception("O campo Profissional é obrigatório.");
		}
		
		if(this.getClinica() == null
				|| this.getClinica().getIdClinica() == null)
		{
			throw new Exception("O campo Clínica é obrigatório.");
		}
	}
	
	public String redirectLogout() throws Exception
	{		
		util.getSession().invalidate();
		
		return ACAO_LOGOUT;
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

	public int getControle() {
		return controle;
	}

	public void setControle(int controle) {
		this.controle = controle;
	}
}
