package br.com.a2dm.cmn.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.ResultTransformer;

import br.com.a2dm.cmn.util.validators.VerificadorPreenchidos;


public abstract class A2DMHbNgc<Entity> implements Business
{
	protected abstract Criteria montaCriteria(Session sessao, int join);
	
	@SuppressWarnings("rawtypes")
	protected abstract Map restritores();
	
	@SuppressWarnings("rawtypes")
	protected abstract Map filtroPropriedade();
	
	public Entity inserir(Entity vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = inserir(sessao, vo);
			tx.commit();
			return vo;
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
	
	public Entity inserir(Session sessao, Entity vo) throws Exception
	{
		validarInserir(sessao, vo);
		sessao.save(vo);
		return vo;
	}
	
	public Entity alterar(Entity vo) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		Transaction tx = sessao.beginTransaction();
		try
		{
			vo = alterar(sessao, vo);
			tx.commit();
			return vo;
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
	
	public Entity alterar(Session sessao, Entity vo) throws Exception
	{
		validarAlterar(sessao, vo);
		sessao.merge(vo);
		return vo;
	}

	
	
	public List<Entity> pesquisar(Entity filter, int join, ResultTransformer resultTransformer) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		try
		{
			return this.pesquisar(sessao, filter, join, resultTransformer);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Entity> pesquisar(Session session, Entity vo, int join, ResultTransformer resultTransformer)
			throws Exception
	{
		Criteria criteria = this.montaCriteria(session, join);
		
		this.setarOrdenacao(criteria, vo, join);
		
		this.restringir(criteria, vo);
		
		this.setNumeroMaximoRegistros(session, criteria, vo, join);

		criteria.setResultTransformer(resultTransformer);
		
		return criteria.list();
	}

	
	
	
	public List<Entity> pesquisar(Entity filter, int join) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		try
		{
			return this.pesquisar(sessao, filter, join);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Entity> pesquisar(Session session, Entity vo, int join)
			throws Exception
	{
		Criteria criteria = this.montaCriteria(session, join);
		
		this.setarOrdenacao(criteria, vo, join);
		
		this.restringir(criteria, vo);
		
		this.setNumeroMaximoRegistros(session, criteria, vo, join);

		return criteria.list();
	}

	
	public Entity get(Entity vo, int join, ResultTransformer resultTransformer) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		try
		{
			return this.get(sessao, vo, join, resultTransformer);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Entity get(Session session, Entity vo, int join, ResultTransformer resultTransformer)
			throws Exception
	{
		Criteria criteria = this.montaCriteria(session, join);
		
		this.setarOrdenacao(criteria, vo, join);
		
		this.restringir(criteria, vo);
		
		criteria.setResultTransformer(resultTransformer);
		
		return (Entity) criteria.uniqueResult();
	}

	
	
	public Entity get(Entity vo, int join) throws Exception
	{
		Session sessao = HibernateUtil.getSession();
		sessao.setFlushMode(FlushMode.COMMIT);
		try
		{
			return this.get(sessao, vo, join);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Entity get(Session session, Entity vo, int join)
			throws Exception
	{
		Criteria criteria = this.montaCriteria(session, join);
		
		this.restringir(criteria, vo);
		
		this.setarOrdenacao(criteria, vo, join);
		
		return (Entity) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	protected void adicionarFiltro(String mappingFieldName, RestritorHb restriction,String voFieldName) 
	{
		this.restritores().put(voFieldName, restriction);
		this.filtroPropriedade().put(voFieldName, mappingFieldName);
	}

	@SuppressWarnings("rawtypes")
	protected void restringir(Criteria criteria, Entity filter) throws Exception
	{
		Set restricoes = this.restritores().keySet();
		for (Iterator iter = restricoes.iterator(); iter.hasNext();)
		{
			String restritorNome = (String) iter.next();
			Object valor;
			try
			{
				valor = PropertyUtils.getProperty(filter, restritorNome);
			}
			catch (Exception exception)
			{
				valor = null;
			}
			if (VerificadorPreenchidos.VRF_PRC_A2DM.isPrenchido(valor))
			{
				RestritorHb restritorHb = (RestritorHb) this.restritores().get(restritorNome);
             
				restritorHb.restringir(criteria, valor, (String) this.filtroPropriedade().get(restritorNome));
			}
		}
	}
	
	protected void setarOrdenacao(Criteria criteria, Entity vo, int join) {}
	protected void validarInserir(Session sessao, Entity vo) throws Exception  {}
	protected void validarAlterar(Session sessao, Entity vo) throws Exception  {}
	protected void setNumeroMaximoRegistros(Session session, Criteria criteria, Entity filter, int join){}
}
