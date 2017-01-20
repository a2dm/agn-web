package br.com.a2dm.cmn.util.validators;

import java.util.Collection;

public class VerificadorPreenchidoA2DM implements VerificadorPreenchido{

	@Override
	@SuppressWarnings("rawtypes")
	public boolean isPrenchido(Object obj) {
	      if (obj == null)
	      {
	         return false;
	      }
	      if (obj instanceof Collection)
	      {
	         return !((Collection) obj).isEmpty();
	      }
	      else
	      {
	         return !obj.toString().equalsIgnoreCase("");
	      }
	}

}
