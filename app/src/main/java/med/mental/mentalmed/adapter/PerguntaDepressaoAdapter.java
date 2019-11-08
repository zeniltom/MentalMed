package med.mental.mentalmed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.PerguntaDepressaoCat;

public class PerguntaDepressaoAdapter extends BaseAdapter {

    private final List<PerguntaDepressaoCat> questoes;
    private final LayoutInflater inflater;
    public static ArrayList<PerguntaDepressao> resultados;

    public PerguntaDepressaoAdapter(Context context, List<PerguntaDepressaoCat> questoes) {
        this.questoes = questoes;
        resultados = new ArrayList<>();

        inflater = (LayoutInflater.from(context));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int posicao, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_pergunta_depressao, null);

        PerguntaDepressaoCat p = questoes.get(posicao);

        TextView categoria_depress = view.findViewById(R.id.categoria_depress);
        RadioGroup radio_group_depressao = view.findViewById(R.id.radio_group_depressao);
        RadioButton radio_0 = view.findViewById(R.id.radio_0);
        RadioButton radio_1 = view.findViewById(R.id.radio_1);
        RadioButton radio_2 = view.findViewById(R.id.radio_2);
        RadioButton radio_3 = view.findViewById(R.id.radio_3);

        categoria_depress.setText(p.getDescricao());

        radio_0.setText(descricaoDaAlternativa(p, 0));
        radio_1.setText(descricaoDaAlternativa(p, 1));
        radio_2.setText(descricaoDaAlternativa(p, 2));
        radio_3.setText(descricaoDaAlternativa(p, 3));

        //COLOCAR OS OBJETOS PERGUNTASDEPRESSAO NOS RADIOS BUTTONS
        radio_0.setTag(p.getPerguntasDeDepressao().get(0));
        radio_1.setTag(p.getPerguntasDeDepressao().get(1));
        radio_2.setTag(p.getPerguntasDeDepressao().get(2));
        radio_3.setTag(p.getPerguntasDeDepressao().get(3));

        //MANTEM O ESTADO DOS ITENS
        radio_0.setChecked(isAltervativaMarcada(radio_0, 0));
        radio_1.setChecked(isAltervativaMarcada(radio_1, 1));
        radio_2.setChecked(isAltervativaMarcada(radio_2, 2));
        radio_3.setChecked(isAltervativaMarcada(radio_3, 3));

        radio_0.setOnCheckedChangeListener((compoundButton, b) -> marcarAlternativa(p, radio_0, 0));
        radio_1.setOnCheckedChangeListener((compoundButton, b) -> marcarAlternativa(p, radio_1, 1));
        radio_2.setOnCheckedChangeListener((compoundButton, b) -> marcarAlternativa(p, radio_2, 2));
        radio_3.setOnCheckedChangeListener((compoundButton, b) -> marcarAlternativa(p, radio_3, 3));

        radio_group_depressao.setOnCheckedChangeListener(this::verificarAlternativaMarcada);

        return view;
    }

    private String descricaoDaAlternativa(PerguntaDepressaoCat p, int posicao) {
        return p.getPerguntasDeDepressao().get(posicao).getDescricao();
    }

    private boolean isAltervativaMarcada(RadioButton radioButton, int posicao) {
        return ((PerguntaDepressao) radioButton.getTag()).isMarcada() && ((PerguntaDepressao) radioButton.getTag()).getResposta() == posicao;
    }

    private void marcarAlternativa(PerguntaDepressaoCat pergunta, RadioButton radioButton, int posicao) {
        pergunta.getPerguntasDeDepressao().get(posicao).setMarcada(true);
        radioButton.setTag(pergunta.getPerguntasDeDepressao().get(posicao));
    }

    private void verificarAlternativaMarcada(RadioGroup radioGroup, int checkedId) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.getId() == checkedId) {
                for (PerguntaDepressao perguntaDepressao : resultados) {
                    if (perguntaDepressao.getCatPergDepressId() == ((PerguntaDepressao) radioButton.getTag()).getCatPergDepressId())
                        removerAlternativa(perguntaDepressao);
                }

                adicionarAlternativa(radioButton);
                return;
            }
        }
    }

    private void removerAlternativa(PerguntaDepressao perguntaDepressao) {
        resultados.remove(perguntaDepressao);
    }

    private void adicionarAlternativa(RadioButton radioButton) {
        resultados.add((PerguntaDepressao) radioButton.getTag());
    }

    @Override
    public int getCount() {
        return questoes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}