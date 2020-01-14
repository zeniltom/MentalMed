package med.mental.mentalmed.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaBurnout;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.Questionario;

public class Preferencias {

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final String CHAVE_IDUSUARIO = "identificadorUsuario";
    private final String CHAVE_QUESTIONARIO = "questionario";
    private final String CHAVE_QUESTSQR20 = "questsqr20";
    private final String CHAVE_QUESTANSIEDADE = "questansiedade";
    private final String CHAVE_QUESTDEPRESSAO = "questdepressao";
    private final String CHAVE_QUESTSINDROMEBURNOUT = "questsindromeburnout";

    private Gson gson;

    public Preferencias(Context contextParametro) {
        String NOME_ARQUIVO = "mentalmed.preferencias";
        int MODE = 0;
        preferences = contextParametro.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.apply();
    }

    public void salvarDados(String idUsuario, Object questionario, Object questSRQ20, Object questAnsiedade, Object questDepressao, Object questSindromeBurnout) {
        gson = new Gson();
        String resultado;

        String questionarioJson = gson.toJson(questionario);
        String questSRQ20Json = gson.toJson(questSRQ20);
        String questAnsiedadeJson = gson.toJson(questAnsiedade);
        String questDepressaoJson = gson.toJson(questDepressao);
        String questSindromeBurnoutJson = gson.toJson(questSindromeBurnout);

        editor.putString(CHAVE_IDUSUARIO, idUsuario);
        editor.putString(CHAVE_QUESTIONARIO, questionarioJson);
        editor.putString(CHAVE_QUESTSQR20, questSRQ20Json);
        editor.putString(CHAVE_QUESTANSIEDADE, questAnsiedadeJson);
        editor.putString(CHAVE_QUESTDEPRESSAO, questDepressaoJson);
        editor.putString(CHAVE_QUESTSINDROMEBURNOUT, questSindromeBurnoutJson);

        resultado = editor.commit() ? "SALVAR NAS PREFERÊNCIAS: OK" : "SALVAR NAS PREFERÊNCIAS: ERRO";

        Log.i("#", resultado);
    }

    public void salvarQuestionario(Questionario questionario) {
        gson = new Gson();
        String questionarioJson = gson.toJson(questionario);
        editor.putString(CHAVE_QUESTIONARIO, questionarioJson);
        editor.commit();
    }

    public void salvarSQR20(List<Pergunta> questSRQ20) {
        gson = new Gson();
        String questSRQ20Json = gson.toJson(questSRQ20);
        editor.putString(CHAVE_QUESTSQR20, questSRQ20Json);
        editor.commit();
    }

    public void salvarAnsiedade(List<PerguntaAnsiedade> questAnsiedade) {
        gson = new Gson();
        String questAnsiedadeJson = gson.toJson(questAnsiedade);
        editor.putString(CHAVE_QUESTANSIEDADE, questAnsiedadeJson);
        editor.commit();
    }

    public void salvarDepressao(List<PerguntaDepressao> questDepressao) {
        gson = new Gson();
        String questDepressaoJson = gson.toJson(questDepressao);
        editor.putString(CHAVE_QUESTDEPRESSAO, questDepressaoJson);
        editor.commit();
    }

    public void salvarSindromeBurnout(List<PerguntaBurnout> questSindromeBurnout) {
        gson = new Gson();
        String questSindromeBurnoutJson = gson.toJson(questSindromeBurnout);
        editor.putString(CHAVE_QUESTSINDROMEBURNOUT, questSindromeBurnoutJson);
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
