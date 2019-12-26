package med.mental.mentalmed.telas;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.adapter.PerguntaSindromeBurnoutAdapter;
import med.mental.mentalmed.adapter.ResultadoAdapter;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaDepressao;

public class Resultado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        List<Pergunta> resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
        List<PerguntaAnsiedade> resultadosQuestAnsiedade = (List<PerguntaAnsiedade>) getIntent().getSerializableExtra("resultadosQuestAnsiedade");
        List<PerguntaDepressao> resultadosQuestDepressao = (List<PerguntaDepressao>) getIntent().getSerializableExtra("resultadosQuestDepressao");
        List<PerguntaSindromeBurnoutAdapter> resultadosQuestSindromeBurnout = (List<PerguntaSindromeBurnoutAdapter>) getIntent().getSerializableExtra("resultadosQuestSindromeBurnout");

        ListView lista_resultados = findViewById(R.id.lista_resultados);
        TextView sims = findViewById(R.id.sims);
        TextView naos = findViewById(R.id.naos);
        TextView semResps = findViewById(R.id.semResps);

        ResultadoAdapter resultadoAdapter = new ResultadoAdapter(getApplicationContext(), resultadosSQR20);
        lista_resultados.setAdapter(resultadoAdapter);

        int qtdSim = perguntasComSim(resultadosSQR20);
        int qtdNao = perguntasComNao(resultadosSQR20);
        int qtdNaoRespondidas = perguntasNaoRespondidas(resultadosSQR20);

        sims.setText("Perguntas com SIM: " + qtdSim);
        naos.setText("Perguntas com NÃO: " + qtdNao);
        semResps.setText("Perguntas sem Respostas: " + qtdNaoRespondidas);
    }


    private int perguntasComSim(List<Pergunta> resultadoFinal) {
        int qtdSim = 0;

        for (int i = 0; i < resultadoFinal.size(); i++) {
            if (resultadoFinal.get(i).isResposta() && resultadoFinal.get(i).isMarcada())
                qtdSim++;
        }
        return qtdSim;
    }

    private int perguntasComNao(List<Pergunta> resultadoFinal) {
        int qtdNao = 0;

        for (int i = 0; i < resultadoFinal.size(); i++) {
            if (!resultadoFinal.get(i).isResposta() && resultadoFinal.get(i).isMarcada())
                qtdNao++;
        }
        return qtdNao;
    }

    private int perguntasNaoRespondidas(List<Pergunta> resultadoFinal) {
        int qtdSemResps = 0;

        for (int i = 0; i < resultadoFinal.size(); i++) {
            if (!resultadoFinal.get(i).isMarcada())
                qtdSemResps++;
        }
        return qtdSemResps;
    }
}
