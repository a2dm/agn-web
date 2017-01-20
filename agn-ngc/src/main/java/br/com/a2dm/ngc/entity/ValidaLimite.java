package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 20/07/2016
 */
@Entity
@Table(name = "vw_valida_limite", schema="agn")
@Proxy(lazy = true)
public class ValidaLimite implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_usuario")
	private BigInteger idUsuario;
	
	@Column(name = "qtd_total_clinica")
	private BigInteger qtdTotalClinica;
	
	@Column(name = "qtd_recepcionista_limite")
	private BigInteger qtdRecepcionistaLimite;
	
	@Column(name = "qtd_clinica_limite")
	private BigInteger qtdClinicaLimite;

	public BigInteger getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(BigInteger idUsuario) {
		this.idUsuario = idUsuario;
	}

	public BigInteger getQtdTotalClinica() {
		return qtdTotalClinica;
	}

	public void setQtdTotalClinica(BigInteger qtdTotalClinica) {
		this.qtdTotalClinica = qtdTotalClinica;
	}

	public BigInteger getQtdRecepcionistaLimite() {
		return qtdRecepcionistaLimite;
	}

	public void setQtdRecepcionistaLimite(BigInteger qtdRecepcionistaLimite) {
		this.qtdRecepcionistaLimite = qtdRecepcionistaLimite;
	}

	public BigInteger getQtdClinicaLimite() {
		return qtdClinicaLimite;
	}

	public void setQtdClinicaLimite(BigInteger qtdClinicaLimite) {
		this.qtdClinicaLimite = qtdClinicaLimite;
	}
}
