package br.com.a2dm.ngc.entity.log;

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
 * @since 29/06/2017
 */
@Entity
@Table(name = "tb_agendamento_log", schema="agn")
@SequenceGenerator(name = "SQ_AGENDAMENTO_LOG", sequenceName = "SQ_AGENDAMENTO_LOG", allocationSize = 1)
@Proxy(lazy = true)
public class AgendamentoLog implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_AGENDAMENTO_LOG")
	@Column(name = "id_agendamento_log")
	private BigInteger idAgendamentoLog;
	
	@Column(name = "id_agendamento")
	private BigInteger idAgendamento;
	
	@Column(name = "id_usuario")
	private BigInteger idUsuario;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;
	
	@Column(name = "des_operacao")
	private String desOperacao;

	public BigInteger getIdAgendamentoLog() {
		return idAgendamentoLog;
	}

	public void setIdAgendamentoLog(BigInteger idAgendamentoLog) {
		this.idAgendamentoLog = idAgendamentoLog;
	}

	public BigInteger getIdAgendamento() {
		return idAgendamento;
	}

	public void setIdAgendamento(BigInteger idAgendamento) {
		this.idAgendamento = idAgendamento;
	}

	public BigInteger getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(BigInteger idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
	}

	public String getDesOperacao() {
		return desOperacao;
	}

	public void setDesOperacao(String desOperacao) {
		this.desOperacao = desOperacao;
	}
}
