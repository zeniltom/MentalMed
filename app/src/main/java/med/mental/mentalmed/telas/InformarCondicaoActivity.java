package med.mental.mentalmed.telas;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaBurnout;
import med.mental.mentalmed.model.PerguntaDepressaoCat;

public class InformarCondicaoActivity extends AppCompatActivity {

    private SpotsDialog progressDialog;
    private TextView tv_nv_ansiedade;
    private TextView tv_nv_depressao;
    private TextView tv_nv_sindrome_b;

    private final List<PerguntaAnsiedade> resultadosQuestAnsiedade = new ArrayList<>();
    private final List<PerguntaDepressaoCat> resultadosQuestDepressao = new ArrayList<>();
    private final List<PerguntaBurnout> resultadosQuestSindromeBurnout = new ArrayList<>();

    private final DatabaseReference refQuestAnsiedade = ConfiguracaoFirebase.getFirebase().child("questionarioAnsiedade");
    private final DatabaseReference refQuestDepressao = ConfiguracaoFirebase.getFirebase().child("questionarioDepressao");
    private final DatabaseReference refQuestSindromeBurnout = ConfiguracaoFirebase.getFirebase().child("questionarioSindromeBurnout");

    private String idUsuario = "";

    private String nivelAnsiedade;
    private int resultadosAnsiedade;

    private String nivelDepressao;
    private int resultadosDepressao;

