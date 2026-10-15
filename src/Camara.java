public class Camara extends Equipo {
    private static final double TARIFA_HORA = 12000;
    private static final double RECARGO_ESTABILIZADOR = 15000;

    private String resolucionMaxima;
    private boolean estabilizador;

    public Camara(String codigo, String marca, String modelo, CondicionEquipo condicion,
                  String resolucionMaxima, boolean estabilizador) {
        super(codigo, marca, modelo, condicion);
        this.resolucionMaxima = resolucionMaxima;
        this.estabilizador = estabilizador;
    }

    public String getResolucionMaxima() {
        return resolucionMaxima;
    }

    public boolean isEstabilizador() {
        return estabilizador;
    }

    @Override
    public double calcularCosto(int horas) {
        double costo = horas * TARIFA_HORA;
        if (estabilizador) {
            costo += RECARGO_ESTABILIZADOR;
        }
        return costo;
    }
}


