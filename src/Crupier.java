
public class Crupier extends Jugador{
    private Mazo mazo;
    public Crupier(String nombre){
        super(nombre);
    }

    public Mazo getMazo() {
        return mazo;
    }

    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }

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
