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
    public void mostrarResultadoRonda() {
        int puntosCrupier = crupier.obtenerPuntaje();
        System.out.println(" RESULTADOS RONDA");
        System.out.println(" Puntos crupier: " + puntosCrupier + " puntos");

        if (puntosCrupier > 21) {
            System.out.println(" El crupier se ha pasado ");
            crupier.setEstado(EstadoJugador.PERDIENDO);
        } else if (puntosCrupier >= 17) {
            System.out.println(" El crupier se ha plantado con " + puntosCrupier + " puntos");
            crupier.setEstado(EstadoJugador.PLANTADO);
        }

        for (Jugador jugador : jugadores) {
            int puntosJugador = jugador.getPuntajeFinal();

            if (crupier.getEstado() == EstadoJugador.PERDIENDO) {
                if (jugador.getEstado() != EstadoJugador.PERDIENDO) {
                    System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos");
                    jugador.setEstado(EstadoJugador.GANANDO);
                } else {
                    System.out.println(jugador.getNombre() + " ha perdido por pasarse.");
                }
            } else if (crupier.getEstado() == EstadoJugador.PLANTADO) {
                if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
                    System.out.println(jugador.getNombre() + " ha perdido por abandonar");
                } else if (puntosJugador > puntosCrupier) {
                    System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos");
                    jugador.setEstado(EstadoJugador.GANANDO);
                } else if (puntosJugador < puntosCrupier) {
                    System.out.println("El crupier ha ganado con " + puntosCrupier + " puntos contra " + puntosJugador+" puntos de " +jugador.getNombre());
                } else {
                    System.out.println("Empate entre el crupier y " + jugador.getNombre() + " con " + puntosJugador + " puntos");
                }
            }
        }
    }


    public void jugarRonda() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador=jugadores.get(i);
                int opcion;
                do {
                    if (jugador.obtenerPuntaje()>21) {
                        jugador.setEstado(EstadoJugador.PERDIENDO);
                        jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                        System.out.println("El jugador "+jugador.getNombre()+" se ha pasado ");
                        break;
                    }
                    if (jugador.getEstado() == EstadoJugador.PLANTADO&&jugador.tieneBlackJack()){
                        jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                        break;
                    }
                    if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
                        jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                        break;
                    }
                        jugador.mostrarMano();
                    System.out.println("Puntos Actuales "+jugador.obtenerPuntaje()+" puntos");
                System.out.println(jugador.getNombre()+" Diga que quiere hacer (1-3)");
                System.out.println("1-PLantarse");
                System.out.println("2-Pedir carta");
                System.out.println("3-Abandonar");
                 opcion=teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        jugador.setEstado(EstadoJugador.PLANTADO);
                        jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                        System.out.println("Jugador "+jugador.getNombre()+" se ha plantado. ");
                        break;
                    case 2:
                        int puntajeAntesdePasarse=jugador.obtenerPuntaje();
                        if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
                            System.out.println("El jugador ya ha perdido. No puede pedir carta.");
                            break;
                        }
                        if (jugador.getEstado() == EstadoJugador.PLANTADO) {
                            System.out.println("El jugador ya está plantado. No puede pedir carta.");
                            break;
                        }
                        jugador.pedirCarta(mazo);
                        jugador.mostrarMano();
                        if (jugador.obtenerPuntaje()>21) {
                            jugador.setEstado(EstadoJugador.PERDIENDO);
                            jugador.setPuntajeFinal(puntajeAntesdePasarse);
                            break;
                        }
                        if (jugador.tieneBlackJack()) {
                            System.out.println("¡Enhorabuena, " + jugador.getNombre() + "! Tienes Blackjack.");
                            jugador.setEstado(EstadoJugador.PLANTADO);
                            jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                            break;
                        }

                        break;
                    case 3:
                        jugador.setEstado(EstadoJugador.PERDIENDO);
                        jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                        System.out.println(jugador.getNombre()+" Hasta luego");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }
                }while (opcion!=3&&!jugador.estaPlantado());
        }
        crupier.mostrarMano();
        crupier.turnoCrupier(mazo);
        crupier.setPuntajeFinal(crupier.obtenerPuntaje());
        mostrarResultadoRonda();
    }
    private boolean alMenosUnJugadorQuiereSeguir() {
        boolean algunoQuiere = false;

         for (Jugador jugador : jugadores) {
            System.out.println(jugador.getNombre() + ", ¿quieres jugar otra ronda? (si/no)");
            String respuesta = teclado.nextLine().trim().toLowerCase();
            if (respuesta.equals("si")) {
                algunoQuiere = true;
                jugador.setQuiereSeguir(true);
            } else {
                jugador.setQuiereSeguir(false);
            }
        }
        return algunoQuiere;
    }

    public void ejecutarPartidaCompleta() {
        do {
            iniciarPartida();
            jugarRonda();
        } while (alMenosUnJugadorQuiereSeguir());
    }



}
