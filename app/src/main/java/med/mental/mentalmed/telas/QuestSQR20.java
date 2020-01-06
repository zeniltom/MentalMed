package med.mental.mentalmed.telas;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.adapter.PerguntaAdapter;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.config.Preferencias;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.Questionario;

public class QuestSQR20 extends AppCompatActivity {

    private final List<Pergunta> listaDePerguntas = new ArrayList<>();

    private DatabaseReference referenciaQuestSQR20 = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private DatabaseReference referenciaListaQuestSQR20 = ConfiguracaoFirebase.getFirebase().child("perguntasSQR20");
    private Questionario questionario = new Questionario();
    private String idUsuario = "";
    private PerguntaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_sqr20);

        questionario = (Questionario) getIntent().getSerializableExtra("questionario");


        carregarComponentes();
        carregarDados();
        carregarPreferencias();
        bloquearComponentes();
    }

    public void avancarSaudeMental(View view) {
        List<Pergunta> resultadosSQR20 = new ArrayList<>(PerguntaAdapter.resultados);

        salvarFirebase(resultadosSQR20);
//        if (temSofrimentoMental(resultadosSQR20)) {
//            Intent intent = new Intent(this, SaudeMentalRuim.class);
//            intent.putExtra("questionario", questionario);
//            intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(this, SaudeMentalBoa.class);
//            intent.putExtra("questionario", questionario);
//            intent.putExtra("resultadosSQR20", (Serializable) resultadosSQR20);
//            startActivity(intent);
//        }
    }

    private void salvarFirebase(List<Pergunta> resultadosSQR20) {

        for (Pergunta pergunta : resultadosSQR20) {
            //Salvar no Firebase
            referenciaQuestSQR20.child(questionario.getId()).child("questSQR20").child(String.valueOf(pergunta.getId()))
                    .setValue(pergunta).addOnSuccessListener(aVoid -> {
                //Salvar nas Preferências
                Preferencias preferencias = new Preferencias(QuestSQR20.this);
                //preferencias.salvarDados(questionario.getId(), questionario, null, null, null, null);
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

        return true;
        //return qtdSim >= 7;
    }

    public void carregarComponentes() {
        ListView lista_perguntas = findViewById(R.id.lista_perguntas);

//        Perguntas perguntas = new Perguntas(this);
//        for (String descricao : perguntas.perguntasSQR20(this)) {
//            Pergunta p = new Pergunta();
//            p.setDescricao(descricao);
//            listaDePerguntas.add(p);
//        }
        referenciaListaQuestSQR20.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaDePerguntas.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Pergunta pergunta = dados.getValue(Pergunta.class);
                    listaDePerguntas.add(pergunta);
                }
                adapter = new PerguntaAdapter(getApplicationContext(), listaDePerguntas);
                lista_perguntas.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void carregarDados() {
        Preferencias preferencias = new Preferencias(QuestSQR20.this);
        Gson gson = new Gson();

        if (preferencias.getIdUsuario() != null) {
            String objeto = preferencias.getQuestionario();
            questionario = gson.fromJson(objeto, Questionario.class);
        }
    }

    public void carregarPreferencias() {
        Preferencias preferencias = new Preferencias(QuestSQR20.this);
        if (preferencias.getIdUsuario() != null) idUsuario = preferencias.getIdUsuario();

        referenciaQuestSQR20.orderByChild("id").equalTo(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    questionario = dados.getValue(Questionario.class);
                    carregarQuestionario(questionario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void carregarQuestionario(Questionario questionario) {
        if (questionario.getId() != null
                && questionario.getGenero() != null && questionario.getMoradia() != null
                && questionario.getRaca() != null && questionario.getSexo() != null) {

        }
    }

    public void bloquearComponentes() {
        if (questionario.isRespondido()) {
//            for (int i = 0; i < constraintLayout.getChildCount(); i++) {
//                View child = constraintLayout.getChildAt(i);
//                if (child.getClass().equals(RelativeLayout.class)) {
//                    RelativeLayout relativeLayout = (RelativeLayout) child;
//                    for (int j = 0; j < relativeLayout.getChildCount(); j++) {
//                        View viewDoRL = relativeLayout.getChildAt(j);
//                        if (viewDoRL.getClass().equals(RadioGroup.class)) {
//                            RadioGroup radioGroup = (RadioGroup) viewDoRL;
//                            for (int r = 0; r < radioGroup.getChildCount(); r++) {
//                                View radio = radioGroup.getChildAt(r);
//                                radio.setEnabled(false);
//                            }
//                        }
//                    }
//                    child.setEnabled(false);
//                }
//            }
//            spinner_idade.setEnabled(false);
//            spinner_raca.setEnabled(false);
//            et_renda.setEnabled(false);
        }
    }

}