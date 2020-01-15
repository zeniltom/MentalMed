package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
import med.mental.mentalmed.adapter.PerguntaAnsiedadeAdapter;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.util.Util;

public class QuestAnsiedade extends AppCompatActivity {

    private ListView lista_perguntas_ansiedade;
    private SpotsDialog progressDialog;

    private final List<PerguntaAnsiedade> listaDePerguntas = new ArrayList<>();
    private PerguntaAnsiedadeAdapter adapter;

    private final DatabaseReference referenciaQuestAnsiedade = ConfiguracaoFirebase.getFirebase().child("questionarioAnsiedade");
    private String idUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_ansiedade);

        carregarComponentes();
        carregarPreferencias();
    }

    public void avancarFase3(View view) {
        List<PerguntaAnsiedade> resultadosQuestAnsiedade = new ArrayList<>(PerguntaAnsiedadeAdapter.resultados);

        try {
            salvarFirebase(resultadosQuestAnsiedade);

            Intent intent = new Intent(this, CadastroFase3.class);
            startActivity(intent);
        } catch (Exception e) {
            Util.msg(this, "Erro: " + e.getLocalizedMessage() + ". Consulte o suporte!");
            e.printStackTrace();
        }
    }

    private void salvarFirebase(List<PerguntaAnsiedade> resultadosQuestAnsiedade) {
        for (PerguntaAnsiedade perguntaAnsiedade : resultadosQuestAnsiedade) {
            //Salvar no Firebase
            referenciaQuestAnsiedade.child(idUsuario).child(String.valueOf(perguntaAnsiedade.getId()))
                    .setValue(perguntaAnsiedade).addOnSuccessListener(aVoid -> {
                //Salvar nas Preferências
                Preferencias preferencias = new Preferencias(QuestAnsiedade.this);
                preferencias.salvarAnsiedade(resultadosQuestAnsiedade);
            });
        }
    }

    private void carregarComponentes() {
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        lista_perguntas_ansiedade = findViewById(R.id.lista_perguntas_ansiedade);
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(QuestAnsiedade.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestAnsiedade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaDePerguntas.clear();

                for (DataSnapshot dados : dataSnapshot.child(idUsuario).getChildren()) {
                    PerguntaAnsiedade perguntaAnsiedade = dados.getValue(PerguntaAnsiedade.class);
                    listaDePerguntas.add(perguntaAnsiedade);
                }

                adapter = new PerguntaAnsiedadeAdapter(getApplicationContext(), listaDePerguntas);
                lista_perguntas_ansiedade.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Log.i("#CARREGAR QUESTANSIEDADE ACTIVITY", listaDePerguntas.size() > 0 ? "OK" : "ERRO");

                if (progressDialog.isShowing()) progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
