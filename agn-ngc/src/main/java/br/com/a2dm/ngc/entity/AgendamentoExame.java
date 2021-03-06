package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 30/07/2017
 */
@Entity
@Table(name = "tb_agendamento_exame", schema="agn")
@SequenceGenerator(name = "SQ_AGENDAMENTO_EXAME", sequenceName = "SQ_AGENDAMENTO_EXAME", allocationSize = 1)
@Proxy(lazy = true)
public class AgendamentoExame implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_AGENDAMENTO_EXAME")
	@Column(name = "id_agendamento_exame")
	private BigInteger idAgendamentoExame;
	
	@Column(name = "id_clinica_profissional")
	private BigInteger idClinicaProfissional;
	
	@Column(name = "id_exame")
	private BigInteger idExame;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_exame", insertable = false, updatable = false)
	private Exame exame;

	@Column(name = "id_agendamento")
	private BigInteger idAgendamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_agendamento", insertable = false, updatable = false)
	private Agendamento agendamento;
	
	
	public BigInteger getIdAgendamentoExame() {
		return idAgendamentoExame;
	}

	public void setIdAgendamentoExame(BigInteger idAgendamentoExame) {
		this.idAgendamentoExame = idAgendamentoExame;
	}

	public BigInteger getIdClinicaProfissional() {
		return idClinicaProfissional;
	}

	public void setIdClinicaProfissional(BigInteger idClinicaProfissional) {
		this.idClinicaProfissional = idClinicaProfissional;
	}

	public BigInteger getIdExame() {
		return idExame;
	}

	public void setIdExame(BigInteger idExame) {
		this.idExame = idExame;
	}

	public BigInteger getIdAgendamento() {
		return idAgendamento;
	}

	public void setIdAgendamento(BigInteger idAgendamento) {
		this.idAgendamento = idAgendamento;
	}

	public Exame getExame() {
		return exame;
	}

	public void setExame(Exame exame) {
		this.exame = exame;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}
}
