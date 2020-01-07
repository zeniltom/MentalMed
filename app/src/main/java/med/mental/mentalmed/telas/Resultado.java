package med.mental.mentalmed.telas;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.adapter.PerguntaSindromeBurnoutAdapter;
import med.mental.mentalmed.adapter.ResultadoAdapter;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.Questionario;

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

        carregarDados();
    }

    private void carregarDados() {
        Preferencias preferencias = new Preferencias(Resultado.this);
        Gson gson = new Gson();

        if (preferencias.getIdUsuario() != null) {
            String questionarioJSON = preferencias.getQuestionario();
            String questSQR20JSON = preferencias.getQuestSQR20();
            String questAnsiedadeJSON = preferencias.getQuestAnsiedade();
            String questDepressaoJSON = preferencias.getQuestDepressao();
            String questSindromeBurnoutJSON = preferencias.getQuestSindromeBurnout();

            Object q = gson.fromJson(questionarioJSON, Questionario.class);
            Object qsr20 = gson.fromJson(questSQR20JSON, Questionario.class);
            Object qans = gson.fromJson(questAnsiedadeJSON, Questionario.class);
            Object qdepr = gson.fromJson(questDepressaoJSON, Questionario.class);
            Object qsbn = gson.fromJson(questSindromeBurnoutJSON, Questionario.class);
        }
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
