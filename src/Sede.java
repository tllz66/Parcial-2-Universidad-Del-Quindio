import java.util.ArrayList;
public class Sede {
    private String codigo;
    private String nombre;
    private String direccion;
    private ArrayList<Equipo> listaEquipos;

    public Sede(String codigo, String nombre, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.listaEquipos = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public ArrayList<Equipo> getListaEquipos() {
        return new ArrayList<>(listaEquipos);
    }

    public boolean agregarEquipo(Equipo e) {
        if (e == null) {
            return false;
        }
        for (Equipo existente : listaEquipos) {
            if (existente.getCodigo().equals(e.getCodigo())) {
                return false;
            }
        }
        listaEquipos.add(e);
        return true;
    }
}