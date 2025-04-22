import java.util.ArrayList;

public class Mazo {
    private ArrayList<Carta> cartas;
    private int numMazos;

    public Mazo() {
        cartas = new ArrayList<>();
    }

    public int getNumMazos() {
        return numMazos;
    }

    public void setNumMazos(int numMazos) {
        this.numMazos = numMazos;
    }

    public ArrayList<Carta> getMazos() {
        return cartas;
    }

    public void setMazos(ArrayList<Carta> mazos) {
        this.cartas = mazos;
    }

    public void generarBaraja(){
        for (int i = 0; i < Palo.values().length; i++) {
            for (int j = 0; j < Valor.values().length; j++) {
                Carta carta=new Carta(Palo.values()[i],Valor.values()[j]);
                cartas.add(carta);
            }
        }
    }
    public void generarMazos(int numMazos){
       for (int i = 0; i < numMazos; i++) {
           generarBaraja();
       }

    }
    public void mezclarMazos(){
        for (int i = cartas.size()-1; i >0; i--) {
            int j= (int) (Math.random()*(i+1));
            Carta carta=cartas.get(i);
            cartas.set(i,cartas.get(j));
            cartas.set(j,carta);
        }
    }

}
