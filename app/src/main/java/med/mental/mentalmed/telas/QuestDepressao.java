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

    private DatabaseReference referenciaQuestDepressaoCat = ConfiguracaoFirebase.getFirebase().child("questionarioDepressaoCat");
    private String idUsuario = "";

    private String nivelAnsiedade;
    private int resultadosAnsiedade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_depressao);

//         resultadosSQR20 = (List<Pergunta>) getIntent().getSerializableExtra("resultadosSQR20");
//        resultadosQuestAnsiedade = (List<PerguntaAnsiedade>) getIntent().getSerializableExtra("resultadosQuestAnsiedade");
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            nivelAnsiedade = bundle.getString("nivelAnsiedade");
//            resultadosAnsiedade = bundle.getInt("resultadosAnsiedade");
//        }
//        listaDePerguntas.addAll(new Perguntas(this).todasCategoriasPergDepress());
//
//        for (int i = 0; i < listaDePerguntas.size(); i++) {
//            PerguntaDepressaoCat p = listaDePerguntas.get(i);
//            p.setPerguntasDeDepressao(new Perguntas(this).perguntaDepressaoPorCat(p.getId()));
//            listaDePerguntas.set(i, p);
//        }

        carregarComponentes();
        carregarPreferencias();
    }

    public void abrirFase4(View view) {
        List<PerguntaDepressao> resultadosQuestDepressao = new ArrayList<>(PerguntaDepressaoAdapter.resultados);

        int resultadosDepressao = verificarResultados(resultadosQuestDepressao);
        String nivelDepressao = nivelDeDepressao(resultadosDepressao);

        //Log.i("#", nivelDepressao);

        //salvarFirebase(resultadosQuestDepressao);

        Intent intent = new Intent(this, CadastroFase4.class);
        //startActivity(intent);

        for (PerguntaDepressao perguntaDepressao : resultadosQuestDepressao) {
            referenciaQuestDepressaoCat.child(idUsuario)
                    .child(String.valueOf(perguntaDepressao.getCatPergDepressId()))
                    .child("perguntasDeDepressao")
                    .child(String.valueOf(perguntaDepressao.getId())).setValue(perguntaDepressao);
        }
    }

    private void salvarFirebase(List<PerguntaDepressao> resultadosQuestDepressao) {
        for (PerguntaDepressao perguntaDepressao : resultadosQuestDepressao) {
            //Salvar no Firebase
            referenciaQuestDepressaoCat.child(idUsuario).child(String.valueOf(perguntaDepressao.getId()))
                    .setValue(perguntaDepressao).addOnSuccessListener(aVoid -> {
                //Salvar nas Preferências
                Preferencias preferencias = new Preferencias(QuestDepressao.this);
                preferencias.salvarDepressao(resultadosQuestDepressao);
            });
        }
    }

    /***
     * VERIFICA O NÍVEL DE DEPRESSAO
     * @param qtd
     */
    private String nivelDeDepressao(int qtd) {
        Log.i("#NIVEL DEPRESSAO", String.valueOf(qtd));

        if (qtd >= 0 && qtd <= 10) return "Depressão ausente";
        else if (qtd >= 10 && qtd <= 18) return "Depressão Leve";
        else if (qtd >= 19 && qtd <= 29) return "Depressão Moderada";
        else if (qtd >= 30 && qtd <= 63) return "Depressão Grave";
        else return "Erro";
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS
     * @param resultadosQuestDepressao
     */
    private int verificarResultados(List<PerguntaDepressao> resultadosQuestDepressao) {
        int resultado = 0;

        for (int i = 0; i < resultadosQuestDepressao.size(); i++) {
            if (resultadosQuestDepressao.get(i).isMarcada())
                resultado = resultado + resultadosQuestDepressao.get(i).getResposta();
        }

        return resultado;
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

        referenciaQuestDepressaoCat.addValueEventListener(new ValueEventListener() {
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
