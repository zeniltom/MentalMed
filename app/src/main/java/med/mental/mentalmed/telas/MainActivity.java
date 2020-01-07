package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.Questionario;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referenciaQuestionario = ConfiguracaoFirebase.getFirebase().child("questionario");
    private DatabaseReference referenciaQuestSQR20 = ConfiguracaoFirebase.getFirebase().child("questionarioSQ20");

    private DatabaseReference referenciaListaQuestSQR20 = ConfiguracaoFirebase.getFirebase().child("perguntasSQR20");

    private Questionario questionario;
    private List<Pergunta> questSRQ20 = new ArrayList<>();

    private String idUsuario = "";
    private String androidId = "";

    private SpotsDialog progressDialog;

    private ValueEventListener valueEventListenerQuestionario;
    private ValueEventListener valueEventListenerListaQuestSQR20;

    @Override
    protected void onStart() {
        super.onStart();
        referenciaQuestionario.addValueEventListener(valueEventListenerQuestionario);
        referenciaListaQuestSQR20.addValueEventListener(valueEventListenerListaQuestSQR20);
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenciaQuestionario.removeEventListener(valueEventListenerQuestionario);
        referenciaListaQuestSQR20.removeEventListener(valueEventListenerListaQuestSQR20);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        carregarComponentes();
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

            //SALVA O QUESTIONÁRIO SEM RESPOSTA COMPLETO NO FIREBASE PARA O USUÁRIO
            referenciaQuestionario.child(androidId).setValue(questionario)
                    .addOnSuccessListener(aVoid -> Log.i("#SALVAR QUESTIONARIO", "OK"))
                    .addOnFailureListener(e -> {
                        msg("Erro ao salvar registros no Servidor! ERRO: " + e.getLocalizedMessage());
                        Log.i("#SALVAR QUESTIONARIO", "ERRO");
                    });

            //SALVA O QUESTSQR20 SEM RESPOSTA COMPLETO NO FIREBASE PARA O USUÁRIO
            for (Pergunta pergunta : questSRQ20) {
                //Salvar no Firebase
                referenciaQuestSQR20.child(androidId).child(String.valueOf(pergunta.getId()))
                        .setValue(pergunta).addOnSuccessListener(aVoid -> {

                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    Log.i("#SALVAR QUESTSQR20", "OK");
                });
            }

            //Salvar nas Preferências
            Preferencias preferencias = new Preferencias(MainActivity.this);
            preferencias.salvarDados(androidId, questionario, questSRQ20, null, null, null);
        }
    }

    private void carregarPreferencias() {
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Preferencias preferencias = new Preferencias(MainActivity.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestionario.orderByChild("id").equalTo(idUsuario);
        valueEventListenerQuestionario = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    questionario = dados.getValue(Questionario.class);
                }

                Log.i("#CARREGAR QUESTIONARIO", questionario != null ? "OK" : "ERRO");
                if (progressDialog.isShowing()) progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public void carregarComponentes() {
        valueEventListenerListaQuestSQR20 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questSRQ20.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Pergunta pergunta = dados.getValue(Pergunta.class);
                    questSRQ20.add(pergunta);
                }

                Log.i("#CARREGAR QUESTSQR20", questSRQ20.size() > 0 ? "OK" : "ERRO");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void msg(String texto) {
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }
}
