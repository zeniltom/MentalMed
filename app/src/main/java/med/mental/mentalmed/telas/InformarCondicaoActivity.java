package med.mental.mentalmed.telas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaBurnout;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.Questionario;

public class InformarCondicaoActivity extends AppCompatActivity {

    private List<Pergunta> resultadosSQR20;
    private List<PerguntaAnsiedade> resultadosQuestAnsiedade;
    private List<PerguntaDepressao> resultadosQuestDepressao;
    private List<PerguntaBurnout> resultadosQuestSindromeBurnout;

    private Questionario questionario;
    private String nivelAnsiedade;
    private int resultadosAnsiedade;
    private String nivelDepressao;
    private int resultadosDepressao;
    HashMap<String, Float> resultadosSindromeB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informar_condicao);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
        resultadosQuestAnsiedade = (List<PerguntaAnsiedade>) getIntent().getSerializableExtra("resultadosQuestAnsiedade");
        resultadosQuestDepressao = (List<PerguntaDepressao>) getIntent().getSerializableExtra("resultadosQuestDepressao");
        resultadosSindromeB = (HashMap<String, Float>) getIntent().getSerializableExtra("resultadosSindromeB");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nivelAnsiedade = bundle.getString("nivelAnsiedade");
            resultadosAnsiedade = bundle.getInt("resultadosAnsiedade");
            nivelDepressao = bundle.getString("nivelDepressao");
            resultadosDepressao = bundle.getInt("resultadosDepressao");
        }

        TextView tv_nv_ansiedade = findViewById(R.id.tv_nv_ansiedade);
        TextView tv_nv_depressao = findViewById(R.id.tv_nv_depressao);
        TextView tv_nv_sindrome_b = findViewById(R.id.tv_nv_sindrome_b);

        tv_nv_ansiedade.setText(nivelAnsiedade);
        tv_nv_depressao.setText(nivelDepressao);

        String results = "Exaustão Emocional: " + (resultadosSindromeB.get("exaustaoEmocional") > 3 ? "POSSUE EXAUSTÃO EMOCIONAL" : "NORMAL")
                + "\n Descrença: " + (resultadosSindromeB.get("descreca") > 3 ? "POSSUE DESCRENÇA" : "NORMAL")
                + "\n Eficácia Profissional: " + (resultadosSindromeB.get("eficaciaProfissional") < 2 ? "NÃO É EFICIENTE" : "NORMAL");


        tv_nv_sindrome_b.setText(results);

        Log.i("#NIVEL ANSIEDADE", String.valueOf(resultadosAnsiedade));
        Log.i("#NIVEL DEPRESSAO", String.valueOf(resultadosDepressao));
        Log.i("#NIVEL SINDROME BURNOUT", String.valueOf(resultadosSindromeB));
    }
}
