package med.mental.mentalmed.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Preferencias {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String CHAVE_IDUSUARIO = "identificadorUsuario";
    private String CHAVE_QUESTIONARIO = "questionario";
    private String CHAVE_QUESTSQR20 = "questsqr20";
    private String CHAVE_QUESTANSIEDADE = "questansiedade";
    private String CHAVE_QUESTDEPRESSAO = "questdepressao";
    private String CHAVE_QUESTSINDROMEBURNOUT = "questsindromeburnout";

    public Preferencias(Context contextParametro) {

        String NOME_ARQUIVO = "mentalmed.preferencias";
        int MODE = 0;
        preferences = contextParametro.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.apply();
    }

    public void salvarDados(String idUsuario, Object objeto) {

        Gson gson = new Gson();
        String json = gson.toJson(objeto);

        editor.putString(CHAVE_IDUSUARIO, idUsuario);
        editor.putString(CHAVE_QUESTIONARIO, json);
        editor.putString(CHAVE_QUESTSQR20, json);
        editor.putString(CHAVE_QUESTANSIEDADE, json);
        editor.putString(CHAVE_QUESTDEPRESSAO, json);
        editor.putString(CHAVE_QUESTSINDROMEBURNOUT, json);
        editor.commit();
    }

    public String getIdUsuario() {
        return preferences.getString(CHAVE_IDUSUARIO, null);
    }

    public String getQuestionario() {
        return preferences.getString(CHAVE_QUESTIONARIO, null);
    }

    public String getQuestSQR20() {
        return preferences.getString(CHAVE_QUESTSQR20, null);
    }

    public String getQuestAnsiedade() {
        return preferences.getString(CHAVE_QUESTANSIEDADE, null);
    }

    public String getQuestDepressao() {
        return preferences.getString(CHAVE_QUESTDEPRESSAO, null);
    }

    public String getQuestSindromeBurnout() {
        return preferences.getString(CHAVE_QUESTSINDROMEBURNOUT, null);
    }
}
