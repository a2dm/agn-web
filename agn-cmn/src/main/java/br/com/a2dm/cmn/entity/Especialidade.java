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
@Table(name = "tb_especialidade", schema="seg")
@Proxy(lazy = true)
public class Especialidade implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_especialidade")
	private BigInteger idEspecialidade;
	
	@Column(name = "descricao")
	private String descricao;

	
	public BigInteger getIdEspecialidade() {
		return idEspecialidade;
	}

	public void setIdEspecialidade(BigInteger idEspecialidade) {
		this.idEspecialidade = idEspecialidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
