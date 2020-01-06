package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import med.mental.mentalmed.model.Questionario;

public class CadastroFase2 extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    private EditText et_semetre_grad;
    private EditText et_horas_estudo;
    private EditText et_periodo_atual;

    private RadioGroup radio_group_ativ_acad;
    private RadioGroup radio_group_estuda;

    private Button bt_proximo_1;

    private Questionario questionario;
    private List<Pergunta> resultadosSQR20;
    private DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_2);

        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");

        carregarComponentes();
        carregarDados();
        carregarPreferencias();
        bloquearComponentes();

        bt_proximo_1.setOnClickListener(v -> abrirAnsiedade());
    }

    private void abrirAnsiedade() {
        coletarRespostas();

        if (isValid()) {

            if (!questionario.isRespondido()) salvarFirebase();

            Intent intent = new Intent(this, QuestAnsiedade.class);
            intent.putExtra("questionario", this.questionario);
            intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
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

        dadosAtualizar.put("respondido", questionario.isRespondido());

        referenciaQuestionario.child(questionario.getId()).child("questionario")
                .updateChildren(dadosAtualizar).addOnSuccessListener(aVoid -> {
            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(CadastroFase2.this);
            preferencias.salvarDados(questionario.getId(), questionario, null, null, null, null);
        });
    }

    private boolean isValid() {
        boolean resultado = true;

        if (questionario.getSemestreInicioGraduacao() == 0) {
            msg("Prencha o semestre que iniciou a graduação");
            resultado = false;
        } else if (questionario.getPeriodoAtual() == 0) {
            msg("Prencha o período atual da sua graduação");
            resultado = false;
        } else if (questionario.getHorasEstudoDiarios() == 0) {
            msg("Informe a quantidade de horas que estuda diariamente");
            resultado = false;
        }

        return resultado;
    }

    private void msg(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }

    private void carregarComponentes() {
        constraintLayout = findViewById(R.id.constraintLayout);

        bt_proximo_1 = findViewById(R.id.bt_proximo_1);

        et_semetre_grad = findViewById(R.id.et_semetre_grad);
        et_periodo_atual = findViewById(R.id.et_periodo_atual);
        et_horas_estudo = findViewById(R.id.et_horas_estudo);

        radio_group_ativ_acad = findViewById(R.id.radio_group_ativ_acad);
        radio_group_estuda = findViewById(R.id.radio_group_estuda);
    }

    private void carregarDados() {
        Preferencias preferencias = new Preferencias(CadastroFase2.this);
        Gson gson = new Gson();

        if (preferencias.getIdUsuario() != null) {
            String objeto = preferencias.getQuestionario();
            questionario = gson.fromJson(objeto, Questionario.class);
        }
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(CadastroFase2.this);
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
                && questionario.getRaca() != null && questionario.getSexo() != null
                && questionario.getSemestreInicioGraduacao() > 0
                && questionario.getPeriodoAtual() > 0
                && questionario.getHorasEstudoDiarios() > 0) {

            et_semetre_grad.setText(String.valueOf(questionario.getSemestreInicioGraduacao()));
            et_periodo_atual.setText(String.valueOf(questionario.getPeriodoAtual()));
            et_horas_estudo.setText(String.valueOf(questionario.getHorasEstudoDiarios()));

            radio_group_ativ_acad.check(questionario.isParticipaAtividadeAcademica() ? R.id.rb_ativ_acad_sim : R.id.rb_ativ_acad_nao);
            radio_group_estuda.check(questionario.isEstudaFimDeSemana() ? R.id.rb_estuda_sim : R.id.rb_estuda_nao);
        }
    }

    private void bloquearComponentes() {
        if (questionario.isRespondido()) {
            for (int i = 0; i < constraintLayout.getChildCount(); i++) {
                View child = constraintLayout.getChildAt(i);
                if (child.getClass().equals(RadioGroup.class) && !child.getClass().equals(AppCompatButton.class)) {
                    RadioGroup radioGroup = (RadioGroup) child;
                    for (int r = 0; r < radioGroup.getChildCount(); r++) {
                        View radio = radioGroup.getChildAt(r);
                        radio.setEnabled(false);
                    }
                }
            }

            et_semetre_grad.setEnabled(false);
            et_periodo_atual.setEnabled(false);
            et_horas_estudo.setEnabled(false);
        }
    }

    private void coletarRespostas() {
        String semestre = et_semetre_grad.getText().toString();
        String periodo = et_periodo_atual.getText().toString();
        String horasEstudo = et_horas_estudo.getText().toString();

        questionario.setSemestreInicioGraduacao(semestre.equals("") ? 0 : Integer.parseInt(semestre));
        questionario.setPeriodoAtual(periodo.equals("") ? 0 : Integer.parseInt(periodo));
        questionario.setHorasEstudoDiarios(horasEstudo.equals("") ? 0 : Float.parseFloat(horasEstudo));

        questionario.setParticipaAtividadeAcademica(radio_group_ativ_acad.getCheckedRadioButtonId() == R.id.rb_ativ_acad_sim);
        questionario.setEstudaFimDeSemana(radio_group_estuda.getCheckedRadioButtonId() == R.id.rb_estuda_sim);
    }
}
