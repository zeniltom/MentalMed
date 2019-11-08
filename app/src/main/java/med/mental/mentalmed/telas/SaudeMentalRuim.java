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

public class SaudeMentalRuim extends AppCompatActivity {

    private List<Pergunta> resultadosSQR20;
    private Questionario questionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saude_mental_ruim);

        ImageView bt_img_saude_ruim = findViewById(R.id.bt_img_saude_ruim);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");
        resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");

        bt_img_saude_ruim.setOnClickListener(view -> avancarFase2());
    }

    private void avancarFase2() {
        Intent intent = new Intent(this, CadastroFase2.class);
        intent.putExtra("questionario", questionario);
        intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
        startActivity(intent);
    }
}
