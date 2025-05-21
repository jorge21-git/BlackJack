import java.util.ArrayList;

public class Mazo {
    private ArrayList<Carta> cartas;
    private int numBarajas;

    /**
     * Inicialización del ArrayList cartas
     */
    public Mazo() {
        cartas = new ArrayList<>();
    }

    public int getNumBarajas() {
        return numBarajas;
    }

    public void setNumBarajas(int numBarajas) {
        this.numBarajas = numBarajas;
    }

    public ArrayList<Carta> getMazos() {
        return cartas;
    }

    public void setMazos(ArrayList<Carta> mazos) {
        this.cartas = mazos;
    }

    /**
     * Añadir carta al ArrayList por cada valor de cada palo
     */
    public void generarBaraja() {
        for (int i = 0; i < Palo.values().length; i++) {
            for (int j = 0; j < Valor.values().length; j++) {
                Carta carta = new Carta(Palo.values()[i], Valor.values()[j]);
                cartas.add(carta);
            }
        }
    }

    /**
     * Se crea un mazo con la cantidad de barajas que se desee
     * @param numBarajas la cantidad de barajas que se desea usar
     */
    public void generarMazos(int numBarajas) {
        for (int i = 0; i < numBarajas; i++) {
            generarBaraja();
        }

    }

    /**
     * Se inicializa la "i" en la ultima posicion -1  del ArrayList cartas, y se recorre el for hasta que este en la posición 0, "i" se va restando. Posteriormente se crea una variable j para que nos de una posicion aleatoria del ArrayList, pasamos a un objeto Carta el valor temporal de la posición del for, despues en la posición "i" se pone el valor de la posicion "j" aleatoria y con nuestra Carta temporal con el valor previo que se pone en la posición aleatoria de la "j", es decir, se hace un intercambio de posiciones
     */
    public void mezclarMazos() {
        for (int i = cartas.size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Carta carta = cartas.get(i);
            cartas.set(i, cartas.get(j));
            cartas.set(j, carta);
        }
    }

    /**
     * Se crea un objeto carta, para quitarla de la ultima posición del ArrayList
     * @return devolvemos la carta para darsela a un jugador
     */
    public Carta repartirCarta() {

        Carta carta; // quitar y guardar la carta
        carta = cartas.remove(cartas.size() - 1);
        return carta;
    }
    
}