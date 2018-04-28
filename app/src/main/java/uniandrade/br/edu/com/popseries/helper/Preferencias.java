package uniandrade.br.edu.com.popseries.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pnda on 27/03/18.
 *
 */

public class Preferencias {

    private Context mContext;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "popseries.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";
    private final String CHAVE_EMAIL = "emailUsuarioLogado";
    private final String CHAVE_URL_PHOTO = "urlPhotoUsuarioLogado";

    public Preferencias( Context context ) {
        mContext = context;
        preferences = mContext.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados( String identificadorUsuario ){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.commit();
    }

    public void salvarNomeEmail(String userName, String userEmail, String userPhoto) {
        editor.putString(CHAVE_NOME, userName);
        editor.putString(CHAVE_EMAIL, userEmail);
        editor.putString(CHAVE_URL_PHOTO, userPhoto);
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }

    public String getEmail(){
        return preferences.getString(CHAVE_EMAIL, null);
    }

    public String getUrlPhoto(){
        return preferences.getString(CHAVE_URL_PHOTO, null);
    }

}
