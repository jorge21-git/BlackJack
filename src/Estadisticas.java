
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Estadisticas {

    private static final String ARCHIVO_ESTADISTICAS = "estadisticas.txt";

    public static void guardarEstadisticas(List<Jugador> jugadores, int numRondas) {
        // Cambiado para que NO sea append, así se sobreescribe cada vez
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_ESTADISTICAS))) {
            bw.write("Resumen de la partida (Rondas jugadas: " + numRondas + ")\n");
            for (Jugador jugador : jugadores) {
                bw.write("Jugador: " + jugador.getNombre() + "\n");
                bw.write("Saldo final: " + jugador.getSaldo() + "\n");
                bw.write("Estado final: " + jugador.getEstado() + "\n");
                bw.write("------------------------------------------------\n");
            }
            bw.write("\n");
            System.out.println("Estadísticas guardadas en " + ARCHIVO_ESTADISTICAS);
        } catch (IOException e) {
            System.out.println("Error al guardar las estadísticas: " + e.getMessage());
        }
    }

    public static void mostrarEstadisticas() {
        System.out.println("Mostrando archivo de estadísticas:");

        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(ARCHIVO_ESTADISTICAS))) {
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error al leer las estadísticas: " + e.getMessage());
        }
    }
}
