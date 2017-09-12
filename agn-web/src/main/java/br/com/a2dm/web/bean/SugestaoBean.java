package br.com.a2dm.web.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.a2dm.cmn.util.jsf.AbstractBean;
import br.com.a2dm.cmn.util.jsf.Variaveis;
import br.com.a2dm.ngc.entity.Dominio;
import br.com.a2dm.ngc.entity.Sugestao;
import br.com.a2dm.ngc.functions.MenuControl;
import br.com.a2dm.ngc.functions.UtilFuncions;
import br.com.a2dm.ngc.service.DominioService;
import br.com.a2dm.ngc.service.SugestaoService;


@RequestScoped
@ManagedBean
public class SugestaoBean extends AbstractBean<Sugestao, SugestaoService>
{
	private List<Dominio> listaTipoSugestao;
	
	public SugestaoBean()
	{
		super(SugestaoService.getInstancia());
		this.ACTION_SEARCH = "sugestao";
		this.pageTitle = "Sugestão";
		
		MenuControl.ativarMenu("flgMenuCfg");
	}

	public String preparaPesquisar()
	{
		try
		{
			if(validarAcesso(Variaveis.ACAO_PREPARA_PESQUISAR))
			{
				setSearchObject(getNewEntityInstance());
				setValoresDefault();
				setCurrentState(STATE_SEARCH);
				setListaPesquisa();
				
				this.popularListaSugestao();
			}
		}
		catch (Exception e)
		{
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			if(e.getMessage() == null)
				FacesContext.getCurrentInstance().addMessage("", message);
			else
				FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return ACTION_SEARCH;
	}
	
	private void popularListaSugestao() throws Exception
	{
		Sugestao sugestao = new Sugestao();
		sugestao.setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		
		List<Sugestao> lista = SugestaoService.getInstancia().pesquisar(sugestao, 0);
		
		for (Sugestao obj : lista)
		{
			if(obj.getDesSugestao().length() > 115)
			{
				obj.setDesSugestao(obj.getDesSugestao().substring(0, 200) + " ...");
			}
		}
		
		this.setSearchResult(lista);
	}
	
	@Override
	protected void setDefaultInserir() throws Exception
	{
		Dominio dominio = new Dominio();
		dominio.setDesDominio("Escolha o Tipo de Sugestão");
		
		//POPULANDO LISTA TIPO SUGESTAO
		Dominio tipoSugestao = new Dominio();
		tipoSugestao.setRefDominio(DominioService.TIPO_SUGESTAO);
		
		List<Dominio> lista = DominioService.getInstancia().pesquisar(tipoSugestao, 0);
		
		List<Dominio> listaFinal = new ArrayList<Dominio>();
		listaFinal.add(dominio);
		listaFinal.addAll(lista);
		
		this.setListaTipoSugestao(listaFinal);		 
	}
	
	@Override
	public void clean(ActionEvent event) 
	{
		this.preparaPesquisar();
	}
	
	public List<Dominio> getListaTipoSugestao() {
		return listaTipoSugestao;
	}

	public void setListaTipoSugestao(List<Dominio> listaTipoSugestao) {
		this.listaTipoSugestao = listaTipoSugestao;
	}
	
	@Override
	protected void validarInserir() throws Exception
	{
		if(this.getEntity().getIdTipoSugestao() == null
				|| this.getEntity().getIdTipoSugestao().intValue() <= 0)
		{
			throw new Exception("O campo Tipo de Sugestão é obrigatório!");			
		}
		
		if(this.getEntity().getDesSugestao() == null
				|| this.getEntity().getDesSugestao().trim().equals(""))
		{
			throw new Exception("O campo Sugestão é obrigatório!");			
		}
	}
	
	@Override
	protected void completarInserir() throws Exception
	{
		this.getEntity().setIdClinicaProfissional(UtilFuncions.getClinicaProfissionalSession().getIdClinicaProfissional());
		this.getEntity().setDatCadastro(new Date());
		this.getEntity().setFlgLida("N");
		this.getEntity().setIdSituacaoSugestao(new BigInteger(Integer.toString(SugestaoService.SITUACAO_ABERTA)));
	}
}