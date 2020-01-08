package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CadastroFase3 extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    private EditText et_lazer_horas;

    private RadioGroup radio_group_fumo;
    private RadioGroup radio_group_bebida;
    private RadioGroup radio_group_atividade;
    private RadioGroup radio_group_drogas;

    private Button bt_proximo_1;
    private SpotsDialog progressDialog;

    private DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("questionario");
    private Questionario questionario = new Questionario();
    private String idUsuario;

    private ValueEventListener valueEventListenerQuestionario;

    @Override
    protected void onStart() {
        super.onStart();
        referenciaQuestionario.addValueEventListener(valueEventListenerQuestionario);
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenciaQuestionario.removeEventListener(valueEventListenerQuestionario);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_3);

        carregarComponentes();
        carregarPreferencias();

        bt_proximo_1.setOnClickListener(v -> avancarDepressao());
    }

    private void avancarDepressao() {
        coletarRespostas();

        if (isValid()) {

            if (!questionario.isRespondido()) salvarFirebase();

            Intent intent = new Intent(this, QuestDepressao.class);
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

        referenciaQuestionario.child(questionario.getId()).updateChildren(dadosAtualizar).addOnSuccessListener(aVoid -> {
            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(CadastroFase3.this);
            preferencias.salvarQuestionario(questionario);
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
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        constraintLayout = findViewById(R.id.constraintLayout);

        bt_proximo_1 = findViewById(R.id.bt_proximo_1);

        et_lazer_horas = findViewById(R.id.et_lazer_horas);

        radio_group_fumo = findViewById(R.id.radio_group_fumo);
        radio_group_bebida = findViewById(R.id.radio_group_bebida);
        radio_group_atividade = findViewById(R.id.radio_group_atividade);
        radio_group_drogas = findViewById(R.id.radio_group_drogas);
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(CadastroFase3.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestionario.orderByChild("id").equalTo(idUsuario);
        valueEventListenerQuestionario = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    questionario = dados.getValue(Questionario.class);
                    Log.i("#CARREGAR QUESTIONARIO FASE3", questionario.getId() != null ? "OK" : "ERRO");
                    carregarQuestionario(questionario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void carregarQuestionario(Questionario questionario) {
        if (questionario.getId() != null
                && questionario.getGenero() != null && questionario.getMoradia() != null
                && questionario.getRaca() != null && questionario.getSexo() != null
                && questionario.getSemestreInicioGraduacao() > 0
                && questionario.getPeriodoAtual() > 0
                && questionario.getHorasEstudoDiarios() > 0
                && questionario.getHorasLazerSemanalmente() > 0) {

            radio_group_fumo.check(questionario.isParticipaAtividadeAcademica() ? R.id.rb_fumo_sim : R.id.rb_fumo_nao);
            radio_group_bebida.check(questionario.isEstudaFimDeSemana() ? R.id.rb_bebida_sim : R.id.rb_bebida_nao);
            radio_group_atividade.check(questionario.isEstudaFimDeSemana() ? R.id.rb_atividade_sim : R.id.rb_atividade_nao);
            radio_group_drogas.check(questionario.isEstudaFimDeSemana() ? R.id.rb_drogas_sim : R.id.rb_drogas_nao);

            et_lazer_horas.setText(String.valueOf(questionario.getHorasLazerSemanalmente()));

            bloquearComponentes();
        }

        if (progressDialog.isShowing()) progressDialog.dismiss();
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
            et_lazer_horas.setEnabled(false);
        }
    }

    private void coletarRespostas() {
        questionario.setFuma(radio_group_fumo.getCheckedRadioButtonId() == R.id.rb_fumo_sim);
        questionario.setConsomeBebibaAlcoolica(radio_group_bebida.getCheckedRadioButtonId() == R.id.rb_bebida_sim);
        questionario.setPraticaAtividadeFisica(radio_group_atividade.getCheckedRadioButtonId() == R.id.rb_atividade_sim);
        questionario.setConsomeDrogasIlicitas(radio_group_drogas.getCheckedRadioButtonId() == R.id.rb_drogas_sim);

        String horasLazer = et_lazer_horas.getText().toString();
        questionario.setHorasLazerSemanalmente(horasLazer.equals("") ? 0 : Float.parseFloat(horasLazer));
    }
}
