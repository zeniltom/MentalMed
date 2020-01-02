package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
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

    private String nivelAnsiedade;
    private int resultadosAnsiedade;

    private Questionario questionario;
    private DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_3);

        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
        resultadosQuestAnsiedade = (List<PerguntaAnsiedade>) getIntent().getSerializableExtra("resultadosQuestAnsiedade");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nivelAnsiedade = bundle.getString("nivelAnsiedade");
            resultadosAnsiedade = bundle.getInt("resultadosAnsiedade");
        }

        carregarComponentes();
        carregarDados();
        carregarPreferencias();

        bt_proximo_1.setOnClickListener(v -> avancarDepressao());
    }

    private void avancarDepressao() {
        coletarRespostas();

        if (isValid()) {

            salvarFirebase();

            Intent intent = new Intent(this, QuestDepressao.class);
            intent.putExtra("questionario", this.questionario);
            intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
            intent.putExtra("resultadosQuestAnsiedade", (Serializable) resultadosQuestAnsiedade);
            intent.putExtra("nivelAnsiedade", nivelAnsiedade);
            intent.putExtra("resultadosAnsiedade", resultadosAnsiedade);
            startActivity(intent);
        }
    }

    private void salvarFirebase() {
        //Salvar no Firebase
        HashMap<String, Object> dadosAtualizar = new HashMap<>();
        dadosAtualizar.put("id", questionario.getId());
        dadosAtualizar.put("idade", questionario.getIdade());
        dadosAtualizar.put("semestreInicioGraduacao", questionario.getSemestreInicioGraduacao());
        dadosAtualizar.put("periodoAtual", questionario.getPeriodoAtual());
        dadosAtualizar.put("rendaFamiliar", questionario.getRendaFamiliar());
        dadosAtualizar.put("horasEstudoDiarios", questionario.getHorasEstudoDiarios());
        dadosAtualizar.put("horasLazerSemanalmente", questionario.getHorasLazerSemanalmente());
        dadosAtualizar.put("genero", questionario.getGenero());
        dadosAtualizar.put("sexo", questionario.getSexo());
        dadosAtualizar.put("moradia", questionario.getMoradia());
        dadosAtualizar.put("raca", questionario.getRaca());
        dadosAtualizar.put("temFilhos", questionario.isTemFilhos());
        dadosAtualizar.put("situacaoConjugal", questionario.isSituacaoConjugal());
        dadosAtualizar.put("estudaETrabalha", questionario.isEstudaETrabalha());
        dadosAtualizar.put("temReligiao", questionario.isTemReligiao());
        dadosAtualizar.put("participaAtividadeAcademica", questionario.isParticipaAtividadeAcademica());
        dadosAtualizar.put("estudaFimDeSemana", questionario.isEstudaFimDeSemana());
        dadosAtualizar.put("fuma", questionario.isFuma());
        dadosAtualizar.put("consomeBebibaAlcoolica", questionario.isConsomeBebibaAlcoolica());
        dadosAtualizar.put("consomeDrogasIlicitas", questionario.isConsomeDrogasIlicitas());
        dadosAtualizar.put("praticaAtividadeFisica", questionario.isPraticaAtividadeFisica());
        dadosAtualizar.put("recebeAcompanhamentoPsicologico", questionario.isRecebeAcompanhamentoPsicologico());
        dadosAtualizar.put("temNecessidadeAcompanhamentoPsicologico", questionario.isTemNecessidadeAcompanhamentoPsicologico());
        dadosAtualizar.put("usaMedicamentoPrescrito", questionario.isUsaMedicamentoPrescrito());
        dadosAtualizar.put("respondido", questionario.isRespondido());

        referenciaQuestionario.child(questionario.getId()).child("questionario")
                .updateChildren(dadosAtualizar).addOnSuccessListener(aVoid -> {
            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(CadastroFase3.this);
            preferencias.salvarDados(questionario.getId(), questionario, null, null, null, null);
        });
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

    private void carregarDados() {
        Preferencias preferencias = new Preferencias(CadastroFase3.this);
        Gson gson = new Gson();

        if (preferencias.getIdUsuario() != null) {
            String objeto = preferencias.getQuestionario();
            questionario = gson.fromJson(objeto, Questionario.class);
        }
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(CadastroFase3.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestionario.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    questionario = dados.getValue(Questionario.class);
                    carregarQuestionario(questionario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarQuestionario(Questionario questionario) {
        if (questionario.getId() != null
                && questionario.getGenero() != null && questionario.getMoradia() != null
                && questionario.getRaca() != null && questionario.getSexo() != null) {

            radio_group_fumo.check(questionario.isParticipaAtividadeAcademica() ? R.id.rb_fumo_sim : R.id.rb_fumo_nao);
            radio_group_bebida.check(questionario.isEstudaFimDeSemana() ? R.id.rb_bebida_sim : R.id.rb_bebida_nao);
            radio_group_atividade.check(questionario.isEstudaFimDeSemana() ? R.id.rb_atividade_sim : R.id.rb_atividade_nao);
            radio_group_drogas.check(questionario.isEstudaFimDeSemana() ? R.id.rb_drogas_sim : R.id.rb_drogas_nao);

            et_lazer_horas.setText(String.valueOf(questionario.getHorasLazerSemanalmente()));
        }
    }

    private void coletarRespostas() {
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
    }
}