    private HashMap<String, Float> resultadosSindromeB;
    private String resultadosSindromeBurnout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informar_condicao);

        carregarComponentes();
        carregarPreferencias();
    }

    private void msg(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }

    private void carregarComponentes() {
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        tv_nv_ansiedade = findViewById(R.id.tv_nv_ansiedade);
        tv_nv_depressao = findViewById(R.id.tv_nv_depressao);
        tv_nv_sindrome_b = findViewById(R.id.tv_nv_sindrome_b);
    }

    private void carregarPreferencias() {
        try {
            Preferencias preferencias = new Preferencias(InformarCondicaoActivity.this);
            if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

            carregarRespostasAnsiedade();
            carregarRespostasDepressao();
            carregarRespostasSindromeBurnout();

            Log.i("#NIVEL ANSIEDADE", String.valueOf(resultadosAnsiedade));
            Log.i("#NIVEL DEPRESSAO", String.valueOf(resultadosDepressao));
            Log.i("#NIVEL SINDROME BURNOUT", String.valueOf(resultadosSindromeB));
        } catch (Exception e) {
            msg("Erro: " + e.getLocalizedMessage() + ". Consulte o suporte!");
            e.printStackTrace();
        }
    }

    private void carregarRespostasDepressao() {
        refQuestDepressao.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultadosQuestDepressao.clear();

                for (DataSnapshot dados : dataSnapshot.child(idUsuario).getChildren()) {
                    PerguntaDepressaoCat perguntaDepressaoCat = dados.getValue(PerguntaDepressaoCat.class);
                    resultadosQuestDepressao.add(perguntaDepressaoCat);
                }

                Log.i("#CARREGAR QUESTDEPRESSAO ACTINFORMARCONDICAO", resultadosQuestDepressao.size() > 0 ? "OK" : "ERRO");

                if (progressDialog.isShowing()) progressDialog.dismiss();

                resultadosDepressao = verificarResultadosDepressao(resultadosQuestDepressao);
                nivelDepressao = nivelDeDepressao(resultadosDepressao);

                tv_nv_depressao.setText(nivelDepressao);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarRespostasAnsiedade() {
        refQuestAnsiedade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultadosQuestAnsiedade.clear();

                for (DataSnapshot dados : dataSnapshot.child(idUsuario).getChildren()) {
                    PerguntaAnsiedade perguntaAnsiedade = dados.getValue(PerguntaAnsiedade.class);
                    resultadosQuestAnsiedade.add(perguntaAnsiedade);
                }

                Log.i("#CARREGAR QUESTANSIEDADE ACTINFORMARCONDICAO", resultadosQuestAnsiedade.size() > 0 ? "OK" : "ERRO");

                if (progressDialog.isShowing()) progressDialog.dismiss();

                resultadosAnsiedade = verificarResultadosAnsiedade(resultadosQuestAnsiedade);
                nivelAnsiedade = nivelDeAnsiedade(resultadosAnsiedade);
                tv_nv_ansiedade.setText(nivelAnsiedade);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarRespostasSindromeBurnout() {
        refQuestSindromeBurnout.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultadosQuestSindromeBurnout.clear();

                for (DataSnapshot dados : dataSnapshot.child(idUsuario).getChildren()) {
                    PerguntaBurnout perguntaBurnout = dados.getValue(PerguntaBurnout.class);
                    resultadosQuestSindromeBurnout.add(perguntaBurnout);
                }

                Log.i("#CARREGAR QUESTSINDROMEB ACTINFORMARCONDICAO", resultadosQuestSindromeBurnout.size() > 0 ? "OK" : "ERRO");

                if (progressDialog.isShowing()) progressDialog.dismiss();

                resultadosSindromeB = verificarResultadosSindrome(resultadosQuestSindromeBurnout);
                resultadosSindromeBurnout =
                        "Exaustão Emocional: " + (Objects.requireNonNull(resultadosSindromeB.get("exaustaoEmocional")) > 3 ? "POSSUE EXAUSTÃO EMOCIONAL" : "NORMAL")
                                + "\nDescrença: " + (Objects.requireNonNull(resultadosSindromeB.get("descreca")) > 3 ? "POSSUE DESCRENÇA" : "NORMAL")
                                + "\nEficácia Profissional: " + (Objects.requireNonNull(resultadosSindromeB.get("eficaciaProfissional")) < 2 ? "NÃO É EFICIENTE PROFISSIONALMENTE" : "NORMAL");

                tv_nv_sindrome_b.setText(resultadosSindromeBurnout);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /***
     * VERIFICA O NÍVEL DE ANSIEDADE
     * @param qtd
     */
    private String nivelDeAnsiedade(int qtd) {
        Log.i("#NIVEL ANSIEDADE", String.valueOf(qtd));

        if (qtd >= 0 && qtd <= 8) return "Ansiedade ausente";
        else if (qtd >= 8 && qtd <= 15) return "Ansiedade Leve";
        else if (qtd >= 16 && qtd <= 25) return "Ansiedade Moderada";
        else if (qtd >= 25 && qtd <= 63) return "Ansiedade Grave";
        else return "Erro";
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS
     * @param resultadosQuestAnsiedade
     */
    private int verificarResultadosAnsiedade(List<PerguntaAnsiedade> resultadosQuestAnsiedade) {
        int resultado = 0;

        for (int i = 0; i < resultadosQuestAnsiedade.size(); i++) {
            if (resultadosQuestAnsiedade.get(i).isMarcada())
                resultado = resultado + resultadosQuestAnsiedade.get(i).getResposta();
        }

        return resultado;
    }

    /***
     * VERIFICA O NÍVEL DE DEPRESSAO
     * @param qtd
     */
    private String nivelDeDepressao(int qtd) {
        Log.i("#NIVEL DEPRESSAO", String.valueOf(qtd));

        if (qtd >= 0 && qtd <= 10) return "Depressão ausente";
        else if (qtd >= 10 && qtd <= 18) return "Depressão Leve";
        else if (qtd >= 19 && qtd <= 29) return "Depressão Moderada";
        else if (qtd >= 30 && qtd <= 63) return "Depressão Grave";
        else return "Erro";
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS
     * @param resultadosQuestDepressao
     */
    private int verificarResultadosDepressao(List<PerguntaDepressaoCat> resultadosQuestDepressao) {
        int resultado = 0;

        for (PerguntaDepressaoCat pdc : resultadosQuestDepressao) {
            for (int i = 0; i < pdc.getPerguntasDeDepressao().size(); i++) {
                if (pdc.getPerguntasDeDepressao().get(i).isMarcada())
                    resultado = resultado + pdc.getPerguntasDeDepressao().get(i).getResposta();
            }
        }

        return resultado;
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS
     * @param resultadosQuestSindromeBurnout
     * @return
     */
    private HashMap<String, Float> verificarResultadosSindrome(List<PerguntaBurnout> resultadosQuestSindromeBurnout) {
        int resultado = 0;

        HashMap<String, Float> resultados = new HashMap<>();

        for (int i = 0; i < resultadosQuestSindromeBurnout.size(); i++) {
            if (resultadosQuestSindromeBurnout.get(i).isMarcada())
                resultado = resultado + resultadosQuestSindromeBurnout.get(i).getResposta();
        }

        resultados.put("exaustaoEmocional", calcularExaustaoEmocional(resultadosQuestSindromeBurnout));
        resultados.put("descreca", calcularDescrenca(resultadosQuestSindromeBurnout));
        resultados.put("eficaciaProfissional", calcularEficaciaProfissional(resultadosQuestSindromeBurnout));

        Log.i("#EXAUSTÃO EMOCIONAL", Objects.requireNonNull(resultados.get("exaustaoEmocional")) > 3 ? "POSSUE EXAUSTÃO EMOCIONAL" : "NORMAL");
        Log.i("#DESCRENÇA", Objects.requireNonNull(resultados.get("descreca")) > 3 ? "POSSUE DESCRENÇA" : "NORMAL");
        Log.i("#EFICÁCIA PROFISSIONAL", Objects.requireNonNull(resultados.get("eficaciaProfissional")) < 2 ? "NÃO É EFICIENTE PROFISSIONALMENTE" : "NORMAL");

        return resultados;
    }

    private float calcularExaustaoEmocional(List<PerguntaBurnout> lista) {
        float resultadoExaustaoEmocional = 0;
        int qtdItens = 1;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isMarcada() && lista.get(i).getCategoriaPergunta().equals("exaustao_emocional")) {
                resultadoExaustaoEmocional = resultadoExaustaoEmocional + lista.get(i).getResposta();
                qtdItens++;
            }
        }

        return resultadoExaustaoEmocional / qtdItens;
    }

    private float calcularDescrenca(List<PerguntaBurnout> lista) {
        float resultadoDescrenca = 0;
        int qtdItens = 1;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isMarcada() && lista.get(i).getCategoriaPergunta().equals("descrenca")) {
                resultadoDescrenca = resultadoDescrenca + lista.get(i).getResposta();
                qtdItens++;
            }
        }

        return resultadoDescrenca / qtdItens;
    }

    private float calcularEficaciaProfissional(List<PerguntaBurnout> lista) {
        float resultadoEficaciaProfissional = 0;
        int qtdItens = 1;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isMarcada() && lista.get(i).getCategoriaPergunta().equals("eficacia_profissional")) {
                resultadoEficaciaProfissional = resultadoEficaciaProfissional + lista.get(i).getResposta();
                qtdItens++;
            }
        }

        return resultadoEficaciaProfissional / qtdItens;
    }
}
