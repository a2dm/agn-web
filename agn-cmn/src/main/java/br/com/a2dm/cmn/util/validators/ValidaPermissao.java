package br.com.a2dm.cmn.util.validators;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.service.UsuarioService;
import br.com.a2dm.cmn.util.jsf.JSFUtil;


/**
 * 
 * @author Carlos Diêgo
 * @since 31/05/2016
 *
 */
public class ValidaPermissao 
{
	private static ValidaPermissao instancia = null;
	
	private JSFUtil util = new JSFUtil();
	
	public static final String NUM_SISTEMA = "numSistema";
	
	public static ValidaPermissao getInstancia()
	{
		if (instancia == null)
		{
			instancia = new ValidaPermissao();
		}
		return instancia;
	}
	
	/**
	 * Método utilizado para verificar a permissão de acesso para o usuário	
	 * 
	 * @param usuario
	 * @param pagina
	 * @param acao
	 * 
	 * @return boolean true (se tiver acesso) ou false (se não tiver acesso)
	 * 
	 */
	public boolean verificaPermissao(String pagina, String acao)
	{
		boolean retorno = false;

		try
		{
			Usuario objPesquisa = new Usuario();
			objPesquisa.setFiltroMap(new HashMap<String, Object>());
			
			objPesquisa.setIdUsuario(util.getUsuarioLogado().getIdUsuario());
			objPesquisa.setFlgAtivo("S");
			objPesquisa.getFiltroMap().put("idSistema", this.getNumSistema());
			objPesquisa.getFiltroMap().put("pagina", pagina.toUpperCase());
			objPesquisa.getFiltroMap().put("acao", acao.toUpperCase());
			objPesquisa.getFiltroMap().put("flgAtivoGrupo", "S");
			objPesquisa.getFiltroMap().put("flgAtivoTelaAcao", "S");
			objPesquisa.getFiltroMap().put("flgAtivoGrupoTelaAcao", "S");

			int join = this.getJoin();
			
			
			List<Usuario> lista = UsuarioService.getInstancia().pesquisar(objPesquisa, join);
			
			if(lista != null 
					&& lista.size() > 0)
			{
				retorno = true;
			}
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		return retorno;
	}	
	

	private int getJoin()
	{
		return UsuarioService.JOIN_GRUPO 
			 | UsuarioService.JOIN_GRUPO_TELA_ACAO 
			 | UsuarioService.JOIN_TELA_ACAO
			 | UsuarioService.JOIN_ACAO;
	}
	
	private BigInteger getNumSistema()
	{
		return new BigInteger(FacesContext.getCurrentInstance().getExternalContext().getInitParameter(NUM_SISTEMA));
	}	
}
