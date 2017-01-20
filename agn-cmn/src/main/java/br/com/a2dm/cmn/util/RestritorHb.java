package br.com.a2dm.cmn.util;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public interface RestritorHb
{
   public static final RestritorHb RESTRITOR_IDEQ = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.idEq(valor));
      }
   };
   
   public static final RestritorHb RESTRITOR_LT = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.lt(propriedade, valor));
      }
   };
   
   public static final RestritorHb RESTRITOR_LE = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.le(propriedade, valor));
      }
   };
   
   public static final RestritorHb RESTRITOR_GT = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.gt(propriedade, valor));
      }
   };

   public static final RestritorHb RESTRITOR_GE = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.ge(propriedade, valor));
      }
   };
   
   public static final RestritorHb RESTRITOR_EQ = new RestritorHb()
   {
	  public void restringir(Criteria criteria, Object valor, String propriedade)
      {
    	 if(valor instanceof String)
    	 {
    		 criteria.add(Restrictions.eq(propriedade, valor).ignoreCase());
    	 }
    	 else
    	 {
    		 criteria.add(Restrictions.eq(propriedade, valor));
    	 }
      }
   };

   /**
    * Apply a "not equal" constraint to two properties
    */
   public static final RestritorHb RESTRITOR_NE = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.ne(propriedade, valor));
      }
   };
   
   @SuppressWarnings("rawtypes")
   public static final RestritorHb RESTRITOR_IN = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         if (valor instanceof Collection)
         {
            Collection valorColl = (Collection) valor;
            criteria.add(Restrictions.in(propriedade, valorColl));
         }
         else if (valor instanceof Object[])
         {
            Object[] valorArr = (Object[]) valor;
            criteria.add(Restrictions.in(propriedade, valorArr));
         }
      }
   };
   
   public static final RestritorHb RESTRITOR_NI = new RestritorHb()
   {
   
	   @SuppressWarnings("rawtypes")
	   public void restringir(Criteria criteria, Object valor, String propriedade)
	   {
		   if (valor instanceof Collection)
		   {
			   Collection valorColl = (Collection) valor;
			   criteria.add(Restrictions.not(Restrictions.in(propriedade, valorColl)));
		   }
		   else if (valor instanceof Object[])
		   {
			   Object[] valorArr = (Object[]) valor;
			   criteria.add(Restrictions.not(Restrictions.in(propriedade, valorArr)));
		   }
	   }
   };   
   
   public static final RestritorHb RESTRITOR_DATA_FINAL = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         if (valor instanceof java.util.Date)
         {
            Date dt = new Date(((java.util.Date) valor).getTime());
            GregorianCalendar cg = new GregorianCalendar();
            cg.setTime(dt);
            cg.add(Calendar.DATE, 1);
            dt = new java.sql.Date(cg.getTimeInMillis());
            criteria.add(Restrictions.lt(propriedade, dt));
         }
      }
   };

   public static final RestritorHb RESTRITOR_DATA_INICIAL = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         if (valor instanceof java.util.Date)
         {
            criteria.add(Restrictions.ge(propriedade, valor));
         }
      }
   };

   public static final RestritorHb RESTRITOR_LIKE_GENERICO = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         if(valor instanceof String )
         {
            criteria.add(Restrictions.like(propriedade,valor).ignoreCase());
         }
      }
   };
   
   public static final RestritorHb RESTRITOR_LIKE = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         if(valor instanceof String )
         {
            criteria.add(Restrictions.like(propriedade,"%" + valor + "%").ignoreCase());
         }
      }
   };
   
   public static final RestritorHb RESTRITOR_LIKE_INICIO = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         if(valor instanceof String )
         {
            criteria.add(Restrictions.like(propriedade,valor + "%").ignoreCase());
         }
      }
   };
   
   /**
    * Restritor generico para ordenacoes.
    * 
    * collection de String na forma "propriedade:asc/desc". Exemplo: new
    * ArrayList(Arrays.asList(new String[]{"data:desc", "estabelecimento:asc"}))
    */
   @SuppressWarnings("rawtypes")
   public static final RestritorHb RESTRITOR_ORDENS = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor,
            String propriedade)
      {
		Collection ordens = (Collection) valor;
         StringTokenizer st;
         for (Iterator iter = ordens.iterator(); iter.hasNext();)
         {
            st = new StringTokenizer((String)iter.next(), ":");
            String ordemPrp = st.nextToken().trim();
            String ascDesc = st.nextToken().trim();
            if (ascDesc.equalsIgnoreCase("desc"))
            {
               criteria.addOrder(Order.desc(ordemPrp));
            }
            else
            {
               criteria.addOrder(Order.asc(ordemPrp));
            }
         }
      }
   };
   
   public static final RestritorHb RESTRITOR_IS_NULL = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.isNull(propriedade));
      }
   };
   
   public static final RestritorHb RESTRITOR_IS_NOTNULL = new RestritorHb()
   {
      public void restringir(Criteria criteria, Object valor, String propriedade)
      {
         criteria.add(Restrictions.isNotNull(propriedade));
      }
   };
   
   /**
    * Executa a adicao da {@link Criterion} no <code>criteria</code>.
    * 
    * @param criteria
    * @param valor Valor a ser atribuido na <code>criteria</code>.
    * @param propriedade nome da propriedade a qual ira ser aplicado o filtro.
    *           Opcional
    */
   public void restringir(Criteria criteria, Object valor, String propriedade);
   


}