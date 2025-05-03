import java.util.ArrayList;

public class Jugador {
private ArrayList<Carta> mano;
private EstadoJugador estado;
private String nombre;

public Jugador(String nombre) {
    mano = new ArrayList<>();
    estado=EstadoJugador.EN_JUEGO;
    this.nombre = nombre;
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
    if (estaPlantado()) {
        System.out.println("El jugador ya esta plantado. No puede pedir una carta.");
        return;
    }
    Carta carta=mazo.repartirCarta();
    mano.add(carta);
    }

    public void plantarse(){
    if (estado==EstadoJugador.EN_JUEGO) {
        estado=EstadoJugador.PLANTADO;
    }

    }
    public boolean estaPlantado(){
    return estado==EstadoJugador.PLANTADO;
    }
    public int obtenerPuntaje() {
    int puntajeTotal=0;
    for(int i=0; i<mano.size(); i++) {
        Carta carta=mano.get(i);
        int puntaje=0;
        if(carta.getValor().equals(Valor.A)&&puntajeTotal>21) {
            carta.setValorNumerico(1);
        }
        puntaje=carta.getValorNumerico();
        puntajeTotal+=puntaje;

    }
    return puntajeTotal;
    }

    public void mostrarMano() {
        int altura = 7; // Cada carta tiene 7 líneas
        ArrayList<String[]> cartasDibujadas = new ArrayList<>();
        // Guardamos cada carta dibujada en la lista
        System.out.println(nombre);
        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
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
    estado=EstadoJugador.EN_JUEGO;
    }

    }




