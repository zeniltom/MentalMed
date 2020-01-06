package med.mental.mentalmed.model;

import java.io.Serializable;

public class Pergunta implements Serializable {

    private Long id;
    private String descricao;
    private boolean marcada = false;
    private boolean resposta;

    public Pergunta() {

    }

    @Override
    public String toString() {
        String r = resposta ? " SIM " : " NÃO ";
        String marcou = marcada ? "" : " - NÃO MARCOU";

        return descricao + " R: " + r + marcou;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isResposta() {
        return resposta;
    }

    public void setResposta(boolean resposta) {
        this.resposta = resposta;
    }
}
