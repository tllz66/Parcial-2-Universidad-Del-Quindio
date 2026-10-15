public class Dron extends Equipo{
    private static final double TARIFA_HORA = 18000;
    private static final double RECARGO_BATERIA = 20000;

    private int autonomiaMinutos;
    private boolean bateriaAdicional;

    public Dron(String codigo, String marca, String modelo, CondicionEquipo condicion,
                int autonomiaMinutos, boolean bateriaAdicional) {
        super(codigo, marca, modelo, condicion);
        this.autonomiaMinutos = autonomiaMinutos;
        this.bateriaAdicional = bateriaAdicional;
    }

    public int getAutonomiaMinutos() {
        return autonomiaMinutos;
    }

    public boolean isBateriaAdicional() {
        return bateriaAdicional;
    }

    @Override
    public double calcularCosto(int horas) {
        double costo = horas * TARIFA_HORA;
        if (bateriaAdicional) {
            costo += RECARGO_BATERIA;
        }
        return costo;
    }
}
