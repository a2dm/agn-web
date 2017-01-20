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
@Table(name = "tb_acao", schema="seg")
@Proxy(lazy = true)
public class Acao implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_acao")
	private BigInteger idAcao;
	
	@Column(name = "descricao")
	private String descricao;

	public BigInteger getIdAcao() {
		return idAcao;
	}

	public void setIdAcao(BigInteger idAcao) {
		this.idAcao = idAcao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
