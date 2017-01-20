package br.com.a2dm.cmn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.com.a2dm.cmn.entity.Usuario;
import br.com.a2dm.cmn.entity.log.UsuarioLog;
import br.com.a2dm.cmn.service.log.UsuarioServiceLog;
import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.HibernateUtil;
import br.com.a2dm.cmn.util.RestritorHb;
import br.com.a2dm.cmn.util.criptografia.CriptoMD5;
import br.com.a2dm.cmn.util.jsf.JSFUtil;
import br.com.a2dm.cmn.util.others.Email;
import br.com.a2dm.cmn.util.others.SenhaRandom;

public class UsuarioService extends A2DMHbNgc<Usuario>
{
	private JSFUtil util = new JSFUtil();
	
	private static UsuarioService instancia = null;
	
	public static final int JOIN_GRUPO = 1;
	
	public static final int JOIN_GRUPO_TELA_ACAO = 2;
	
	public static final int JOIN_TELA_ACAO = 4;
	
	public static final int JOIN_ACAO = 8;
	
	public static final int JOIN_USUARIO_CAD = 16;
	
	public static final int JOIN_USUARIO_ALT = 32;
	
	public static final int JOIN_ESTADO = 64;
	
	public static final int JOIN_CLIENTE = 128;

	@SuppressWarnings("rawtypes")
	private static Map filtroPropriedade = new HashMap();
	
	@SuppressWarnings("rawtypes")
	private static Map restritores = new HashMap();
	
	public static UsuarioService getInstancia()
	{
		if (instancia == null)
		{
			instancia = new UsuarioService();
		}
		return instancia;
	}
	
	public UsuarioService()
	{
		adicionarFiltro("nome", RestritorHb.RESTRITOR_LIKE, "nome");
		adicionarFiltro("nome", RestritorHb.RESTRITOR_EQ, "filtroMap.nome");
		adicionarFiltro("idUsuario", RestritorHb.RESTRITOR_EQ,"idUsuario");
		adicionarFiltro("idUsuario", RestritorHb.RESTRITOR_NE, "filtroMap.idUsuarioNotEq");
		adicionarFiltro("login", RestritorHb.RESTRITOR_EQ, "login");
		adicionarFiltro("login", RestritorHb.RESTRITOR_LIKE, "filtroMap.likeLogin");
		adicionarFiltro("email", RestritorHb.RESTRITOR_EQ, "email");
		adicionarFiltro("senha", RestritorHb.RESTRITOR_EQ, "senha");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("idConselho", RestritorHb.RESTRITOR_EQ, "idConselho");
		adicionarFiltro("numConselho", RestritorHb.RESTRITOR_EQ, "numConselho");
		adicionarFiltro("cpf", RestritorHb.RESTRITOR_EQ, "cpf");
		adicionarFiltro("flgAtivo", RestritorHb.RESTRITOR_EQ, "flgAtivo");
		adicionarFiltro("flgSeguranca", RestritorHb.RESTRITOR_EQ, "flgSeguranca");
		adicionarFiltro("telaAcao.idSistema", RestritorHb.RESTRITOR_EQ, "filtroMap.idSistema");
		adicionarFiltro("telaAcao.pagina", RestritorHb.RESTRITOR_EQ, "filtroMap.pagina");
		adicionarFiltro("acao.descricao", RestritorHb.RESTRITOR_EQ, "filtroMap.acao");
		adicionarFiltro("grupo.flgAtivo", RestritorHb.RESTRITOR_EQ, "filtroMap.flgAtivoGrupo");
		adicionarFiltro("listaGrupoTelaAcao.flgAtivo", RestritorHb.RESTRITOR_EQ, "filtroMap.flgAtivoGrupoTelaAcao");
		adicionarFiltro("telaAcao.flgAtivo", RestritorHb.RESTRITOR_EQ, "filtroMap.flgAtivoTelaAcao");
		adicionarFiltro("idCliente", RestritorHb.RESTRITOR_EQ, "idCliente");
	}
	
