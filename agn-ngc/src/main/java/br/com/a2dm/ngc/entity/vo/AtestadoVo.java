package br.com.a2dm.ngc.entity.vo;

import java.io.Serializable;
import java.util.Date;

/** 
 * @author Carlos Diego
 * @since 31/07/2017
 */
public class AtestadoVo implements Serializable
{
	private static final long serialVersionUID = -2086907695357206806L;

	private String nomPaciente;
	
	private String rgPaciente;
	
	private Date datAtestado;
	
	private Integer qtdDias;

	public String getNomPaciente() {
		return nomPaciente;
	}

	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}

	public String getRgPaciente() {
		return rgPaciente;
	}

	public void setRgPaciente(String rgPaciente) {
		this.rgPaciente = rgPaciente;
	}

	public Date getDatAtestado() {
		return datAtestado;
	}

	public void setDatAtestado(Date datAtestado) {
		this.datAtestado = datAtestado;
	}

	public Integer getQtdDias() {
		return qtdDias;
	}

	public void setQtdDias(Integer qtdDias) {
		this.qtdDias = qtdDias;
	}
}
