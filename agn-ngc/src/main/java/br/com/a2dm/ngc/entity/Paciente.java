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
@Table(name = "tb_paciente", schema="agn")
@SequenceGenerator(name = "SQ_PACIENTE", sequenceName = "SQ_PACIENTE", allocationSize = 1)
@Proxy(lazy = true)
public class Paciente implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PACIENTE")
	@Column(name = "id_paciente")
	private BigInteger idPaciente;
	
	@Column(name = "nom_paciente")
	private String nomPaciente;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_nascimento")
	private Date datNascimento;
	
	@Column(name = "cpf_paciente")
	private String cpfPaciente;
	
	@Column(name = "rg_paciente")
	private String rgPaciente;
	
	@Column(name = "sex_paciente")
	private String sexPaciente;
	
	@Column(name = "tel_paciente")
	private String telPaciente;
	
	@Column(name = "cel_paciente")
	private String celPaciente;
	
	@Column(name = "eml_paciente")
	private String emlPaciente;
	
	@Column(name = "cep_paciente")
	private String cepPaciente;
	
	@Column(name = "lgd_paciente")
	private String lgdPaciente;
	
	@Column(name = "num_endereco")
	private Long numEndereco;
	
	@Column(name = "brr_paciente")
	private String brrPaciente;
	
	@Column(name = "cid_paciente")
	private String cidPaciente;
	
	@Column(name = "id_estado")
	private BigInteger idEstado;
	
	@Column(name = "cmp_paciente")
	private String cmpPaciente;

	@Column(name = "ref_paciente")
	private String refPaciente;
	
	@Column(name = "id_profissional")
	private BigInteger idProfissional;
	
	@Column(name = "flg_completo")
	private String flgCompleto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_profissional", insertable = false, updatable = false)
	private Usuario profissional;
	
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

	
	public BigInteger getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(BigInteger idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getNomPaciente() {
		return nomPaciente;
	}

	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}

	public Date getDatNascimento() {
		return datNascimento;
	}

	public void setDatNascimento(Date datNascimento) {
		this.datNascimento = datNascimento;
	}

	public String getCpfPaciente() {
		return cpfPaciente;
	}

	public void setCpfPaciente(String cpfPaciente) {
		this.cpfPaciente = cpfPaciente;
	}

	public String getRgPaciente() {
		return rgPaciente;
	}

	public void setRgPaciente(String rgPaciente) {
		this.rgPaciente = rgPaciente;
	}

	public String getSexPaciente() {
		return sexPaciente;
	}

	public void setSexPaciente(String sexPaciente) {
		this.sexPaciente = sexPaciente;
	}

	public String getTelPaciente() {
		return telPaciente;
	}

	public void setTelPaciente(String telPaciente) {
		this.telPaciente = telPaciente;
	}

	public String getCelPaciente() {
		return celPaciente;
	}

	public void setCelPaciente(String celPaciente) {
		this.celPaciente = celPaciente;
	}

	public String getEmlPaciente() {
		return emlPaciente;
	}

	public void setEmlPaciente(String emlPaciente) {
		this.emlPaciente = emlPaciente;
	}

	public String getCepPaciente() {
		return cepPaciente;
	}

	public void setCepPaciente(String cepPaciente) {
		this.cepPaciente = cepPaciente;
	}

	public String getLgdPaciente() {
		return lgdPaciente;
	}

	public void setLgdPaciente(String lgdPaciente) {
		this.lgdPaciente = lgdPaciente;
	}

	public Long getNumEndereco() {
		return numEndereco;
	}

	public void setNumEndereco(Long numEndereco) {
		this.numEndereco = numEndereco;
	}

	public String getBrrPaciente() {
		return brrPaciente;
	}

	public void setBrrPaciente(String brrPaciente) {
		this.brrPaciente = brrPaciente;
	}

	public String getCidPaciente() {
		return cidPaciente;
	}

	public void setCidPaciente(String cidPaciente) {
		this.cidPaciente = cidPaciente;
	}

	public BigInteger getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(BigInteger idEstado) {
		this.idEstado = idEstado;
	}

	public String getCmpPaciente() {
		return cmpPaciente;
	}

	public void setCmpPaciente(String cmpPaciente) {
		this.cmpPaciente = cmpPaciente;
	}

	public String getRefPaciente() {
		return refPaciente;
	}

	public void setRefPaciente(String refPaciente) {
		this.refPaciente = refPaciente;
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

	public BigInteger getIdProfissional() {
		return idProfissional;
	}

	public void setIdProfissional(BigInteger idProfissional) {
		this.idProfissional = idProfissional;
	}

	public Usuario getProfissional() {
		return profissional;
	}

	public void setProfissional(Usuario profissional) {
		this.profissional = profissional;
	}

	public String getFlgCompleto() {
		return flgCompleto;
	}

	public void setFlgCompleto(String flgCompleto) {
		this.flgCompleto = flgCompleto;
	}
}
