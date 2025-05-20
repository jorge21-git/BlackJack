import java.util.ArrayList;
import java.util.Scanner;

public class Partida {

    ArrayList<Jugador> jugadores;
    private boolean enCurso;
    private Scanner teclado;
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
        enCurso = true;

        if (mazo.getMazos().isEmpty()) {
            mazo.generarMazos(6);
            mazo.mezclarMazos();
        }

        crupier.reiniciarJugador();
        for (Jugador jugador : jugadores) {
            if (jugador.isQuiereSeguir()) {
                jugador.reiniciarJugador();
            }
        }
        pedirApuestas();
        for (Jugador jugador : jugadores) {
            if (jugador.isQuiereSeguir()) {
                jugador.pedirCarta(mazo);
                jugador.mostrarMano();
            }
        }

        crupier.pedirCarta(mazo);
        crupier.mostrarMano();
    }


    public void mostrarResultadoRonda() {

        int puntosCrupier = crupier.obtenerPuntaje();

        System.out.println(" RESULTADOS RONDA");
        System.out.println(" Puntos crupier: " + puntosCrupier + " puntos");

        if (crupier.tieneBlackJack()) {
            System.out.println(" El crupier tiene black jack");
        }
        if (puntosCrupier > 21) {
            System.out.println(" El crupier se ha pasado ");
            crupier.setEstado(EstadoJugador.PERDIENDO);
        } else if (puntosCrupier >= 17) {
            System.out.println(" El crupier se ha plantado con " + puntosCrupier + " puntos");
            crupier.setEstado(EstadoJugador.PLANTADO);
        }

        boolean crupierTieneBlackJack = crupier.tieneBlackJack();

        for (Jugador jugador : jugadores) {
            int saldoAntesdeRonda= jugador.getSaldo();
            boolean jugadorTieneBlackJack = jugador.tieneBlackJack();

            if (jugador.getEstado() == EstadoJugador.ABANDONADO) {
                System.out.println(jugador.getNombre() + " ha abandonado la partida.");
                continue;
            }

            if (crupierTieneBlackJack && jugadorTieneBlackJack) {
                System.out.println("Empate entre " + jugador.getNombre() + " y el crupier con BlackJack.");
                jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual());
                continue;
            }

            if (!crupierTieneBlackJack && jugadorTieneBlackJack) {
                System.out.println(jugador.getNombre() + " ha ganado con BlackJack.");
                jugador.setEstado(EstadoJugador.GANANDO);
                jugador.setSaldo(jugador.getSaldo() + (int) (jugador.getApuestaActual() * 2.5));
                mostrarGananciasJugador(jugador, saldoAntesdeRonda);
                continue;
            }

            if (crupierTieneBlackJack && !jugadorTieneBlackJack) {
                System.out.println(jugador.getNombre() + " ha perdido. El crupier tiene BlackJack.");
                continue;
            }

            int puntosJugador = jugador.getPuntajeFinal();

            if (crupier.getEstado() == EstadoJugador.PERDIENDO) {
                if (jugador.getEstado() != EstadoJugador.PERDIENDO) {
                    System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos");
                    jugador.setEstado(EstadoJugador.GANANDO);
                    jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual() * 2);
                } else {
                    System.out.println(jugador.getNombre() + " ha perdido por pasarse.");
                }
            } else if (crupier.getEstado() == EstadoJugador.PLANTADO) {
                if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
                    System.out.println(jugador.getNombre() + " ha perdido por pasarse.");
                } else if (puntosJugador > puntosCrupier) {
                    System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos");
                    jugador.setEstado(EstadoJugador.GANANDO);
                    jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual() * 2);
                    mostrarGananciasJugador(jugador,saldoAntesdeRonda);
                } else if (puntosJugador < puntosCrupier) {
                    System.out.println("El crupier ha ganado con " + puntosCrupier + " puntos contra " + puntosJugador + " puntos de " + jugador.getNombre());
                } else {
                    System.out.println("Empate entre el crupier y " + jugador.getNombre() + " con " + puntosJugador + " puntos");
                    jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual());
                    mostrarGananciasJugador(jugador,saldoAntesdeRonda);
                }
            }

        }

    }

    public void jugarRonda() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador=jugadores.get(i);
            //se salta a un jugador que no quiere seguir
            if (!jugador.isQuiereSeguir()) {
                continue;
            }
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
                        jugador.setEstado(EstadoJugador.ABANDONADO);
                        jugador.setQuiereSeguir(false);
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

        System.out.println("Todos los jugadores han abandonado. ¡Gracias por jugar!");
    }
    public void pedirApuestas() {
        for (Jugador jugador : jugadores) {
            System.out.println(jugador.getNombre());
            if (!jugador.isQuiereSeguir())
                continue;

            int apuesta = jugador.apostar();
            jugador.setApuestaActual(apuesta);
        }
    }
    public void mostrarGananciasJugador(Jugador jugador, int saldoAnterior) {
        int saldoActual = jugador.getSaldo();
        int diferencia = saldoActual - saldoAnterior;

        if (diferencia > 0) {
            System.out.println(jugador.getNombre() + " ha ganado " + diferencia + " fichas.");
        } else if (diferencia < 0) {
            System.out.println(jugador.getNombre() + " ha perdido " + (-diferencia) + " fichas.");
        } else {
            System.out.println(jugador.getNombre() + " no ha ganado ni perdido fichas.");
        }
    }


}
