import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Estadisticas {

    private static final String ARCHIVO_ESTADISTICAS = "estadisticas.txt";

    // Guarda las estadísticas sin borrar lo anterior
    public static void guardarEstadisticas(List<Jugador> jugadores, int rondasJugadas,
                                           Map<String, EstadisticasJugador> mapaEstadisticas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_ESTADISTICAS, true))) {
            LocalDateTime fechaHora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.write("=== PARTIDA registrada en: " + fechaHora.format(formato) + " ===\n");
            writer.write("Resumen de la partida (Rondas jugadas: " + rondasJugadas + ")\n");

            for (Jugador jugador : jugadores) {
                String nombre = jugador.getNombre();
                int saldoFinal = jugador.getSaldo();
                EstadisticasJugador est = mapaEstadisticas.getOrDefault(nombre, new EstadisticasJugador());

                writer.write("Jugador: " + nombre + "\n");
                writer.write("Saldo final: " + saldoFinal + " fichas\n");
                writer.write("Victorias: " + est.getVictorias() + "\n");
                writer.write("Empates: " + est.getEmpates() + "\n");
                writer.write("Derrotas (o abandonos): " + est.getDerrotas() + "\n");
                writer.write("Blackjacks conseguidos: " + est.getBlacjacks() + "\n");
                writer.write("------------------------------------------------\n");
            }
            writer.newLine();

        } catch (IOException e) {
            System.err.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }

    // Muestra resumen de estadísticas en consola
    public static void mostrarEstadisticas(List<Jugador> jugadores, int rondasJugadas,
                                           Map<String, EstadisticasJugador> mapaEstadisticas) {

        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("\n=== RESULTADOS DE LA PARTIDA ===");
        System.out.println("Fecha y hora: " + fechaHora.format(formato));
        System.out.println("Rondas jugadas: " + rondasJugadas + "\n");

        int maxFichas = Integer.MIN_VALUE, minFichas = Integer.MAX_VALUE;
        int maxVictorias = Integer.MIN_VALUE, maxEmpates = Integer.MIN_VALUE, maxDerrotas = Integer.MIN_VALUE;

        List<String> mejoresJugadores = new ArrayList<>();
        List<String> jugadoresMasEmpates = new ArrayList<>();
        List<String> jugadoresMasDerrotas = new ArrayList<>();

        List<Jugador> jugadoresMasFichas = new ArrayList<>();
        List<Jugador> jugadoresMenosFichas = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            String nombre = jugador.getNombre();
            int saldo = jugador.getSaldo();
            EstadisticasJugador est = mapaEstadisticas.getOrDefault(nombre, new EstadisticasJugador());

            System.out.println("Jugador: " + nombre);
            System.out.println("Saldo final: " + saldo + " fichas");
            System.out.println("Victorias: " + est.getVictorias());
            System.out.println("Empates: " + est.getEmpates());
            System.out.println("Derrotas (o abandonos): " + est.getDerrotas());
            System.out.println("Blackjacks conseguidos: " + est.getBlacjacks());
            System.out.println("------------------------------------------------");

            // Comparaciones para máximos
            if (est.getVictorias() > maxVictorias) {
                maxVictorias = est.getVictorias();
                mejoresJugadores.clear();
                mejoresJugadores.add(nombre);
            } else if (est.getVictorias() == maxVictorias) {
                mejoresJugadores.add(nombre);
            }

            if (est.getEmpates() > maxEmpates) {
                maxEmpates = est.getEmpates();
                jugadoresMasEmpates.clear();
                jugadoresMasEmpates.add(nombre);
            } else if (est.getEmpates() == maxEmpates) {
                jugadoresMasEmpates.add(nombre);
            }

            if (est.getDerrotas() > maxDerrotas) {
                maxDerrotas = est.getDerrotas();
                jugadoresMasDerrotas.clear();
                jugadoresMasDerrotas.add(nombre);
            } else if (est.getDerrotas() == maxDerrotas) {
                jugadoresMasDerrotas.add(nombre);
            }

            if (saldo > maxFichas) {
                maxFichas = saldo;
                jugadoresMasFichas.clear();
                jugadoresMasFichas.add(jugador);
            } else if (saldo == maxFichas) {
                jugadoresMasFichas.add(jugador);
            }

            if (saldo < minFichas) {
                minFichas = saldo;
                jugadoresMenosFichas.clear();
                jugadoresMenosFichas.add(jugador);
            } else if (saldo == minFichas) {
                jugadoresMenosFichas.add(jugador);
            }
        }
        // Mostrar los resultados finales
        System.out.println();
        System.out.println("🏆 Jugador(es) con más victorias: " + String.join(", ", mejoresJugadores) + " (" + maxVictorias + ")");
        System.out.print("💰 Jugador(es) con más fichas: ");
        for (Jugador j : jugadoresMasFichas) {
            System.out.print(j.getNombre() + " ");
        }
        System.out.println("(" + maxFichas + ")");

        System.out.print("💸 Jugador(es) con menos fichas: ");
        for (Jugador j : jugadoresMenosFichas) {
            System.out.print(j.getNombre() + " ");
        }
        System.out.println("(" + minFichas + ")");

        System.out.println("❌ Jugador(es) con más derrotas: " + String.join(", ", jugadoresMasDerrotas) + " (" + maxDerrotas + ")");
        System.out.println("🤝 Jugador(es) con más empates: " + String.join(", ", jugadoresMasEmpates) + " (" + maxEmpates + ")");
    }
}