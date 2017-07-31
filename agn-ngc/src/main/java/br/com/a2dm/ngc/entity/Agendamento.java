package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

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
import javax.persistence.Transient;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_servico", insertable = false, updatable = false)
	private Servico servico;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_convenio", insertable = false, updatable = false)
	private Convenio convenio;
	
	@Column(name = "hor_inicio")
	private String horInicio;
	
	@Column(name = "hor_fim")
	private String horFim;
	
	@Column(name = "hor_presenca")
	private String horPresenca;
	
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_confirmacao")
	private Date datConfirmacao;
	
	@Column(name = "id_usuario_confirm")
	private BigInteger idUsuarioConfirm;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_confirm", insertable = false, updatable = false)
	private Usuario usuarioConfirm;
	
	@Formula("(select d.des_dominio from agn.tb_dominio d where d.vlr_dominio = id_situacao and d.ref_dominio = 'SITUACAO_AGENDAMENTO')")
	private String desSituacao;
	
	@Column(name = "vlr_desconto")
	private Double vlrDesconto;
	
	@Column(name = "vlr_agendamento")
	private Double vlrAgendamento;
	
	@Column(name = "obs_agendamento")
	private String obsAgendamento;
	
	@Column(name = "tel_paciente")
	private String telPaciente;
	
	@Column(name = "eml_paciente")
	private String emlPaciente;
	
	@Column(name = "des_prescricao")
	private String desAnamnese;
	
	@Column(name = "des_anamnese")
	private String desPrescricao;
	
	@Transient
	private String vlrAgendamentoFormatado;
	
	@Transient
	private String vlrDescontoFormatado;
	
	@Transient
	private HashMap<String, Object> filtroMap;

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

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public HashMap<String, Object> getFiltroMap() {
		return filtroMap;
	}

	public void setFiltroMap(HashMap<String, Object> filtroMap) {
		this.filtroMap = filtroMap;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Date getDatConfirmacao() {
		return datConfirmacao;
	}

	public void setDatConfirmacao(Date datConfirmacao) {
		this.datConfirmacao = datConfirmacao;
	}

	public BigInteger getIdUsuarioConfirm() {
		return idUsuarioConfirm;
	}

	public void setIdUsuarioConfirm(BigInteger idUsuarioConfirm) {
		this.idUsuarioConfirm = idUsuarioConfirm;
	}

	public Usuario getUsuarioConfirm() {
		return usuarioConfirm;
	}

	public void setUsuarioConfirm(Usuario usuarioConfirm) {
		this.usuarioConfirm = usuarioConfirm;
	}

	public String getTelPaciente() {
		return telPaciente;
	}

	public void setTelPaciente(String telPaciente) {
		this.telPaciente = telPaciente;
	}

	public String getEmlPaciente() {
		return emlPaciente;
	}

	public void setEmlPaciente(String emlPaciente) {
		this.emlPaciente = emlPaciente;
	}

	public String getHorPresenca() {
		return horPresenca;
	}

	public void setHorPresenca(String horPresenca) {
		this.horPresenca = horPresenca;
	}

	public Double getVlrDesconto() {
		return vlrDesconto;
	}

	public void setVlrDesconto(Double vlrDesconto) {
		this.vlrDesconto = vlrDesconto;
	}

	public Double getVlrAgendamento() {
		return vlrAgendamento;
	}

	public void setVlrAgendamento(Double vlrAgendamento) {
		this.vlrAgendamento = vlrAgendamento;
	}

	public String getVlrAgendamentoFormatado() {
		return vlrAgendamentoFormatado;
	}

	public void setVlrAgendamentoFormatado(String vlrAgendamentoFormatado) {
		this.vlrAgendamentoFormatado = vlrAgendamentoFormatado;
	}

	public String getVlrDescontoFormatado() {
		return vlrDescontoFormatado;
	}

	public void setVlrDescontoFormatado(String vlrDescontoFormatado) {
		this.vlrDescontoFormatado = vlrDescontoFormatado;
	}

	public String getDesAnamnese() {
		return desAnamnese;
	}

	public void setDesAnamnese(String desAnamnese) {
		this.desAnamnese = desAnamnese;
	}

	public String getDesPrescricao() {
		return desPrescricao;
	}

	public void setDesPrescricao(String desPrescricao) {
		this.desPrescricao = desPrescricao;
	}
}
