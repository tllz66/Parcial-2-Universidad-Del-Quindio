import java.time.LocalDate;
import java.util.ArrayList;

public class Reserva {

    private final String codigo;
    private final Cliente cliente;
    private final LocalDate fecha;
    private final Turno turno;
    private EstadoReserva estado;
    private Operador operador;
    private ArrayList<DetalleReserva> listaDetallesReserva;

    public Reserva(String codigo, Cliente cliente, LocalDate fecha, Turno turno) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.fecha = fecha;
        this.turno = turno;
        this.estado = EstadoReserva.SOLICITADA;
        this.operador = null;
        this.listaDetallesReserva = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Turno getTurno() {
        return turno;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public Operador getOperador() {
        return operador;
    }

    public ArrayList<DetalleReserva> getListaDetallesReserva() {
        return new ArrayList<>(listaDetallesReserva);
    }

    void setEstado(EstadoReserva nuevo) {
        this.estado = nuevo;
    }
    public boolean agregarEquipo(Equipo e, int horas) {
        if (e == null || horas <= 0) {
            return false;
        }
        if (estado != EstadoReserva.SOLICITADA) {
            return false;
        }
        if (!e.estaDisponible()) {
            return false;
        }
        if (contieneEquipo(e.getCodigo())) {
            return false;
        }
        listaDetallesReserva.add(new DetalleReserva(e, horas));
        return true;
    }

    public boolean contieneEquipo(String codigoEquipo) {
        for (DetalleReserva d : listaDetallesReserva) {
            if (d.equipo().getCodigo().equals(codigoEquipo)) {
                return true;
            }
        }
        return false;
    }

    public boolean tieneDron() {
        for (DetalleReserva d : listaDetallesReserva) {
            if (d.equipo() instanceof Dron) {
                return true;
            }
        }
        return false;
    }

    public boolean asignarOperador(Operador o) {
        if (o == null || operador != null) {
            return false;
        }
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.FINALIZADA) {
            return false;
        }
        this.operador = o;
        return true;
    }

    public boolean puedeCambiarA(EstadoReserva nuevo) {
        return switch (estado) {
            case SOLICITADA -> nuevo == EstadoReserva.CONFIRMADA || nuevo == EstadoReserva.CANCELADA;
            case CONFIRMADA -> nuevo == EstadoReserva.CANCELADA || nuevo == EstadoReserva.FINALIZADA;
            default -> false;
        };
    }

    public double calcularValorTotal() {
        double total = 0;
        for (DetalleReserva d : listaDetallesReserva) {
            total += d.calcularSubtotal();
        }
        return total;
    }

    public int calcularTotalHoras() {
        int totalHoras = 0;
        for (DetalleReserva d : listaDetallesReserva) {
            totalHoras += d.horas();
        }
        return totalHoras;
    }

    @Override
    public String toString() {
        return "Reserva " + codigo + " [" + cliente.nombreCompleto() + ", " + fecha + ", "
                + turno + ", " + estado + "]";
    }
}
