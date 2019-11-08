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
import med.mental.mentalmed.adapter.PerguntaAnsiedadeAdapter;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.Questionario;
import med.mental.mentalmed.repository.Perguntas;

public class QuestAnsiedade extends AppCompatActivity {

    private List<Pergunta> resultadosSQR20;
    private final List<PerguntaAnsiedade> listaDePerguntas = new ArrayList<>();

    private Questionario questionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_ansiedade);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");

        Perguntas perguntas = new Perguntas(this);
        for (String descricao : perguntas.perguntasAnsiedade(this)) {
            PerguntaAnsiedade p = new PerguntaAnsiedade();
            p.setDescricao(descricao);
            listaDePerguntas.add(p);
        }

        ListView lista_perguntas_ansiedade = findViewById(R.id.lista_perguntas_ansiedade);

        PerguntaAnsiedadeAdapter adapter = new PerguntaAnsiedadeAdapter(getApplicationContext(), listaDePerguntas);
        lista_perguntas_ansiedade.setAdapter(adapter);
    }

    public void avancarFase3(View view) {
        List<PerguntaAnsiedade> resultadosQuestAnsiedade = new ArrayList<>(PerguntaAnsiedadeAdapter.resultados);

        int resultadosAnsiedade = verificarResultados(resultadosQuestAnsiedade);
        String nivelAnsiedade = nivelDeAnsiedade(resultadosAnsiedade);

        Intent intent = new Intent(this, CadastroFase3.class);
        intent.putExtra("questionario", questionario);
        intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
        intent.putExtra("resultadosQuestAnsiedade", (Serializable) resultadosQuestAnsiedade);
        intent.putExtra("nivelAnsiedade", nivelAnsiedade);
        intent.putExtra("resultadosAnsiedade", resultadosAnsiedade);
        startActivity(intent);
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
    private int verificarResultados(List<PerguntaAnsiedade> resultadosQuestAnsiedade) {
        int resultado = 0;

        for (int i = 0; i < resultadosQuestAnsiedade.size(); i++) {
            if (resultadosQuestAnsiedade.get(i).isMarcada())
                resultado = resultado + resultadosQuestAnsiedade.get(i).getResposta();
        }

        return resultado;
    }


}
