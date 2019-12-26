package med.mental.mentalmed.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Preferencias {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private String CHAVE_TIPO_USUARIO = "tipoUsuario";
    private String CHAVE_USUARIO = "usuario";

    public Preferencias(Context contextParametro) {

        String NOME_ARQUIVO = "nordesteempregos.preferencias";
        int MODE = 0;
        preferences = contextParametro.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String idUsuarioLogado, String tipo, Object objeto) {

        Gson gson = new Gson();
        String json = gson.toJson(objeto);

        editor.putString(CHAVE_IDENTIFICADOR, idUsuarioLogado);
        editor.putString(CHAVE_TIPO_USUARIO, tipo);
        editor.putString(CHAVE_USUARIO, json);
        editor.commit();
    }

    public String getIdentificador() {
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getTipoUsuario() {
        return preferences.getString(CHAVE_TIPO_USUARIO, null);
    }

    public String getObjeto() {
        return preferences.getString(CHAVE_USUARIO, null);
    }
}
