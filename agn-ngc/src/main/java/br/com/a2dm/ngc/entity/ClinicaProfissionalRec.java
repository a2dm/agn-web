package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Proxy;

import br.com.a2dm.cmn.entity.Usuario;

/** 
 * @author Carlos Diego
 * @since 23/03/2017
 */
@Entity
@Table(name = "tb_clinica_profissional_rec", schema="agn")
@SequenceGenerator(name = "SQ_CLINICA_PROFISSIONAL_REC", sequenceName = "SQ_CLINICA_PROFISSIONAL_REC", allocationSize = 1)
@Proxy(lazy = true)
public class ClinicaProfissionalRec implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CLINICA_PROFISSIONAL_REC")
	@Column(name = "id_clinica_profissional_rec")
	private BigInteger idClinicaProfissionalRec;
	
	@Column(name = "id_clinica_profissional")
	private BigInteger idClinicaProfissional;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_clinica_profissional", insertable = false, updatable = false)
	private ClinicaProfissional clinicaProfissional;
	
	@Column(name = "id_usuario_rec")
	private BigInteger idUsuarioRec;
		
	@Column(name = "id_usuario_cad")
	private BigInteger idUsuarioCad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_cad", insertable = false, updatable = false)
	private Usuario usuarioCad;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;

	
	public BigInteger getIdClinicaProfissionalRec() {
		return idClinicaProfissionalRec;
	}

	public void setIdClinicaProfissionalRec(BigInteger idClinicaProfissionalRec) {
		this.idClinicaProfissionalRec = idClinicaProfissionalRec;
	}

	public BigInteger getIdClinicaProfissional() {
		return idClinicaProfissional;
	}

	public void setIdClinicaProfissional(BigInteger idClinicaProfissional) {
		this.idClinicaProfissional = idClinicaProfissional;
	}

	public BigInteger getIdUsuarioRec() {
		return idUsuarioRec;
	}

	public void setIdUsuarioRec(BigInteger idUsuarioRec) {
		this.idUsuarioRec = idUsuarioRec;
	}

	public BigInteger getIdUsuarioCad() {
		return idUsuarioCad;
	}

	public void setIdUsuarioCad(BigInteger idUsuarioCad) {
		this.idUsuarioCad = idUsuarioCad;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
	}

	public ClinicaProfissional getClinicaProfissional() {
		return clinicaProfissional;
	}

	public void setClinicaProfissional(ClinicaProfissional clinicaProfissional) {
		this.clinicaProfissional = clinicaProfissional;
	}

	public Usuario getUsuarioCad() {
		return usuarioCad;
	}

	public void setUsuarioCad(Usuario usuarioCad) {
		this.usuarioCad = usuarioCad;
	}
}
