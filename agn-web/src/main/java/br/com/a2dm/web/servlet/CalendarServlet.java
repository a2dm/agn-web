package br.com.a2dm.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.a2dm.ngc.entity.Agendamento;
import br.com.a2dm.ngc.entity.vo.AgendamentoVo;
import br.com.a2dm.ngc.service.AgendamentoService;

public class CalendarServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try
    	{
	    	SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
	    	
	    	//LISTA TODOS OS AGENDAMENTOS DO DIA 20 (MES ANTERIOR) AO DIA 20 (PROX MES)
	    	Date data = sDate.parse(request.getParameter("dtAgn"));
	    	
	    	GregorianCalendar gc1 = new GregorianCalendar();
	    	gc1.setTime(data);
	    	gc1.set(Calendar.DAY_OF_MONTH, 1);
	    	gc1.add(GregorianCalendar.DAY_OF_MONTH, -20);	    	
	    	Date dataInicioFiltro = gc1.getTime();
	    	
	    	GregorianCalendar gc2 = new GregorianCalendar();
	    	gc2.setTime(data);
	    	gc2.set(Calendar.DAY_OF_MONTH, 28);
	    	gc2.add(GregorianCalendar.DAY_OF_MONTH, 20);	    	
	    	Date dataFimFiltro = gc2.getTime();	    	
	    	
	    	Agendamento agendamento = new Agendamento();
	    	agendamento.setFiltroMap(new HashMap<String, Object>());
	    	agendamento.getFiltroMap().put("datAgendamentoInicio", dataInicioFiltro);
	    	agendamento.getFiltroMap().put("datAgendamentoFim", dataFimFiltro);
	    	agendamento.setIdClinicaProfissional(new BigInteger(request.getParameter("idCliPro")));
	    	agendamento.setFlgAtivo("S");	    	
	    
	    	List<Agendamento> lista = AgendamentoService.getInstancia().pesquisar(agendamento, AgendamentoService.JOIN_SERVICO);
	    	List<AgendamentoVo> jsonLista = new ArrayList<AgendamentoVo>();		
	    			
			for (Agendamento obj : lista)
			{
				AgendamentoVo agendamentoVo = new AgendamentoVo();
				agendamentoVo.setId(obj.getIdAgendamento());
				agendamentoVo.setTitle(obj.getNomPaciente() + " - (" + obj.getServico().getDesServico() +")");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				GregorianCalendar gcIni = new GregorianCalendar();
				gcIni.setTime(obj.getDatAgendamento());
				
				gcIni.set(GregorianCalendar.HOUR, Integer.parseInt(obj.getHorInicio().split(":")[0]));
				gcIni.set(GregorianCalendar.MINUTE, Integer.parseInt(obj.getHorInicio().split(":")[1]));
				gcIni.set(GregorianCalendar.SECOND, 0);
				gcIni.set(GregorianCalendar.MILLISECOND, 0);
				agendamentoVo.setStart(sdf.format(gcIni.getTime()));
				
				
				GregorianCalendar gcFim = new GregorianCalendar();
				gcFim.setTime(obj.getDatAgendamento());
				
				gcFim.set(GregorianCalendar.HOUR, Integer.parseInt(obj.getHorFim().split(":")[0]));
				gcFim.set(GregorianCalendar.MINUTE, Integer.parseInt(obj.getHorFim().split(":")[1]));
				gcFim.set(GregorianCalendar.SECOND, 0);
				gcFim.set(GregorianCalendar.MILLISECOND, 0);
				agendamentoVo.setEnd(sdf.format(gcFim.getTime()));
				
				agendamentoVo.setAllDay(false);
				
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_AGENDADA)
				{
					agendamentoVo.setClassName("agendada");
				}
				
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_PRESENTE)
				{
					agendamentoVo.setClassName("presente");
				}
				
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_EM_ATENDIMENTO)
				{
					agendamentoVo.setClassName("atendimento");
				}
				
				if(obj.getIdSituacao().intValue() == AgendamentoService.SITUACAO_CONCLUIDA)
				{
					agendamentoVo.setClassName("concluida");
				}
				
				jsonLista.add(agendamentoVo);
			}
	
			response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();
	        out.write(new Gson().toJson(jsonLista));
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
		}
    }
}