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

import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 11/08/2017
 */
@Entity
@Table(name = "tb_noticia", schema="agn")
@SequenceGenerator(name = "SQ_NOTICIA", sequenceName = "SQ_NOTICIA", allocationSize = 1)
@Proxy(lazy = true)
public class Noticia implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_NOTICIA")
	@Column(name = "id_noticia")
	private BigInteger idNoticia;
	
	@Column(name = "des_titulo")
	private String desTitulo;
	
	@Column(name = "des_noticia")
	private String desNoticia;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;

	public BigInteger getIdNoticia() {
		return idNoticia;
	}

	public void setIdNoticia(BigInteger idNoticia) {
		this.idNoticia = idNoticia;
	}

	public String getDesTitulo() {
		return desTitulo;
	}

	public void setDesTitulo(String desTitulo) {
		this.desTitulo = desTitulo;
	}

	public String getDesNoticia() {
		return desNoticia;
	}

	public void setDesNoticia(String desNoticia) {
		this.desNoticia = desNoticia;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
	}
}
