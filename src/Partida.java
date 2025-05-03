import java.util.ArrayList;
import java.util.Scanner;

public class Partida {
    Scanner teclado=new Scanner(System.in);
    ArrayList<Jugador> jugadores;
    private boolean enCurso;
    Crupier crupier;
    Mazo mazo;
    Jugador jugador;

    public Partida() {
        jugadores=new ArrayList<>();
        enCurso=false;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }

    public Crupier getCrupier() {
        return crupier;
    }

    public void setCrupier(Crupier crupier) {
        this.crupier = crupier;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public boolean isEnCurso() {
        return enCurso;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void añadirJugador(Jugador jugador) {
       jugadores.add(jugador);
    }

    public void iniciarPartida() {
        enCurso=true;
        mazo.generarMazos(6);
        mazo.mezclarMazos();
        crupier.reiniciarJugador();
        for (Jugador jugador : jugadores) {
            jugador.reiniciarJugador();
        }

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador=jugadores.get(i);
            jugador.pedirCarta(mazo);
        }
        crupier.pedirCarta(mazo);
    }
}
