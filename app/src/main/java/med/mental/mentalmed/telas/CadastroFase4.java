package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.Serializable;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.Questionario;

public class CadastroFase4 extends AppCompatActivity {

    private RadioGroup radio_group_acompanhamento;
    private RadioGroup radio_group_ness_acomp;
    private RadioGroup radio_group_medicamento;

    private Button bt_proximo_1;

    private Questionario questionario;
    private List<Pergunta> resultadosSQR20;
    private List<PerguntaAnsiedade> resultadosQuestAnsiedade;
    private List<PerguntaDepressao> resultadosQuestDepressao;
    private String nivelAnsiedade;
    private int resultadosAnsiedade;
    private String nivelDepressao;
    private int resultadosDepressao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_4);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
        resultadosQuestAnsiedade = (List<PerguntaAnsiedade>) getIntent().getSerializableExtra("resultadosQuestAnsiedade");
        resultadosQuestDepressao = (List<PerguntaDepressao>) getIntent().getSerializableExtra("resultadosQuestDepressao");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nivelAnsiedade = bundle.getString("nivelAnsiedade");
            resultadosAnsiedade = bundle.getInt("resultadosAnsiedade");
            nivelDepressao = bundle.getString("nivelDepressao");
            resultadosDepressao = bundle.getInt("resultadosDepressao");
        }

        carregarComponentes();

        bt_proximo_1.setOnClickListener(v -> avancarSindromeBurnout());
    }

    private void avancarSindromeBurnout() {
        preencherQuestionarioFazes();
        Intent intent = new Intent(this, QuestSindromeBurnout.class);
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

    private void carregarComponentes() {
        bt_proximo_1 = findViewById(R.id.bt_proximo_1);

        radio_group_acompanhamento = findViewById(R.id.radio_group_acompanhamento);
        radio_group_ness_acomp = findViewById(R.id.radio_group_ness_acomp);
        radio_group_medicamento = findViewById(R.id.radio_group_medicamento);
    }

    private void preencherQuestionarioFazes() {
        switch (radio_group_acompanhamento.getCheckedRadioButtonId()) {
            case R.id.rb_acomp_sim:
                questionario.setRecebeAcompanhamentoPsicologico(true);
                break;
            case R.id.rb_acomp_nao:
                questionario.setRecebeAcompanhamentoPsicologico(false);
                break;
        }

        switch (radio_group_ness_acomp.getCheckedRadioButtonId()) {
            case R.id.rb_ness_acomp_sim:
                questionario.setTemNecessidadeAcompanhamentoPsicologico(true);
                break;
            case R.id.rb_ness_acomp_nao:
                questionario.setTemNecessidadeAcompanhamentoPsicologico(false);
                break;
        }

        switch (radio_group_medicamento.getCheckedRadioButtonId()) {
            case R.id.rb_medicamento_sim:
                questionario.setUsaMedicamentoPrescrito(true);
                break;
            case R.id.rb_medicamento_nao:
                questionario.setUsaMedicamentoPrescrito(false);
                break;
        }

        Log.i("#", questionario.toString());
    }
}
