package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Horario;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.HorarioService;


@RequestScoped
@ManagedBean
public class HorarioBean extends AbstractBean<Horario, HorarioService>
{
	private List<Horario> listaHorarioDefault;
	
	public HorarioBean()
	{
		super(HorarioService.getInstancia());
		this.ACTION_SEARCH = "horario";
		this.pageTitle = "Configuração / Horarios";
		
		MenuControl.ativarMenu("flgMenuCfg");
	}
	
	public String preparaHorario()
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_INSERIR))
			{
				this.setListaHorarioDefault(this.popularHorario());
				
				if(this.getListaHorarioDefault() == null
						|| this.getListaHorarioDefault().size() <= 0)
				{
					this.setListaHorarioDefault(this.popularHorarioDefault());
				}
				
				this.setCurrentState(STATE_EDIT);
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
		
		return ACTION_SEARCH;
	}

	public void salvar(ActionEvent event) 
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_ALTERAR))
			{
				validarHorario();
				HorarioService.getInstancia().salvar(getListaHorarioDefault());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Alterações gravadas com sucesso.", null));
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
	
	public void selecionarDia()
	{
		if(this.getListaHorarioDefault() != null)
		{
			for (Horario horario : this.getListaHorarioDefault())
			{
				if(!horario.getFlgAtivo().booleanValue())
				{
					horario.setHorInicio(null);
					horario.setHorFim(null);
				}
			}
		}
	}
	
	private void validarHorario() throws Exception
	{
		if(this.getListaHorarioDefault() != null)
		{
			for (Horario horario : this.getListaHorarioDefault())
			{
				if(horario.getFlgAtivo().booleanValue())
				{
					if(horario.getHorInicio() == null || horario.getHorInicio().equals("") 
							|| horario.getHorFim() == null || horario.getHorFim().equals(""))
					{
						throw new Exception("Ao selecionar um dia da semana, para ser possível realizar agendamentos, os campos Horário Início e Horário Fim devem ser informados.");
					}
					
					//INICIO
					String[] arrayInicio = horario.getHorInicio().split(":");
					
					Integer horaIni = Integer.parseInt(arrayInicio[0]);
					Integer minutoIni = Integer.parseInt(arrayInicio[1]);
					
					if(horaIni > 23)
					{
						throw new Exception("A hora " + horario.getHorInicio() + " é inválida!");
					}
					
					if(minutoIni != 30 && minutoIni != 0)
					{
						throw new Exception("Os horários só podem ser informados no intervalo de 30 minutos.");
					}
					
					//FIM
					String[] arrayFim = horario.getHorFim().split(":");
					
					Integer horaFim = Integer.parseInt(arrayFim[0]);
					Integer minutoFim = Integer.parseInt(arrayFim[1]);
					
					if(horaFim > 23)
					{
						throw new Exception("A hora " + horario.getHorFim() + " é inválida!");
					}
					
					if(minutoFim != 30 && minutoFim != 0)
					{
						throw new Exception("Os horários só podem ser informados no intervalo de 30 minutos.");
					}
					
					
					//INICIO MAIOR QUE FIM
					if(horaIni >= horaFim)
					{
						throw new Exception("Todas as Horas de Início devem ser menores que as Horas de Término.");
					}
				}
			}
		}
	}
	
	public void limpar()
	{
		if(listaHorarioDefault != null)
		{
			for (Horario horario : listaHorarioDefault)
			{
				horario.setFlgAtivo(false);
				horario.setHorInicio(null);
				horario.setHorFim(null);
			}
		}
	}
	
	private List<Horario> popularHorario() throws Exception
	{
		Horario horario = new Horario();
		horario.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		
		List<Horario> lista = HorarioService.getInstancia().pesquisar(horario, 0);		
		return lista;
	}
	
	private List<Horario> popularHorarioDefault()
	{
		List<Horario> lista = new ArrayList<Horario>();
		
		Horario horario0 = new Horario();
		horario0.setNumHorario(new BigInteger("1"));
		horario0.setDesHorario("Domingo");
		
		Horario horario1 = new Horario();
		horario1.setNumHorario(new BigInteger("2"));
		horario1.setDesHorario("Segunda-Feira");
		
		Horario horario2 = new Horario();
		horario2.setNumHorario(new BigInteger("3"));
		horario2.setDesHorario("Terça-Feira");
		
		Horario horario3 = new Horario();
		horario3.setNumHorario(new BigInteger("4"));
		horario3.setDesHorario("Quarta-Feira");
		
		Horario horario4 = new Horario();
		horario4.setNumHorario(new BigInteger("5"));
		horario4.setDesHorario("Quinta-Feira");
		
		Horario horario5 = new Horario();
		horario5.setNumHorario(new BigInteger("6"));
		horario5.setDesHorario("Sexta-Feira");
		
		Horario horario6 = new Horario();
		horario6.setNumHorario(new BigInteger("7"));
		horario6.setDesHorario("Sabado");
		
		lista.add(horario0);
		lista.add(horario1);
		lista.add(horario2);
		lista.add(horario3);
		lista.add(horario4);
		lista.add(horario5);
		lista.add(horario6);
		
		return lista;
	}
	
	@Override
	public String getFullTitle()
	{
		return this.pageTitle;
	}
	
//	@Override
//	protected boolean validarAcesso(String acao)
//	{
//		boolean temAcesso = true;
//
//		if (!ValidaPermissao.getInstancia().verificaPermissao("horario", acao))
//		{
//			temAcesso = false;
//			HttpServletResponse rp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//			try
//			{
//				rp.sendRedirect("/agn-web/pages/acessoNegado.jsf");
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		
//		return temAcesso;
//	}

	public List<Horario> getListaHorarioDefault() {
		return listaHorarioDefault;
	}

	public void setListaHorarioDefault(List<Horario> listaHorarioDefault) {
		this.listaHorarioDefault = listaHorarioDefault;
	}
}
