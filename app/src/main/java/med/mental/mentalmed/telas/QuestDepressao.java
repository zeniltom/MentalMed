package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.adapter.PerguntaDepressaoAdapter;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.PerguntaDepressaoCat;
import med.mental.mentalmed.model.Questionario;
import med.mental.mentalmed.repository.Perguntas;

public class QuestDepressao extends AppCompatActivity {

    private final List<PerguntaDepressaoCat> listaDePerguntas = new ArrayList<>();

    private List<Pergunta> resultadosSQR20;
    private List<PerguntaAnsiedade> resultadosQuestAnsiedade;

    private Questionario questionario;
    private String nivelAnsiedade;
    private int resultadosAnsiedade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_depressao);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
        resultadosQuestAnsiedade = (List<PerguntaAnsiedade>) getIntent().getSerializableExtra("resultadosQuestAnsiedade");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nivelAnsiedade = bundle.getString("nivelAnsiedade");
            resultadosAnsiedade = bundle.getInt("resultadosAnsiedade");
        }
        listaDePerguntas.addAll(new Perguntas(this).todasCategoriasPergDepress());

        for (int i = 0; i < listaDePerguntas.size(); i++) {
            PerguntaDepressaoCat p = listaDePerguntas.get(i);
            p.setPerguntasDeDepressao(new Perguntas(this).perguntaDepressaoPorCat(p.getId()));
            listaDePerguntas.set(i, p);
        }

        ListView lista_perguntas = findViewById(R.id.lista_perguntas_depressao);

        PerguntaDepressaoAdapter adapter = new PerguntaDepressaoAdapter(getApplicationContext(), listaDePerguntas);
        lista_perguntas.setAdapter(adapter);
    }

    public void abrirFase4(View view) {
        List<PerguntaDepressao> resultadosQuestDepressao = new ArrayList<>(PerguntaDepressaoAdapter.resultados);

        int resultadosDepressao = verificarResultados(resultadosQuestDepressao);
        String nivelDepressao = nivelDeDepressao(resultadosDepressao);

        Intent intent = new Intent(this, CadastroFase4.class);
        intent.putExtra("questionario", this.questionario);
        intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
        intent.putExtra("resultadosQuestAnsiedade", (Serializable) resultadosQuestAnsiedade);
        intent.putExtra("resultadosQuestDepressao", (Serializable) resultadosQuestDepressao);
        intent.putExtra("nivelAnsiedade", nivelAnsiedade);
        intent.putExtra("resultadosAnsiedade", resultadosAnsiedade);
        intent.putExtra("nivelDepressao", nivelDepressao);
        intent.putExtra("resultadosDepressao", resultadosDepressao);
        startActivity(intent);
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
    private int verificarResultados(List<PerguntaDepressao> resultadosQuestDepressao) {
        int resultado = 0;

        for (int i = 0; i < resultadosQuestDepressao.size(); i++) {
            if (resultadosQuestDepressao.get(i).isMarcada())
                resultado = resultado + resultadosQuestDepressao.get(i).getResposta();
        }

        return resultado;
    }

}
