package br.com.a2dm.ngc.entity.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/** 
 * @author Carlos Diego
 * @since 31/05/2017
 */
public class AgendamentoVo implements Serializable
{
	private static final long serialVersionUID = -2086907695357206806L;

	private BigInteger id;
	
	private String nomePaciente;
	
	private Date dataAgendamento;
	
	private String title;
	
	private String start;
	
	private String end;
	
	private boolean allDay;
	
	private String className;
	

	public String getNomePaciente() {
		return nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
