package br.com.a2dm.cmn.util.jsf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.BeanMap;

import br.com.a2dm.cmn.entity.Usuario;


@SuppressWarnings("deprecation")
public class JSFUtil 
{
	
	public static final String acao = "IsghAcaoExecutar";
	
    /**
     * This method is responsible by getting the request context name
     * @return String current request context name
     */
    public String getRequestContext()
    {
        // getting current context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        // returning request context path
        return facesContext.getExternalContext().getRequestContextPath();
    }
    
    /**
     * This method is responsible by returning the current faces context
     * @return FacesContext current faces context
     */
    public FacesContext getFacesContext()
    {
        // returning request context path
        return FacesContext.getCurrentInstance();
    }
    
    /**
     * Method responsible by adding a message to be displayed in application
     * 
     * @param String message key from bundle to be displayed
     * @param Severity message's severity
     */
    public void addMessage(String messageBundleKey, Severity facesSeverity)
    {       
        // translanting message
        ResourceBundle bundle = getBundle();
        String translatedMessage = bundle.getString(messageBundleKey);
        
        // getting message map and adding a new message
        Map<Severity, String> messageMap = getMessagesMap();
        messageMap.put(facesSeverity, translatedMessage);
    }

    
    /**
     * This method is responsible by getting the message map into session and,
     * if it doesn't exist, create it to be used.
     * 
     * @return Map<Severity, String> messages map retrieved
     */
    private Map<Severity, String> getMessagesMap() 
    {
        /*
         * This map will be used to replace the messages function in jsf. There are some 
         * problems in using the funcionality for some jsf lifecycle's steps. The map will  
         * work like the jsf messages. For while, just one one message can be put into map to 
         * each type of severity. Although, is perfectly possible to extend this function.
         * 
         * JSF messages will be used only for tags who supports conversion.
         */
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map <String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        @SuppressWarnings("unchecked")
		Map<Severity, String> messageMap = (Map<Severity, String>) sessionMap.get("message_map");
        if ( messageMap == null )
        {
            messageMap = new HashMap<Severity, String>();
            sessionMap.put("message_map", messageMap);
        }
        
        return messageMap;
    }
    
    /**
     * This method is responsible by getting the maximum severity message from
     * message map. The severity degree is analyzed based on jsf severity object. 
     * 
     * @return Severity severity object representing the maximum severity
     */
    public Severity getMaximumMessageSeverity()
    {
        // checking the meximum message severity to be returned
        Map<Severity, String> messageMap = getMessagesMap();
        Severity maximumSeverity = null;
        
        if ( !messageMap.isEmpty() )
        {
            maximumSeverity = FacesMessage.SEVERITY_INFO;
            for(Severity severity: messageMap.keySet())
            {
                if (maximumSeverity.getOrdinal() < severity.getOrdinal())
                {
                    maximumSeverity = severity;
                }
            }
        }
        
        return maximumSeverity;
    }
    
    /**
     * Return the error message from message map
     * 
     * @return String error message from message map
     */
    public String getErrorMessage()
    {
        Map<Severity, String> messageMap = getMessagesMap();
        
        return messageMap.remove(FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Return the warning message from message map
     * 
     * @return String warning message from message map
     */
    public String getWarningMessage()
    {
        Map<Severity, String> messageMap = getMessagesMap();
        
        return messageMap.remove(FacesMessage.SEVERITY_WARN);
    }

    /**
     * Return the success message from message map
     * 
     * @return String success message from message map
     */
    public String getSuccessMessage()
    {
        Map<Severity, String> messageMap = getMessagesMap();
        
        return messageMap.remove(FacesMessage.SEVERITY_INFO);
    }
    
    /**
     * Method responsible by getting the resource bundle in according of 
     * the user locale. If the user's locale doesn't have a bundle file, use default
     * @return ResourceBundle resource bundle
     */
    public ResourceBundle getBundle()
    {
        // getting faces context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        // getting resource bundle
        Application facesApplication = facesContext.getApplication();       
        ResourceBundle bundle = null;
        try
        {
            bundle = ResourceBundle.getBundle(facesApplication.getMessageBundle(), facesContext.getExternalContext().getRequestLocale());
        }
        catch(MissingResourceException mre)
        {
            // if not found, get default
            bundle = ResourceBundle.getBundle(facesApplication.getMessageBundle(), facesApplication.getDefaultLocale());
        }
        
        // returning translated message
        return bundle;
    }
    
    public HttpSession getSession()
    {
       return ((HttpServletRequest) getFacesContext().getExternalContext().getRequest()).getSession();
    }
    
    public Usuario getUsuarioLogado() throws Exception
   {

      Object usuarioLogadoObj = getSession().getAttribute(AbstractBean.USUARIO_LOGADO);
      if (usuarioLogadoObj == null)
      {
         throw new Exception("N�o existe usu�rio logado no sistema!");
      }

      return (Usuario) usuarioLogadoObj;
   }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copiarPropriedades(Object origem, Object destino)
    	throws Exception
    {
		try {
			if (destino instanceof Map) {
				Map voMap = (Map) destino;
				BeanMap bm = new BeanMap(origem);
				voMap.putAll(bm);
				voMap.remove("class");
				voMap.remove("servletWrapper");
				voMap.remove("multipartRequestHandler");
			} else {
				PropertyUtils.copyProperties(destino, origem);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
    	
    }
    
	public static boolean validarData(String data, String separador) {
		try {
			int dd, mm, aaaa;
			int[] numeroDiasMes = {31,28,31,30,31,30,31,31,30,31,30,31};
			
			if (data.equals(""))
				return true;
			try {
				dd = Integer.parseInt(data.substring(0, data.indexOf(separador)));
				data = data.substring(data.indexOf(separador) + 1);
				mm = Integer.parseInt(data.substring(0, data.indexOf(separador)));
				data = data.substring(data.indexOf(separador) + 1);
				aaaa = Integer.parseInt(data);
			} catch (NumberFormatException e) {
				return false;
			} catch (StringIndexOutOfBoundsException s) {
				return false;
			}
			if (anoBisexto(aaaa))
				numeroDiasMes[1] = 29;
			else
				numeroDiasMes[1] = 28;

			if (aaaa < 1900) {
				return false;
			} else if (mm < 1 || mm > 12) {
				return false;
			} else if (dd < 1 || dd > numeroDiasMes[mm - 1]) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
    public static boolean anoBisexto(int ano)
    {
        if((ano % 4) != 0)
            return false;
        return true;
    }	
    
    public static String obterSetorRH() 
    {
		return FacesContext.getCurrentInstance()
				.getExternalContext()
				.getInitParameter("setorRH");
	}
    
    public static String obterSetorNAC() 
    {
		return FacesContext.getCurrentInstance()
				.getExternalContext()
				.getInitParameter("setorNAC");
	}
    
    public static String obterSetorCentroEstudo() 
    {
		return FacesContext.getCurrentInstance()
				.getExternalContext()
				.getInitParameter("setorCentroEstudo");
	}
    
    public static Date converteStringData(String dataCaracter)
    {
    	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    	Date dataConvertida;
    	if (dataCaracter == "") {
    		return null;
    	} 
    	try {
    		dataConvertida = formato.parse(dataCaracter);
    	} catch (ParseException e) {
    		dataConvertida = null;
    	}
    	return dataConvertida;
    }
    
    public static Integer getNumSistema()
    {
    	return Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext()
				.getInitParameter("numSistema"));
    }
    
}
