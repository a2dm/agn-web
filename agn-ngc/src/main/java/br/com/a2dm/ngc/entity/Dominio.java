package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 18/08/2017
 */
@Entity
@Table(name = "tb_dominio", schema="agn")
@SequenceGenerator(name = "SQ_DOMINIO", sequenceName = "SQ_DOMINIO", allocationSize = 1)
@Proxy(lazy = true)
public class Dominio implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DOMINIO")
	@Column(name = "id_dominio")
	private BigInteger idDominio;
	
	@Column(name = "vlr_dominio")
	private BigInteger vlrDominio;
	
	@Column(name = "des_dominio")
	private String desDominio;
	
	@Column(name = "ref_dominio")
	private String refDominio;

	public BigInteger getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(BigInteger idDominio) {
		this.idDominio = idDominio;
	}

	public BigInteger getVlrDominio() {
		return vlrDominio;
	}

	public void setVlrDominio(BigInteger vlrDominio) {
		this.vlrDominio = vlrDominio;
	}

	public String getDesDominio() {
		return desDominio;
	}

	public void setDesDominio(String desDominio) {
		this.desDominio = desDominio;
	}

	public String getRefDominio() {
		return refDominio;
	}

	public void setRefDominio(String refDominio) {
		this.refDominio = refDominio;
	}
}
