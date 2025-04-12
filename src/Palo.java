public enum Palo {
    CORAZON("♥", "\u001B[31m"), // Rojo
    DIAMANTE("♦", "\u001B[31m"), // Rojo
    TREBOL("♣", "\u001B[30m"),   // Negro
    PICA("♠", "\u001B[30m");     // Negro

    private final String simbolo;
    private final String color;

    Palo(String simbolo, String color) {
        this.simbolo = simbolo;
        this.color = color;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getColor() {
        return color;
    }
}




