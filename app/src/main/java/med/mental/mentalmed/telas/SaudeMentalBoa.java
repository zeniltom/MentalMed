package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.Questionario;

public class SaudeMentalBoa extends AppCompatActivity {

    private Questionario questionario;
    private List<Pergunta> resultadosSQR20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saude_mental_boa);

        ImageView bt_img_saude_ruim = findViewById(R.id.bt_img_saude_ruim);

        bt_img_saude_ruim.setOnClickListener(view -> abrirIndicacao());

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
    }

    private void abrirIndicacao() {
        Intent intent = new Intent(this, IndicacaoActivity.class);
        intent.putExtra("questionario", questionario);
        intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
        startActivity(intent);
    }
}
