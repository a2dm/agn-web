package br.com.a2dm.web.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.ibm.icu.text.SimpleDateFormat;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
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
			this.validarGerar();
			
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

	private void validarGerar() throws Exception
	{
//		if(this.getAtestado().getNomPaciente() == null
//				|| this.getAtestado().getNomPaciente().trim().equals(""))
//		{
//			throw new Exception("O campo Paciente é obrigatório!");
//		}
//		
//		if(this.getAtestado().getRgPaciente() == null
//				|| this.getAtestado().getRgPaciente().trim().equals(""))
//		{
//			throw new Exception("O campo Rg é obrigatório!");
//		}
//		
//		if(this.getAtestado().getDatAtestado() == null
//				|| this.getAtestado().getDatAtestado().toString().equals(""))
//		{
//			throw new Exception("O campo Data do Atestado é obrigatório!");
//		}
//		
//		if(this.getAtestado().getQtdDias() == null
//				|| this.getAtestado().getQtdDias().intValue() <= 0)
//		{
//			throw new Exception("O campo Dias é obrigatório!");
//		}
		
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