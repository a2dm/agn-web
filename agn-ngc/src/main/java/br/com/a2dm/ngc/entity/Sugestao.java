package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 26/08/2016
 */
@Entity
@Table(name = "tb_sugestao", schema="agn")
@SequenceGenerator(name = "SQ_SUGESTAO", sequenceName = "SQ_SUGESTAO", allocationSize = 1)
@Proxy(lazy = true)
public class Sugestao implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SUGESTAO")
	@Column(name = "id_sugestao")
	private BigInteger idSugestao;
	
	@Column(name = "id_clinica_profissional")
	private BigInteger idClinicaProfissional;
	
	@Column(name = "id_tipo_sugestao")
	private BigInteger idTipoSugestao;
	
	@Column(name = "des_sugestao")
	private String desSugestao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;
	
	@Column(name = "flg_lida")
	private String flgLida;
	
	@Column(name = "id_situacao_sugestao")
	private BigInteger idSituacaoSugestao;
	
	@Formula("(select d.des_dominio from agn.tb_dominio d where d.vlr_dominio = id_situacao_sugestao and d.ref_dominio = 'SITUACAO_SUGESTAO')")
	private String desSituacaoSugestao;
	
	@Column(name = "msg_administrador")
	private String msgAdministrador;
	
	@Formula("(select d.des_dominio from agn.tb_dominio d where d.vlr_dominio = id_tipo_sugestao and d.ref_dominio = 'TIPO_SUGESTAO')")
	private String desTipoSugestao;

	public BigInteger getIdSugestao() {
		return idSugestao;
	}

	public void setIdSugestao(BigInteger idSugestao) {
		this.idSugestao = idSugestao;
	}

	public BigInteger getIdClinicaProfissional() {
		return idClinicaProfissional;
	}

	public void setIdClinicaProfissional(BigInteger idClinicaProfissional) {
		this.idClinicaProfissional = idClinicaProfissional;
	}

	public String getDesSugestao() {
		return desSugestao;
	}

	public void setDesSugestao(String desSugestao) {
		this.desSugestao = desSugestao;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
	}

	public String getFlgLida() {
		return flgLida;
	}

	public void setFlgLida(String flgLida) {
		this.flgLida = flgLida;
	}

	public BigInteger getIdTipoSugestao() {
		return idTipoSugestao;
	}

	public void setIdTipoSugestao(BigInteger idTipoSugestao) {
		this.idTipoSugestao = idTipoSugestao;
	}

	public String getDesTipoSugestao() {
		return desTipoSugestao;
	}

	public void setDesTipoSugestao(String desTipoSugestao) {
		this.desTipoSugestao = desTipoSugestao;
	}

	public BigInteger getIdSituacaoSugestao() {
		return idSituacaoSugestao;
	}

	public void setIdSituacaoSugestao(BigInteger idSituacaoSugestao) {
		this.idSituacaoSugestao = idSituacaoSugestao;
	}

	public String getMsgAdministrador() {
		return msgAdministrador;
	}

	public void setMsgAdministrador(String msgAdministrador) {
		this.msgAdministrador = msgAdministrador;
	}

	public String getDesSituacaoSugestao() {
		return desSituacaoSugestao;
	}

	public void setDesSituacaoSugestao(String desSituacaoSugestao) {
		this.desSituacaoSugestao = desSituacaoSugestao;
	}
}
