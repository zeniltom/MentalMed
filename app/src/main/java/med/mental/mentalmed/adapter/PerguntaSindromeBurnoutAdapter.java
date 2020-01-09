package med.mental.mentalmed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import med.mental.mentalmed.model.PerguntaBurnout;


public class PerguntaSindromeBurnoutAdapter extends BaseAdapter {

    private final List<PerguntaBurnout> questoes;
    private final LayoutInflater inflater;
    public static ArrayList<PerguntaBurnout> resultados;
    private Context context;

    public PerguntaSindromeBurnoutAdapter(Context context, List<PerguntaBurnout> questionsList) {
        this.questoes = questionsList;
        this.context = context;
        resultados = new ArrayList<>();

        for (int i = 0; i < questionsList.size(); i++) {
            PerguntaBurnout p;
            p = questionsList.get(i);
            resultados.add(p);
        }

        inflater = (LayoutInflater.from(context));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_pergunta_sindrome, null);

        final PerguntaBurnout perguntaBurnout = questoes.get(position);

        RadioGroup radio_group = view.findViewById(R.id.radio_group);
        TextView descricao = view.findViewById(R.id.descricao);
        TextView categoria_perg_sindrome = view.findViewById(R.id.categoria_perg_sindrome);
        RadioButton radio_0 = view.findViewById(R.id.radio_0);
        RadioButton radio_1 = view.findViewById(R.id.radio_1);
        RadioButton radio_2 = view.findViewById(R.id.radio_2);
        RadioButton radio_3 = view.findViewById(R.id.radio_3);
        RadioButton radio_4 = view.findViewById(R.id.radio_4);
        RadioButton radio_5 = view.findViewById(R.id.radio_5);
        RadioButton radio_6 = view.findViewById(R.id.radio_6);

        descricao.setText(perguntaBurnout.getDescricao());
        categoria_perg_sindrome.setVisibility(View.GONE);

        //MANTER ESTADO DOS ITENS
        radio_0.setChecked(isAltervativaMarcada(perguntaBurnout, 0));
        radio_1.setChecked(isAltervativaMarcada(perguntaBurnout, 1));
        radio_2.setChecked(isAltervativaMarcada(perguntaBurnout, 2));
        radio_3.setChecked(isAltervativaMarcada(perguntaBurnout, 3));
        radio_4.setChecked(isAltervativaMarcada(perguntaBurnout, 4));
        radio_5.setChecked(isAltervativaMarcada(perguntaBurnout, 5));
        radio_6.setChecked(isAltervativaMarcada(perguntaBurnout, 6));

        //BLOQUEAR RESPOSTAS JÁ RESPONDIDAS NO SINDROMEBURNOUT
        for (int i = 0; i < radio_group.getChildCount(); i++) {
            View radio = radio_group.getChildAt(i);
            if (perguntaBurnout.isMarcada()) {
                descricao.setTextColor(Color.GRAY);
                if (perguntaBurnout.getResposta() == 0
                        || perguntaBurnout.getResposta() == 1 || perguntaBurnout.getResposta() == 2
                        || perguntaBurnout.getResposta() == 3 || perguntaBurnout.getResposta() == 4
                        || perguntaBurnout.getResposta() == 5 || perguntaBurnout.getResposta() == 6)
                    radio.setEnabled(false);
            }
        }

        String categoria = perguntaBurnout.getCategoriaPergunta();

        //MOSTRAR CATEGORIA DA PERGUNTA
        if ((categoria.equalsIgnoreCase("exaustao_emocional") && perguntaBurnout.getId() == 1)
                || (categoria.equalsIgnoreCase("descrenca") && perguntaBurnout.getId() == 6)
                || (categoria.equalsIgnoreCase("eficacia_profissional") && perguntaBurnout.getId() == 10)) {
            categoria_perg_sindrome.setVisibility(View.VISIBLE);
            categoria_perg_sindrome.setTextSize(22);

            if (categoria.equalsIgnoreCase("exaustao_emocional"))
                categoria_perg_sindrome.setText(context.getString(R.string.exaustao_emocional));
            else if (categoria.equalsIgnoreCase("descrenca"))
                categoria_perg_sindrome.setText(context.getString(R.string.descrenca));
            else if (categoria.equalsIgnoreCase("eficacia_profissional"))
                categoria_perg_sindrome.setText(context.getString(R.string.eficacia_profissional));
        }

        radio_0.setOnCheckedChangeListener((compoundButton, isChecked) -> marcarAlternativa(position, perguntaBurnout, isChecked, 0));
        radio_1.setOnCheckedChangeListener((compoundButton, isChecked) -> marcarAlternativa(position, perguntaBurnout, isChecked, 1));
        radio_2.setOnCheckedChangeListener((compoundButton, isChecked) -> marcarAlternativa(position, perguntaBurnout, isChecked, 2));
        radio_3.setOnCheckedChangeListener((compoundButton, isChecked) -> marcarAlternativa(position, perguntaBurnout, isChecked, 3));
        radio_4.setOnCheckedChangeListener((compoundButton, isChecked) -> marcarAlternativa(position, perguntaBurnout, isChecked, 4));
        radio_5.setOnCheckedChangeListener((compoundButton, isChecked) -> marcarAlternativa(position, perguntaBurnout, isChecked, 5));
        radio_6.setOnCheckedChangeListener((compoundButton, isChecked) -> marcarAlternativa(position, perguntaBurnout, isChecked, 6));

        return view;
    }

    private boolean isAltervativaMarcada(PerguntaBurnout perguntaBurnout, int i) {
        return perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == i;
    }

    private void marcarAlternativa(int posicao, PerguntaBurnout perguntaBurnout, boolean isChecked, int valor) {
        if (isChecked) {
            perguntaBurnout.setResposta(valor);
            perguntaBurnout.setMarcada(true);
            resultados.set(posicao, perguntaBurnout);
        }
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