public abstract class Equipo {
    protected String codigo;
    protected String marca;
    protected String modelo;
    protected CondicionEquipo condicion;

    public Equipo(String codigo, String marca, String modelo, CondicionEquipo condicion) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.condicion = condicion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public CondicionEquipo getCondicion() {
        return condicion;
    }




    public void setCondicion(CondicionEquipo condicion) {
        this.condicion = condicion;
    }
    public boolean estaDisponible() {
        return condicion == CondicionEquipo.DISPONIBLE;
    }
    public abstract double calcularCosto(int horas);

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + codigo + " - " + marca + " " + modelo
                + " - " + condicion + "]";
    }

}
