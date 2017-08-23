package br.com.a2dm.ngc.functions;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.ClinicaProfissional;
import br.com.a2dm.ngc.entity.vo.RetornoVo;

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
	
	public static RetornoVo checkHoraAgendamento(List<Agendamento> lista, String horaInicio, String horaFim)
	{
		RetornoVo retorno = new RetornoVo();
		retorno.setFlgRetorno(false);
		
		if(lista != null
				&& horaInicio != null && horaFim != null
				&& !horaInicio.equals("") && !horaFim.equals(""))
		{
		
			int hr1 = Integer.parseInt(horaInicio.replace(":", ""));
			int hr2 = Integer.parseInt(horaFim.replace(":", ""));
			
			for (Agendamento agendamento : lista)
			{
				int hrBd1 = Integer.parseInt(agendamento.getHorInicio().replace(":", ""));
				int hrBd2 = Integer.parseInt(agendamento.getHorFim().replace(":", ""));
				
				if(hrBd1 < hr1 && hrBd2 > hr2
						|| hrBd1 < hr1 && hrBd2 > hr1
						|| hrBd1 > hr1 && hrBd2 < hr2
						|| hrBd1 < hr2 && hrBd2 > hr2)
				{
					retorno.setFlgRetorno(true);
					retorno.setMensagem("Existe agendamento cadastrado neste período. Hora Ínicio: " + agendamento.getHorInicio() + " , Hora Fim: " + agendamento.getHorFim());
				}					
			}
		}	
		
		return retorno;
	}

}
