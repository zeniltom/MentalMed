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
import med.mental.mentalmed.model.PerguntaAnsiedade;


public class PerguntaAnsiedadeAdapter extends BaseAdapter {

    private final List<PerguntaAnsiedade> questoes;
    private final LayoutInflater inflater;
    public static ArrayList<PerguntaAnsiedade> resultados;

    public PerguntaAnsiedadeAdapter(Context applicationContext, List<PerguntaAnsiedade> questionsList) {
        this.questoes = questionsList;
        resultados = new ArrayList<>();

        for (int i = 0; i < questionsList.size(); i++) {
            PerguntaAnsiedade p;
            p = questionsList.get(i);
            resultados.add(p);
        }

        inflater = (LayoutInflater.from(applicationContext));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_pergunta_ansiedade, null);

        final PerguntaAnsiedade perguntaAnsiedade = questoes.get(position);

        TextView descricao = view.findViewById(R.id.descricao);
        RadioGroup radioGroup = view.findViewById(R.id.radio_group_ansiedade);
        RadioButton radio_0 = view.findViewById(R.id.radio_0);
        RadioButton radio_1 = view.findViewById(R.id.radio_1);
        RadioButton radio_2 = view.findViewById(R.id.radio_2);
        RadioButton radio_4 = view.findViewById(R.id.radio_3);

        descricao.setText(perguntaAnsiedade.getDescricao());

        //MANTER ESTADO DOS ITENS
        radio_0.setChecked(perguntaAnsiedade.isMarcada() && perguntaAnsiedade.getResposta() == 0);
        radio_1.setChecked(perguntaAnsiedade.isMarcada() && perguntaAnsiedade.getResposta() == 1);
        radio_2.setChecked(perguntaAnsiedade.isMarcada() && perguntaAnsiedade.getResposta() == 2);
        radio_4.setChecked(perguntaAnsiedade.isMarcada() && perguntaAnsiedade.getResposta() == 4);

        //BLOQUEAR RESPOSTAS JÁ RESPONDIDAS NO SQR20
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View radio = radioGroup.getChildAt(i);
            if (perguntaAnsiedade.isMarcada()) {
                descricao.setTextColor(Color.GRAY);
                if (perguntaAnsiedade.getResposta() == 0)
                    radio.setEnabled(false);
                else if (perguntaAnsiedade.getResposta() == 1)
                    radio.setEnabled(false);
                else if (perguntaAnsiedade.getResposta() == 2)
                    radio.setEnabled(false);
                else if (perguntaAnsiedade.getResposta() == 4)
                    radio.setEnabled(false);
            }
        }

        radio_0.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                perguntaAnsiedade.setResposta(0);
                perguntaAnsiedade.setMarcada(true);
                resultados.set(position, perguntaAnsiedade);
            }
        });

        radio_1.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                perguntaAnsiedade.setResposta(1);
                perguntaAnsiedade.setMarcada(true);
                resultados.set(position, perguntaAnsiedade);
            }
        });

        radio_2.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                perguntaAnsiedade.setResposta(2);
                perguntaAnsiedade.setMarcada(true);
                resultados.set(position, perguntaAnsiedade);
            }
        });

        radio_4.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                perguntaAnsiedade.setResposta(4);
                perguntaAnsiedade.setMarcada(true);
                resultados.set(position, perguntaAnsiedade);
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