package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Proxy;

import br.com.a2dm.cmn.entity.Usuario;

/** 
 * @author Carlos Diego
 * @since 19/04/2017
 */
@Entity
@Table(name = "tb_horario", schema="agn")
@SequenceGenerator(name = "SQ_HORARIO", sequenceName = "SQ_HORARIO", allocationSize = 1)
@Proxy(lazy = true)
public class Horario implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_HORARIO")
	@Column(name = "id_horario")
	private BigInteger idHorario;

	@Column(name = "num_horario")
	private BigInteger numHorario;
	
	@Column(name = "des_horario")
	private String desHorario;
	
	@Column(name = "hor_inicio")
	private String horInicio;
	
	@Column(name = "hor_fim")
	private String horFim;
	
	@Column(name = "id_clinica_profissional")
	private BigInteger idClinicaProfissional;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_alteracao")
	private Date datAlteracao;
	
	@Column(name = "id_usuario_alt")
	private BigInteger idUsuarioAlt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_alt", insertable = false, updatable = false)
	private Usuario usuarioAlt;
	
	@Column(name = "flg_ativo")
	private boolean flgAtivo;
	
	@Transient
	private HashMap<String, Object> filtroMap;

	public BigInteger getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(BigInteger idHorario) {
		this.idHorario = idHorario;
	}

	public BigInteger getNumHorario() {
		return numHorario;
	}

	public void setNumHorario(BigInteger numHorario) {
		this.numHorario = numHorario;
	}

	public String getDesHorario() {
		return desHorario;
	}

	public void setDesHorario(String desHorario) {
		this.desHorario = desHorario;
	}

	public String getHorInicio() {
		return horInicio;
	}

	public void setHorInicio(String horInicio) {
		this.horInicio = horInicio;
	}

	public String getHorFim() {
		return horFim;
	}

	public void setHorFim(String horFim) {
		this.horFim = horFim;
	}

	public BigInteger getIdClinicaProfissional() {
		return idClinicaProfissional;
	}

	public void setIdClinicaProfissional(BigInteger idClinicaProfissional) {
		this.idClinicaProfissional = idClinicaProfissional;
	}

	public Date getDatAlteracao() {
		return datAlteracao;
	}

	public void setDatAlteracao(Date datAlteracao) {
		this.datAlteracao = datAlteracao;
	}

	public BigInteger getIdUsuarioAlt() {
		return idUsuarioAlt;
	}

	public void setIdUsuarioAlt(BigInteger idUsuarioAlt) {
		this.idUsuarioAlt = idUsuarioAlt;
	}

	public Usuario getUsuarioAlt() {
		return usuarioAlt;
	}

	public void setUsuarioAlt(Usuario usuarioAlt) {
		this.usuarioAlt = usuarioAlt;
	}

	public HashMap<String, Object> getFiltroMap() {
		return filtroMap;
	}

	public void setFiltroMap(HashMap<String, Object> filtroMap) {
		this.filtroMap = filtroMap;
	}

	public boolean isFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

}
