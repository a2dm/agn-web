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
 * @since 09/07/2016
 */
@Entity
@Table(name = "tb_convenio_servico", schema="agn")
@SequenceGenerator(name = "SQ_CONVENIO_SERVICO", sequenceName = "SQ_CONVENIO_SERVICO", allocationSize = 1)
@Proxy(lazy = true)
public class ConvenioServico implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CONVENIO_SERVICO")
	@Column(name = "id_convenio_servico")
	private BigInteger idConvenioServico;
	
	@Column(name = "id_usuario")
	private BigInteger idUsuario;
	
	@Column(name = "id_convenio")
	private BigInteger idConvenio;
	
	@Column(name = "id_servico")
	private BigInteger idServico;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_servico", insertable = false, updatable = false)
	private Servico servico;
	
	@Column(name = "vlr_receber")
	private Double vlrReceber;
	
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

	public BigInteger getIdConvenioServico() {
		return idConvenioServico;
	}

	public void setIdConvenioServico(BigInteger idConvenioServico) {
		this.idConvenioServico = idConvenioServico;
	}

	public BigInteger getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(BigInteger idUsuario) {
		this.idUsuario = idUsuario;
	}

	public BigInteger getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(BigInteger idConvenio) {
		this.idConvenio = idConvenio;
	}

	public BigInteger getIdServico() {
		return idServico;
	}

	public void setIdServico(BigInteger idServico) {
		this.idServico = idServico;
	}
	
	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public Double getVlrReceber() {
		return vlrReceber;
	}

	public void setVlrReceber(Double vlrReceber) {
		this.vlrReceber = vlrReceber;
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
