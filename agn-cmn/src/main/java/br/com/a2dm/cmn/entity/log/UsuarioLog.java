package br.com.a2dm.cmn.entity.log;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Proxy;

/** 
 * @author Carlos Diego
 * @since 19/05/2016
 */

@Entity
@Table(name = "tb_usuario_log", schema="seg")
@Proxy(lazy = true)
public class UsuarioLog implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "SQ_USUARIO_LOG", sequenceName = "seg.SQ_USUARIO_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USUARIO_LOG")
	@Column(name = "id_usuario_log")
	private BigInteger idUsuarioLog;
	
	@Column(name = "id_usuario")
	private BigInteger idUsuario;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "login")
	private String login;

	@Column(name = "senha")
	private String senha;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "telefone")
	private String telefone;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dat_nascimento")
	private Date dataNascimento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_cadastro")
	private Date dataCadastro;
	
	@Column(name = "id_usuario_cad")
	private BigInteger idUsuarioCad;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dat_alteracao")
	private Date dataAlteracao;
	
	@Column(name = "id_usuario_alt")
	private BigInteger idUsuarioAlt;
	
	@Column(name = "flg_ativo")
	private String flgAtivo;
	
	@Column(name = "id_especialidade")
	private BigInteger idEspecialidade;
	
	@Column(name = "id_conselho")
	private BigInteger idConselho;
	
	@Column(name = "num_conselho")
	private BigInteger numConselho;
	
	@Column(name = "cep")
	private String cep;

	@Column(name = "logradouro")
	private String logradouro;
	
	@Column(name = "num_endereco")
	private BigInteger numEndereco;
	
	@Column(name = "bairro")
	private String bairro;
	
	@Column(name = "cidade")
	private String cidade;
	
	@Column(name = "id_estado")
	private BigInteger idEstado;
	
	@Column(name = "complemento")
	private String complemento;
	
	@Column(name = "referencia")
	private String referencia;
	
	@Column(name = "id_grupo")
	private BigInteger idGrupo;
	
	@Column(name = "flg_seguranca")
	private String flgSeguranca;
	
	@Column(name = "log_mapping")
	private String logMapping;
	
	@Transient
	private BigInteger[] arrayPermissoes;
	
	@Transient
	private HashMap<Object, String> filtroMap;
	
	
	public BigInteger getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(BigInteger idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public BigInteger getIdUsuarioCad() {
		return idUsuarioCad;
	}

	public void setIdUsuarioCad(BigInteger idUsuarioCad) {
		this.idUsuarioCad = idUsuarioCad;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public BigInteger getIdUsuarioAlt() {
		return idUsuarioAlt;
	}

	public void setIdUsuarioAlt(BigInteger idUsuarioAlt) {
		this.idUsuarioAlt = idUsuarioAlt;
	}

	public String getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(String flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	public BigInteger getIdEspecialidade() {
		return idEspecialidade;
	}

	public void setIdEspecialidade(BigInteger idEspecialidade) {
		this.idEspecialidade = idEspecialidade;
	}

	public BigInteger getIdConselho() {
		return idConselho;
	}

	public void setIdConselho(BigInteger idConselho) {
		this.idConselho = idConselho;
	}

	public BigInteger getNumConselho() {
		return numConselho;
	}

	public void setNumConselho(BigInteger numConselho) {
		this.numConselho = numConselho;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public BigInteger getNumEndereco() {
		return numEndereco;
	}

	public void setNumEndereco(BigInteger numEndereco) {
		this.numEndereco = numEndereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public BigInteger getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(BigInteger idEstado) {
		this.idEstado = idEstado;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public BigInteger[] getArrayPermissoes() {
		return arrayPermissoes;
	}

	public void setArrayPermissoes(BigInteger[] arrayPermissoes) {
		this.arrayPermissoes = arrayPermissoes;
	}

	public HashMap<Object, String> getFiltroMap() {
		return filtroMap;
	}

	public void setFiltroMap(HashMap<Object, String> filtroMap) {
		this.filtroMap = filtroMap;
	}

	public BigInteger getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(BigInteger idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getFlgSeguranca() {
		return flgSeguranca;
	}

	public void setFlgSeguranca(String flgSeguranca) {
		this.flgSeguranca = flgSeguranca;
	}

	public BigInteger getIdUsuarioLog() {
		return idUsuarioLog;
	}

	public void setIdUsuarioLog(BigInteger idUsuarioLog) {
		this.idUsuarioLog = idUsuarioLog;
	}

	public String getLogMapping() {
		return logMapping;
	}

	public void setLogMapping(String logMapping) {
		this.logMapping = logMapping;
	}
}
