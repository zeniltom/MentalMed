package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;

import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.ENGenero;
import med.mental.mentalmed.model.ENMoradia;
import med.mental.mentalmed.model.ENRaca;
import med.mental.mentalmed.model.ENSexo;
import med.mental.mentalmed.model.Questionario;

public class CadastroFase1 extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    private EditText et_renda;

    private RadioGroup rg_campo_genero;
    private RadioGroup rg_campo_sexo;
    private RadioGroup rg_campo_moradia;
    private RadioGroup rg_campo_filiacao;
    private RadioGroup rg_campo_conjugal;
    private RadioGroup rg_campo_trab;
    private RadioGroup rg_campo_religiao;

    private Spinner spinner_idade;
    private Spinner spinner_raca;

    private Button bt_proximo_fases;

    private DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private Questionario questionario = new Questionario();
    private String idUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_1);

        carregarComponentes();
        carregarDados();
        carregarPreferencias();
        bloquearComponentes();

        bt_proximo_fases.setOnClickListener(v -> avancarSQR20());
    }

    private void avancarSQR20() {
        coletarRespostas();

        if (validarCadastro()) {

            if (!questionario.isRespondido()) salvarFirebase();

            Intent intent = new Intent(this, QuestSQR20.class);
            intent.putExtra("questionario", questionario);
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

        dadosAtualizar.put("respondido", questionario.isRespondido());

        referenciaQuestionario.child(questionario.getId()).child("questionario")
                .updateChildren(dadosAtualizar).addOnSuccessListener(aVoid -> {
            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(CadastroFase1.this);
            preferencias.salvarDados(questionario.getId(), questionario, null, null, null, null);
        });
    }

    private boolean validarCadastro() {
        boolean resultado = true;

        if (questionario.getGenero() == null) {
            msg("Escolha seu gênero");
            resultado = false;
        } else if (questionario.getSexo() == null) {
            msg("Escolha seu sexo");
            resultado = false;
        } else if (questionario.getMoradia() == null) {
            msg("Informe com quem mora");
            resultado = false;
        } else if (questionario.getIdade() == 0) {
            msg("Escolha sua idade");
            resultado = false;
        } else if (questionario.getRaca().equals(ENRaca.SELECINE)) {
            msg("Escolha sua raça");
            resultado = false;
        }

        return resultado;
    }

    private void msg(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }

    private void carregarComponentes() {
        constraintLayout = findViewById(R.id.constraintLayout);

        bt_proximo_fases = findViewById(R.id.bt_proximo_fases);

        et_renda = findViewById(R.id.et_renda);

        rg_campo_genero = findViewById(R.id.rg_campo_genero);
        rg_campo_sexo = findViewById(R.id.rg_campo_sexo);
        rg_campo_moradia = findViewById(R.id.rg_campo_moradia);
        rg_campo_filiacao = findViewById(R.id.rg_campo_filiacao);
        rg_campo_conjugal = findViewById(R.id.rg_campo_conjugal);
        rg_campo_trab = findViewById(R.id.rg_campo_trab);
        rg_campo_religiao = findViewById(R.id.rg_campo_religiao);

        spinner_idade = findViewById(R.id.spinner_idade);
        spinner_raca = findViewById(R.id.spinner_raca);

        spinner_idade.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.idade)));
        spinner_raca.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, ENRaca.values()));
    }

    private void carregarDados() {
        Preferencias preferencias = new Preferencias(CadastroFase1.this);
        Gson gson = new Gson();

        if (preferencias.getIdUsuario() != null) {
            String objeto = preferencias.getQuestionario();
            questionario = gson.fromJson(objeto, Questionario.class);
        }
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(CadastroFase1.this);
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

            if (questionario.getGenero().equals(ENGenero.FEMININO))
                rg_campo_genero.check(R.id.rb_fem);
            else if (questionario.getGenero().equals(ENGenero.MASCULINO))
                rg_campo_genero.check(R.id.rb_masc);
            else if (questionario.getGenero().equals(ENGenero.LGBT))
                rg_campo_genero.check(R.id.rb_lgbt);

            rg_campo_sexo.check(questionario.getSexo().equals(ENSexo.HOMEN) ? R.id.homem : R.id.mulher);

            if (questionario.getMoradia().equals(ENMoradia.FAMILIARES))
                rg_campo_moradia.check(R.id.rb_familiar);
            else if (questionario.getMoradia().equals(ENMoradia.AMIGOS))
                rg_campo_moradia.check(R.id.rb_amigos);
            else if (questionario.getMoradia().equals(ENMoradia.SOZINHO))
                rg_campo_moradia.check(R.id.rb_sozinho);

            rg_campo_filiacao.check(questionario.isTemFilhos() ? R.id.rb_sim : R.id.rb_nao);
            rg_campo_conjugal.check(questionario.isSituacaoConjugal() ? R.id.c_com : R.id.s_com);
            rg_campo_trab.check(questionario.isEstudaETrabalha() ? R.id.rb_tsim : R.id.rb_tnao);

            spinner_idade.setSelection(getIndex(spinner_idade, String.valueOf(questionario.getIdade())));
            spinner_raca.setSelection(getIndex(spinner_raca, questionario.getRaca().toString()));

            et_renda.setText(String.valueOf(questionario.getRendaFamiliar()));

            rg_campo_religiao.check(questionario.isTemReligiao() ? R.id.rb_religao_sim : R.id.rb_religao_nao);

        }
    }

    private void bloquearComponentes() {
        if (questionario.isRespondido()) {
            for (int i = 0; i < constraintLayout.getChildCount(); i++) {
                View child = constraintLayout.getChildAt(i);
                if (child.getClass().equals(RelativeLayout.class)) {
                    RelativeLayout relativeLayout = (RelativeLayout) child;
                    for (int j = 0; j < relativeLayout.getChildCount(); j++) {
                        View viewDoRL = relativeLayout.getChildAt(j);
                        if (viewDoRL.getClass().equals(RadioGroup.class)) {
                            RadioGroup radioGroup = (RadioGroup) viewDoRL;
                            for (int r = 0; r < radioGroup.getChildCount(); r++) {
                                View radio = radioGroup.getChildAt(r);
                                radio.setEnabled(false);
                            }
                        }
                    }
                    child.setEnabled(false);
                }
            }
            spinner_idade.setEnabled(false);
            spinner_raca.setEnabled(false);
            et_renda.setEnabled(false);
        }
    }

    private void coletarRespostas() {

        int checkedRadioButtonId = rg_campo_genero.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_fem)
            questionario.setGenero(ENGenero.FEMININO);
        else if (checkedRadioButtonId == R.id.rb_masc)
            questionario.setGenero(ENGenero.MASCULINO);
        else if (checkedRadioButtonId == R.id.rb_lgbt)
            questionario.setGenero(ENGenero.LGBT);
        else if (checkedRadioButtonId == -1)
            questionario.setGenero(null);

        int radioButtonId = rg_campo_sexo.getCheckedRadioButtonId();
        if (radioButtonId == R.id.mulher)
            questionario.setSexo(ENSexo.MULHER);
        else if (radioButtonId == R.id.homem)
            questionario.setSexo(ENSexo.HOMEN);
        else if (radioButtonId == -1)
            questionario.setSexo(null);

        int buttonId = rg_campo_moradia.getCheckedRadioButtonId();
        if (buttonId == R.id.rb_familiar)
            questionario.setMoradia(ENMoradia.FAMILIARES);
        else if (buttonId == R.id.rb_amigos)
            questionario.setMoradia(ENMoradia.AMIGOS);
        else if (buttonId == R.id.rb_sozinho)
            questionario.setMoradia(ENMoradia.SOZINHO);
        else if (buttonId == -1)
            questionario.setMoradia(null);

        questionario.setTemFilhos(rg_campo_filiacao.getCheckedRadioButtonId() == R.id.rb_sim);
        questionario.setSituacaoConjugal(rg_campo_conjugal.getCheckedRadioButtonId() == R.id.c_com);
        questionario.setEstudaETrabalha(rg_campo_trab.getCheckedRadioButtonId() == R.id.rb_tsim);

        String itemEscolhido = spinner_idade.getSelectedItem().toString();
        questionario.setIdade(itemEscolhido.equals("Selecione") ? 0 : Integer.parseInt(itemEscolhido));

        questionario.setRaca((ENRaca) spinner_raca.getSelectedItem());

        String valorRenda = et_renda.getText().toString();

        questionario.setRendaFamiliar(valorRenda.equals("") ? 0 : Float.parseFloat(valorRenda));
        questionario.setTemReligiao(rg_campo_religiao.getCheckedRadioButtonId() == R.id.rb_religao_sim);
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++)
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) return i;

        return 0;
    }
}
