package med.mental.mentalmed.telas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import med.mental.mentalmed.R;
import med.mental.mentalmed.config.ConfiguracaoFirebase;
import med.mental.mentalmed.model.ENGenero;
import med.mental.mentalmed.model.Pergunta;
import med.mental.mentalmed.model.PerguntaAnsiedade;
import med.mental.mentalmed.model.PerguntaBurnout;
import med.mental.mentalmed.model.PerguntaDepressaoCat;
import med.mental.mentalmed.model.Questionario;
import med.mental.mentalmed.model.Usuario;

public class Administrador extends AppCompatActivity {

    private SpotsDialog progressDialog;
    private Toolbar toolbar;
    private TextView tv_qtd_usuarios;
    private TextView tv_idade;
    private TextView tv_qtd_masculino;
    private TextView tv_qtd_feminino;
    private TextView tv_qtd_lgbt;
    private TextView tv_qtd_com_comp;
    private TextView tv_qtd_sem_comp;
    private TextView tv_media_semestre_estudam;
    private TextView tv_qtd_com_sofrimento;
    private TextView tv_qtd_sem_sofrimento;
    private TextView tv_qtd_com_ansiedade;
    private TextView tv_qtd_ansiedade_leve;
    private TextView tv_qtd_ansiedade_moderada;
    private TextView tv_qtd_ansiedade_grave;
    private TextView tv_qtd_com_depressao;
    private TextView tv_qtd_depressao_leve;
    private TextView tv_qtd_depressao_moderada;
    private TextView tv_qtd_depressao_grave;
    private TextView tv_qtd_com_sindromeb;

    private FirebaseAuth auth;

    private final DatabaseReference refUsuario = ConfiguracaoFirebase.getFirebase().child("usuarios");
    private final DatabaseReference refQuestionario = ConfiguracaoFirebase.getFirebase().child("questionario");
    private final DatabaseReference refQuestSQR20 = ConfiguracaoFirebase.getFirebase().child("questionarioSQ20");
    private final DatabaseReference refQuestAnsiedade = ConfiguracaoFirebase.getFirebase().child("questionarioAnsiedade");
    private final DatabaseReference refQuestDepressao = ConfiguracaoFirebase.getFirebase().child("questionarioDepressao");
    private final DatabaseReference refQuestSindromeB = ConfiguracaoFirebase.getFirebase().child("questionarioSindromeBurnout");

    private final List<Pergunta> resultadosQuestSQR20 = new ArrayList<>();
    private final List<PerguntaAnsiedade> resultadosQuestAnsiedade = new ArrayList<>();
    private final List<PerguntaDepressaoCat> resultadosQuestDepressao = new ArrayList<>();
    private final List<PerguntaBurnout> resultadosQuestSindromeBurnout = new ArrayList<>();

    private Usuario usuario = new Usuario();
    private String idUsuario = "";

    private String nivelAnsiedade;
    private String nivelDepressao;

