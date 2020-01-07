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
import med.mental.mentalmed.model.Pergunta;


public class PerguntaAdapter extends BaseAdapter {

    private final List<Pergunta> questoes;
    private final LayoutInflater inflater;
    public static ArrayList<Pergunta> resultados;

    public PerguntaAdapter(Context applicationContext, List<Pergunta> questionsList) {
        this.questoes = questionsList;
        resultados = new ArrayList<>();

        for (int i = 0; i < questionsList.size(); i++) {
            Pergunta p;
            p = questionsList.get(i);
            resultados.add(p);
        }

        inflater = (LayoutInflater.from(applicationContext));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_pergunta, null);

        final Pergunta pergunta = questoes.get(position);

        TextView descricao = view.findViewById(R.id.descricao);
        RadioGroup radioGroup = view.findViewById(R.id.radio_group_pergunta);
        RadioButton radio_sim = view.findViewById(R.id.radio_sim);
        RadioButton radio_nao = view.findViewById(R.id.radio_nao);

        descricao.setText(pergunta.getDescricao());

        radio_sim.setChecked(pergunta.isMarcada() && pergunta.isResposta());
        radio_nao.setChecked(pergunta.isMarcada() && !pergunta.isResposta());

        //BLOQUEAR RESPOSTAS JÁ RESPONDIDAS NO SQR20
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View radio = radioGroup.getChildAt(i);
            if (pergunta.isMarcada()) {
                descricao.setTextColor(Color.LTGRAY);
                radio.setEnabled(false);
            }
        }

        radio_sim.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pergunta.setResposta(true);
                pergunta.setMarcada(true);
                resultados.set(position, pergunta);
            }
        });

        radio_nao.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pergunta.setResposta(false);
                pergunta.setMarcada(true);
                resultados.set(position, pergunta);
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