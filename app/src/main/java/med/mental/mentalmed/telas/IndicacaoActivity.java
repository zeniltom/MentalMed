package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.Questionario;

public class IndicacaoActivity extends AppCompatActivity {

    private Questionario questionario;
    private List<Pergunta> resultadosSQR20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicacao);

        Button bt_informar_condicao = findViewById(R.id.bt_informar_condicao);

        bt_informar_condicao.setOnClickListener(view -> abrirInformarCondicao());

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
    }

    private void abrirInformarCondicao() {
        Intent intent = new Intent(this, InformarCondicaoActivity.class);
        intent.putExtra("questionario", questionario);
        intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
        // startActivity(intent);
    }
}
