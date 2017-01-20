package br.com.a2dm.cmn.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 27/01/2016
 */

@Entity
@Table(name = "tb_tela_acao", schema="seg")
@SequenceGenerator(name = "SQ_TELA_ACAO", sequenceName = "SQ_TELA_ACAO", allocationSize = 1)
@Proxy(lazy = true)
public class TelaAcao implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TELA_ACAO")
	@Column(name = "id_tela_acao")
	private BigInteger idTelaAcao;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "pagina")
	private String pagina;
	
	@Column(name = "id_sistema")
	private BigInteger idSistema;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sistema", insertable = false, updatable = false)
	private Sistema sistema;
	
	@Column(name = "id_acao")
	private BigInteger idAcao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_acao", insertable = false, updatable = false)
	private Acao acao;
	
	@OneToMany(mappedBy="telaAcao", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<GrupoTelaAcao> listaGrupoTelaAcao; 
	
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

	public BigInteger getIdTelaAcao() {
		return idTelaAcao;
	}

	public void setIdTelaAcao(BigInteger idTelaAcao) {
		this.idTelaAcao = idTelaAcao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}

	public BigInteger getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(BigInteger idSistema) {
		this.idSistema = idSistema;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public BigInteger getIdAcao() {
		return idAcao;
	}

	public void setIdAcao(BigInteger idAcao) {
		this.idAcao = idAcao;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
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

	public HashMap<String, Object> getFiltroMap() {
		return filtroMap;
	}

	public void setFiltroMap(HashMap<String, Object> filtroMap) {
		this.filtroMap = filtroMap;
	}

	public List<GrupoTelaAcao> getListaGrupoTelaAcao() {
		return listaGrupoTelaAcao;
	}

	public void setListaGrupoTelaAcao(List<GrupoTelaAcao> listaGrupoTelaAcao) {
		this.listaGrupoTelaAcao = listaGrupoTelaAcao;
	}
}
