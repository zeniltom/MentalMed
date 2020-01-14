package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Questionario;

public class CadastroFase4 extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    private RadioGroup radio_group_acompanhamento;
    private RadioGroup radio_group_ness_acomp;
    private RadioGroup radio_group_medicamento;

    private Button bt_proximo_1;
    private SpotsDialog progressDialog;

    private final DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("questionario");
    private Questionario questionario = new Questionario();
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_4);

        carregarComponentes();
        carregarPreferencias();

        bt_proximo_1.setOnClickListener(v -> avancarSindromeBurnout());
    }

    private void avancarSindromeBurnout() {
        try {
            coletarRespostas();

            if (!questionario.isRespondido()) {
                questionario.setRespondido(true);
                salvarFirebase();
            }

            Intent intent = new Intent(this, QuestSindromeBurnout.class);
            startActivity(intent);
        } catch (Exception e) {
            msg("Erro: " + e.getLocalizedMessage() + ". Consulte o suporte!");
            e.printStackTrace();
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

        referenciaQuestionario.child(questionario.getId()).updateChildren(dadosAtualizar).addOnSuccessListener(aVoid -> {
            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(CadastroFase4.this);
            preferencias.salvarQuestionario(questionario);
        });
    }

    private void msg(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }

    private void carregarComponentes() {
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        constraintLayout = findViewById(R.id.constraintLayout);

        bt_proximo_1 = findViewById(R.id.bt_proximo_1);

        radio_group_acompanhamento = findViewById(R.id.radio_group_acompanhamento);
        radio_group_ness_acomp = findViewById(R.id.radio_group_ness_acomp);
        radio_group_medicamento = findViewById(R.id.radio_group_medicamento);
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(CadastroFase4.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestionario.orderByChild("id").equalTo(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    questionario = dados.getValue(Questionario.class);
                    Log.i("#CARREGAR QUESTIONARIO FASE4", questionario != null ? "OK" : "ERRO");
                    carregarQuestionario(questionario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void carregarQuestionario(Questionario questionario) {
        if (questionario.getId() != null
                && questionario.getGenero() != null && questionario.getMoradia() != null
                && questionario.getRaca() != null && questionario.getSexo() != null
                && questionario.getSemestreInicioGraduacao() > 0
                && questionario.getPeriodoAtual() > 0
                && questionario.getHorasEstudoDiarios() > 0
                && questionario.getHorasLazerSemanalmente() > 0) {

            radio_group_acompanhamento.check(questionario.isRecebeAcompanhamentoPsicologico() ? R.id.rb_acomp_sim : R.id.rb_acomp_nao);
            radio_group_ness_acomp.check(questionario.isTemNecessidadeAcompanhamentoPsicologico() ? R.id.rb_ness_acomp_sim : R.id.rb_ness_acomp_nao);
            radio_group_medicamento.check(questionario.isUsaMedicamentoPrescrito() ? R.id.rb_medicamento_sim : R.id.rb_medicamento_nao);

            bloquearComponentes();
        }
    }

    private void bloquearComponentes() {
        if (questionario.isRespondido()) {
            for (int i = 0; i < constraintLayout.getChildCount(); i++) {
                View child = constraintLayout.getChildAt(i);
                if (child.getClass().equals(RadioGroup.class)) {
                    RadioGroup radioGroup = (RadioGroup) child;
                    for (int r = 0; r < radioGroup.getChildCount(); r++) {
                        View radio = radioGroup.getChildAt(r);
                        radio.setEnabled(false);
                    }
                }
            }
        }
    }

    private void coletarRespostas() {
        questionario.setRecebeAcompanhamentoPsicologico(radio_group_acompanhamento.getCheckedRadioButtonId() == R.id.rb_acomp_sim);
        questionario.setTemNecessidadeAcompanhamentoPsicologico(radio_group_ness_acomp.getCheckedRadioButtonId() == R.id.rb_ness_acomp_sim);
        questionario.setUsaMedicamentoPrescrito(radio_group_medicamento.getCheckedRadioButtonId() == R.id.rb_medicamento_sim);
    }
}
