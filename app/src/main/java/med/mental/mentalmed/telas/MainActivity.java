package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Questionario;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private String idUsuario = "";
    private Questionario questionario;
    private String androidId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        carregarPreferencias();
    }

    public void abrirMainCadastro(View view) {
        carregarPreferencias();
        criarPreferencias();

        Intent intent = new Intent(this, CadastroInicio.class);
        startActivity(intent);
    }

    private void criarPreferencias() {
        if (questionario == null) {
            questionario = new Questionario();
            questionario.setId(androidId);
            questionario.setRespondido(false);

            referenciaQuestionario.child(questionario.getId()).child("questionario")
                    .setValue(questionario).addOnSuccessListener(aVoid -> {
            }).addOnFailureListener(e -> msg("Erro ao salvar registros no Servidor! ERRO: " + e.getLocalizedMessage()));

            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(MainActivity.this);
            preferencias.salvarDados(questionario.getId(), questionario, null, null, null, null);
        }
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(MainActivity.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestionario.orderByChild("id").equalTo(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    questionario = dados.getValue(Questionario.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void msg(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }
}
