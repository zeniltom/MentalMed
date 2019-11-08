package med.mental.mentalmed.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Criado por Zenilton on 05/11/2019.
 */
public class DataBaseUtil extends SQLiteOpenHelper {

    //NOME DA BASE DE DADOS
    private static final String NOME_BASE_DE_DADOS = "MENTAL_MED.db";

    //VERSÃO DO BANCO DE DADOS
    private static final int VERSAO_BASE_DE_DADOS = 1;

    //CONSTRUTOR
    public DataBaseUtil(Context context) {

        super(context, NOME_BASE_DE_DADOS, null, VERSAO_BASE_DE_DADOS);
    }

    /*CRIAR A TABELAS */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String criarTabelaCatDepresPer = "CREATE TABLE tb_cat_depress_perg (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL)";
        db.execSQL(criarTabelaCatDepresPer);

        String criarTabelaDepresPer = "CREATE TABLE tb_depress_perg (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT NOT NULL, " +
                "resposta INTEGER (10), " +
                "cat_perg_depress_id INTEGER NOT NULL, " +
                "FOREIGN KEY (cat_perg_depress_id) REFERENCES tb_cat_depress_perg (id))";
        db.execSQL(criarTabelaDepresPer);

        db.execSQL(inserirCategoriasPerguntasDepressao());
        db.execSQL(inserirPerguntasDepressao());
    }

    private String inserirCategoriasPerguntasDepressao() {
        return "INSERT INTO tb_cat_depress_perg ( descricao, id ) VALUES ( '1.', 1 ), ( '2.', 2 ), ( '3.', 3 ), ( '4.', 4 ), ( '5.', 5 ), ( '6.', 6 ), ( '7.', 7 ), ( '8.', 8 ), ( '9.', 9 ), ( '10.', 10 ), ( '11.', 11 ), ( '12.', 12 ), ( '13.', 13 ), ( '14.', 14 ), ( '15.', 15 ), ( '16.', 16 ), ( '17.', 17 ), ( '18.', 18 ), ( '19.', 19 ), ( '20.', 20 ), ( '21.', 21 );";
    }

    private String inserirPerguntasDepressao() {
        return "INSERT INTO tb_depress_perg (resposta, cat_perg_depress_id, descricao, id) VALUES " +
                "  (0, 1, '0 = não me sinto triste', 1)," +
                "  (1, 1, '1 = sinto-me triste', 2)," +
                "  (2, 1, '2 = sinto-me triste o tempo todo e não consigo sair disto', 3)," +
                "  (3, 1, '3 = estou tão triste e infeliz que não posso aguentar', 4)," +
                "  (0, 2, '0 = não estou particularmente desencorajado(a) frente ao futuro', 5)," +
                "  (1, 2, '1 = sinto-me desencorajado(a) frente ao futuro', 6)," +
                "  (2, 2, '2 = sinto que não tenho nada por que esperar', 7)," +
                "  (3, 2, '3 = sinto que o futuro é sem esperança e que as coisas não vão melhorar', 8)," +
                "  (0, 3, '0 = não me sinto fracassado(a)', 9)," +
                "  (1, 3, '1 = sinto que falhei mais do que um indivíduo médio', 10)," +
                "  (2, 3, '2 = quando olho para trás em minha vida, só vejo uma porção de fracassos', 11)," +
                "  (3, 3, '3 = sinto que sou um fracasso completo como pessoa', 12)," +
                "  (0, 4, '0 = não obtenho tanta satisfação com as coisas como costumava fazer', 13)," +
                "  (1, 4, '1 = não gosto das coisas da maneira como costumava gostar', 14)," +
                "  (2, 4, '2 = não consigo mais sentir satisfação real com coisa alguma', 15)," +
                "  (3, 4, '3 = estou insatisfeito(a) ou entediado(a) com tudo', 16)," +
                "  (0, 5, '0 = não me sinto particularmente culpado(a)', 17)," +
                "  (1, 5, '1 = sinto-me culpado(a) boa parte do tempo', 18)," +
                "  (2, 5, '2 = sinto-me muito culpado(a) a maior parte do tempo', 19)," +
                "  (3, 5, '3 = sinto-me culpado(a) o tempo todo', 20)," +
                "  (0, 6, '0 = não sinto que esteja sendo punido(a)', 21)," +
                "  (1, 6, '1 = sinto que posso ser punido(a)', 22)," +
                "  (2, 6, '2 = espero ser punido(a)', 23)," +
                "  (3, 6, '3 = sinto que estou sendo punido(a)', 24)," +
                "  (0, 7, '0 = não me sinto desapontad❑(a) comigo mesmo(a)', 25)," +
                "  (1, 7, '1 = sinto-me desapontado(a) comigo mesmo(a)', 26)," +
                "  (2, 7, '2 = sinto-me aborrecido(a) comigo mesmo(a)', 27)," +
                "  (3, 7, '3 = eu me odeio', 28)," +
                "  (0, 8, '8. 0 = não sinto que seja pior que qualquer pessoa', 29)," +
                "  (1, 8, '1 = critico minhas fraquezas ou erros', 30)," +
                "  (2, 8, '2 = responsabilizo-me ❑ tempo todo por minhas falhas', 31)," +
                "  (3, 8, '3 = culpo-me por todas as coisas ruins que acontecem', 32)," +
                "  (0, 9, '0 = não tenho nenhum pensamento a respeito de me matar', 33)," +
                "  (1, 9, '1 = tenho pensamentos a respeito de me matar mas não os levaria adiante', 34)," +
                "  (2, 9, '2 = gostaria de me matar', 35)," +
                "  (3, 9, '3 = eu me mataria se tivesse uma oportunidade', 36)," +
                "  (0, 10, '0 = não costumo chorar mais do que o habitual', 37)," +
                "  (1, 10, '1 = choro mais agora do que costumava chorar antes', 38)," +
                "  (2, 10, '2 = atualmente choro o tempo todo', 39)," +
                "  (3, 10, '3 = eu costumava chorar, mas agora não consigo mesmo que queira', 40)," +
                "  (0, 11, '0 = não me irrito mais agora do que em qualquer outra época', 41)," +
                "  (1, 11, '1 = fico molestado(a) ou irritado(a) mais facilmente do que costumava', 42)," +
                "  (2, 11, '2 = atualmente sinto-me irritado(a) o tempo todo', 43)," +
                "  (3, 11, '3 = absolutamente não me irrito com as coisas que costumam irritar-me', 44)," +
                "  (0, 12, '0 = não perdi o Interesse nas outras pessoas', 45)," +
                "  (1, 12, '1 = interesso-me menos do que costumava pelas outras pessoas', 46)," +
                "  (2, 12, '2 = perdi a maior parle do meu interesse pelas outras pessoas', 47)," +
                "  (3, 12, '3 = perdi todo o meu interesse nas outras pessoas', 48)," +
                "  (0, 13, '0 = tomo as decisões quase tão bem como em qualquer outra época', 49)," +
                "  (1, 13, '1 = adio minhas decisões mais do que costumava', 50)," +
                "  (2, 13, '2 = tenho maior dificuldade em tomar decisões do que antes', 51)," +
                "  (3, 13, '3 = não consigo mais tomar decisões', 52)," +
                "  (0, 14, '0 = não sinto que minha aparência seja pior do que costumava ser', 53)," +
                "  (1, 14, '1 = preocupo-me por estar parecendo velho(a) ou sem atrativos', 54)," +
                "  (2, 14, '2 = sinto que há mudanças em minha aparência que me fazem parecer sem atrativos', 55)," +
                "  (3, 14, '3 considero-me feio(a)', 56)," +
                "  (0, 15, '15. O posso trabalhar mais ou meros tão bem quanto antes', 57)," +
                "  (1, 15, '1 = preciso de um esforço extra para começar qualquer coisa', 58)," +
                "  (2, 15, '2 = tenho que me esforçar muito até fazer qualquer coisa', 59)," +
                "  (3, 15, '3 = não consigo fazer trabalho nenhum', 60)," +
                "  (0, 16, '0 = durmo tão bem quanto de habito', 61)," +
                "  (1, 16, '1 = não durmo tão bem quanto costumava', 62)," +
                "  (2, 16, '2 = acordo 1 ou 2 horas mais cedo do que de hábito e tenho dificuldade de voltar a dormir', 63)," +
                "  (3, 16, '3 = acordo várias horas mais cedo do que costumava e tenho dificuldade de voltar a dormir', 64)," +
                "  (0, 17, '0 = não fico mais cansado(a) do que de hábito', 65)," +
                "  (1, 17, '1 = fico cansado(a) com mais facilidade do que costumava', 66)," +
                "  (2, 17, '2 = sinto-me cansado(a) ao fazer qualquer coisa', 67)," +
                "  (3, 17, '3 = estou cansado(a) demais para fazer qualquer coisa', 68)," +
                "  (0, 18, '0 = o meu apetite não está pior do que de hábito', 69)," +
                "  (1, 18, '1 = meu apetite não é tão bom como costumava ser', 70)," +
                "  (2, 18, '2 = meu apetite está muito pior agora', 71)," +
                "  (3, 18, '3 = não tenho mais nenhum apetite', 72)," +
                "  (0, 19, '0 = não perdi muito peso se é que perdi algum ultimamente', 73)," +
                "  (1, 19, '1 = perdi mais de 2,5 kg estou deliberadamente', 74)," +
                "  (2, 19, '2 = perdi mais de 5,0 kg tentando perder peso', 75)," +
                "  (3, 19, '3 = perdi mais de 7,0 kg comendo menos', 76)," +
                "  (0, 20, '0 = não me preocupo mais do que de hábito com minha saúde', 77)," +
                "  (1, 20, '1 = preocupo-me com problemas físicos como dores e aflições, ou perturbações no estômago, ou prisões de ventre', 78)," +
                "  (2, 20, '2 = estou preocupado(a) com problemas físicos e é difícil pensar em muito mais do que isso', 79)," +
                "  (3, 20, '3 = estou tão preocupado(a) em ter problemas físicos que não consigo pensar em outra coisa', 80)," +
                "  (0, 21, '0 = não tenho observado qualquer mudança recente em meu interesse sexual', 81)," +
                "  (1, 21, '1 = estou menos interessado(a) por sexo do que acostumava', 82)," +
                "  (2, 21, '2 = estou bem menos interessado(a) por sexo atualmente', 83)," +
                "  (3, 21, '3 = perdi completamente o interesse por sexo', 84);";
    }

    /*SE TROCAR A VERSÃO DO BANCO DE DADOS, PODE EXECUTAR ALGUMA ROTINA COMO CRIAR COLUNAS, EXCLUIR ENTRE OUTRAS */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tb_depress_perg");
        db.execSQL("DROP TABLE IF EXISTS tb_cat_depress_perg");
        onCreate(db);
    }

    /*EXECUTA AS ROTINAS NO BANCO DE DADOS*/
    public SQLiteDatabase GetConexaoDataBase() {
        return this.getWritableDatabase();
    }
}