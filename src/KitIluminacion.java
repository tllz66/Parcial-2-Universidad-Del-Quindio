public class KitIluminacion extends Equipo {
    private static final double TARIFA_HORA = 10000;
    private static final double RECARGO_CONTROL = 12000;

    private int cantidadLuces;
    private boolean controlInalambrico;

    public KitIluminacion(String codigo, String marca, String modelo, CondicionEquipo condicion,
                          int cantidadLuces, boolean controlInalambrico) {
        super(codigo, marca, modelo, condicion);
        this.cantidadLuces = cantidadLuces;
        this.controlInalambrico = controlInalambrico;
    }

    public int getCantidadLuces() {
        return cantidadLuces;
    }

    public boolean isControlInalambrico() {
        return controlInalambrico;
    }

    @Override
    public double calcularCosto(int horas) {
        double costo = horas * TARIFA_HORA;
        if (controlInalambrico) {
            costo += RECARGO_CONTROL;
        }
        return costo;
    }
}
