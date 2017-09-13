package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.service.GrupoService;
import br.com.a2dm.cmn.service.UsuarioService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.Clinica;
import br.com.a2dm.ngc.entity.ClinicaProfissional;
import br.com.a2dm.ngc.entity.ClinicaProfissionalRec;
import br.com.a2dm.ngc.entity.Noticia;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.AgendamentoService;
import br.com.a2dm.ngc.service.ClinicaProfissionalRecService;
import br.com.a2dm.ngc.service.ClinicaProfissionalService;
import br.com.a2dm.ngc.service.NoticiaService;


@RequestScoped
@ManagedBean
public class PrincipalBean extends AbstractBean<Usuario, UsuarioService>
{
	private static final String ACAO_LOGOUT  = "login";
	
	private List<Usuario> listaProfissional;
	private List<Clinica> listaClinica;
	private List<ClinicaProfissional> listaClinicaProfissional;
	
	private List<Agendamento> listaAgnUltimaSemana;
	
	private List<Noticia> listaNoticia;
	
	private Usuario profissional;
	private Clinica clinica;
	private String msgConfig;
	
	private JSFUtil util = new JSFUtil();	
	
	public PrincipalBean()
	{
		MenuControl.ativarMenu("flgMenuDsh");
		
		if( util.getSession().getAttribute("startConfig") == null )
		{
			util.getSession().setAttribute("controleSemPermissao", new Integer(0));
			util.getSession().setAttribute("controleTrocarClinicaProfissional", new Integer(0));
			
			this.configuracaoInicio();
			
			util.getSession().setAttribute("startConfig", Integer.MIN_VALUE);
			util.getSession().setAttribute("listaProfissional", this.getListaProfissional());
			util.getSession().setAttribute("listaClinica", this.getListaClinica());
			util.getSession().setAttribute("listaClinicaProfissional", this.getListaClinicaProfissional());
		}
	}
	
	private void atualizarCountAgendamentos()
	{
		try
		{
			if(UtilFuncions.getClinicaProfissionalSession() != null)
			{
				Agendamento agendamento = new Agendamento();
				agendamento.setFlgAtivo("S");
				agendamento.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				
				HashMap<BigInteger, Long> map = AgendamentoService.getInstancia().countAgendamentoSituacao(agendamento);
				
				for (int i = 0; i < map.size(); i++)
				{
					if(map.keySet().toArray()[i].toString().equals(Integer.toString(AgendamentoService.SITUACAO_AGENDADA)))
					{
						util.getSession().setAttribute("countAgendada", map.get(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_AGENDADA))));
					}
					
					if(map.keySet().toArray()[i].toString().equals(Integer.toString(AgendamentoService.SITUACAO_PRESENTE)))
					{
						util.getSession().setAttribute("countPresente", map.get(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_PRESENTE))));
					}
					
