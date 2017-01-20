package br.com.a2dm.cmn.entity;

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

/** 
 * @author Carlos Diego
 * @since 04/02/2016
 */

@Entity
@Table(name = "tb_grupo_tela_acao", schema="seg")
@SequenceGenerator(name = "SQ_GRUPO_TELA_ACAO", sequenceName = "SQ_GRUPO_TELA_ACAO", allocationSize = 1)
@Proxy(lazy = true)
public class GrupoTelaAcao implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_GRUPO_TELA_ACAO")
	@Column(name = "id_grupo_tela_acao")
	private BigInteger idGrupoTelaAcao;
	
	@Column(name = "id_grupo")
	private BigInteger idGrupo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_grupo", insertable = false, updatable = false)
	private Grupo grupo;
	
	@Column(name = "id_tela_acao")
	private BigInteger idTelaAcao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tela_acao", insertable = false, updatable = false)
	private TelaAcao telaAcao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date dataCadastro;
	
	@Column(name = "id_usuario_cad")
	private BigInteger idUsuarioCad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_cad", insertable = false, updatable = false)
	private Usuario usuarioCad;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_alteracao")
	private Date dataAlteracao;
	
	@Column(name = "id_usuario_alt")
	private BigInteger idUsuarioAlt;
	
	@Column(name = "flg_ativo")
	private String flgAtivo;
	
	@Transient
	private HashMap<String, Object> filtroMap;

	public BigInteger getIdGrupoTelaAcao() {
		return idGrupoTelaAcao;
	}

	public void setIdGrupoTelaAcao(BigInteger idGrupoTelaAcao) {
		this.idGrupoTelaAcao = idGrupoTelaAcao;
	}

	public BigInteger getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(BigInteger idGrupo) {
		this.idGrupo = idGrupo;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public BigInteger getIdTelaAcao() {
		return idTelaAcao;
	}

	public void setIdTelaAcao(BigInteger idTelaAcao) {
		this.idTelaAcao = idTelaAcao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
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

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public BigInteger getIdUsuarioAlt() {
		return idUsuarioAlt;
	}

	public void setIdUsuarioAlt(BigInteger idUsuarioAlt) {
		this.idUsuarioAlt = idUsuarioAlt;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	public TelaAcao getTelaAcao() {
		return telaAcao;
	}

	public void setTelaAcao(TelaAcao telaAcao) {
		this.telaAcao = telaAcao;
	}

	public HashMap<String, Object> getFiltroMap() {
		return filtroMap;
	}

	public void setFiltroMap(HashMap<String, Object> filtroMap) {
		this.filtroMap = filtroMap;
	}
}
