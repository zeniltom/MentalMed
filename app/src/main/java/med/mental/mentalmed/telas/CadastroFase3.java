package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.Questionario;

public class CadastroFase3 extends AppCompatActivity {

    private EditText et_lazer_horas;

    private RadioGroup radio_group_fumo;
    private RadioGroup radio_group_bebida;
    private RadioGroup radio_group_atividade;
    private RadioGroup radio_group_drogas;

    private Button bt_proximo_1;

    private List<Pergunta> resultadosSQR20;
    private List<PerguntaAnsiedade> resultadosQuestAnsiedade;

    private Questionario questionario;
    private String nivelAnsiedade;
    private int resultadosAnsiedade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_3);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
        resultadosQuestAnsiedade = (List<PerguntaAnsiedade>) getIntent().getSerializableExtra("resultadosQuestAnsiedade");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nivelAnsiedade = bundle.getString("nivelAnsiedade");
            resultadosAnsiedade = bundle.getInt("resultadosAnsiedade");
        }

        carregarComponentes();

        bt_proximo_1.setOnClickListener(v -> avancarDepressao());
    }

    private void avancarDepressao() {
        preencherQuestionarioFazes();

        if (isValid()) {
            Intent intent = new Intent(this, QuestDepressao.class);
            intent.putExtra("questionario", this.questionario);
            intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
            intent.putExtra("resultadosQuestAnsiedade", (Serializable) resultadosQuestAnsiedade);
            intent.putExtra("nivelAnsiedade", nivelAnsiedade);
            intent.putExtra("resultadosAnsiedade", resultadosAnsiedade);
            startActivity(intent);

            //Salvar no Firebase
            DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("usuarios").child("questionario");

            referenciaQuestionario.child(questionario.getId()).setValue(questionario);

            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(CadastroFase3.this);
            preferencias.salvarDados(questionario.getId(), questionario, null, null, null, null);
        }
    }

    private boolean isValid() {
        boolean resultado = true;

        if (questionario.getHorasLazerSemanalmente() == 0) {
            msg();
            resultado = false;
        }

        return resultado;
    }

    private void msg() {
        Toast.makeText(getApplicationContext(), "Informe a quantidade de horas de lazer semanalmente", Toast.LENGTH_SHORT).show();
    }

    private void carregarComponentes() {
        bt_proximo_1 = findViewById(R.id.bt_proximo_1);

        et_lazer_horas = findViewById(R.id.et_lazer_horas);

        radio_group_fumo = findViewById(R.id.radio_group_fumo);
        radio_group_bebida = findViewById(R.id.radio_group_bebida);
        radio_group_atividade = findViewById(R.id.radio_group_atividade);
        radio_group_drogas = findViewById(R.id.radio_group_drogas);
    }

    private void preencherQuestionarioFazes() {
        int checkedRadioButtonId = radio_group_fumo.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_fumo_sim) questionario.setFuma(true);
        else if (checkedRadioButtonId == R.id.rb_fumo_nao)
            questionario.setFuma(false);

        int radioButtonId = radio_group_bebida.getCheckedRadioButtonId();
        if (radioButtonId == R.id.rb_bebida_sim)
            questionario.setConsomeBebibaAlcoolica(true);
        else if (radioButtonId == R.id.rb_bebida_nao)
            questionario.setConsomeBebibaAlcoolica(false);

        int buttonId = radio_group_atividade.getCheckedRadioButtonId();
        if (buttonId == R.id.rb_atividade_sim)
            questionario.setFuma(true);
        else if (buttonId == R.id.rb_atividade_nao)
            questionario.setFuma(false);

        int id = radio_group_drogas.getCheckedRadioButtonId();
        if (id == R.id.rb_drogas_sim)
            questionario.setConsomeDrogasIlicitas(true);
        else if (id == R.id.rb_drogas_nao)
            questionario.setConsomeDrogasIlicitas(false);

        questionario.setConsomeBebibaAlcoolica(radio_group_bebida.getCheckedRadioButtonId() == R.id.rb_bebida_nao);
        questionario.setPraticaAtividadeFisica(radio_group_atividade.getCheckedRadioButtonId() == R.id.c_com);
        questionario.setConsomeDrogasIlicitas(radio_group_drogas.getCheckedRadioButtonId() == R.id.rb_tsim);

        String horasLazer = et_lazer_horas.getText().toString();
        questionario.setHorasLazerSemanalmente(horasLazer.equals("") ? 0 : Float.parseFloat(horasLazer));

        Log.i("#", questionario.toString());
    }
}
