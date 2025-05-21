import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Partida partida = new Partida();

        System.out.println("██████╗ ██╗      █████╗  ██████╗██╗  ██╗      ██████╗  █████╗  ██████╗██╗  ██╗");
        System.out.println("██╔══██╗██║     ██╔══██╗██╔════╝██║ ██╔╝     ██╔════╝ ██╔══██╗██╔════╝██║ ██╔╝");
        System.out.println("██████╔╝██║     ███████║██║     █████╔╝█████╗██║  ███╗███████║██║     █████╔╝ ");
        System.out.println("██╔═══╝ ██║     ██╔══██║██║     ██╔═██╗╚════╝██║   ██║██╔══██║██║     ██╔═██╗ ");
        System.out.println("██║     ███████╗██║  ██║╚██████╗██║  ██╗     ╚██████╔╝██║  ██║╚██████╗██║  ██╗");
        System.out.println("╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝      ╚═════╝ ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝");
        System.out.println();
        System.out.println("          Bienvenido al Black-Jack de Jorge 🃏");
        System.out.println("        ──────────────────────────────────────");

        int opcion;
        do {
            System.out.println("¿Qué desea hacer? Escoja una opción (1-3):");
            System.out.println("1. Iniciar Partida");
            System.out.println("2. Registrar Jugadores ");
            System.out.println("3. Salir");
            try{
                opcion = teclado.nextInt();
                teclado.nextLine();
            }catch(Exception e){
                System.out.println("Opcion no valida. tine que poner u n numero");
                teclado.nextLine();
                opcion=0;
                continue;
            }


            switch (opcion) {
                case 1:
                    if (partida.getJugadores().isEmpty()) {
                        System.out.println(" Error , Debe registarse primero");
                    } else {
                        System.out.println(" Mucha suerte ");
                       partida.ejecutarPartidaCompleta();
                    }
                    break;
                case 2:
                    String opcion1;
                    do {
                        System.out.print("Introduzca el nombre del jugador: ");
                        String nombre = teclado.nextLine();
                        System.out.println(" Diga cuantas fichas quiere empezar ");
                        int fichas = teclado.nextInt();
                        teclado.nextLine();
                        Jugador jugador = new Jugador(nombre, fichas);
                        partida.añadirJugador(jugador);
                        System.out.println(jugador.getNombre() + ", bienvenido al BlackjacK");

                        System.out.print(" Desea registrar a otro jugador? (si/no): ");
                        opcion1 = teclado.nextLine().trim().toLowerCase();
                    } while (opcion1.equals("si"));
                    break;
                case 3:
                    System.out.println("Gracias por jugar al Blackjack ");
                    break;
                default:
                    System.out.println("❌ Opción no válida. Inténtelo de nuevo.");
            }

        } while (opcion != 3);

        teclado.close();
    }
}
