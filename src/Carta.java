public class Carta {

private Palo palo;
private Valor valor;
private int valorNumerico;

public Carta(Palo palo, Valor valor) {
    this.palo = palo;
    this.valor = valor;
    this.valorNumerico = valor.getValorNumerico();
}
    public int getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }
    public Palo getPalo() {
    return palo;
}
public void setPalo(Palo palo) {
    this.palo = palo;
}
public Valor getValor() {
    return valor;
}
public void setValor(Valor valor) {
    this.valor = valor;
}

    public String[] dibujarCarta() {
        int altura = 7;
        int ancho = 11;
        String reset = "\u001B[0m"; // Reset del color al valor predeterminado
        String[] lineas = new String[altura]; // Array para guardar las líneas de la carta

        for (int i = 0; i < altura; i++) {
            StringBuilder linea = new StringBuilder(); // Usamos StringBuilder en lugar de una cadena vacía
            for (int j = 0; j < ancho; j++) {
                if (i == 0) {
                    if (j == 0) {
                        linea.append("┌"); // Esquina superior izquierda
                    } else if (j == ancho - 1) {
                        linea.append("┐"); // Esquina superior derecha
                    } else {
                        linea.append("─"); // Borde superior
                    }
                } else if (i == altura - 1) {
                    if (j == 0) {
                        linea.append("└"); // Esquina inferior izquierda
                    } else if (j == ancho - 1) {
                        linea.append("┘"); // Esquina inferior derecha
                    } else {
                        linea.append("─"); // Borde inferior
                    }
                } else {
                    if (j == 0 || j == ancho - 1) {
                        linea.append("│"); // Bordes laterales
                    }

                    // Logo arriba izquierdo
                    else if (i == 1 && j == 1) {
                        linea.append(palo.getColor()).append(valor.getValor()).append(reset); // Imprime el valor con el color y luego lo resetea
                    }
                    else if (i == 1 && j == 2) {
                        linea.append(palo.getColor()).append(palo.getSimbolo()).append(reset);
                    }

                    // Logo medio
                    else if (i == 3 && j == 5) {
                        linea.append(palo.getColor()).append(valor.getValor()).append(reset);
                    }
                    else if (i == 3 && j == 6) {
                        linea.append(palo.getColor()).append(palo.getSimbolo()).append(reset);
                    }

                    // Esquina abajo logo
                    else if (i == altura - 2 && j == ancho - 3) {
                        linea.append(palo.getColor()).append(valor.getValor()).append(reset);
                    }
                    else if (i == altura - 2 && j == ancho - 2) {
                        linea.append(palo.getColor()).append(palo.getSimbolo()).append(reset);
                    }
                    else {
                        linea.append(" "); // Espacios en blanco
                    }
                }
            }
            lineas[i] = linea.toString(); // Convertimos el StringBuilder a String y lo guardamos en el array 'lineas'
        }
        return lineas; // Retornamos el array con las líneas
    }



}

