package med.mental.mentalmed.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import med.mental.mentalmed.R;
import med.mental.mentalmed.model.PerguntaDepressao;
import med.mental.mentalmed.model.PerguntaDepressaoCat;
import med.mental.mentalmed.util.DataBaseUtil;

public class Perguntas {


    private final DataBaseUtil databaseUtil;

    public Perguntas(Context context) {
        databaseUtil = new DataBaseUtil(context);
    }

    /***
     * SALVA UM NOVO REGISTRO NA BASE DE DADOS
     * @param perguntaDepressaoCat
     */
    public void salvarPerguntaCat(PerguntaDepressaoCat perguntaDepressaoCat) {

        ContentValues contentValues = new ContentValues();
        /*MONTANDO OS PARAMETROS PARA SEREM SALVOS*/
        contentValues.put("descricao", perguntaDepressaoCat.getDescricao());

        /*EXECUTANDO INSERT DE UM NOVO REGISTRO*/
        databaseUtil.GetConexaoDataBase().insert("tb_cat_depress_perg", null, contentValues);
    }

    public void salvarPergunta(PerguntaDepressao perguntaDepressao) {

        ContentValues contentValues = new ContentValues();
        /*MONTANDO OS PARAMETROS PARA SEREM SALVOS*/
        contentValues.put("descricao", perguntaDepressao.getDescricao());
        contentValues.put("resposta", perguntaDepressao.getResposta());
        contentValues.put("cat_perg_depress_id", perguntaDepressao.getCatPergDepressId());

        /*EXECUTANDO INSERT DE UM NOVO REGISTRO*/
        databaseUtil.GetConexaoDataBase().insert("tb_depress_perg", null, contentValues);
    }

    public List<PerguntaDepressaoCat> todasCategoriasPergDepress() {
        List<PerguntaDepressaoCat> listinha = new ArrayList<>();

        //MONTA A QUERY A SER EXECUTADA
        String stringBuilderQuery = "SELECT * FROM tb_cat_depress_perg";


        //CONSULTANDO OS REGISTROS CADASTRADOS
        Cursor cursor = databaseUtil.GetConexaoDataBase().rawQuery(stringBuilderQuery, null);

        /*POSICIONA O CURSOR NO PRIMEIRO REGISTRO*/
        cursor.moveToFirst();


        PerguntaDepressaoCat perguntaDepressaoCat;

        //REALIZA A LEITURA DOS REGISTROS ENQUANTO NÃO FOR O FIM DO CURSOR
        while (!cursor.isAfterLast()) {

            /* CRIANDO UMA NOVA PESSOAS */
            perguntaDepressaoCat = new PerguntaDepressaoCat();

            //ADICIONANDO OS DADOS DA PESSOA
            perguntaDepressaoCat.setId(cursor.getInt(cursor.getColumnIndex("id")));
            perguntaDepressaoCat.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));

            //ADICIONANDO UMA PESSOA NA LISTA
            listinha.add(perguntaDepressaoCat);

            //VAI PARA O PRÓXIMO REGISTRO
            cursor.moveToNext();
        }

        //RETORNANDO A LISTA DE PESSOAS
        return listinha;
    }

    public List<PerguntaDepressao> perguntaDepressaoPorCat(int id) {
        List<PerguntaDepressao> listinha = new ArrayList<>();

        String stringBuilderQuery = "SELECT * FROM tb_depress_perg WHERE cat_perg_depress_id =" + id + "  ORDER BY cat_perg_depress_id, id";

        Cursor cursor = databaseUtil.GetConexaoDataBase().rawQuery(stringBuilderQuery, null);
        cursor.moveToFirst();

        PerguntaDepressao perguntaDepressao;
        while (!cursor.isAfterLast()) {

            perguntaDepressao = new PerguntaDepressao();
            perguntaDepressao.setId(cursor.getInt(cursor.getColumnIndex("id")));
            perguntaDepressao.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            perguntaDepressao.setResposta(cursor.getInt(cursor.getColumnIndex("resposta")));
            perguntaDepressao.setCatPergDepressId(cursor.getInt(cursor.getColumnIndex("cat_perg_depress_id")));

            listinha.add(perguntaDepressao);
            cursor.moveToNext();
        }

        return listinha;
    }

    public List<PerguntaDepressao> todasPergDepress() {
        List<PerguntaDepressao> listinha = new ArrayList<>();

        String stringBuilderQuery = "SELECT * FROM tb_depress_perg ORDER BY cat_perg_depress_id";

        Cursor cursor = databaseUtil.GetConexaoDataBase().rawQuery(stringBuilderQuery, null);
        cursor.moveToFirst();

        PerguntaDepressao perguntaDepressao;
        while (!cursor.isAfterLast()) {

            perguntaDepressao = new PerguntaDepressao();
            perguntaDepressao.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            perguntaDepressao.setResposta(cursor.getInt(cursor.getColumnIndex("resposta")));
            perguntaDepressao.setCatPergDepressId(cursor.getInt(cursor.getColumnIndex("cat_perg_depress_id")));

            listinha.add(perguntaDepressao);
            cursor.moveToNext();
        }

        return listinha;
    }

    public String[] perguntasSQR20(Context context) {
        return context.getResources().getStringArray(R.array.perguntas);
    }

    public String[] perguntasAnsiedade(Context context) {
        return context.getResources().getStringArray(R.array.perguntas_ansiedade);
    }

    public String[] perguntasDepressaoCat(Context context) {
        return context.getResources().getStringArray(R.array.perguntas_depressao_cat);
    }

    public String[] perguntasDepressao(Context context) {
        return context.getResources().getStringArray(R.array.perguntas_depressao);
    }

    public String[] perguntasSindromeBurnout(Context context) {
        return context.getResources().getStringArray(R.array.perguntas_sindrome_burnout);
    }
}
