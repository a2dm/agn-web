package br.com.a2dm.cmn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.a2dm.cmn.entity.Parametro;
import br.com.a2dm.cmn.entity.RecuperarSenha;
import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.entity.log.UsuarioLog;
import br.com.a2dm.cmn.service.log.UsuarioServiceLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.criptografia.CriptoMD5;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.others.Email;

public class RecuperarSenhaService extends A2DMHbNgc<RecuperarSenha>
{
	private static RecuperarSenhaService instancia = null;
		
	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static RecuperarSenhaService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new RecuperarSenhaService();
		}
		return instancia;
	}
	
	public RecuperarSenhaService()
	{
		adicionarFiltro("codigo", RestritorHb.RESTRITOR_EQ, "codigo");
		adicionarFiltro("codigoUsuario", RestritorHb.RESTRITOR_EQ, "codigoUsuario");
		adicionarFiltro("hash", RestritorHb.RESTRITOR_EQ, "hash");
	}
	
	public void gerarHash(String email) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			this.gerarHash(sessao, email);
			tx.commit();
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public void gerarHash(Session sessao, String email) throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setFlgAtivo("S");
		usuario.setEmail(email);
		usuario = UsuarioService.getInstancia().get(sessao, usuario, 0);

		if(usuario != null)
		{
			//VERIFICA SE EXISTE ALGUM HASH PARA O USUARIO E EXCLUI, SE TIVER
			RecuperarSenha recuperarSenha = new RecuperarSenha();
			recuperarSenha.setIdUsuario(usuario.getIdUsuario());			
			List<RecuperarSenha> lista = this.pesquisar(sessao, recuperarSenha, 0);
			
			if(lista != null
					&& lista.size() > 0)
			{
				for (RecuperarSenha objRemove : lista)
				{
					sessao.delete(objRemove);
				}
			}
			
			//GERAR NOVA HASH COM NOVA DATA
			RecuperarSenha objInsert = new RecuperarSenha();
			objInsert.setIdUsuario(usuario.getIdUsuario());
			objInsert.setDatCadastro(new Date());
			
			sessao.save(objInsert);
			sessao.flush();
			
			String hash = objInsert.getIdUsuario().intValue() + "SPECIALE"+objInsert.getDatCadastro().getTime();
			objInsert.setHash(CriptoMD5.stringHexa(hash));
			
			sessao.merge(objInsert);
			sessao.flush();
			
			this.enviarEmailRecuperarSenha(sessao, usuario, objInsert.getHash());
		}
		else
		{
			throw new Exception("Não existe um usuário ativo com este E-mail!");
		}
	}
	
	public void deletar(RecuperarSenha vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			this.deletar(sessao, vo);
			tx.commit();
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public void deletar(Session sessao, RecuperarSenha vo) throws Exception
	{
		sessao.delete(vo);
	}
	
	private void enviarEmailRecuperarSenha(Session sessao, Usuario vo, String hash) throws Exception
	{
		Parametro parametro = new Parametro();
		parametro.setDescricao("CAMINHOAPP");
		parametro = ParametroService.getInstancia().get(sessao, parametro, 0);
		
		Email email = new Email();

		String assunto = "Speciale - Recuperar Senha";
		String texto = "Prezado (a): "+ vo.getNome() +", Você acabou de solicitar a recuperação de senha do sistema da Speciale. \n\n\n" +
				       "Para proceder, por favor clique no link abaixo: \n\n" +
				       "" +	parametro.getValor() + "pages/recuperarSenha.jsf?b=" + hash;
		
		String to = vo.getEmail();
		
		email.enviar(to, assunto, texto);
	}
	
	public void atualizarNovaSenha(Usuario vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			this.atualizarNovaSenha(sessao, vo);
			tx.commit();
		}
		catch (Exception e)
		{
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public void atualizarNovaSenha(Session sessao, Usuario vo) throws Exception
	{
		String novaSenha = CriptoMD5.stringHexa(vo.getNovaSenha().toUpperCase().trim());
		
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(vo.getIdUsuario());
		usuario = UsuarioService.getInstancia().get(sessao, usuario, 0);
		
		if(!novaSenha.equals(usuario.getSenha()))
		{
			//INSERINDO LOG
			UsuarioLog usuarioLog = new UsuarioLog();
			JSFUtil.copiarPropriedades(usuario, usuarioLog);
			UsuarioServiceLog.getInstancia().inserir(sessao, usuarioLog);
			
			//ALTERANDO SENHA
			usuario.setDataAlteracao(new Date());
			usuario.setIdUsuarioAlt(vo.getIdUsuario());
			usuario.setSenha(novaSenha);
			sessao.merge(usuario);
			
			//INATIVANDO O LINK DE RECUPERAR SENHA
			RecuperarSenha recuperarSenha = new RecuperarSenha();
			recuperarSenha.setIdUsuario(vo.getIdUsuario());
			
			List<RecuperarSenha> lista = RecuperarSenhaService.getInstancia().pesquisar(sessao, recuperarSenha, 0);
			
			if(lista != null
					&& lista.size() > 0)
			{
				for (RecuperarSenha objDelete : lista)
				{
					sessao.delete(objDelete);
				}
			}
		}
		else
		{
			throw new Exception("Favor informar uma senha diferente da anterior!");
		}
	}
	
	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(RecuperarSenha.class);
		return criteria;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected Map restritores() 
	{
		return restritores;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Map filtroPropriedade() 
	{
		return filtroPropriedade;
	}
}
