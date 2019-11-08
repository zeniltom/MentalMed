package med.mental.mentalmed.model;

public enum ENRaca {

    SELECINE("Selecione"),
    NEGRO("Negro"),
    BRANCO("Branco"),
    PARDO("Pardo"),
    INDIGENA("Indígena");

    private final String descricao;

    ENRaca(String aState) {
        descricao = aState;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