					if(map.keySet().toArray()[i].toString().equals(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO)))
					{
						util.getSession().setAttribute("countAtendimento", map.get(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_EM_ATENDIMENTO))));
					}
					
					if(map.keySet().toArray()[i].toString().equals(Integer.toString(AgendamentoService.SITUACAO_CONCLUIDA)))
					{
						util.getSession().setAttribute("countConcluida", map.get(new BigInteger(Integer.toString(AgendamentoService.SITUACAO_CONCLUIDA))));
					}
				}
			}
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			if(e.getMessage() == null)
				FacesContext.getCurrentInstance().addMessage("", message);
			else
				FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	private void carregarGrafico()
	{
		try
		{
			if(UtilFuncions.getClinicaProfissionalSession() != null)
			{
				//DEFININDO INTERVALOS DA SEMANA PARA O GRAFICO
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				
				gc.set(GregorianCalendar.HOUR, 0);
				gc.set(GregorianCalendar.MINUTE, 0);
				gc.set(GregorianCalendar.SECOND, 0);
				gc.set(GregorianCalendar.MILLISECOND, 0);
				
				Date dataInicio = gc.getTime();			
				gc.add(GregorianCalendar.DAY_OF_MONTH, -7);			
				Date dataFim = gc.getTime();
				
				//LISTAR AGENDAMENTOS DA ULTIMA SEMANA
				Agendamento agendamentosAtivos = new Agendamento();
				agendamentosAtivos.setFlgAtivo("S");
				agendamentosAtivos.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
				agendamentosAtivos.setFiltroMap(new HashMap<String, Object>());
				agendamentosAtivos.getFiltroMap().put("datAgendamentoInicio", dataInicio);
				agendamentosAtivos.getFiltroMap().put("datAgendamentoFim", dataFim);
				
				List<Agendamento> listaAgendados = AgendamentoService.getInstancia().pesquisar(agendamentosAtivos, 0);
				this.setListaAgnUltimaSemana(listaAgendados);
			}
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			if(e.getMessage() == null)
				FacesContext.getCurrentInstance().addMessage("", message);
			else
				FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	private void carregarListaNoticias()
	{
		try
		{
			if(UtilFuncions.getClinicaProfissionalSession() != null)
			{
				Noticia noticia = new Noticia();
				List<Noticia> lista = NoticiaService.getInstancia().pesquisar(noticia, 0);
				this.setListaNoticia(lista);
			}
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			if(e.getMessage() == null)
				FacesContext.getCurrentInstance().addMessage("", message);
			else
				FacesContext.getCurrentInstance().addMessage(null, message);
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
					//CONFIGURACAO PARA ENTRAR DIRETO NO SISTEMA COM CLINICA PROFISSIONAL SELECIONADA
					util.getSession().setAttribute("clinicaProfissional", lista.get(0));
					
					this.atualizarCountAgendamentos();
					this.carregarGrafico();
					this.carregarListaNoticias();
				}
				else
				{
					util.getSession().removeAttribute("clinicaProfissional");
					
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
						
						//SETANDO CONTROLE DE RENDERIZAR O MENU DE TROCAR CLINICA / PROFISSIONAL
						util.getSession().setAttribute("controleTrocarClinicaProfissional", new Integer(1));
					}
					else
					{						
						util.getSession().setAttribute("controleSemPermissao", new Integer(1));
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
						//CONFIGURACAO PARA ENTRAR DIRETO NO SISTEMA COM CLINICA PROFISSIONAL SELECIONADA						
						util.getSession().setAttribute("clinicaProfissional", lista.get(0).getClinicaProfissional());
						
						this.atualizarCountAgendamentos();
						this.carregarGrafico();
						this.carregarListaNoticias();						
					}
					else
					{
						util.getSession().removeAttribute("clinicaProfissional");
						
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
							
							//SETANDO CONTROLE DE RENDERIZAR O MENU DE TROCAR CLINICA / PROFISSIONAL
							util.getSession().setAttribute("controleTrocarClinicaProfissional", new Integer(1));
						}
						else
						{
							util.getSession().setAttribute("controleSemPermissao", new Integer(1));
						}
					}
				}
				else
				{
					util.getSession().removeAttribute("clinicaProfissional");
					
					//USUARIO ADMINISTRADOR
					if(util.getUsuarioLogado().getIdGrupo().intValue() == GrupoService.GRUPO_ADMINISTRADOR)
					{
						util.getSession().setAttribute("controleSemPermissao", new Integer(1));
					}
					else
					{
						util.getSession().setAttribute("controleSemPermissao", new Integer(1));
					}
				}
			}
		}
		catch(Exception e)
		{
			util.getSession().setAttribute("controleSemPermissao", new Integer(1));
		}
	}

	public void alterarClinicaProfissional() 
	{
		util.getSession().removeAttribute("startConfig");
		
		
		
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
			
			this.atualizarCountAgendamentos();
			this.carregarGrafico();
			this.carregarListaNoticias();
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

	public List<Noticia> getListaNoticia() {
		return listaNoticia;
	}

	public void setListaNoticia(List<Noticia> listaNoticia) {
		this.listaNoticia = listaNoticia;
	}

	public List<Agendamento> getListaAgnUltimaSemana() {
		return listaAgnUltimaSemana;
	}

	public void setListaAgnUltimaSemana(List<Agendamento> listaAgnUltimaSemana) {
		this.listaAgnUltimaSemana = listaAgnUltimaSemana;
	}
}
