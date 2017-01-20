package br.com.a2dm.cmn.util.others;

import java.util.Random;

public class SenhaRandom
{
	private static final char[] ALL_CHARS = new char[62];  
    private static final Random RANDOM = new Random();
    private static final int tamanho = 5;
    
    static
    {  
        for (int i = 48, j = 0; i < 123; i++)
        {  
            if (Character.isLetterOrDigit(i))
            {  
                ALL_CHARS[j] = (char) i;  
                j++;  
            }  
        }  
    }
    
    public static String getSenhaRandom()
    {
    	final char[] result = new char[tamanho];
    	String retorno = "";
    	
    	for(int i = 0; i < tamanho; i++)
    	{
    			result[i] = ALL_CHARS[RANDOM.nextInt(ALL_CHARS.length)];
    	}
    	
    	retorno = new String(result);
    	
    	return retorno.toUpperCase();
    }
    
    
}