	@SuppressWarnings("unchecked")
	protected void validarInserir(Session sessao, Usuario vo) throws Exception
	{
		Criteria criteria = sessao.createCriteria(Usuario.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.eq("nome", vo.getNome()).ignoreCase());
		or.add(Restrictions.eq("email",vo.getEmail()).ignoreCase());
		or.add(Restrictions.eq("login",vo.getLogin()).ignoreCase());
		or.add(Restrictions.eq("cpf",vo.getCpf()).ignoreCase());
		criteria.add(or);
		
		List<Usuario> lista = criteria.list();
		
		if(lista != null
				&& lista.size() > 0)
		{
			if(vo.getNome().equalsIgnoreCase(lista.get(0).getNome()))
			{
				throw new Exception("Já existe um usuário cadastrado com este Nome.");
			}
			if(vo.getEmail().equalsIgnoreCase(lista.get(0).getEmail()))
			{
				throw new Exception("Já existe um usuário cadastrado com este E-mail.");
			}
			if(vo.getLogin().equalsIgnoreCase(lista.get(0).getLogin()))
			{
				throw new Exception("Já existe um usuário cadastrado com este Login.");
			}
			
			if(vo.getCpf().equalsIgnoreCase(lista.get(0).getCpf()))
			{
				throw new Exception("Já existe um usuário cadastrado com este Cpf.");
			}
		}
		
		if(vo.getIdConselho() != null
				&& vo.getIdConselho().intValue() >= 0)
		{
			Usuario usrCons = new Usuario();
			usrCons.setIdConselho(vo.getIdConselho());
			usrCons.setNumConselho(vo.getNumConselho());
			
			List<Usuario> listaUsuario = this.pesquisar(sessao, usrCons, 0);
			
			if(listaUsuario != null
					&& listaUsuario.size() > 0)
			{
				throw new Exception("Já existe um usuário com este Conselho.");
			}
		}
	}
	
	@Override
	protected void validarAlterar(Session sessao, Usuario vo) throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setFiltroMap(new HashMap<String, Object>());
		usuario.getFiltroMap().put("idUsuarioNotEq", vo.getIdUsuario());
		usuario.getFiltroMap().put("nome", vo.getNome().trim());
		usuario.setFlgAtivo(vo.getFlgAtivo());
		
		usuario = this.get(sessao, usuario, 0);
		
