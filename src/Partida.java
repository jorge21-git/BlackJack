import java.util.ArrayList;
import java.util.Scanner;

public class Partida {

    private ArrayList<Jugador> jugadores;
    private boolean enCurso;
    private Scanner teclado;
    private Crupier crupier;
    private Mazo mazo;
    private int contadorRondas;

    public Partida() {
        jugadores = new ArrayList<>();
        enCurso = false;
        mazo = new Mazo();
        crupier = new Crupier("Crupier");
        teclado = new Scanner(System.in);
        contadorRondas = 0;
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

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public boolean isEnCurso() {
        return enCurso;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
    }

    public void añadirJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public void prepararMazo() {
        if (mazo.getMazos().isEmpty()) {
            mazo.generarMazos(6);
            mazo.mezclarMazos();
        }
    }

    public void reiniciarJugador() {
        for (Jugador jugador : jugadores) {
            if (jugador.isQuiereSeguir()) {
                jugador.reiniciarJugador();
            }
        }
    }

    public void repartirCartas() {
        for (Jugador jugador : jugadores) {
            if (jugador.isQuiereSeguir()) {
                jugador.pedirCarta(mazo);
                jugador.mostrarMano();
            }
        }
        crupier.pedirCarta(mazo);
        crupier.mostrarMano();
    }

    public void iniciarPartida() {
        enCurso = true;
        prepararMazo();
        reiniciarJugador();
        crupier.reiniciarJugador();
        pedirApuestas();
        repartirCartas();
    }

    public void noPagar(Jugador jugador) {
        jugador.noCobrar();
    }

    public void pagarBlackJack(Jugador jugador) {
        jugador.cobrarBlackJack();
    }

    public void pagar(Jugador jugador) {
        jugador.cobrar();
    }

    public void evaluarPorPuntaje(Jugador jugador, int puntosCrupier, int puntosJugador) {
        if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
            System.out.println(jugador.getNombre() + " ha perdido por pasarse.");
            jugador.setSaldo(jugador.getSaldo() - jugador.getApuestaActual());
        } else if (crupier.getEstado() == EstadoJugador.PERDIENDO) {
            System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos");
            jugador.setEstado(EstadoJugador.GANANDO);
            pagar(jugador);
        } else if (crupier.getEstado() == EstadoJugador.PLANTADO) {
            if (puntosJugador > puntosCrupier) {
                System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos");
                jugador.setEstado(EstadoJugador.GANANDO);
                pagar(jugador);
            } else if (puntosJugador < puntosCrupier) {
                System.out.println("El crupier ha ganado con " + puntosCrupier + " puntos contra " + puntosJugador + " puntos de " + jugador.getNombre());
                jugador.setSaldo(jugador.getSaldo() - jugador.getApuestaActual());
            } else {
                System.out.println("Empate entre el crupier y " + jugador.getNombre() + " con " + puntosJugador + " puntos");
                jugador.setEstado(EstadoJugador.EMPATADO);
                System.out.println(jugador.getNombre() + " mantiene sus fichas ");
            }
        }
    }

    public void evaluarJugador(Jugador jugador, int puntosCrupier, boolean crupierTieneBlackJack) {
        if (jugador.getEstado() == EstadoJugador.ABANDONADO) {
            System.out.println(jugador.getNombre() + " ha abandonado la partida.");
            return;
        }

        int saldoAntes = jugador.getSaldo();
        boolean jugadorTieneBlackJack = jugador.tieneBlackJack();
        int puntosJugador = jugador.getPuntajeFinal();

        if (crupierTieneBlackJack && jugadorTieneBlackJack) {
            System.out.println("Empate: " + jugador.getNombre() + " y el crupier tienen BlackJack.");
            jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual());
            jugador.setEstado(EstadoJugador.EMPATADO);
        } else if (!crupierTieneBlackJack && jugadorTieneBlackJack) {
            System.out.println(jugador.getNombre() + " ha ganado con BlackJack.");
            jugador.setEstado(EstadoJugador.GANANDO);
            jugador.setSaldo(jugador.getSaldo() + (int) (jugador.getApuestaActual() * 2.5));
        } else if (crupierTieneBlackJack) {
            System.out.println(jugador.getNombre() + " ha perdido. El crupier tiene BlackJack.");
            jugador.setSaldo(jugador.getSaldo() - jugador.getApuestaActual());
            jugador.setEstado(EstadoJugador.PERDIENDO);
        } else {
            evaluarPorPuntaje(jugador, puntosCrupier, puntosJugador);
        }

