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
 * @since 17/05/2016
 */

@Entity
@Table(name = "tb_conselho", schema="seg")
@Proxy(lazy = true)
public class Conselho implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_conselho")
	private BigInteger idConselho;
	
	@Column(name = "descricao")
	private String descricao;
	

	public BigInteger getIdConselho() {
		return idConselho;
	}

	public void setIdConselho(BigInteger idConselho) {
		this.idConselho = idConselho;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
