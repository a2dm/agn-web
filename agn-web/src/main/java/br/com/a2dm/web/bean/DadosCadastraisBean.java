package br.com.a2dm.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.a2dm.cmn.entity.Conselho;
import br.com.a2dm.cmn.entity.Especialidade;
import br.com.a2dm.cmn.entity.Estado;
import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.service.ConselhoService;
import br.com.a2dm.cmn.service.EspecialidadeService;
import br.com.a2dm.cmn.service.EstadoService;
import br.com.a2dm.cmn.service.GrupoService;
import br.com.a2dm.cmn.service.UsuarioService;
import br.com.a2dm.cmn.util.criptografia.CriptoMD5;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.ngc.functions.MenuControl;


@RequestScoped
@ManagedBean
public class DadosCadastraisBean extends AbstractBean<Usuario, UsuarioService>
{
	private String siglaEstado;
	private String senha;
	
	private List<Estado> listaEstado;
	private List<Especialidade> listaEspecialidade;
	private List<Conselho> listaConselho;
	
	private String mensagem;
	
	private JSFUtil util = new JSFUtil();
	
	public DadosCadastraisBean()
	{
		super(UsuarioService.getInstancia());
		this.ACTION_SEARCH = "dadosCadastrais";
		this.pageTitle = "Dados Cadastrais";
		
		MenuControl.ativarMenu("");
	}
	
