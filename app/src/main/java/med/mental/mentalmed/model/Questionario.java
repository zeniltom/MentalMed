package med.mental.mentalmed.model;

import java.io.Serializable;

public class Questionario implements Serializable {

    private String id;

    private int idade;
    private int semestreInicioGraduacao;
    private int periodoAtual;

    private float rendaFamiliar;
    private float horasEstudoDiarios;
    private float horasLazerSemanalmente;

    private ENGenero genero;
    private ENSexo sexo;
    private ENMoradia moradia;
    private ENRaca raca;

    private boolean temFilhos;
    private boolean situacaoConjugal;
    private boolean estudaETrabalha;
    private boolean temReligiao;
    private boolean participaAtividadeAcademica;
    private boolean estudaFimDeSemana;
    private boolean fuma;
    private boolean consomeBebibaAlcoolica;
    private boolean consomeDrogasIlicitas;
    private boolean praticaAtividadeFisica;
    private boolean recebeAcompanhamentoPsicologico;
    private boolean temNecessidadeAcompanhamentoPsicologico;
    private boolean usaMedicamentoPrescrito;

    public Questionario() {

    }

    @Override
    public String toString() {
        return "QUESTIONÁRIO {" +
                "\n id = " + id +
                "\n idade = " + idade +
                "\n semestreInicioGraduacao = " + semestreInicioGraduacao +
                "\n periodoAtual = " + periodoAtual +
                "\n rendaFamiliar = " + rendaFamiliar +
                "\n horasEstudoDiarios = " + horasEstudoDiarios +
                "\n horasLazerSemanalmente = " + horasLazerSemanalmente +
                "\n genero = " + genero +
                "\n sexo = " + sexo +
                "\n moradia = " + moradia +
                "\n raca = " + raca +
                "\n temFilhos = " + temFilhos +
                "\n situacaoConjugal = " + situacaoConjugal +
                "\n estudaETrabalha = " + estudaETrabalha +
                "\n temReligiao = " + temReligiao +
                "\n participaAtividadeAcademica = " + participaAtividadeAcademica +
                "\n estudaFimDeSemana = " + estudaFimDeSemana +
                "\n fuma = " + fuma +
                "\n consomeBebibaAlcoolica = " + consomeBebibaAlcoolica +
                "\n consomeDrogasIlicitas = " + consomeDrogasIlicitas +
                "\n praticaAtividadeFisica = " + praticaAtividadeFisica +
                "\n recebeAcompanhamentoPsicologico = " + recebeAcompanhamentoPsicologico +
                "\n temNecessidadeAcompanhamentoPsicologico = " + temNecessidadeAcompanhamentoPsicologico +
                "\n usaMedicamentoPrescrito = " + usaMedicamentoPrescrito + " }";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getSemestreInicioGraduacao() {
        return semestreInicioGraduacao;
    }

    public void setSemestreInicioGraduacao(int semestreInicioGraduacao) {
        this.semestreInicioGraduacao = semestreInicioGraduacao;
    }

    public int getPeriodoAtual() {
        return periodoAtual;
    }

    public void setPeriodoAtual(int periodoAtual) {
        this.periodoAtual = periodoAtual;
    }

    public float getRendaFamiliar() {
        return rendaFamiliar;
    }

    public void setRendaFamiliar(float rendaFamiliar) {
        this.rendaFamiliar = rendaFamiliar;
    }

    public float getHorasEstudoDiarios() {
        return horasEstudoDiarios;
    }

    public void setHorasEstudoDiarios(float horasEstudoDiarios) {
        this.horasEstudoDiarios = horasEstudoDiarios;
    }

    public float getHorasLazerSemanalmente() {
        return horasLazerSemanalmente;
    }

    public void setHorasLazerSemanalmente(float horasLazerSemanalmente) {
        this.horasLazerSemanalmente = horasLazerSemanalmente;
    }

    public ENGenero getGenero() {
        return genero;
    }

    public void setGenero(ENGenero genero) {
        this.genero = genero;
    }

    public ENSexo getSexo() {
        return sexo;
    }

    public void setSexo(ENSexo sexo) {
        this.sexo = sexo;
    }

    public ENMoradia getMoradia() {
        return moradia;
    }

    public void setMoradia(ENMoradia moradia) {
        this.moradia = moradia;
    }

    public ENRaca getRaca() {
        return raca;
    }

    public void setRaca(ENRaca raca) {
        this.raca = raca;
    }

    public boolean isTemFilhos() {
        return temFilhos;
    }

    public void setTemFilhos(boolean temFilhos) {
        this.temFilhos = temFilhos;
    }

    public boolean isSituacaoConjugal() {
        return situacaoConjugal;
    }

    public void setSituacaoConjugal(boolean situacaoConjugal) {
        this.situacaoConjugal = situacaoConjugal;
    }

    public boolean isEstudaETrabalha() {
        return estudaETrabalha;
    }

    public void setEstudaETrabalha(boolean estudaETrabalha) {
        this.estudaETrabalha = estudaETrabalha;
    }

    public boolean isTemReligiao() {
        return temReligiao;
    }

    public void setTemReligiao(boolean temReligiao) {
        this.temReligiao = temReligiao;
    }

    public boolean isParticipaAtividadeAcademica() {
        return participaAtividadeAcademica;
    }

    public void setParticipaAtividadeAcademica(boolean participaAtividadeAcademica) {
        this.participaAtividadeAcademica = participaAtividadeAcademica;
    }

    public boolean isEstudaFimDeSemana() {
        return estudaFimDeSemana;
    }

    public void setEstudaFimDeSemana(boolean estudaFimDeSemana) {
        this.estudaFimDeSemana = estudaFimDeSemana;
    }

    public boolean isFuma() {
        return fuma;
    }

    public void setFuma(boolean fuma) {
        this.fuma = fuma;
    }

    public boolean isConsomeBebibaAlcoolica() {
        return consomeBebibaAlcoolica;
    }

    public void setConsomeBebibaAlcoolica(boolean consomeBebibaAlcoolica) {
        this.consomeBebibaAlcoolica = consomeBebibaAlcoolica;
    }

    public boolean isConsomeDrogasIlicitas() {
        return consomeDrogasIlicitas;
    }

    public void setConsomeDrogasIlicitas(boolean consomeDrogasIlicitas) {
        this.consomeDrogasIlicitas = consomeDrogasIlicitas;
    }

    public boolean isPraticaAtividadeFisica() {
        return praticaAtividadeFisica;
    }

    public void setPraticaAtividadeFisica(boolean praticaAtividadeFisica) {
        this.praticaAtividadeFisica = praticaAtividadeFisica;
    }

    public boolean isRecebeAcompanhamentoPsicologico() {
        return recebeAcompanhamentoPsicologico;
    }

    public void setRecebeAcompanhamentoPsicologico(boolean recebeAcompanhamentoPsicologico) {
        this.recebeAcompanhamentoPsicologico = recebeAcompanhamentoPsicologico;
    }

    public boolean isTemNecessidadeAcompanhamentoPsicologico() {
        return temNecessidadeAcompanhamentoPsicologico;
    }

    public void setTemNecessidadeAcompanhamentoPsicologico(boolean temNecessidadeAcompanhamentoPsicologico) {
        this.temNecessidadeAcompanhamentoPsicologico = temNecessidadeAcompanhamentoPsicologico;
    }

    public boolean isUsaMedicamentoPrescrito() {
        return usaMedicamentoPrescrito;
    }

    public void setUsaMedicamentoPrescrito(boolean usaMedicamentoPrescrito) {
        this.usaMedicamentoPrescrito = usaMedicamentoPrescrito;
    }
}
