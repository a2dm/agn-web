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
@Table(name = "tb_sistema", schema="seg")
@Proxy(lazy = true)
public class Sistema implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_sistema")
	private BigInteger idSistema;
	
	@Column(name = "descricao")
	private String descricao;


	public BigInteger getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(BigInteger idSistema) {
		this.idSistema = idSistema;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