	public String preparaDadosCadastrais()
	{
		try
		{
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
			usuario = UsuarioService.getInstancia().get(usuario, 0);
			
			this.setEntity(usuario);
			this.setarListas();
			
			if(this.getEntity().getIdEstado() != null
					&& this.getEntity().getIdEstado().intValue() > 0)
			{
				Estado estado = new Estado();
				estado.setIdEstado(this.getEntity().getIdEstado());
				estado = EstadoService.getInstancia().get(estado, 0);
				this.setSiglaEstado(estado.getSigla());
			}
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return ACTION_SEARCH;
	}
	
	private void setarListas() throws Exception
	{
		//LISTA DE ESTADOS
		List<Estado> resultEst = EstadoService.getInstancia().pesquisar(new Estado(), 0);
		
		Estado est = new Estado();
		est.setDescricao("Escolha o Estado");
		
		List<Estado> listaEstado = new ArrayList<Estado>();
		listaEstado.add(est);
		listaEstado.addAll(resultEst);
		
		this.setListaEstado(listaEstado);
		
		//LISTA DE ESPECIALIDADES
		List<Especialidade> resultEsp = EspecialidadeService.getInstancia().pesquisar(new Especialidade(), 0);
		
		Especialidade esp = new Especialidade();
		esp.setDescricao("Escolha e Especialidade");
		
		List<Especialidade> listaEspecialidade = new ArrayList<Especialidade>();
		listaEspecialidade.add(esp);
		listaEspecialidade.addAll(resultEsp);
		
		this.setListaEspecialidade(listaEspecialidade);
		
		//LISTA DE CONSELHOS
		List<Conselho> resultCon = ConselhoService.getInstancia().pesquisar(new Conselho(), 0);
		
		Conselho con = new Conselho();
		con.setDescricao("Escolha o Conselho");
		
		List<Conselho> listaConselho = new ArrayList<Conselho>();
		listaConselho.add(con);
		listaConselho.addAll(resultCon);
		
		this.setListaConselho(listaConselho);
	}
	
	public void preparaSalvar()
	{
		try
		{
			this.setMensagem(null);
			this.validar();
		}
		catch (Exception e)
		{
			this.setMensagem("Erro");
			FacesMessage message = new FacesMessage(e.getMessage());
	        message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void salvar() throws Exception
	{		
		//ATUALIZAR ESTADO
		if (this.getSiglaEstado() != null && !this.getSiglaEstado().equalsIgnoreCase(""))  
		{
			Estado estado = new Estado();
			estado.setSigla(this.getSiglaEstado());
			estado = EstadoService.getInstancia().get(estado, 0);
			
			this.getEntity().setIdEstado(estado.getIdEstado());
		}
		
		//SALVAR
		UsuarioService.getInstancia().alterar(this.getEntity());				
	}
	
	public void autenticar()
	{
		try
		{
			this.setMensagem(null);
			
			if(this.getSenha() == null
					|| this.getSenha().trim().equals(""))
			{
				throw new Exception("O campo Senha é obrigatório.");
			}
			
			Usuario usuario = new Usuario();
			usuario.setLogin(util.getUsuarioLogado().getLogin());
			usuario.setSenha(CriptoMD5.stringHexa(this.getSenha().toUpperCase()));
			usuario.setFlgAtivo("S");
			
			usuario = UsuarioService.getInstancia().get(usuario, 0);
			
			if(usuario == null)
			{
				throw new Exception("Sua senha está incorreta.");
			}
			else
			{
				this.salvar();
			}
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Seus dados foram alterados com sucesso.", null));
		}
		catch (Exception e)
		{
			this.setMensagem(e.getMessage());
		}
	}
	
	private void validar() throws Exception
	{
		//DADOS PESSOAIS
		if(this.getEntity().getNome() == null || this.getEntity().getNome().trim().equals(""))
		{
			throw new Exception("O campo Nome é obrigatório.");
		}
		
		if(this.getEntity().getEmail() == null || this.getEntity().getEmail().trim().equals(""))
		{
			throw new Exception("O campo E-mail é obrigatório.");
		}
		
		if(this.getEntity().getLogin() == null || this.getEntity().getLogin().trim().equals(""))
		{
			throw new Exception("O campo Login é obrigatório.");
		}
		
		if(this.getEntity().getCpf() == null || this.getEntity().getCpf().trim().equals(""))
		{
			throw new Exception("O campo Cpf é obrigatório.");
		}
		
		if(this.getEntity().getTelefone() == null || this.getEntity().getTelefone().trim().equals(""))
		{
			throw new Exception("O campo Telefone é obrigatório.");
		}
		
		if(this.getEntity().getDataNascimento() == null || this.getEntity().getDataNascimento().toString().trim().equals(""))
		{
			throw new Exception("O campo Nascimento é obrigatório.");
		}
		
		//ENDERECO
		if(this.getEntity().getCep() == null || this.getEntity().getCep().trim().equals(""))
		{
			throw new Exception("O campo Cep é obrigatório.");
		}
		
		if(this.getEntity().getLogradouro() == null || this.getEntity().getLogradouro().trim().equals(""))
		{
			throw new Exception("O campo Logradouro é obrigatório.");
		}
		
		if(this.getEntity().getNumEndereco() == null || this.getEntity().getNumEndereco().longValue() <= 0)
		{
			throw new Exception("O campo Número da Residência é obrigatório.");
		}
		
		if(this.getEntity().getBairro() == null || this.getEntity().getBairro().trim().equals(""))
		{
			throw new Exception("O campo Bairro é obrigatório.");
		}
		
		if(this.getEntity().getCidade() == null || this.getEntity().getCidade().trim().equals(""))
		{
			throw new Exception("O campo Cidade é obrigatório.");
		}
		
		if(this.getSiglaEstado() == null || this.getSiglaEstado().equals(""))
		{
			throw new Exception("O campo Estado é obrigatório.");
		}
		
		//PROFISSIONAL
		if(this.getEntity().getIdGrupo().intValue() == GrupoService.GRUPO_PROFISSIONAL)
		{
			if(this.getEntity().getIdEspecialidade() == null || this.getEntity().getIdEspecialidade().longValue() <= 0)
			{
				throw new Exception("O campo Especialidade é obrigatório.");
			}
		}
	}
	
	@Override
	public String getFullTitle()
	{
		return this.pageTitle;
	}

	public String getSiglaEstado() {
		return siglaEstado;
	}

	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}

	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public List<Especialidade> getListaEspecialidade() {
		return listaEspecialidade;
	}

	public void setListaEspecialidade(List<Especialidade> listaEspecialidade) {
		this.listaEspecialidade = listaEspecialidade;
	}

	public List<Conselho> getListaConselho() {
		return listaConselho;
	}

	public void setListaConselho(List<Conselho> listaConselho) {
		this.listaConselho = listaConselho;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
