package br.com.a2dm.ngc.configuracao;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.com.a2dm.ngc.entity.ClinicaProfissional;

public class UtilFuncions
{
	private static final String CLINICA_PROFISSIONAL_SESSION = "clinicaProfissional";
	
	public static ClinicaProfissional getClinicaProfissionalSession() throws Exception
	{
		Object clinicaProfissional = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute(CLINICA_PROFISSIONAL_SESSION);
    	
    	if (clinicaProfissional == null)
    	{
    		throw new Exception("Não existe Clinica/Profissional selecionado para o sistema.");
    	}

    	return (ClinicaProfissional) clinicaProfissional;
	}

}