        mostrarGananciasJugador(jugador, saldoAntes);
    }

    public void mostrarResultadoRonda() {
        int puntosCrupier = crupier.obtenerPuntaje();
        System.out.println("\n--- RESULTADOS DE LA RONDA ---");
        System.out.println("Puntos del crupier: " + puntosCrupier);

        if (crupier.tieneBlackJack()) {
            System.out.println("El crupier tiene BlackJack.");
        }

        if (puntosCrupier > 21) {
            System.out.println("El crupier se ha pasado.");
            crupier.setEstado(EstadoJugador.PERDIENDO);
        } else if (puntosCrupier >= 17) {
            System.out.println("El crupier se ha plantado con " + puntosCrupier + " puntos.");
            crupier.setEstado(EstadoJugador.PLANTADO);
        }

        for (Jugador jugador : jugadores) {
            evaluarJugador(jugador, puntosCrupier, crupier.tieneBlackJack());
        }
    }

    public void jugar(Jugador jugador) {
        System.out.println(jugador.getNombre() + ", elige una opción:");

        int opcion;
        do {
            if (jugador.obtenerPuntaje() > 21) {
                jugador.setEstado(EstadoJugador.PERDIENDO);
                jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                System.out.println("El jugador " + jugador.getNombre() + " se ha pasado.");
                break;
            }

            if (jugador.getEstado() == EstadoJugador.PLANTADO && jugador.tieneBlackJack()) {
                jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                break;
            }

            if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
                jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                break;
            }

            jugador.mostrarMano();
            System.out.println("Puntos actuales: " + jugador.obtenerPuntaje());
            System.out.println("¿Qué desea hacer?");
            System.out.println("1 - Plantarse");
            System.out.println("2 - Pedir carta");
            System.out.println("3 - Abandonar");

            while (!teclado.hasNextInt()) {
                System.out.println("Por favor, ingrese un número válido (1, 2 o 3):");
                teclado.next();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    jugador.setEstado(EstadoJugador.PLANTADO);
                    jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                    System.out.println("Jugador " + jugador.getNombre() + " se ha plantado.");
                    break;
                case 2:
                    int puntajeAntes = jugador.obtenerPuntaje();
                    jugador.pedirCarta(mazo);
                    jugador.mostrarMano();
                    if (jugador.obtenerPuntaje() > 21) {
                        jugador.setEstado(EstadoJugador.PERDIENDO);
                        jugador.setPuntajeFinal(puntajeAntes);
                    } else if (jugador.tieneBlackJack()) {
                        System.out.println("¡Enhorabuena, " + jugador.getNombre() + "! Tienes BlackJack.");
                        jugador.setEstado(EstadoJugador.PLANTADO);
                        jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                    }
                    break;
                case 3:
                    jugador.setEstado(EstadoJugador.ABANDONADO);
                    jugador.setQuiereSeguir(false);
                    jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                    noPagar(jugador);
                    System.out.println(jugador.getNombre() + ", hasta luego.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 3 && !jugador.estaPlantado());
    }

    public void jugarRonda() {
        if (crupier.tieneBlackJack()) {
            crupier.mostrarMano();
            crupier.setEstado(EstadoJugador.PLANTADO);
            crupier.setPuntajeFinal(crupier.obtenerPuntaje());
            mostrarResultadoRonda();
            return;
        }
        for (Jugador jugador : jugadores) {
            if (jugador.isQuiereSeguir()) {
                jugar(jugador);
            }
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
        contadorRondas = 0;
        do {
            iniciarPartida();
            jugarRonda();
            contadorRondas++;
        } while (alMenosUnJugadorQuiereSeguir());
        System.out.println("Todos los jugadores han abandonado. ¡Gracias por jugar!");
        Estadisticas.guardarEstadisticas(jugadores, contadorRondas);
        Estadisticas.mostrarEstadisticas();
    }

    public void pedirApuestas() {
        for (Jugador jugador : jugadores) {
            if (!jugador.isQuiereSeguir()) continue;

            int apuesta = 0;
            do {
                System.out.println(jugador.getNombre() + ", introduce tu apuesta (saldo: " + jugador.getSaldo() + "):");
                while (!teclado.hasNextInt()) {
                    System.out.println("Por favor, ingresa un número válido para la apuesta:");
                    teclado.next();
                }
                apuesta = teclado.nextInt();
                teclado.nextLine();
                if (apuesta <= 0 || apuesta > jugador.getSaldo()) {
                    System.out.println("La apuesta debe ser positiva y menor o igual que tu saldo.");
                }
            } while (apuesta <= 0 || apuesta > jugador.getSaldo());

            jugador.setApuestaActual(apuesta);
        }
    }

    private void mostrarGananciasJugador(Jugador jugador, int saldoAntes) {
        int diferencia = jugador.getSaldo() - saldoAntes;
        if (diferencia > 0) {
            System.out.println(jugador.getNombre() + " ha ganado " + diferencia + " fichas.");
        } else if (diferencia < 0) {
            System.out.println(jugador.getNombre() + " ha perdido " + (-diferencia) + " fichas.");
        } else {
            System.out.println(jugador.getNombre() + " no ha ganado ni perdido fichas.");
        }
    }
}
