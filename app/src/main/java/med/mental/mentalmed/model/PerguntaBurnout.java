package med.mental.mentalmed.model;

import java.io.Serializable;

public class PerguntaBurnout implements Serializable {

    private Long id;
    private String categoriaPergunta;
    private String descricao;
    private boolean marcada = false;
    private int resposta = 0;

    public PerguntaBurnout() {

    }

    @Override
    public String toString() {
        String marcou = marcada ? "" : " - NÃO MARCOU";

        return descricao + " CAT: " + categoriaPergunta + " R: (" + resposta + ")" + marcou;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoriaPergunta() {
        return categoriaPergunta;
    }

    public void setCategoriaPergunta(String categoriaPergunta) {
        this.categoriaPergunta = categoriaPergunta;
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
