import java.util.ArrayList;
import java.util.Scanner;


public class Jugador implements BlackJack {
    private ArrayList<Carta> mano;
    private EstadoJugador estado;
    private String nombre;
    private int puntajeFinal;
    private boolean quiereSeguir = true;
    private int saldo;
    private int apuestaActual;
    private Scanner teclado;

    public Jugador(String nombre) {
        mano = new ArrayList<>();
        estado = EstadoJugador.EN_JUEGO;
        this.nombre = nombre;
        this.saldo = saldo;
        this.apuestaActual = 0;
        teclado = new Scanner(System.in);
    }

    public Jugador(String nombre,int saldo) {
        mano = new ArrayList<>();
        estado = EstadoJugador.EN_JUEGO;
        this.nombre = nombre;
        this.saldo = saldo;
        this.apuestaActual = 0;
        teclado = new Scanner(System.in);
    }


    public int getApuestaActual() {
        return apuestaActual;
    }

    public void setApuestaActual(int apuestaActual) {
        this.apuestaActual = apuestaActual;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public boolean isQuiereSeguir() {
        return quiereSeguir;
    }

    public void setQuiereSeguir(boolean quiereSeguir) {
        this.quiereSeguir = quiereSeguir;
    }

    public int getPuntajeFinal() {
        return puntajeFinal;
    }

    public void setPuntajeFinal(int puntajeFinal) {
        this.puntajeFinal = puntajeFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public EstadoJugador getEstado() {
        return estado;
    }

    public void setEstado(EstadoJugador estado) {
        this.estado = estado;
    }

    public void pedirCarta(Mazo mazo) {
        if(mazo==null) {
            throw new IllegalArgumentException(" el mazo no puedes ser nulo");
        }
        if (estaPlantado()) {
            System.out.println("El jugador ya esta plantado. No puede pedir una carta.");
            return;
        }
        Carta carta = mazo.repartirCarta();
        if(carta==null) {
            throw new IllegalArgumentException(" el mazo esta vacio no se pudo repartir la carta.");
        }
        mano.add(carta);
    }

    public boolean estaPlantado() {
        return estado == EstadoJugador.PLANTADO;
    }

    public int obtenerPuntaje() {
        int puntajeTotal = 0;
        int ases = 0;
        for (Carta carta : mano) {
            int valorCarta = carta.getValorNumerico();
            puntajeTotal += valorCarta;
            if (carta.getValor().equals(Valor.A)) {
                ases++;
            }
        }
        while (puntajeTotal > 21 && ases > 0) {
            puntajeTotal = puntajeTotal - 10;
            ases--;
        }
        return puntajeTotal;
    }

    public void mostrarMano() {
        int altura = 7; // Cada carta tiene 7 líneas
        ArrayList<String[]> cartasDibujadas = new ArrayList<>();
        // Guardamos cada carta dibujada en la lista
        System.out.println(nombre);
        for (Carta carta : mano) {
            cartasDibujadas.add(carta.dibujarCarta());
        }
        // Imprimimos línea por línea
        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < cartasDibujadas.size(); j++) {
                String[] carta = cartasDibujadas.get(j);
                System.out.print(carta[i] + " "); // Línea i de cada carta
            }
            System.out.println(); // Salto de línea después de imprimir todas las cartas en esa línea
        }
    }

    public void reiniciarJugador() {
        mano.clear();
        estado = EstadoJugador.EN_JUEGO;
        apuestaActual=0;
        puntajeFinal=0;
    }

    @Override
    public boolean tieneBlackJack() {
        return mano.size() == 2 && obtenerPuntaje() == 21;
    }
}



