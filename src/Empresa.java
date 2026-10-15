import java.time.LocalDate;
import java.util.ArrayList;

public class Empresa {
    private String nit;
    private String nombre;
    private ArrayList<Sede> listaSedes;
    private ArrayList<Equipo> listaEquipos;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<Operador> listaOperadores;
    private ArrayList<Reserva> listaReservas;

    public Empresa(String nit, String nombre) {
        this.nit = nit;
        this.nombre = nombre;
        listaSedes = new ArrayList<>();
        listaEquipos = new ArrayList<>();
        listaClientes = new ArrayList<>();
        listaOperadores = new ArrayList<>();
        listaReservas = new ArrayList<>();
    }

    public String getNit() {
        return nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Equipo buscarEquipo(String codigo) {
        for (Equipo e : listaEquipos) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String identificacion) {
        for (Cliente c : listaClientes) {
            if (c.identificacion().equals(identificacion)) {
                return c;
            }
        }
        return null;
    }

    public Operador buscarOperador(String identificacion) {
        for (Operador o : listaOperadores) {
            if (o.identificacion().equals(identificacion)) {
                return o;
            }
        }
        return null;
    }

    public Reserva buscarReserva(String codigo) {
        for (Reserva r : listaReservas) {
            if (r.getCodigo().equals(codigo)) {
                return r;
            }
        }
        return null;
    }

    private Sede buscarSede(String codigo) {
        for (Sede s : listaSedes) {
            if (s.getCodigo().equals(codigo)) {
                return s;
            }
        }
        return null;
    }

    public boolean registrarSede(Sede s) {
        if (s == null || buscarSede(s.getCodigo()) != null) {
            return false;
        }
        listaSedes.add(s);
        return true;
    }

    public boolean registrarEquipo(Equipo e) {
        if (e == null || buscarEquipo(e.getCodigo()) != null) {
            return false;
        }
        listaEquipos.add(e);
        return true;
    }

    public boolean registrarCliente(Cliente c) {
        if (c == null || buscarCliente(c.identificacion()) != null) {
            return false;
        }
        listaClientes.add(c);
        return true;
    }

    public boolean registrarOperador(Operador o) {
        if (o == null || buscarOperador(o.identificacion()) != null) {
            return false;
        }
        listaOperadores.add(o);
        return true;
    }

    public boolean registrarReserva(Reserva r) {
        if (r == null || buscarReserva(r.getCodigo()) != null) {
            return false;
        }
        if (buscarCliente(r.getCliente().identificacion()) == null) {
            return false;
        }
        listaReservas.add(r);
        return true;
    }

    public boolean asignarEquipoASede(String codSede, String codEquipo) {
        Sede s = buscarSede(codSede);
        Equipo e = buscarEquipo(codEquipo);
        if (s == null || e == null) {
            return false;
        }
        return s.agregarEquipo(e);
    }

    public boolean agregarEquipoAReserva(String codReserva, String codEquipo, int horas) {
        Reserva r = buscarReserva(codReserva);
        Equipo e = buscarEquipo(codEquipo);
        if (r == null || e == null) {
            return false;
        }
        return r.agregarEquipo(e, horas);
    }

    public boolean asignarOperador(String codReserva, String idOperador) {
        Reserva r = buscarReserva(codReserva);
        Operador o = buscarOperador(idOperador);
        if (r == null || o == null) {
            return false;
        }
        return r.asignarOperador(o);
    }

    public boolean cambiarEstado(String codReserva, EstadoReserva nuevo) {
        Reserva r = buscarReserva(codReserva);
        if (r == null || nuevo == null) {
            return false;
        }
        if (!r.puedeCambiarA(nuevo)) {
            return false;
        }
        if (nuevo == EstadoReserva.CONFIRMADA) {
            if (r.getListaDetallesReserva().isEmpty()) {
                return false;
            }
            if (r.tieneDron() && r.getOperador() == null) {
                return false;
            }
            for (DetalleReserva d : r.getListaDetallesReserva()) {
                Equipo e = d.equipo();
                if (!e.estaDisponible()
                        || equipoOcupado(e, r.getFecha(), r.getTurno(), r)) {
                    return false;
                }
            }
        }
        r.setEstado(nuevo);
        return true;
    }

    private boolean equipoOcupado(Equipo e, LocalDate fecha, Turno turno, Reserva excluir) {
        for (Reserva r : listaReservas) {
            if (r != excluir
                    && r.getEstado() == EstadoReserva.CONFIRMADA
                    && r.getFecha().equals(fecha)
                    && r.getTurno() == turno
                    && r.contieneEquipo(e.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Equipo> consultarDisponibles(LocalDate fecha, Turno turno) {
        ArrayList<Equipo> disponibles = new ArrayList<>();
        for (Equipo e : listaEquipos) {
            if (e.estaDisponible() && !equipoOcupado(e, fecha, turno, null)) {
                disponibles.add(e);
            }
        }
        return disponibles;
    }

    public ArrayList<Equipo> consultarDisponibles(LocalDate fecha, Turno turno, String tipo) {
        ArrayList<Equipo> resultado = new ArrayList<>();
        for (Equipo e : consultarDisponibles(fecha, turno)) {
            if (e.getClass().getSimpleName().equalsIgnoreCase(tipo)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public ArrayList<Reserva> consultarConfirmadasPorCliente(String identificacion) {
        ArrayList<Reserva> resultado = new ArrayList<>();
        for (Reserva r : listaReservas) {
            if (r.getCliente().identificacion().equals(identificacion)
                    && r.getEstado() == EstadoReserva.CONFIRMADA) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public double calcularValorReserva(String codReserva) {
        Reserva r = buscarReserva(codReserva);
        if (r == null) {
            return -1;
        }
        return r.calcularValorTotal();
    }
}

