package med.mental.mentalmed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.Pergunta;


public class ResultadoAdapter extends BaseAdapter {

    private final List<Pergunta> questoes;
    private final LayoutInflater inflater;
    private final Context context;

    public ResultadoAdapter(Context context, List<Pergunta> questionsList) {
        this.questoes = questionsList;
        this.context = context;

        ArrayList<Pergunta> resultados = new ArrayList<>();

        for (int i = 0; i < questionsList.size(); i++) {
            Pergunta p;
            p = questionsList.get(i);
            resultados.add(p);
        }

        inflater = (LayoutInflater.from(context));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_resultado, null);

        final Pergunta pergunta = questoes.get(i);

        ConstraintLayout itemResultadoId = view.findViewById(R.id.item_resultado_id);
        TextView descricao = view.findViewById(R.id.descricao);
        TextView tvSim = view.findViewById(R.id.tvSim);
        TextView tvNao = view.findViewById(R.id.tvNao);

        descricao.setText(pergunta.getDescricao());

        if (pergunta.isResposta()) {
            tvSim.setVisibility(View.VISIBLE);
            tvNao.setVisibility(View.INVISIBLE);
        } else {
            tvSim.setVisibility(View.INVISIBLE);
            tvNao.setVisibility(View.VISIBLE);
        }

        if (!pergunta.isMarcada()) {
            tvSim.setVisibility(View.INVISIBLE);
            tvNao.setVisibility(View.INVISIBLE);
            itemResultadoId.setBackgroundColor(context.getColor(R.color.naoMarcado));
        }

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