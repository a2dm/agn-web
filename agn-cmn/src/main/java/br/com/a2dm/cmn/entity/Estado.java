package br.com.a2dm.cmn.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 26/01/2016
 */

@Entity
@Table(name = "tb_estado", schema="seg")
@Proxy(lazy = true)
public class Estado implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_estado")
	private BigInteger idEstado;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "sigla")
	private String sigla;

	
	public BigInteger getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(BigInteger idEstado) {
		this.idEstado = idEstado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
}
