package med.mental.mentalmed.model;

import java.io.Serializable;
import java.util.List;

public class PerguntaDepressaoCat implements Serializable {

    private Long id;
    private String descricao;
    private List<PerguntaDepressao> perguntasDeDepressao;

    public PerguntaDepressaoCat() {

    }

    @Override
    public String toString() {
        return id + " " + descricao + " RESPOSTAS: {" + perguntasDeDepressao.toString() + "}";
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

    public List<PerguntaDepressao> getPerguntasDeDepressao() {
        return perguntasDeDepressao;
    }

    public void setPerguntasDeDepressao(List<PerguntaDepressao> perguntasDeDepressao) {
        this.perguntasDeDepressao = perguntasDeDepressao;
    }
}
