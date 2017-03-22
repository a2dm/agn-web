package br.com.a2dm.web.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.a2dm.cmn.entity.Estado;
import br.com.a2dm.cmn.service.EstadoService;
import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.validators.ValidaPermissao;
import br.com.a2dm.ngc.entity.Paciente;
import br.com.a2dm.ngc.service.PacienteService;
import br.com.a2dm.web.configuracao.MenuControl;


@RequestScoped
@ManagedBean
public class PacienteBean extends AbstractBean<Paciente, PacienteService>
{
	private List<Estado> listaEstado;
	
	private JSFUtil util = new JSFUtil();
	
	public PacienteBean()
	{
		super(PacienteService.getInstancia());
		this.ACTION_SEARCH = "paciente";
		this.pageTitle = "Paciente";
		
		MenuControl.ativarMenu("flgMenuPct");
	}
		
	@Override
	protected void setDefaultInserir() throws Exception 
	{
		this.getEntity().setSexPaciente("M");
	}
	
	@Override
	protected void setListaInserir() throws Exception
	{
		//LISTA DE ESTADOS
		List<Estado> resultEst = EstadoService.getInstancia().pesquisar(new Estado(), 0);
		
		Estado est = new Estado();
		est.setDescricao("Escolha o Estado");
		
		List<Estado> listaEstado = new ArrayList<Estado>();
		listaEstado.add(est);
		listaEstado.addAll(resultEst);
		
		this.setListaEstado(listaEstado);
	}
	
	@Override
	protected void completarInserir() throws Exception
	{
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setIdUsuarioCad(util.getUsuarioLogado().getIdUsuario());
		this.getEntity().setFlgCompleto("S");
	}
	
	@Override
	protected boolean validarAcesso(String acao)
	{
		boolean temAcesso = true;

		if (!ValidaPermissao.getInstancia().verificaPermissao("paciente", acao))
		{
			temAcesso = false;
			HttpServletResponse rp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			try
			{
				rp.sendRedirect("/agn-web/pages/acessoNegado.jsf");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return temAcesso;
	}
	
	@Override
	protected void validarInserir() throws Exception {
		
		if(this.getEntity().getNomPaciente() == null || this.getEntity().getNomPaciente().trim().equals(""))
		{
			throw new Exception("O campo Nome é obrigatório.");
		}
		
		if(this.getEntity().getEmlPaciente() == null || this.getEntity().getEmlPaciente().trim().equals(""))
		{
			throw new Exception("O campo E-mail é obrigatório.");
		}
		
		if(this.getEntity().getCpfPaciente() == null || this.getEntity().getCpfPaciente().trim().equals(""))
		{
			throw new Exception("O campo Cpf é obrigatório.");
		}
		
		if(this.getEntity().getDatNascimento() == null || this.getEntity().getDatNascimento().toString().equals(""))
		{
			throw new Exception("O campo Data de Nascimento é obrigatório.");
		}
		
		if(this.getEntity().getCepPaciente() == null || this.getEntity().getCepPaciente().trim().equals(""))
		{
			throw new Exception("O campo Cep é obrigatório.");
		}
		
		if(this.getEntity().getLgdPaciente() == null || this.getEntity().getLgdPaciente().trim().equals(""))
		{
			throw new Exception("O campo Logradouro é obrigatório.");
		}
		
		if(this.getEntity().getNumEndereco() == null || this.getEntity().getNumEndereco().longValue() <= 0)
		{
			throw new Exception("O campo Número da Residência é obrigatório.");
		}
		
		if(this.getEntity().getBrrPaciente() == null || this.getEntity().getBrrPaciente().trim().equals(""))
		{
			throw new Exception("O campo Bairro é obrigatório.");
		}
		
		if(this.getEntity().getCidPaciente() == null || this.getEntity().getCidPaciente().trim().equals(""))
		{
			throw new Exception("O campo Cidade é obrigatório.");
		}
		
		if(this.getEntity().getIdEstado() == null || this.getEntity().getIdEstado().longValue() <= 0)
		{
			throw new Exception("O campo Estado é obrigatório.");
		}
		
		if(this.getEntity().getDatNascimento() != null
				&& this.getEntity().getDatNascimento().after(new Date())){
			throw new Exception("O campo Data de Nascimento deve ser anterior a Data de Hoje.");
		}
		
	}

	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}
}
