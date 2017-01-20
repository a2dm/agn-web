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
 * @since 24/06/2016
 */
@Entity
@Table(name = "tb_clinica", schema="agn")
@SequenceGenerator(name = "SQ_CLINICA", sequenceName = "SQ_CLINICA", allocationSize = 1)
@Proxy(lazy = true)
public class Clinica implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CLINICA")
	@Column(name = "id_clinica")
	private BigInteger idClinica;
	
	@Column(name = "id_usuario")
	private BigInteger idUsuario;
	
	@Column(name = "des_clinica")
	private String desClinica;
	
	@Column(name = "num_cnpj_clinica")
	private String numCnpjClinica;
	
	@Column(name = "tlf_clinica")
	private String tlfClinica;
	
	@Column(name = "cep_clinica")
	private String cepClinica;
	
	@Column(name = "lgd_clinica")
	private String lgdClinica;
	
	@Column(name = "num_end_clinica")
	private BigInteger numEndClinica;
	
	@Column(name = "bro_clinica")
	private String broClinica;
	
	@Column(name = "cid_clinica")
	private String cidClinica;
	
	@Column(name = "id_estado")
	private BigInteger idEstado;
	
	@Column(name = "cmp_clinica")
	private String cmpClinica;
	
	@Column(name = "flg_ativo")
	private String flgAtivo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;
	
	@Column(name = "id_usuario_cad")
	private BigInteger idUsuarioCad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_cad", insertable = false, updatable = false)
	private Usuario usuarioCad;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_alteracao")
	private Date datAlteracao;
	
	@Column(name = "id_usuario_alt")
	private BigInteger idUsuarioAlt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_alt", insertable = false, updatable = false)
	private Usuario usuarioAlt;
	
	@Transient
	private HashMap<String, Object> filtroMap;

	public BigInteger getIdClinica() {
		return idClinica;
	}

	public void setIdClinica(BigInteger idClinica) {
		this.idClinica = idClinica;
	}

	public BigInteger getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(BigInteger idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getDesClinica() {
		return desClinica;
	}

	public void setDesClinica(String desClinica) {
		this.desClinica = desClinica;
	}

	public String getNumCnpjClinica() {
		return numCnpjClinica;
	}

	public void setNumCnpjClinica(String numCnpjClinica) {
		this.numCnpjClinica = numCnpjClinica;
	}

	public String getTlfClinica() {
		return tlfClinica;
	}

	public void setTlfClinica(String tlfClinica) {
		this.tlfClinica = tlfClinica;
	}

	public String getCepClinica() {
		return cepClinica;
	}

	public void setCepClinica(String cepClinica) {
		this.cepClinica = cepClinica;
	}

	public String getLgdClinica() {
		return lgdClinica;
	}

	public void setLgdClinica(String lgdClinica) {
		this.lgdClinica = lgdClinica;
	}

	public BigInteger getNumEndClinica() {
		return numEndClinica;
	}

	public void setNumEndClinica(BigInteger numEndClinica) {
		this.numEndClinica = numEndClinica;
	}

	public String getBroClinica() {
		return broClinica;
	}

	public void setBroClinica(String broClinica) {
		this.broClinica = broClinica;
	}

	public String getCidClinica() {
		return cidClinica;
	}

	public void setCidClinica(String cidClinica) {
		this.cidClinica = cidClinica;
	}

	public BigInteger getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(BigInteger idEstado) {
		this.idEstado = idEstado;
	}

	public String getCmpClinica() {
		return cmpClinica;
	}

	public void setCmpClinica(String cmpClinica) {
		this.cmpClinica = cmpClinica;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
	}

	public BigInteger getIdUsuarioCad() {
		return idUsuarioCad;
	}

	public void setIdUsuarioCad(BigInteger idUsuarioCad) {
		this.idUsuarioCad = idUsuarioCad;
	}

	public Usuario getUsuarioCad() {
		return usuarioCad;
	}

	public void setUsuarioCad(Usuario usuarioCad) {
		this.usuarioCad = usuarioCad;
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
}
