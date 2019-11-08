package med.mental.mentalmed.model;

import java.io.Serializable;

public class PerguntaAnsiedade implements Serializable {

    private String descricao;
    private boolean marcada = false;
    private int resposta = 0;

    public PerguntaAnsiedade() {

    }

    @Override
    public String toString() {
        String marcou = marcada ? "" : " - NÃO MARCOU";

        return descricao + " R: (" + resposta + ")" + marcou;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isMarcada() {
        return marcada;
    }

    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }

    public int getResposta() {
        return resposta;
    }

    public void setResposta(int resposta) {
        this.resposta = resposta;
    }
}
