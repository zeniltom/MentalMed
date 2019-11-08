package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import med.mental.mentalmed.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirMainCadastro(View view) {
        Intent intent = new Intent(this, CadastroInicio.class);
        startActivity(intent);
    }
}
