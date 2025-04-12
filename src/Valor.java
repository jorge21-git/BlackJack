public enum Valor {
    A("A"),
    DOS("2"),
    TRES("3"),
    CUATRO("4"),
    CINCO("5"),
    SEIS("6"),
    SIETE("7"),
    OCHO("8"),
    NUEVE("9"),
    DIEZ("10"),
    J("J"),
    Q("Q"),
    K("K");

    private final String valor;

    Valor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
