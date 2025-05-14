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
        mazo=new Mazo();
        crupier=new Crupier("Crupier");
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
        if (mazo.getMazos().isEmpty()) {
            mazo.generarMazos(6);
            mazo.mezclarMazos();
        }

        crupier.reiniciarJugador();
        for (Jugador jugador : jugadores) {
            jugador.reiniciarJugador();
        }

        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador=jugadores.get(i);
            jugador.pedirCarta(mazo);
            jugador.mostrarMano();
        }
        crupier.pedirCarta(mazo);
        crupier.mostrarMano();
    }
    public void mostrarResultadoRonda(){

    }

    public void siguienteTurno() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador=jugadores.get(i);
                int opcion;
                do {
                    if (jugador.getEstado() == EstadoJugador.PLANTADO){
                        return;
                    }
                    if (i>0){
                        jugador.mostrarMano();
                    }
                System.out.println(jugador.getNombre()+" Diga que quiere hacer (1-3)");
                System.out.println("1-PLantarse");
                System.out.println("2-Pedir carta");
                System.out.println("3-Abandonar");
                 opcion=teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        jugador.setEstado(EstadoJugador.PLANTADO);
                        System.out.println("Jugador "+jugador.getNombre()+" se ha plantado. ");
                        break;
                    case 2:
                        if (jugador.estaPlantado()&&jugador.obtenerPuntaje()>21) {
                            System.out.print(" el jugador "+jugador.getNombre()+" se ha pasado no puede pedir la carta ");
                        }
                        else {
                            jugador.pedirCarta(mazo);
                            jugador.mostrarMano();
                            if (jugador.obtenerPuntaje()>21){
                                System.out.println(jugador.getNombre()+"se ha pasado lo siento ");
                                jugador.setEstado(EstadoJugador.PERDIENDO);
                            }
                            if (jugador.tieneBlackJack()){
                                System.out.println(jugador.getNombre()+" BlackJack Enhorabuena ");
                                jugador.setEstado(EstadoJugador.GANANDO);
                            }
                        }
                        break;
                    case 3:
                        jugador.setEstado(EstadoJugador.PERDIENDO);
                        System.out.println(jugador.getNombre()+" Hasta luego");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }
                }while (opcion!=3&&!jugador.estaPlantado());
        }
        crupier.mostrarMano();
        crupier.turnoCrupier(mazo);
    }

}
