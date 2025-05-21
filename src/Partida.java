import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Partida {

    private ArrayList<Jugador> jugadores;
    private boolean enCurso;
    private Scanner teclado;
    private Crupier crupier;
    private Mazo mazo;
    private int contadorRondas;
    private HashMap<String, EstadisticasJugador> estadisticasJugadores;

    public Partida() {
        jugadores = new ArrayList<>();
        enCurso = false;
        mazo = new Mazo();
        crupier = new Crupier("Crupier");
        teclado = new Scanner(System.in);
        contadorRondas = 0;
        estadisticasJugadores = new HashMap<>();
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
        estadisticasJugadores.put(jugador.getNombre(), new EstadisticasJugador());
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
        crupier.reiniciarJugador();
    }

    public void repartirCartas() {
        for (Jugador jugador : jugadores) {
            if (jugador.isQuiereSeguir()&&jugador.getApuestaActual()>0) {
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

    public void evaluarPorPuntaje(Jugador jugador, int puntosCrupier, int puntosJugador) {
        if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
            System.out.println(jugador.getNombre() + " se pasó puntos y ha perdido su apuesta ");
        }
        // Si el crupier se pasó, y el jugador no, el jugador gana.
        else if (crupier.getEstado() == EstadoJugador.PERDIENDO) {
            System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos al crupier que se pasó con " + puntosCrupier + " puntos).");
            jugador.setEstado(EstadoJugador.GANANDO);
            jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual());
            // pagar(jugador); // Ya se ajusta el saldo directamente
        }
        // Si nadie se pasó y nadie tiene Blackjack, comparamos puntos
        else if (crupier.getEstado() == EstadoJugador.PLANTADO) {
            if (puntosJugador > puntosCrupier) {
                System.out.println(jugador.getNombre() + " ha ganado con " + puntosJugador + " puntos contra " + puntosCrupier + " del crupier.");
                jugador.setEstado(EstadoJugador.GANANDO);
                jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual());
                // pagamos actualizamos saldo
            } else if (puntosJugador < puntosCrupier) {
                System.out.println("El crupier ha ganado con " + puntosCrupier + " puntos contra " + puntosJugador + " puntos de " + jugador.getNombre() + ".");
                jugador.setSaldo(jugador.getSaldo() - jugador.getApuestaActual()); // El jugador pierde su apuesta
                jugador.setEstado(EstadoJugador.PERDIENDO);
            } else
       //si estan iguales
                {
                System.out.println("Esatan empatados con "+puntosJugador+" puntos.");
                jugador.setEstado(EstadoJugador.EMPATADO);
                // no actualizmos se queda igual
            }
        }
    }

    public void evaluarJugador(Jugador jugador, int puntosCrupier, boolean crupierTieneBlackJack) {
        // sino estan jugando y tiene 0 de  fuera paar que no queden evaludados
        if (!jugador.isQuiereSeguir() || jugador.getApuestaActual() <= 0) {

            System.out.println(jugador.getNombre() + " no participó en la evaluación final de esta ronda.");
            return;
        }
        // Si abndona una vez servido
        if (jugador.getEstado() == EstadoJugador.ABANDONADO) {
            System.out.println(  jugador.getNombre() + " abandonó la ronda perdio esta ronda");
            return; // no evaluamos solo informamos.
        }
        int saldoAntes = jugador.getSaldo(); // variable para guardar saldo anteiror
        boolean jugadorTieneBlackJack = jugador.tieneBlackJack();
        int puntosJugador = jugador.getPuntajeFinal(); // para el puntaje final

        System.out.println(  jugador.getNombre() + " tiene " + puntosJugador + " puntos.");
        // 1. si Ambos tienen BlackJack (empate)
        if (crupierTieneBlackJack && jugadorTieneBlackJack) {
            System.out.println("Empate. Ambos, " + jugador.getNombre() + " y el Crupier, tienen BlackJack.");
            jugador.setSaldo(jugador.getSaldo() + jugador.getApuestaActual()); // Devuelve la apuesta
            jugador.setEstado(EstadoJugador.EMPATADO);
        }
        // 2. Jugador tiene BlackJack, Crupier no
        else if (jugadorTieneBlackJack) {
            System.out.println("¡" + jugador.getNombre() + " ha ganado con BlackJack! Pago de 3:2.");
            jugador.setEstado(EstadoJugador.GANANDO);
            // Calcula 1.5 veces la apuesta y la suma al saldo (ej: 10 fichas -> 10 + 15 = 25 total)
            jugador.setSaldo(jugador.getSaldo() + (int) (jugador.getApuestaActual() * 1.5));
            // pagarBlackJack(jugador); // Ya se ajusta el saldo directamente
            EstadisticasJugador estadistica = estadisticasJugadores.get(jugador.getNombre());
            if (estadistica != null) {
                estadistica.guardarBlacjacks();
            }
        }
        // 3. Crupier tiene BlackJack, Jugador no
        else if (crupierTieneBlackJack) {
            System.out.println("El crupier tiene BlackJack. " + jugador.getNombre() + " pierde su apuesta de " + jugador.getApuestaActual() + " fichas.");
            jugador.setSaldo(jugador.getSaldo() - jugador.getApuestaActual());
            jugador.setEstado(EstadoJugador.PERDIENDO);
        }
        // 4. El resto de los casos (comparación de puntos o crupier se pasó)
        else {
            evaluarPorPuntaje(jugador, puntosCrupier, puntosJugador);
        }

        mostrarGananciasJugador(jugador, saldoAntes);
    }

    public void mostrarResultadoRonda() {
        System.out.println("--- RESULTADOS DE LA RONDA ---");

        System.out.println("Puntos del crupier: " + crupier.getPuntajeFinal());

        if (crupier.tieneBlackJack()) {
            System.out.println("El crupier tiene BlackJack.");
        } else if (crupier.getEstado() == EstadoJugador.PERDIENDO) {
            System.out.println("El crupier se ha pasado.");
        } else {
            System.out.println("El crupier se ha plantado con " + crupier.getPuntajeFinal() + " puntos.");
        }

        // Evaluar y mostrar resultados para cada jugador
        int puntosCrupierFinal = crupier.getPuntajeFinal();
        boolean crupierTieneBlackJackFinal = crupier.tieneBlackJack();

        for (Jugador jugador : jugadores) {
            // Solo evaluar si el jugador sigue en la ronda y apostó (o abandonó durante la ronda)
            if (jugador.getApuestaActual() > 0 || jugador.getEstado() == EstadoJugador.ABANDONADO) {
                evaluarJugador(jugador, puntosCrupierFinal, crupierTieneBlackJackFinal);

                // Actualizar estadísticas (esto siempre al final de la evaluación del jugador)
                EstadisticasJugador datos = estadisticasJugadores.get(jugador.getNombre());
                if (datos != null) {
                    if (jugador.getEstado() == EstadoJugador.GANANDO) {
                        datos.guardarVictorias();
                    } else if (jugador.getEstado() == EstadoJugador.PERDIENDO) {
                        // Solo guardar derrota si no fue por ABANDONADO, que es otro tipo de "derrota"
                        if (jugador.getEstado() != EstadoJugador.ABANDONADO) {
                            datos.guardarDerrotas();
                        }
                    } else if (jugador.getEstado() == EstadoJugador.EMPATADO) {
                        datos.guardarEmpates();
                    }
                }
            } else {
                System.out.println(jugador.getNombre() + " no participó en esta ronda.");
            }
        }
    }

    public void jugar(Jugador jugador) {
        // Si el jugador no quiere seguir, ya se pasó, abandonó, o no apostó, no juega su turno
        if (!jugador.isQuiereSeguir() || jugador.getApuestaActual() <= 0 ||
                jugador.getEstado() == EstadoJugador.PERDIENDO || jugador.getEstado() == EstadoJugador.ABANDONADO) {
            System.out.println("\n" + jugador.getNombre() + " no participa en esta ronda (motivo: inactivo, sin apuesta, ya se pasó o abandonó).");
            return;
        }

        System.out.println("\n--- Turno de " + jugador.getNombre() + " ---");
        int opcion;
        do {
            jugador.mostrarMano();
            System.out.println("Puntos actuales: " + jugador.obtenerPuntaje());

            // 1. Comprobar si el jugador se pasa *antes* de ofrecer opciones
            if (jugador.obtenerPuntaje() > 21) {
                System.out.println("¡" + jugador.getNombre() + " se ha pasado con " + jugador.obtenerPuntaje() + " puntos!");
                jugador.setEstado(EstadoJugador.PERDIENDO); // Marca el estado como perdiendo
                jugador.setPuntajeFinal(jugador.obtenerPuntaje()); // Guarda el puntaje final (con el que se pasó)
                // Deduce la apuesta inmediatamente al pasarse
                System.out.println(jugador.getNombre() + " pierde su apuesta de " + jugador.getApuestaActual() + " fichas.");
                jugador.setSaldo(jugador.getSaldo() - jugador.getApuestaActual());
                break; // El jugador se pasó, termina su turno
            }

            // 2. Comprobar si tiene BlackJack *antes* de ofrecer opciones (solo si aún no se pasó)
            if (jugador.tieneBlackJack()) {
                System.out.println("¡Enhorabuena, " + jugador.getNombre() + "! ¡Tienes BlackJack!");
                jugador.setEstado(EstadoJugador.PLANTADO); // Se planta automáticamente con BlackJack
                jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                // El saldo se ajustará en 'evaluarJugador' para el pago 3:2
                break; // Termina su turno, ya tiene BlackJack
            }

            System.out.println("¿Qué desea hacer?");
            System.out.println("1 - Plantarse");
            System.out.println("2 - Pedir carta");
            System.out.println("3 - Abandonar (pierdes tu apuesta actual)"); // Aclaro que pierde la apuesta
            System.out.print("Elige una opción: ");

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("¡Error! Entrada no válida. Por favor, ingresa un número (1, 2 o 3).");
                teclado.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1:
                    jugador.setEstado(EstadoJugador.PLANTADO);
                    jugador.setPuntajeFinal(jugador.obtenerPuntaje());
                    System.out.println("Jugador " + jugador.getNombre() + " se ha plantado con " + jugador.getPuntajeFinal() + " puntos.");
                    break;
                case 2:
                    jugador.pedirCarta(mazo);
                    System.out.print(jugador.getNombre() + ", nueva mano: ");
                    jugador.mostrarMano();
                    // La comprobación de pasarse o Blackjack se hará al inicio del siguiente ciclo del do-while
                    break;
                case 3:
                    jugador.setEstado(EstadoJugador.ABANDONADO);
                    jugador.setQuiereSeguir(false); // Este jugador no participará más en futuras rondas
                    jugador.setPuntajeFinal(jugador.obtenerPuntaje()); // Guarda su puntaje al abandonar
                    System.out.println(jugador.getNombre() + " ha abandonado la ronda. Pierde su apuesta de " + jugador.getApuestaActual() + " fichas.");
                    jugador.setSaldo(jugador.getSaldo() - jugador.getApuestaActual()); // Pierde la apuesta
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elige 1, 2 o 3.");
            }
            // El turno continúa mientras el jugador no se plante, no se pase, o no abandone
        } while (jugador.getEstado() == EstadoJugador.EN_JUEGO); // Asumo que tu enum tiene EstadoJugador.JUGANDO
    }

    public void jugarRonda() {
        System.out.println("\n--- Turno del Crupier ---");
        crupier.mostrarMano(); // El crupier revela su segunda carta

        // 1. Comprobar si el crupier tiene Blackjack de inmediato
        if (crupier.tieneBlackJack()) {
            System.out.println("¡El crupier tiene BlackJack!");
            crupier.setEstado(EstadoJugador.PLANTADO); // Se considera plantado con 21
            crupier.setPuntajeFinal(crupier.obtenerPuntaje());
        } else {
            // 2. Comprobar si hay jugadores activos contra los que el crupier deba jugar
            boolean hayJugadoresActivosParaCrupier = false;
            for (Jugador jugador : jugadores) {
                // Un jugador es "activo" para el crupier si quiere seguir Y NO se pasó Y NO abandonó Y APOSTÓ.
                if (jugador.isQuiereSeguir() && jugador.getApuestaActual() > 0 &&
                        jugador.getEstado() != EstadoJugador.PERDIENDO && jugador.getEstado() != EstadoJugador.ABANDONADO) {
                    hayJugadoresActivosParaCrupier = true;
                    break;
                }
            }

            if (!hayJugadoresActivosParaCrupier) {
                System.out.println("No quedan jugadores activos contra los que el crupier deba jugar. El crupier se planta con lo que tiene.");
                crupier.setEstado(EstadoJugador.PLANTADO); // Crupier se planta si no hay a quien enfrentar
            } else {
                crupier.turnoCrupier(mazo);
            }
            crupier.setPuntajeFinal(crupier.obtenerPuntaje()); // Guarda el puntaje final del crupier
        }

        if (crupier.getEstado() == EstadoJugador.PERDIENDO) {
            System.out.println("El crupier se ha pasado con " + crupier.getPuntajeFinal() + " puntos.");
        } else {
            System.out.println("El crupier se ha plantado con " + crupier.getPuntajeFinal() + " puntos.");
        }
        mostrarResultadoRonda();
    }

    private boolean alMenosUnJugadorQuiereSeguir() {
        boolean algunoQuiere = false;
        System.out.println("\n--- ¿Quieres jugar otra ronda? ---");

        for (Jugador jugador : jugadores) {
            // Solo preguntamos si el jugador está marcado como que QUIERE SEGUIR y TIENE FICHAS
            if (jugador.isQuiereSeguir() && jugador.getSaldo() > 0) {
                System.out.println(jugador.getNombre() + ", ¿quieres jugar otra ronda? (si/no). Tu saldo actual es: " + jugador.getSaldo());
                String respuesta = teclado.nextLine().trim().toLowerCase();
                if (respuesta.equals("si")) {
                    algunoQuiere = true;
                    jugador.setQuiereSeguir(true); // Se asegura que sigue queriendo
                } else {
                    jugador.setQuiereSeguir(false); // No quiere seguir
                    System.out.println(jugador.getNombre() + " ha decidido no continuar.");
                }
            } else {
                // Si el jugador no tiene fichas o ya había decidido no seguir, informamos
                if (jugador.getSaldo() <= 0) {
                    System.out.println(jugador.getNombre() + ", no tienes fichas para seguir jugando.");
                } else { // Si !isQuiereSeguir pero tiene saldo
                    System.out.println(jugador.getNombre() + " ya había decidido no continuar en rondas previas.");
                }
                jugador.setQuiereSeguir(false); // Asegurarse de que no siga
            }
        }
        return algunoQuiere;
    }

    public void ejecutarPartidaCompleta() {
        contadorRondas = 0;
        enCurso = true; // La partida empieza

        // Inicializar que todos los jugadores quieren empezar al inicio del juego
        for (Jugador jugador : jugadores) {
            jugador.setQuiereSeguir(true);
        }

        do {
            // 1. Comprobación inicial de si hay jugadores activos para empezar la ronda
            boolean hayJugadoresActivosParaEstaRonda = false;
            for (Jugador j : jugadores) {
                if (j.isQuiereSeguir() && j.getSaldo() > 0) { // Solo si quiere seguir Y tiene saldo
                    hayJugadoresActivosParaEstaRonda = true;
                    break;
                }
            }

            if (!hayJugadoresActivosParaEstaRonda) {
                System.out.println("No quedan jugadores con fichas o que quieran jugar. Fin de la partida.");
                break; // Salimos del bucle principal de la partida
            }
            iniciarPartida();
            // 2. Volver a verificar si hay jugadores con apuestas válidas después de la fase de apuestas
            hayJugadoresActivosParaEstaRonda = false; // Resetear la bandera
            for (Jugador j : jugadores) {
                if (j.isQuiereSeguir() && j.getApuestaActual() > 0) { // Solo si quiere seguir Y apostó
                    hayJugadoresActivosParaEstaRonda = true;
                    break;
                }
            }
            if (!hayJugadoresActivosParaEstaRonda) {
                System.out.println("Ningún jugador activo realizó una apuesta válida para esta ronda. Terminando la partida.");
                break; // Salimos del bucle si nadie apostó en esta ronda
            }

            // 3. Turno de los jugadores activos
            for (Jugador jugador : jugadores) {
                // Solo llama a 'jugar' para quienes siguen y apostaron (y no se han pasado aún)
                if (jugador.isQuiereSeguir() && jugador.getApuestaActual() > 0) {
                    jugar(jugador);
                }
            }
            // 4. Turno del crupier y evaluación de la ronda
            jugarRonda();

            contadorRondas++;
            // 5. Preguntar a los jugadores si quieren seguir jugando otra ronda
        } while (alMenosUnJugadorQuiereSeguir());

        System.out.println("Todos los jugadores han abandonado. ¡Gracias por jugar!");
        System.out.println("=== RESULTADOS DE LA PARTIDA ===");
        Estadisticas.guardarEstadisticas(jugadores, contadorRondas, estadisticasJugadores);
        Estadisticas.mostrarEstadisticas(jugadores, contadorRondas, estadisticasJugadores);
    }

    public void pedirApuestas() {
        System.out.println("--- Fase de Apuestas ---");

        for (Jugador jugador : jugadores) { // Recorremos todos los jugadores
            // Si el jugador NO quiere seguir, simplemente lo saltamos para esta ronda
            if (!jugador.isQuiereSeguir()) {
                System.out.println(jugador.getNombre() + " no jugará esta ronda (ha abandonado o se ha retirado antes).");
                jugador.setApuestaActual(0); // Aseguramos que su apuesta sea 0 si no juega
                continue; // Pasa al siguiente jugador
            }

            // --- Comprobación de saldo y opción de recarga ---
            if (jugador.getSaldo() <= 0) {
                System.out.println("\n" + jugador.getNombre() + ", te has quedado sin fichas.");
                System.out.print("¿Quieres recargar fichas para continuar? (si/no): ");
                String respuestaRecarga = teclado.nextLine().trim().toLowerCase();

                if (respuestaRecarga.equals("si")) {
                    int recarga = 0;
                    while (true) {
                        System.out.print("Introduce la cantidad de fichas a recargar (mínimo 1): ");
                        try {
                            recarga = teclado.nextInt();
                            teclado.nextLine(); // Consumir el salto de línea
                            if (recarga > 0) {
                                break; // Cantidad válida
                            } else {
                                System.out.println("La recarga debe ser un número positivo.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("¡Error! Entrada no válida. Por favor, ingresa un número.");
                            teclado.nextLine(); // Limpiar el buffer
                        }
                    }
                    jugador.setSaldo(jugador.getSaldo() + recarga);
                    System.out.println("Saldo actualizado: " + jugador.getSaldo() + " fichas.");
                } else {
                    System.out.println(jugador.getNombre() + " ha decidido no recargar y no jugará esta ronda.");
                    jugador.setQuiereSeguir(false); // Desactiva al jugador para esta y futuras rondas
                    jugador.setApuestaActual(0); // Aseguramos que su apuesta sea 0
                    continue; // Pasa al siguiente jugador
                }
            }
            // --- Pedir la apuesta ---
            // Solo pedimos apuesta si el jugador QUIERE SEGUIR Y TIENE FICHAS después de la posible recarga
            if (jugador.isQuiereSeguir() && jugador.getSaldo() > 0) {
                System.out.println("\n" + jugador.getNombre() + ", tu saldo actual es de " + jugador.getSaldo() + " fichas.");
                int apuesta = 0;
                while (true) {
                    System.out.print("¿Cuánto deseas apostar para esta ronda? (entre 1 y " + jugador.getSaldo() + "): ");
                    try {
                        apuesta = teclado.nextInt();
                        teclado.nextLine(); // Consumir el salto de línea
                        if (apuesta > 0 && apuesta <= jugador.getSaldo()) {
                            break; // Apuesta válida, salimos del bucle de apuesta
                        } else {
                            System.out.println("Apuesta no válida. Debe ser mayor que 0 y no puede exceder tu saldo.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("¡Error! Entrada no válida. Por favor, ingresa un número.");
                        teclado.nextLine(); // Limpiar el buffer
                    }
                }
                jugador.setApuestaActual(apuesta);
                System.out.println(jugador.getNombre() + " ha apostado " + apuesta + " fichas.");
            } else {
                // Si llegó aquí y no quiere seguir o no tiene saldo, aseguramos apuesta en 0
                jugador.setApuestaActual(0);
            }
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

    public HashMap<String, EstadisticasJugador> getEstadisticasJugadores() {
        return estadisticasJugadores;
    }

    public void setEstadisticasJugadores(HashMap<String, EstadisticasJugador> estadisticasJugadores) {
        this.estadisticasJugadores = estadisticasJugadores;
    }
}
