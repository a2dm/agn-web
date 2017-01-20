package br.com.a2dm.cmn.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.a2dm.cmn.entity.Usuario;

/**
 * @author Carlos Diego
 */

public class AutorizacaoFilter implements Filter
{
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
	        Usuario usuario = (Usuario)((HttpServletRequest)request).getSession().getAttribute("loginUsuario");
	        
	        HttpServletRequest req = (HttpServletRequest) request;
	            
	        if (! (req).getRequestURI().contains("/login.jsf")
	        		&& ! (req).getRequestURI().contains("/recuperarSenha.jsf")
	        		&& ! (req).getRequestURI().contains("/recuperarSenhaExpired.jsf")
	        		&& (usuario == null || usuario.getIdUsuario() == null) 
	        		&& ( !(req).getRequestURI().contains(".js.jsf"))  
	        		&& ( !(req).getRequestURI().contains(".css.jsf")) 
	        		&& ( !(req).getRequestURI().contains(".ecss.jsf"))
	        		&& ( !(req).getRequestURI().contains(".png.jsf"))
	        		&&( !(req).getRequestURI().equals("/agn-seg/"))  )
	        {	        		        	
	        	((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/pages/login.jsf");
	        }
	        else
	        {
	            chain.doFilter(request, response);	              
	        }
	}

	public void init(FilterConfig config) throws ServletException {
		// Nothing to do here!
	}

	public void destroy() {
		// Nothing to do here!
	}

}