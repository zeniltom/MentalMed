package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.adapter.PerguntaAdapter;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.Questionario;
import med.mental.mentalmed.repository.Perguntas;

public class QuestSQR20 extends AppCompatActivity {

    private final List<Pergunta> listaDePerguntas = new ArrayList<>();
    private Questionario questionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_sqr20);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");

        Perguntas perguntas = new Perguntas(this);
        for (String descricao : perguntas.perguntasSQR20(this)) {
            Pergunta p = new Pergunta();
            p.setDescricao(descricao);
            listaDePerguntas.add(p);
        }

        ListView lista_perguntas = findViewById(R.id.lista_perguntas);

        PerguntaAdapter adapter = new PerguntaAdapter(getApplicationContext(), listaDePerguntas);
        lista_perguntas.setAdapter(adapter);
    }

    public void avancarSaudeMental(View view) {
        List<Pergunta> resultadosSQR20 = new ArrayList<>(PerguntaAdapter.resultados);

        if (temSofrimentoMental(resultadosSQR20)) {
            Intent intent = new Intent(this, SaudeMentalRuim.class);
            intent.putExtra("questionario", questionario);
            intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SaudeMentalBoa.class);
            intent.putExtra("questionario", questionario);
            intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
            startActivity(intent);
        }
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS MARCADAS COM 'SIM'
     * @param resultadosSQR20
     */
    private boolean temSofrimentoMental(List<Pergunta> resultadosSQR20) {
        int qtdSim = 0;

        for (int i = 0; i < resultadosSQR20.size(); i++) {
            if (resultadosSQR20.get(i).isResposta() && resultadosSQR20.get(i).isMarcada())
                qtdSim++;
        }

        return qtdSim >= 7;
    }
}