package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Proxy;

import br.com.a2dm.cmn.entity.Usuario;

/** 
 * @author Carlos Diego
 * @since 10/04/2017
 */
@Entity
@Table(name = "tb_agendamento", schema="agn")
@SequenceGenerator(name = "SQ_AGENDAMENTO", sequenceName = "SQ_AGENDAMENTO", allocationSize = 1)
@Proxy(lazy = true)
public class Agendamento implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_AGENDAMENTO")
	@Column(name = "id_agendamento")
	private BigInteger idAgendamento;
	
	@Column(name = "id_clinica_profissional")
	private BigInteger idClinicaProfissional;
	
	@Column(name = "id_servico")
	private BigInteger idServico;
	
	@Column(name = "id_paciente")
	private BigInteger idPaciente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_paciente", insertable = false, updatable = false)
	private Paciente paciente;
	
	@Column(name = "nom_paciente")
	private String nomPaciente;
	
	@Column(name = "cpf_paciente")
	private String cpfPaciente;
	
	@Column(name = "id_convenio")
	private BigInteger idConvenio;
	
	@Column(name = "hor_inicio")
	private String horInicio;
	
	@Column(name = "hor_fim")
	private String horFim;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dat_agendamento")
	private Date datAgendamento;

	@Column(name = "flg_ativo")
	private String flgAtivo;
	
	@Column(name = "flg_confirmado")
	private String flgConfirmado;
	
	@Column(name = "id_situacao")
	private BigInteger idSituacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;
	
	@Column(name = "tp_agendamento")
	private String tpAgendamento;
	
	@Column(name = "id_usuario_cad")
	private BigInteger idUsuarioCad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_cad", insertable = false, updatable = false)
	private Usuario usuarioCad;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_alteracao")
	private Date datAlteracao;
	
	@Column(name = "id_usuario_alt")
	private BigInteger idUsuarioAlt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_alt", insertable = false, updatable = false)
	private Usuario usuarioAlt;
	
	@Formula("(select d.des_dominio from agn.tb_dominio d where d.vlr_dominio = id_situacao and d.ref_dominio = 'SITUACAO_AGENDAMENTO')")
	private String desSituacao;
	
	@Column(name = "obs_agendamento")
	private String obsAgendamento;

	public BigInteger getIdAgendamento() {
		return idAgendamento;
	}

	public void setIdAgendamento(BigInteger idAgendamento) {
		this.idAgendamento = idAgendamento;
	}

	public BigInteger getIdClinicaProfissional() {
		return idClinicaProfissional;
	}

	public void setIdClinicaProfissional(BigInteger idClinicaProfissional) {
		this.idClinicaProfissional = idClinicaProfissional;
	}

	public BigInteger getIdServico() {
		return idServico;
	}

	public void setIdServico(BigInteger idServico) {
		this.idServico = idServico;
	}

	public BigInteger getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(BigInteger idPaciente) {
		this.idPaciente = idPaciente;
	}

	public BigInteger getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(BigInteger idConvenio) {
		this.idConvenio = idConvenio;
	}

	public String getHorInicio() {
		return horInicio;
	}

	public void setHorInicio(String horInicio) {
		this.horInicio = horInicio;
	}

	public String getHorFim() {
		return horFim;
	}

	public void setHorFim(String horFim) {
		this.horFim = horFim;
	}

	public Date getDatAgendamento() {
		return datAgendamento;
	}

	public void setDatAgendamento(Date datAgendamento) {
		this.datAgendamento = datAgendamento;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	public String getFlgConfirmado() {
		return flgConfirmado;
	}

	public void setFlgConfirmado(String flgConfirmado) {
		this.flgConfirmado = flgConfirmado;
	}

	public BigInteger getIdSituacao() {
		return idSituacao;
	}

	public void setIdSituacao(BigInteger idSituacao) {
		this.idSituacao = idSituacao;
	}

	public Date getDatCadastro() {
		return datCadastro;
	}

	public void setDatCadastro(Date datCadastro) {
		this.datCadastro = datCadastro;
	}

	public BigInteger getIdUsuarioCad() {
		return idUsuarioCad;
	}

	public void setIdUsuarioCad(BigInteger idUsuarioCad) {
		this.idUsuarioCad = idUsuarioCad;
	}

	public Usuario getUsuarioCad() {
		return usuarioCad;
	}

	public void setUsuarioCad(Usuario usuarioCad) {
		this.usuarioCad = usuarioCad;
	}

	public Date getDatAlteracao() {
		return datAlteracao;
	}

	public void setDatAlteracao(Date datAlteracao) {
		this.datAlteracao = datAlteracao;
	}

	public BigInteger getIdUsuarioAlt() {
		return idUsuarioAlt;
	}

	public void setIdUsuarioAlt(BigInteger idUsuarioAlt) {
		this.idUsuarioAlt = idUsuarioAlt;
	}

	public Usuario getUsuarioAlt() {
		return usuarioAlt;
	}

	public void setUsuarioAlt(Usuario usuarioAlt) {
		this.usuarioAlt = usuarioAlt;
	}

	public String getDesSituacao() {
		return desSituacao;
	}

	public void setDesSituacao(String desSituacao) {
		this.desSituacao = desSituacao;
	}

	public String getTpAgendamento() {
		return tpAgendamento;
	}

	public void setTpAgendamento(String tpAgendamento) {
		this.tpAgendamento = tpAgendamento;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public String getObsAgendamento() {
		return obsAgendamento;
	}

	public void setObsAgendamento(String obsAgendamento) {
		this.obsAgendamento = obsAgendamento;
	}

	public String getNomPaciente() {
		return nomPaciente;
	}

	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}

	public String getCpfPaciente() {
		return cpfPaciente;
	}

	public void setCpfPaciente(String cpfPaciente) {
		this.cpfPaciente = cpfPaciente;
	}
}
