package uniandrade.br.edu.com.popseries.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pnda on 27/03/18.
 */

public class Preferencias {

    private Context mContext;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "popseries.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";

    public Preferencias( Context context ) {
        mContext = context;
        preferences = mContext.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados( String identificadorUsuario ){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

}
