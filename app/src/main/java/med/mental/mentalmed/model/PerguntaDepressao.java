package med.mental.mentalmed.model;

import java.io.Serializable;

public class PerguntaDepressao implements Serializable {

    private int id;
    private String descricao;
    private boolean marcada = false;
    private int catPergDepressId;
    private int resposta;

    public PerguntaDepressao() {

    }

    @Override
    public String toString() {
        String marcou = marcada ? " OK" : " - NÃO MARCOU";

        return id + " - " + descricao + " - CATEGORIA: " + catPergDepressId + " " + marcou + " R: " + resposta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCatPergDepressId() {
        return catPergDepressId;
    }

    public void setCatPergDepressId(int catPergDepressId) {
        this.catPergDepressId = catPergDepressId;
    }

    public int getResposta() {
        return resposta;
    }

    public void setResposta(int resposta) {
        this.resposta = resposta;
    }
}
