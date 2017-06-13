package br.com.a2dm.ngc.entity.vo;

import java.io.Serializable;

/** 
 * @author Carlos Diego
 * @since 31/05/2017
 */
public class RetornoVo implements Serializable
{
	private static final long serialVersionUID = -2086907695357206806L;

	private boolean flgRetorno;
	
	private String mensagem;

	public boolean isFlgRetorno() {
		return flgRetorno;
	}

	public void setFlgRetorno(boolean flgRetorno) {
		this.flgRetorno = flgRetorno;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
