package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.Questionario;

public class VamosLa extends AppCompatActivity {

    private Questionario questionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vamos_la);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");

        ImageView img_vamos_la = findViewById(R.id.img_vamos_la);
        img_vamos_la.setOnClickListener(view -> abrirQuestionario());
    }

    private void abrirQuestionario() {
        Intent intent = new Intent(this, QuestSQR20.class);
        intent.putExtra("questionario", this.questionario);
        startActivity(intent);
    }
}
