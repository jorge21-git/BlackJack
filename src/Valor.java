public enum Valor {
    A("A", 11), // As
    DOS("2", 2),
    TRES("3", 3),
    CUATRO("4", 4),
    CINCO("5", 5),
    SEIS("6", 6),
    SIETE("7", 7),
    OCHO("8", 8),
    NUEVE("9", 9),
    DIEZ("10", 10),
    J("J", 10),  // Jota
    Q("Q", 10),  // Reina
    K("K", 10);  // Rey

    private final String valor;
    private final int valorNumerico;

    Valor(String valor, int valorNumerico) {
        this.valor = valor;
        this.valorNumerico = valorNumerico;
    }

    public String getValor() {
        return valor;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }
}
