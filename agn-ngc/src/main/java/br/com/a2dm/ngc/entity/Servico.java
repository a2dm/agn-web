package br.com.a2dm.ngc.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Proxy;

import br.com.a2dm.cmn.entity.Usuario;

/** 
 * @author Carlos Diego
 * @since 24/06/2016
 */
@Entity
@Table(name = "tb_servico", schema="agn")
@SequenceGenerator(name = "SQ_SERVICO", sequenceName = "SQ_SERVICO", allocationSize = 1)
@Proxy(lazy = true)
public class Servico implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SERVICO")
	@Column(name = "id_servico")
	private BigInteger idServico;
	
	@Column(name = "id_clinica_profissional")
	private BigInteger idClinicaProfissional;
	
	@Column(name = "des_servico")
	private String desServico;
	
	@Column(name = "vlr_servico")
	private Double vlrServico;
	
	@Column(name = "drc_servico")
	private Integer drcServico;
	
	@Column(name = "flg_ativo")
	private String flgAtivo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date datCadastro;
	
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
	
	@OneToMany(mappedBy="servico", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
	private List<ConvenioServico> listaConvenioServico;
	
	@OneToMany(mappedBy = "servico", fetch = FetchType.LAZY)
	private List<Agendamento> listaAgendamento;
	
	@Transient
	private String vlrServicoFormatado;
	
	@Transient
	private Long countAgendamento;
	
	@Transient
	private HashMap<String, Object> filtroMap;

	
	public BigInteger getIdServico() {
		return idServico;
	}

	public void setIdServico(BigInteger idServico) {
		this.idServico = idServico;
	}

	public BigInteger getIdClinicaProfissional() {
		return idClinicaProfissional;
	}

	public void setIdClinicaProfissional(BigInteger idClinicaProfissional) {
		this.idClinicaProfissional = idClinicaProfissional;
	}

	public String getDesServico() {
		return desServico;
	}

	public void setDesServico(String desServico) {
		this.desServico = desServico;
	}
	
	public Double getVlrServico() {
		return vlrServico;
	}

	public void setVlrServico(Double vlrServico) {
		this.vlrServico = vlrServico;
	}

	public Integer getDrcServico() {
		return drcServico;
	}

	public void setDrcServico(Integer drcServico) {
		this.drcServico = drcServico;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
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

	public String getVlrServicoFormatado() {
		return vlrServicoFormatado;
	}

	public void setVlrServicoFormatado(String vlrServicoFormatado) {
		this.vlrServicoFormatado = vlrServicoFormatado;
	}

	public HashMap<String, Object> getFiltroMap() {
		return filtroMap;
	}

	public void setFiltroMap(HashMap<String, Object> filtroMap) {
		this.filtroMap = filtroMap;
	}

	public Usuario getUsuarioCad() {
		return usuarioCad;
	}

	public void setUsuarioCad(Usuario usuarioCad) {
		this.usuarioCad = usuarioCad;
	}

	public Usuario getUsuarioAlt() {
		return usuarioAlt;
	}

	public void setUsuarioAlt(Usuario usuarioAlt) {
		this.usuarioAlt = usuarioAlt;
	}

	public List<ConvenioServico> getListaConvenioServico() {
		return listaConvenioServico;
	}

	public void setListaConvenioServico(List<ConvenioServico> listaConvenioServico) {
		this.listaConvenioServico = listaConvenioServico;
	}

	public List<Agendamento> getListaAgendamento() {
		return listaAgendamento;
	}

	public void setListaAgendamento(List<Agendamento> listaAgendamento) {
		this.listaAgendamento = listaAgendamento;
	}

	public Long getCountAgendamento() {
		return countAgendamento;
	}

	public void setCountAgendamento(Long countAgendamento) {
		this.countAgendamento = countAgendamento;
	}
}