
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

    public void turnoCrupier(){
        while (obtenerPuntaje()<17){
            pedirCarta(mazo);
        }
    }
}
