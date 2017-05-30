package br.com.a2dm.cmn.util.others;

import java.io.File;

public class Utilitarios
{
	public static void removerArquivos(String dir)
	{
		File file = new File(dir);
		
		if (file.isDirectory())
		{
            File[] files = file.listFiles();
            
            for (File f : files)
            {
                    f.delete();
            }
		}
	}
	
	/**
	 * Metodo que soma duas horas no formato hh:mm e retorna o resultado em String (hh:mm)
	 * Formato da hora: hh:mm
	 * @param hora1
	 * @param hora2
	 */
	public static String somarHoras(String hora1, Integer minAdd)
	{
        int minutosInicio      = (Integer.parseInt(hora1.substring(0,2))*60)+(Integer.parseInt(hora1.substring(3,5)));   
        int minutosAdicionar   = minAdd;
        
        int minutosFinal = minutosInicio + minutosAdicionar;
        
        int horas   = 0;
        int minutos = 0;
        
        if(minutosFinal > minutosFinal / 60)
        {
        	horas   = minutosFinal / 60; 
        	minutos = minutosFinal - horas*60;
        }
        else
        {
        	horas = 0;
        	minutos = minutosFinal;
        }
        
        String hr = horas+"";
        String mn = minutos+"";
        
        hr = hr.length() < 2 ? "0"+hr : hr;
        mn = mn.length() < 2 ? "0"+mn : mn;
        
        return hr+":"+mn;
	}
}
