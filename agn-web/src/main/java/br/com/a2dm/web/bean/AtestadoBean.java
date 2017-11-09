package br.com.a2dm.web.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.icu.text.SimpleDateFormat;

import br.com.a2dm.cmn.service.GrupoService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.entity.vo.AtestadoVo;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.PacienteService;


@RequestScoped
@ManagedBean
public class AtestadoBean extends AbstractBean<Paciente, PacienteService>
{
	private AtestadoVo atestado;
	
	private JSFUtil util = new JSFUtil();
	
	public AtestadoBean()
	{
		super(PacienteService.getInstancia());
		this.ACTION_SEARCH = "atestado";
		this.pageTitle = "Atestado";
		
		MenuControl.ativarMenu("flgMenuAts");	
	}
	
	public void gerar()
	{
		try
		{
			Paciente paciente = new Paciente();
			List<Paciente> lista = new ArrayList<Paciente>();
			lista.add(paciente);
			
			this.setSearchResult(lista);
			this.imprimir();
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
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void configuraRelatorio(Map parameters, HttpServletRequest request) throws Exception
	{
		this.REPORT_NAME = "atestadoReport";
		
		parameters.put("NOME_PACIENTE", this.getAtestado().getNomPaciente() == null ? "" : this.getAtestado().getNomPaciente());
		parameters.put("RG_PACIENTE", this.getAtestado().getRgPaciente() == null ? "" : this.getAtestado().getRgPaciente());		
		parameters.put("DATA_ATESTADO", this.getAtestado().getDatAtestado() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(this.getAtestado().getDatAtestado()));
		parameters.put("DIAS", this.getAtestado().getQtdDias());
		parameters.put("PROFISSIONAL", "Dr. " + UtilFuncions.getClinicaProfissionalSession().getUsuario().getNome());
	}
	
	@Override
	public String getFullTitle()
	{
		return this.pageTitle;
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
	protected void setValoresDefault() throws Exception
	{
		this.setAtestado(new AtestadoVo());
	}

	public AtestadoVo getAtestado() {
		return atestado;
	}

	public void setAtestado(AtestadoVo atestado) {
		this.atestado = atestado;
	}
}