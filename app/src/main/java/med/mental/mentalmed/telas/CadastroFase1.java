package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.ENGenero;
import med.mental.mentalmed.model.ENMoradia;
import med.mental.mentalmed.model.ENRaca;
import med.mental.mentalmed.model.ENSexo;
import med.mental.mentalmed.model.Questionario;


public class CadastroFase1 extends AppCompatActivity {

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

    private Questionario questionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_fase_1);

        carregarComponentes();

        bt_proximo_fases.setOnClickListener(v -> avancarSQR20());
    }

    private void avancarSQR20() {
        preencherQuestionario();

        if (validarCadastro()) {
            Intent intent = new Intent(this, QuestSQR20.class);
            intent.putExtra("questionario", questionario);
            startActivity(intent);
        }
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

    private void preencherQuestionario() {
        questionario = new Questionario();

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

        questionario.setTemFilhos(rg_campo_religiao.getCheckedRadioButtonId() == R.id.rb_religao_sim);

        Log.i("#", questionario.toString());
    }
}
