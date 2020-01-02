package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

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
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.Questionario;

public class CadastroFase4 extends AppCompatActivity {

    private RadioGroup radio_group_acompanhamento;
    private RadioGroup radio_group_ness_acomp;
    private RadioGroup radio_group_medicamento;

    private Button bt_proximo_1;

    private List<Pergunta> resultadosSQR20;
    private List<PerguntaAnsiedade> resultadosQuestAnsiedade;
    private List<PerguntaDepressao> resultadosQuestDepressao;
    private String nivelAnsiedade;
    private int resultadosAnsiedade;
    private String nivelDepressao;
    private int resultadosDepressao;

    private Questionario questionario;
    private DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_4);

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
        carregarDados();
        carregarPreferencias();

        bt_proximo_1.setOnClickListener(v -> avancarSindromeBurnout());
    }

    private void avancarSindromeBurnout() {
        coletarRespostas();

        salvarFirebase();

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
            Preferencias preferencias = new Preferencias(CadastroFase4.this);
            preferencias.salvarDados(questionario.getId(), questionario, null, null, null, null);
        });
    }

    private void carregarComponentes() {
        bt_proximo_1 = findViewById(R.id.bt_proximo_1);

        radio_group_acompanhamento = findViewById(R.id.radio_group_acompanhamento);
        radio_group_ness_acomp = findViewById(R.id.radio_group_ness_acomp);
        radio_group_medicamento = findViewById(R.id.radio_group_medicamento);
    }

    private void carregarDados() {
        Preferencias preferencias = new Preferencias(CadastroFase4.this);
        Gson gson = new Gson();

        if (preferencias.getIdUsuario() != null) {
            String objeto = preferencias.getQuestionario();
            questionario = gson.fromJson(objeto, Questionario.class);
        }
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(CadastroFase4.this);
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

            radio_group_acompanhamento.check(questionario.isParticipaAtividadeAcademica() ? R.id.rb_acomp_sim : R.id.rb_acomp_nao);
            radio_group_ness_acomp.check(questionario.isEstudaFimDeSemana() ? R.id.rb_ness_acomp_sim : R.id.rb_ness_acomp_nao);
            radio_group_medicamento.check(questionario.isEstudaFimDeSemana() ? R.id.rb_medicamento_sim : R.id.rb_medicamento_nao);
        }
    }

    private void coletarRespostas() {
        int radioButtonId = radio_group_acompanhamento.getCheckedRadioButtonId();
        if (radioButtonId == R.id.rb_acomp_sim)
            questionario.setRecebeAcompanhamentoPsicologico(true);
        else if (radioButtonId == R.id.rb_acomp_nao)
            questionario.setRecebeAcompanhamentoPsicologico(false);

        int checkedRadioButtonId = radio_group_ness_acomp.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_ness_acomp_sim)
            questionario.setTemNecessidadeAcompanhamentoPsicologico(true);
        else if (checkedRadioButtonId == R.id.rb_ness_acomp_nao)
            questionario.setTemNecessidadeAcompanhamentoPsicologico(false);


        int buttonId = radio_group_medicamento.getCheckedRadioButtonId();
        if (buttonId == R.id.rb_medicamento_sim) questionario.setUsaMedicamentoPrescrito(true);
        else if (buttonId == R.id.rb_medicamento_nao)
            questionario.setUsaMedicamentoPrescrito(false);

        questionario.setRespondido(true);
    }
}
