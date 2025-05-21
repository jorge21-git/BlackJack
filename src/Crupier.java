
public class Crupier extends Jugador{
    private Mazo mazo;

    /**
     * Constructor que hereda nombre de clase madre Jugador
     * @param nombre
     */
    public Crupier(String nombre) {
        super(nombre);
    }

    public Mazo getMazo() {
        return mazo;
    }

    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }

    /**
     * Metodo turnoCrupier, recibe como parametro recibe un Mazo y mientras el valor de las cartas repartidas sea menor que 17,  hace la comprobación de si esta vacio el mazo, mientras se cumplan estas comprobaciones pide una Carta y la muestra. Comprobacion de los puntos de Crupier, y dependiendo de estos se pone en un estado u otro.
     * @param mazo tiene un ArrayList de cartas
     */
    public void turnoCrupier(Mazo mazo) {

        while (obtenerPuntaje()<17){
            if (mazo.getMazos().isEmpty()){
                System.out.println(" EL mazo esta vacio el crupier no puede pedir carta ");
                break;
            }
            pedirCarta(mazo);
            mostrarMano();
        }

        int puntos=obtenerPuntaje();

        if (puntos>=17&&obtenerPuntaje()<=21){
            setEstado(EstadoJugador.PLANTADO);
        }
        else if (puntos>21){
            setEstado(EstadoJugador.PERDIENDO);
        }
    }
}
