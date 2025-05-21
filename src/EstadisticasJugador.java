public class EstadisticasJugador {
    private int victorias;
    private int empates;
    private int derrotas;
    private int blacjacks;

    public EstadisticasJugador() {
        this.victorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.blacjacks = 0;
    }
    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getEmpates() {
        return empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getBlacjacks() {
        return blacjacks;
    }

    public void setBlacjacks(int blacjacks) {
        this.blacjacks = blacjacks;
    }
    public void guardarVictorias() {
        victorias++;
    }
    public void guardarEmpates() {
        empates++;
    }
    public void guardarDerrotas() {
        derrotas++;
    }
    public void guardarBlacjacks() {
        blacjacks++;
    }

}
