public class Carta {

private Palo palo;
private Valor valor;

public Carta(Palo palo, Valor valor) {
    this.palo = palo;
    this.valor = valor;
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
    public void dibujarCarta() {
        int altura = 7;
        int ancho = 11;
        String reset = "\u001B[0m"; // Reset del color al valor predeterminado

        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < ancho; j++) {
                if (i == 0) {
                    if (j == 0) {
                        System.out.print("┌");
                    } else if (j == ancho - 1) {
                        System.out.print("┐");
                    } else {
                        System.out.print("─");
                    }
                } else if (i == altura - 1) {
                    if (j == 0) {
                        System.out.print("└");
                    } else if (j == ancho - 1) {
                        System.out.print("┘");
                    } else {
                        System.out.print("─");
                    }
                } else {
                    if (j == 0 || j == ancho - 1) {
                        System.out.print("│");
                    }

                    // logo arriba izquierdo
                    else if (i == 1 && j == 1) {
                        System.out.print(palo.getColor() + valor.getValor() + reset); // Imprime el valor con el color y luego lo resetea para que no se quede por defecto
                    }
                    else if (i == 1 && j == 2) {
                        System.out.print(palo.getColor() + palo.getSimbolo() + reset);
                    }

                    // logo medio
                    else if (i == 3 && j == 5) {
                        System.out.print(palo.getColor() + valor.getValor() + reset);
                    }
                    else if (i == 3 && j == 6) {
                        System.out.print(palo.getColor() + palo.getSimbolo() + reset);
                    }

                    // esquina abajo logo
                    else if (i == altura - 2 && j == ancho - 3) {
                        System.out.print(palo.getColor() + valor.getValor() + reset);
                    }
                    else if (i == altura - 2 && j == ancho - 2) {
                        System.out.print(palo.getColor() + palo.getSimbolo() + reset);
                    }
                    else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

}

