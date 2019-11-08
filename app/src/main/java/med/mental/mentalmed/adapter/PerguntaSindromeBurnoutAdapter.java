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
import med.mental.mentalmed.model.PerguntaBurnout;


public class PerguntaSindromeBurnoutAdapter extends BaseAdapter {

    private final List<PerguntaBurnout> questoes;
    private final LayoutInflater inflater;
    public static ArrayList<PerguntaBurnout> resultados;

    public PerguntaSindromeBurnoutAdapter(Context applicationContext, List<PerguntaBurnout> questionsList) {
        this.questoes = questionsList;
        resultados = new ArrayList<>();

        for (int i = 0; i < questionsList.size(); i++) {
            PerguntaBurnout p;

            p = questionsList.get(i);

            resultados.add(p);
        }

        inflater = (LayoutInflater.from(applicationContext));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_pergunta_sindrome, null);

        final PerguntaBurnout perguntaBurnout = questoes.get(i);

        RadioGroup radio_group = view.findViewById(R.id.radio_group);
        TextView descricao = view.findViewById(R.id.descricao);
        RadioButton radio_0 = view.findViewById(R.id.radio_0);
        RadioButton radio_1 = view.findViewById(R.id.radio_1);
        RadioButton radio_2 = view.findViewById(R.id.radio_2);
        RadioButton radio_3 = view.findViewById(R.id.radio_3);
        RadioButton radio_4 = view.findViewById(R.id.radio_4);
        RadioButton radio_5 = view.findViewById(R.id.radio_5);
        RadioButton radio_6 = view.findViewById(R.id.radio_6);

        descricao.setText(perguntaBurnout.getDescricao());

        //MANTER ESTADO DOS ITENS
        radio_0.setChecked(perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == 0);
        radio_1.setChecked(perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == 1);
        radio_2.setChecked(perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == 2);
        radio_3.setChecked(perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == 3);
        radio_4.setChecked(perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == 4);
        radio_5.setChecked(perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == 5);
        radio_6.setChecked(perguntaBurnout.isMarcada() && perguntaBurnout.getResposta() == 6);

        //MOSTRAR CATEGORIA DA PERGUNTA
        if (i == 0 || i == 6 || i == 11) {
            radio_group.setVisibility(View.GONE);
            descricao.setTextSize(22);
            perguntaBurnout.setMarcada(false);
            perguntaBurnout.setResposta(99);
        }

        radio_0.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                perguntaBurnout.setResposta(0);
                perguntaBurnout.setMarcada(true);
                resultados.set(i, perguntaBurnout);
            }
        });

        radio_1.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                perguntaBurnout.setResposta(1);
                perguntaBurnout.setMarcada(true);
                resultados.set(i, perguntaBurnout);
            }
        });

        radio_2.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                perguntaBurnout.setResposta(2);
                perguntaBurnout.setMarcada(true);
                resultados.set(i, perguntaBurnout);
            }
        });

        radio_3.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                perguntaBurnout.setResposta(3);
                perguntaBurnout.setMarcada(true);
                resultados.set(i, perguntaBurnout);
            }
        });

        radio_4.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                perguntaBurnout.setResposta(4);
                perguntaBurnout.setMarcada(true);
                resultados.set(i, perguntaBurnout);
            }
        });
        radio_5.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                perguntaBurnout.setResposta(5);
                perguntaBurnout.setMarcada(true);
                resultados.set(i, perguntaBurnout);
            }
        });
        radio_6.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                perguntaBurnout.setResposta(6);
                perguntaBurnout.setMarcada(true);
                resultados.set(i, perguntaBurnout);
            }
        });

        return view;
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