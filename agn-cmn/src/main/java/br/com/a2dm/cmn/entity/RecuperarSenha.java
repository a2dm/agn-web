package br.com.a2dm.cmn.entity;

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

import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 05/08/2016
 */

@Entity
@Table(name = "tb_recuperar_senha", schema="seg")
@SequenceGenerator(name = "SQ_RECUPERAR_SENHA", sequenceName = "SQ_RECUPERAR_SENHA", allocationSize = 1)
@Proxy(lazy = true)
public class RecuperarSenha implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_RECUPERAR_SENHA")
	@Column(name = "id_recuperar_senha")
	private BigInteger idRecuperarSenha;
	
	@Column(name = "id_usuario")
	private BigInteger idUsuario;
	
	@Column(name = "hash")
	private String hash;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;

	public BigInteger getIdRecuperarSenha() {
		return idRecuperarSenha;
	}

	public void setIdRecuperarSenha(BigInteger idRecuperarSenha) {
		this.idRecuperarSenha = idRecuperarSenha;
	}

	public BigInteger getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(BigInteger idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
	}
}
