package uniandrade.br.edu.com.popseries.helper;

import android.util.Base64;

/**
 * Created by pnda on 27/03/18.
 */

public class Base64Custom {

    public static String encodeBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT)
                .replaceAll("(\\n|\\r)","");
    }

    public static String decodeBase64(String textoCodificado){
        return new String( Base64.decode(textoCodificado, Base64.DEFAULT ) );
    }

}
