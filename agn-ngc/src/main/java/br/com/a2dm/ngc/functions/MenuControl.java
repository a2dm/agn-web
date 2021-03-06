package br.com.a2dm.ngc.functions;

import br.com.a2dm.cmn.util.jsf.JSFUtil;

public class MenuControl
{
	private static JSFUtil util = new JSFUtil();
	
	private MenuControl(){}
	
	public static void ativarMenu(String desMenu)
	{
		util.getSession().removeAttribute("flgMenuDsh");
		util.getSession().removeAttribute("flgMenuAgn");
		util.getSession().removeAttribute("flgMenuCfm");
		util.getSession().removeAttribute("flgMenuRcp");
		util.getSession().removeAttribute("flgMenuAtn");
		util.getSession().removeAttribute("flgMenuAts");
		util.getSession().removeAttribute("flgMenuPct");
		util.getSession().removeAttribute("flgMenuRlt");
		util.getSession().removeAttribute("flgMenuCfg");
		
		util.getSession().setAttribute(desMenu, "active");
	}
}
