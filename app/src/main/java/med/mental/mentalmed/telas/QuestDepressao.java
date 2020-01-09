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
import med.mental.mentalmed.adapter.PerguntaDepressaoAdapter;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.PerguntaDepressaoCat;

public class QuestDepressao extends AppCompatActivity {

    private SpotsDialog progressDialog;
    private ListView lista_perguntas_depressao;

    private final List<PerguntaDepressaoCat> listaDePerguntas = new ArrayList<>();
    private PerguntaDepressaoAdapter adapter;

    private DatabaseReference referenciaQuestDepressao = ConfiguracaoFirebase.getFirebase().child("questionarioDepressao");
    private String idUsuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_depressao);

        carregarComponentes();
        carregarPreferencias();
    }

    public void abrirFase4(View view) {
        List<PerguntaDepressao> resultadosQuestDepressao = new ArrayList<>(PerguntaDepressaoAdapter.resultados);

        salvarFirebase(resultadosQuestDepressao);

        Intent intent = new Intent(this, CadastroFase4.class);
        startActivity(intent);
    }

    private void salvarFirebase(List<PerguntaDepressao> resultadosQuestDepressao) {
        //Salvar no Firebase
        for (PerguntaDepressao pergDepressao : resultadosQuestDepressao) {
            referenciaQuestDepressao.child(idUsuario)
                    .child(String.valueOf(pergDepressao.getCatPergDepressId()))
                    .child("perguntasDeDepressao").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dado : dataSnapshot.getChildren()) {
                        PerguntaDepressao pergDepressaoAchada = dado.getValue(PerguntaDepressao.class);

                        if (dado.getKey() != null && pergDepressaoAchada != null) {
                            if (pergDepressaoAchada.getId() == pergDepressao.getId()) {
                                referenciaQuestDepressao.child(idUsuario)
                                        .child(String.valueOf(pergDepressaoAchada.getCatPergDepressId()))
                                        .child("perguntasDeDepressao")
                                        .child(dado.getKey()).setValue(pergDepressao);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //Salvar nas Preferências
        Preferencias preferencias = new Preferencias(QuestDepressao.this);
        preferencias.salvarDepressao(resultadosQuestDepressao);
    }

    private void carregarComponentes() {
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        lista_perguntas_depressao = findViewById(R.id.lista_perguntas_depressao);
    }

    private void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(QuestDepressao.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestDepressao.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaDePerguntas.clear();

                for (DataSnapshot dados : dataSnapshot.child(idUsuario).getChildren()) {
                    PerguntaDepressaoCat perguntaDepressaoCat = dados.getValue(PerguntaDepressaoCat.class);
                    listaDePerguntas.add(perguntaDepressaoCat);
                }

                adapter = new PerguntaDepressaoAdapter(getApplicationContext(), listaDePerguntas);
                lista_perguntas_depressao.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Log.i("#CARREGAR QUESTDEPRESSAOACTIVITY", listaDePerguntas.size() > 0 ? "OK" : "ERRO");

                if (progressDialog.isShowing()) progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