		if(usuario != null)
		{
			throw new Exception("Este usuário já está cadastrado na sua base de dados!");
		}
	}
	
	@Override
	public Usuario alterar(Session sessao, Usuario vo) throws Exception
	{
		this.validarAlterar(sessao, vo);
		
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(vo.getIdUsuario());		
		usuario = this.get(sessao, usuario, 0);
		
		UsuarioLog usuarioLog = new UsuarioLog();
		JSFUtil.copiarPropriedades(usuario, usuarioLog);
		UsuarioServiceLog.getInstancia().inserir(sessao, usuarioLog);
		
		sessao.merge(vo);
		
		return vo;
	}
	
	@Override
	public Usuario inserir(Session sessao, Usuario vo) throws Exception
	{
		this.validarInserir(sessao, vo);
		
		//GERAR SENHA AUTOMATICA E INSERIR USUARIO
		String senha = SenhaRandom.getSenhaRandom();
		vo.setSenha(CriptoMD5.stringHexa(senha));
		sessao.save(vo);
		sessao.flush();
		
		//ENVIAR EMAIL
		this.enviarEmailSenha(vo, senha);
		
		return vo;
	}
	
	private void enviarEmailSenha(Usuario vo, String senha) throws Exception
	{
		Email email = new Email();
		
		String assunto = "Acesso Speciale - Sistema de Pedido";
		String texto = "Nome: "+ vo.getNome() +" \n" +
				   "Login: "+ vo.getLogin() +" \n" +
				   "Senha: "+ senha;
		
		String to = vo.getEmail();
		
		email.enviar(to, assunto, texto);		
	}
	
	public Usuario ativar(Usuario vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = ativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("N");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Usuario ativar(Session sessao, Usuario vo) throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(vo.getIdUsuario());
		usuario = this.get(sessao, usuario, 0);
		
		UsuarioLog usuarioLog = new UsuarioLog();
		JSFUtil.copiarPropriedades(usuario, usuarioLog);
		usuarioLog.setLogMapping("REGISTRO ATIVADO");
		UsuarioServiceLog.getInstancia().inserir(sessao, usuarioLog);
		
		vo.setFlgAtivo("S");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDataAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	public Usuario inativar(Usuario vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = inativar(sessao, vo);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("S");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}

	public Usuario inativar(Session sessao, Usuario vo) throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(vo.getIdUsuario());
		usuario = this.get(sessao, usuario, 0);
		
		UsuarioLog usuarioLog = new UsuarioLog();
		JSFUtil.copiarPropriedades(usuario, usuarioLog);
		usuarioLog.setLogMapping("REGISTRO INATIVADO");
		UsuarioServiceLog.getInstancia().inserir(sessao, usuarioLog);
		
		vo.setFlgAtivo("N");
		vo.setIdUsuarioAlt(util.getUsuarioLogado().getIdUsuario());
		vo.setDataAlteracao(new Date());
		
		super.alterar(sessao, vo);
		
		return vo;
	}
	
	public Usuario alterarSenha(Usuario vo, String novaSenha) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = alterarSenha(sessao, vo, novaSenha);
			tx.commit();
			return vo;
		}
		catch (Exception e)
		{
			vo.setFlgAtivo("N");
			tx.rollback();
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	public Usuario alterarSenha(Session sessao, Usuario vo, String novaSenha) throws Exception
	{
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(vo.getIdUsuarioAlt());
		usuario.setNome(vo.getNome());
		usuario.setSenha(CriptoMD5.stringHexa(vo.getSenha().toUpperCase()));
		
		usuario = this.get(sessao, usuario, 0);
		
		if(usuario != null)
		{
			usuario.setSenha(CriptoMD5.stringHexa(novaSenha.toUpperCase()));
			usuario.setIdUsuarioAlt(vo.getIdUsuarioAlt());
			usuario.setDataAlteracao(vo.getDataAlteracao());
			
			usuario = this.alterar(sessao, usuario);
		}
		else
		{
			throw new Exception("O campo Senha Atual está incorreto.");
		}
		
		return usuario;
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

	@Override
	protected Criteria montaCriteria(Session sessao, int join)
	{
		Criteria criteria = sessao.createCriteria(Usuario.class);
		
		if ((join & JOIN_GRUPO) != 0)
	    {
			criteria.createAlias("grupo", "grupo");
			
			if ((join & JOIN_GRUPO_TELA_ACAO) != 0)
		    {
		         criteria.createAlias("grupo.listaGrupoTelaAcao", "listaGrupoTelaAcao");
		         
		         if ((join & JOIN_TELA_ACAO) != 0)
				 {
		        	 criteria.createAlias("listaGrupoTelaAcao.telaAcao", "telaAcao");
		        	 
		        	 if ((join & JOIN_ACAO) != 0)
					 {
			        	 criteria.createAlias("telaAcao.acao", "acao");
					 }
				 }
		    }
	    }
		
		if ((join & JOIN_USUARIO_CAD) != 0)
	    {
	         criteria.createAlias("usuarioCad", "usuarioCad");
	    }
		
		if ((join & JOIN_USUARIO_ALT) != 0)
	    {
	         criteria.createAlias("usuarioAlt", "usuarioAlt", JoinType.LEFT_OUTER_JOIN);
	    }
		
		if ((join & JOIN_ESTADO) != 0)
	    {
	         criteria.createAlias("estado", "estado", JoinType.LEFT_OUTER_JOIN);
	    }
		
		if ((join & JOIN_CLIENTE) != 0)
	    {
	         criteria.createAlias("cliente", "cliente");
	    }
		
		return criteria;
	}
	
	@Override
	protected void setarOrdenacao(Criteria criteria, Usuario vo, int join)
	{
		criteria.addOrder(Order.asc("nome"));
	}
}
