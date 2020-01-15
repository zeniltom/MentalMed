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
import med.mental.mentalmed.adapter.PerguntaAdapter;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.util.Util;

public class QuestSQR20 extends AppCompatActivity {

    private ListView lista_perguntas;
    private SpotsDialog progressDialog;

    private final List<Pergunta> listaDePerguntas = new ArrayList<>();
    private PerguntaAdapter adapter;

    private final DatabaseReference referenciaQuestSQR20 = ConfiguracaoFirebase.getFirebase().child("questionarioSQ20");
    private String idUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_sqr20);

        carregarComponentes();
        carregarPreferencias();
    }

    public void avancarSaudeMental(View view) {
        List<Pergunta> resultadosSQR20 = new ArrayList<>(PerguntaAdapter.resultados);

        try {
            salvarFirebase(resultadosSQR20);
            if (temSofrimentoMental(resultadosSQR20)) {
                Intent intent = new Intent(this, SaudeMentalRuim.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, SaudeMentalBoa.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Util.msg(this, "Erro: " + e.getLocalizedMessage() + ". Consulte o suporte!");
            e.printStackTrace();
        }
    }

    private void salvarFirebase(List<Pergunta> resultadosSQR20) {
        for (Pergunta pergunta : resultadosSQR20) {
            //Salvar no Firebase
            referenciaQuestSQR20.child(idUsuario).child(String.valueOf(pergunta.getId()))
                    .setValue(pergunta).addOnSuccessListener(aVoid -> {
                //Salvar nas Preferências
                Preferencias preferencias = new Preferencias(QuestSQR20.this);
                preferencias.salvarSQR20(resultadosSQR20);
            });
        }
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS MARCADAS COM 'SIM'
     * @param resultadosSQR20
     */
    private boolean temSofrimentoMental(List<Pergunta> resultadosSQR20) {
        int qtdSim = 0;

        for (int i = 0; i < resultadosSQR20.size(); i++) {
            if (resultadosSQR20.get(i).isResposta() && resultadosSQR20.get(i).isMarcada())
                qtdSim++;
        }

        return qtdSim >= 7;
    }

    private void carregarComponentes() {
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        lista_perguntas = findViewById(R.id.lista_perguntas);
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(QuestSQR20.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestSQR20.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaDePerguntas.clear();

                for (DataSnapshot dados : dataSnapshot.child(idUsuario).getChildren()) {
                    Pergunta pergunta = dados.getValue(Pergunta.class);
                    listaDePerguntas.add(pergunta);
                }

                adapter = new PerguntaAdapter(getApplicationContext(), listaDePerguntas);
                lista_perguntas.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Log.i("#CARREGAR QUESTSQR20 ACTIVITY", listaDePerguntas.size() > 0 ? "OK" : "ERRO");

                if (progressDialog.isShowing()) progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}