    private int resultadosAnsiedade;
    private int resultadosDepressao;
    private HashMap<String, Float> resultadosFinais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_administrador);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("MentalMed");
        setSupportActionBar(toolbar);

        verificarUsuarioLogado();
        carregarComponentes();
    }

    private void verificarUsuarioLogado() {
        progressDialog = new SpotsDialog(this, "Carregando...", R.style.dialogEmpregosAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.getCurrentUser();

        if (auth.getCurrentUser() != null)
            carregarPreferencias();
    }

    private void carregarComponentes() {
        tv_qtd_usuarios = findViewById(R.id.tv_qtd_usuarios);
        tv_idade = findViewById(R.id.tv_idade);
        tv_qtd_masculino = findViewById(R.id.tv_qtd_masculino);
        tv_qtd_feminino = findViewById(R.id.tv_qtd_feminino);
        tv_qtd_lgbt = findViewById(R.id.tv_qtd_lgbt);
        tv_qtd_com_comp = findViewById(R.id.tv_qtd_com_comp);
        tv_qtd_sem_comp = findViewById(R.id.tv_qtd_sem_comp);
        tv_media_semestre_estudam = findViewById(R.id.tv_media_semestre_estudam);
        tv_qtd_com_sofrimento = findViewById(R.id.tv_qtd_com_sofrimento);
        tv_qtd_sem_sofrimento = findViewById(R.id.tv_qtd_sem_sofrimento);

        tv_qtd_com_ansiedade = findViewById(R.id.tv_qtd_com_ansiedade);
        tv_qtd_ansiedade_leve = findViewById(R.id.tv_qtd_ansiedade_leve);
        tv_qtd_ansiedade_moderada = findViewById(R.id.tv_qtd_ansiedade_moderada);
        tv_qtd_ansiedade_grave = findViewById(R.id.tv_qtd_ansiedade_grave);

        tv_qtd_com_depressao = findViewById(R.id.tv_qtd_com_depressao);
        tv_qtd_depressao_leve = findViewById(R.id.tv_qtd_depressao_leve);
        tv_qtd_depressao_moderada = findViewById(R.id.tv_qtd_depressao_moderada);
        tv_qtd_depressao_grave = findViewById(R.id.tv_qtd_depressao_grave);

        tv_qtd_com_sindromeb = findViewById(R.id.tv_qtd_com_sindromeb);

        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void carregarPreferencias() {
        carregarUsuario();
        consultarQuestionario();
        consultarSofrimentoMental();
        consultarAnsiedade();
        consultarDepressao();
        consultarSindromeBurnout();
    }

    private void carregarUsuario() {
        idUsuario = auth.getUid();

        refUsuario.child(Objects.requireNonNull(idUsuario)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!idUsuario.equals("")) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    toolbar.setTitle("MentalMed - " + Objects.requireNonNull(usuario).getNome());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void consultarQuestionario() {
        refQuestionario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int qtdUsuarios = 0;
                int semestre = 0;
                int qtdMasculino = 0;
                int qtdFeminino = 0;
                int qtdLGBT = 0;
                int qtdComCompanheiro = 0;
                int qtdSemCompanheiro = 0;
                int qtdComSofrimentoMental = 0;
                int qtdSemSofrimentoMental = 0;
                double idade = 0;

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Questionario q = dados.getValue(Questionario.class);

                    idade = idade + Objects.requireNonNull(q).getIdade();
                    semestre = semestre + q.getPeriodoAtual();

                    if (q.getGenero() != null && q.getGenero().equals(ENGenero.MASCULINO))
                        qtdMasculino++;
                    else if (q.getGenero() != null && q.getGenero().equals(ENGenero.FEMININO))
                        qtdFeminino++;
                    else if (q.getGenero() != null && q.getGenero().equals(ENGenero.LGBT))
                        qtdLGBT++;

                    if (q.isSituacaoConjugal()) qtdComCompanheiro++;
                    else qtdSemCompanheiro++;

                    qtdUsuarios++;
                }

                double mediaIdades = idade / qtdUsuarios;
                int mediaSemestres = semestre / qtdUsuarios;

                try {
                    tv_qtd_usuarios.setText(String.valueOf(qtdUsuarios));
                    tv_idade.setText(new DecimalFormat("#0.##").format(mediaIdades));
                    tv_qtd_masculino.setText(String.valueOf(qtdMasculino));
                    tv_qtd_feminino.setText(String.valueOf(qtdFeminino));
                    tv_qtd_lgbt.setText(String.valueOf(qtdLGBT));
                    tv_qtd_com_comp.setText(String.valueOf(qtdComCompanheiro));
                    tv_qtd_sem_comp.setText(String.valueOf(qtdSemCompanheiro));
                    tv_media_semestre_estudam.setText(String.valueOf(mediaSemestres));
                    tv_qtd_com_sofrimento.setText(String.valueOf(qtdComSofrimentoMental));
                    tv_qtd_sem_sofrimento.setText(String.valueOf(qtdSemSofrimentoMental));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void consultarSofrimentoMental() {
        refQuestSQR20.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultadosQuestSQR20.clear();
                int qtdComSofrimento = 0;
                int qtdSemSofrimento = 0;

                try {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        for (DataSnapshot d : dados.getChildren())
                            resultadosQuestSQR20.add(d.getValue(Pergunta.class));

                        if (temSofrimentoMental(resultadosQuestSQR20))
                            qtdComSofrimento++;
                        else qtdSemSofrimento++;
                    }

                    tv_qtd_com_sofrimento.setText(String.valueOf(qtdComSofrimento));
                    tv_qtd_sem_sofrimento.setText(String.valueOf(qtdSemSofrimento));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void consultarAnsiedade() {
        refQuestAnsiedade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int qtdComAnsiedade = 0;
                int qtdAnsiedadeLeve = 0;
                int qtdAnsiedadeModerada = 0;
                int qtdAnsiedadeGrave = 0;

                try {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        resultadosQuestAnsiedade.clear();

                        for (DataSnapshot d : dados.getChildren())
                            resultadosQuestAnsiedade.add(d.getValue(PerguntaAnsiedade.class));

                        resultadosAnsiedade = verificarResultadosAnsiedade(resultadosQuestAnsiedade);
                        nivelAnsiedade = nivelDeAnsiedade(resultadosAnsiedade);
                        if (nivelAnsiedade.equals("Ansiedade Leve")) qtdAnsiedadeLeve++;
                        if (nivelAnsiedade.equals("Ansiedade Moderada")) qtdAnsiedadeModerada++;
                        if (nivelAnsiedade.equals("Ansiedade Grave")) qtdAnsiedadeGrave++;

                        boolean temAnsiedade = resultadosAnsiedade > 8;
                        if (temAnsiedade) qtdComAnsiedade++;
                    }

                    tv_qtd_com_ansiedade.setText(String.valueOf(qtdComAnsiedade));
                    tv_qtd_ansiedade_leve.setText(String.valueOf(qtdAnsiedadeLeve));
                    tv_qtd_ansiedade_moderada.setText(String.valueOf(qtdAnsiedadeModerada));
                    tv_qtd_ansiedade_grave.setText(String.valueOf(qtdAnsiedadeGrave));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void consultarDepressao() {
        refQuestDepressao.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int qtdComAnsiedade = 0;
                int qtdDepressaoLeve = 0;
                int qtdDepressaoModerada = 0;
                int qtdDepressaoGrave = 0;

                try {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        resultadosQuestDepressao.clear();

                        for (DataSnapshot d : dados.getChildren())
                            resultadosQuestDepressao.add(d.getValue(PerguntaDepressaoCat.class));

                        resultadosDepressao = verificarResultadosDepressao(resultadosQuestDepressao);
                        nivelDepressao = nivelDeDepressao(resultadosDepressao);

                        if (nivelDepressao.equals("Depressão Leve")) qtdDepressaoLeve++;
                        if (nivelDepressao.equals("Depressão Moderada")) qtdDepressaoModerada++;
                        if (nivelDepressao.equals("Depressão Grave")) qtdDepressaoGrave++;

                        boolean temDepressao = resultadosDepressao > 10;
                        if (temDepressao) qtdComAnsiedade++;
                    }

                    tv_qtd_com_depressao.setText(String.valueOf(qtdComAnsiedade));
                    tv_qtd_depressao_leve.setText(String.valueOf(qtdDepressaoLeve));
                    tv_qtd_depressao_moderada.setText(String.valueOf(qtdDepressaoModerada));
                    tv_qtd_depressao_grave.setText(String.valueOf(qtdDepressaoGrave));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void consultarSindromeBurnout() {
        refQuestSindromeB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int qtdComSindrome = 0;

                try {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        resultadosQuestSindromeBurnout.clear();

                        for (DataSnapshot d : dados.getChildren())
                            resultadosQuestSindromeBurnout.add(d.getValue(PerguntaBurnout.class));

                        resultadosFinais = verificarResultadosSindrome(resultadosQuestSindromeBurnout);

                        if ((Objects.requireNonNull(resultadosFinais.get("exaustaoEmocional")) > 3
                                && Objects.requireNonNull(resultadosFinais.get("descreca")) > 3
                                && Objects.requireNonNull(resultadosFinais.get("eficaciaProfissional")) < 2))
                            qtdComSindrome++;
                    }

                    tv_qtd_com_sindromeb.setText(String.valueOf(qtdComSindrome));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    /***
     * VERIFICA O NÍVEL DE ANSIEDADE
     * @param qtd
     */
    private String nivelDeAnsiedade(int qtd) {
        Log.i("@NIVEL ANSIEDADE", String.valueOf(qtd));

        if (qtd >= 0 && qtd <= 8) return "Ansiedade ausente";
        else if (qtd >= 8 && qtd <= 15) return "Ansiedade Leve";
        else if (qtd >= 16 && qtd <= 25) return "Ansiedade Moderada";
        else if (qtd >= 25 && qtd <= 63) return "Ansiedade Grave";
        else return "Erro";
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS
     * @param resultadosQuestAnsiedade
     */
    private int verificarResultadosAnsiedade(List<PerguntaAnsiedade> resultadosQuestAnsiedade) {
        int resultado = 0;

        for (int i = 0; i < resultadosQuestAnsiedade.size(); i++) {
            if (resultadosQuestAnsiedade.get(i).isMarcada())
                resultado = resultado + resultadosQuestAnsiedade.get(i).getResposta();
        }

        return resultado;
    }

    /***
     * VERIFICA O NÍVEL DE DEPRESSAO
     * @param qtd
     */
    private String nivelDeDepressao(int qtd) {
        Log.i("@NIVEL DEPRESSAO", String.valueOf(qtd));

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
    private int verificarResultadosDepressao(List<PerguntaDepressaoCat> resultadosQuestDepressao) {
        int resultado = 0;

        for (PerguntaDepressaoCat pdc : resultadosQuestDepressao) {
            for (int i = 0; i < pdc.getPerguntasDeDepressao().size(); i++) {
                if (pdc.getPerguntasDeDepressao().get(i).isMarcada())
                    resultado = resultado + pdc.getPerguntasDeDepressao().get(i).getResposta();
            }
        }

        return resultado;
    }

    /***
     * VERIFICA A QUANTIDADE DE RESPOSTAS
     * @param resultadosQuestSindromeBurnout
     * @return
     */
    private HashMap<String, Float> verificarResultadosSindrome(List<PerguntaBurnout> resultadosQuestSindromeBurnout) {
        int resultado = 0;

        HashMap<String, Float> resultados = new HashMap<>();

        for (int i = 0; i < resultadosQuestSindromeBurnout.size(); i++) {
            if (resultadosQuestSindromeBurnout.get(i).isMarcada())
                resultado = resultado + resultadosQuestSindromeBurnout.get(i).getResposta();
        }

        resultados.put("exaustaoEmocional", calcularExaustaoEmocional(resultadosQuestSindromeBurnout));
        resultados.put("descreca", calcularDescrenca(resultadosQuestSindromeBurnout));
        resultados.put("eficaciaProfissional", calcularEficaciaProfissional(resultadosQuestSindromeBurnout));

        Log.i("#SB EXAUSTÃO EMOCIONAL", Objects.requireNonNull(resultados.get("exaustaoEmocional")) > 3 ? "POSSUE EXAUSTÃO EMOCIONAL" : "NORMAL");
        Log.i("#SB DESCRENÇA", Objects.requireNonNull(resultados.get("descreca")) > 3 ? "POSSUE DESCRENÇA" : "NORMAL");
        Log.i("#SB EFICÁCIA PROFISSIONAL", Objects.requireNonNull(resultados.get("eficaciaProfissional")) < 2 ? "NÃO É EFICIENTE PROFISSIONALMENTE" : "NORMAL");

        return resultados;
    }

    private float calcularExaustaoEmocional(List<PerguntaBurnout> lista) {
        float resultadoExaustaoEmocional = 0;
        int qtdItens = 1;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isMarcada() && lista.get(i).getCategoriaPergunta().equals("exaustao_emocional")) {
                resultadoExaustaoEmocional = resultadoExaustaoEmocional + lista.get(i).getResposta();
                qtdItens++;
            }
        }

        return resultadoExaustaoEmocional / qtdItens;
    }

    private float calcularDescrenca(List<PerguntaBurnout> lista) {
        float resultadoDescrenca = 0;
        int qtdItens = 1;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isMarcada() && lista.get(i).getCategoriaPergunta().equals("descrenca")) {
                resultadoDescrenca = resultadoDescrenca + lista.get(i).getResposta();
                qtdItens++;
            }
        }

        return resultadoDescrenca / qtdItens;
    }

    private float calcularEficaciaProfissional(List<PerguntaBurnout> lista) {
        float resultadoEficaciaProfissional = 0;
        int qtdItens = 1;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).isMarcada() && lista.get(i).getCategoriaPergunta().equals("eficacia_profissional")) {
                resultadoEficaciaProfissional = resultadoEficaciaProfissional + lista.get(i).getResposta();
                qtdItens++;
            }
        }

        return resultadoEficaciaProfissional / qtdItens;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_sair) {
            deslogarUsuario();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
