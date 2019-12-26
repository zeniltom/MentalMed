package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import med.mental.mentalmed.R;


public class CadastroInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_inicio);
    }

    public void abrirCadastro1(View view) {
        Intent intent = new Intent(this, CadastroFase1.class);
        startActivity(intent);
    }
}
