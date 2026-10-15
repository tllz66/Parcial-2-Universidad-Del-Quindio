import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Main {

    private static final Empresa empresa = new Empresa("900123456-7", "PixelRent");

    public static void main(String[] args) {
        int opcion = -1;
        do {
            String menu = """
                    ===== SISTEMA PIXELRENT =====
                    1. Registrar sede
                    2. Registrar equipo
                    3. Asignar equipo a una sede
                    4. Registrar cliente
                    5. Registrar operador
                    6. Registrar reserva
                    7. Agregar equipo a una reserva
                    8. Asignar operador a una reserva
                    9. Cambiar estado de una reserva
                    10. Consultar equipos disponibles (fecha y turno)
                    11. Consultar reservas confirmadas de cliente
                    12. Calcular valor total de una reserva
                    13. Ver detalle de una reserva
                    14. Cargar datos de prueba
                    0. Salir
                    
                    Seleccione una opción:
                    """;

            opcion = leerEntero(menu);

            switch (opcion) {
                case 1 -> registrarSede();
                case 2 -> registrarEquipo();
                case 3 -> asignarEquipoASede();
                case 4 -> registrarCliente();
                case 5 -> registrarOperador();
                case 6 -> registrarReserva();
                case 7 -> agregarEquipoAReserva();
                case 8 -> asignarOperadorAReserva();
                case 9 -> cambiarEstadoReserva();
                case 10 -> consultarDisponibles();
                case 11 -> consultarConfirmadasPorCliente();
                case 12 -> calcularValorReserva();
                case 13 -> verReserva();
                case 14 -> cargarDatosPrueba();
                case 0 -> mostrarMensaje("¡Hasta luego!");
                default -> mostrarMensaje("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    // ================= OPCIONES DEL MENÚ =================

    private static void registrarSede() {
        String codigo = leerTexto("Código de la sede:");
        if (codigo.isEmpty()) return;
        String nombre = leerTexto("Nombre de la sede:");
        String direccion = leerTexto("Dirección:");

        resultado(empresa.registrarSede(new Sede(codigo, nombre, direccion)),
                "Sede registrada exitosamente.",
                "No se pudo registrar: el código ya existe.");
    }

    private static void registrarEquipo() {
        String[] opciones = {"Cámara", "Dron", "Kit de Iluminación"};
        int tipo = JOptionPane.showOptionDialog(null, "Seleccione el tipo de equipo:",
                "Tipo de Equipo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (tipo == JOptionPane.CLOSED_OPTION) return;

        String codigo = leerTexto("Código del equipo:");
        if (codigo.isEmpty()) return;
        String marca = leerTexto("Marca:");
        String modelo = leerTexto("Modelo:");
        CondicionEquipo condicion = leerCondicion();

        Equipo equipo = switch (tipo) {
            case 0 -> {
                String resolucion = leerTexto("Resolución máxima (ej. 4K, 8K):");
                boolean estabilizador = leerBoolean("¿Tiene estabilizador?");
                yield new Camara(codigo, marca, modelo, condicion, resolucion, estabilizador);
            }
            case 1 -> {
                int autonomia = leerEntero("Autonomía de vuelo (minutos):");
                boolean bateria = leerBoolean("¿Tiene batería adicional?");
                yield new Dron(codigo, marca, modelo, condicion, autonomia, bateria);
            }
            default -> {
                int luces = leerEntero("Cantidad de luces:");
                boolean control = leerBoolean("¿Tiene control inalámbrico?");
                yield new KitIluminacion(codigo, marca, modelo, condicion, luces, control);
            }
        };

        resultado(empresa.registrarEquipo(equipo),
                "Equipo registrado exitosamente.",
                "No se pudo registrar: el código ya existe.");
    }

    private static void asignarEquipoASede() {
        String codSede = leerTexto("Código de la sede:");
        String codEquipo = leerTexto("Código del equipo:");
        resultado(empresa.asignarEquipoASede(codSede, codEquipo),
                "Equipo asignado a la sede.",
                "No se pudo asignar: la sede o el equipo no existen, o el equipo ya está asignado a esa sede.");
    }

    private static void registrarCliente() {
        String nombre = leerTexto("Nombre completo del cliente:");
        String id = leerTexto("Número de identificación:");
        String telefono = leerTexto("Teléfono de contacto:");
        String correo = leerTexto("Correo electrónico:");

        resultado(empresa.registrarCliente(new Cliente(nombre, id, telefono, correo)),
                "Cliente registrado exitosamente.",
                "No se pudo registrar: la identificación ya existe.");
    }

    private static void registrarOperador() {
        String id = leerTexto("Número de identificación del operador:");
        String nombre = leerTexto("Nombre completo:");
        String certificacion = leerTexto("Certificación oficial:");
        int anios = leerEntero("Años de experiencia:");

        resultado(empresa.registrarOperador(new Operador(id, nombre, certificacion, anios)),
                "Operador registrado exitosamente.",
                "No se pudo registrar: la identificación ya existe.");
    }

    private static void registrarReserva() {
        String codigo = leerTexto("Código único de la reserva:");
        String idCliente = leerTexto("Identificación del cliente:");
        Cliente cliente = empresa.buscarCliente(idCliente);

        if (cliente == null) {
            mostrarMensaje("El cliente con identificación " + idCliente + " no se encuentra registrado.");
            return;
        }

        LocalDate fecha = leerFecha();
        Turno turno = leerTurno();

        resultado(empresa.registrarReserva(new Reserva(codigo, cliente, fecha, turno)),
                "Reserva creada exitosamente en estado SOLICITADA.",
                "No se pudo crear: el código de reserva ya existe.");
    }

    private static void agregarEquipoAReserva() {
        String codReserva = leerTexto("Código de la reserva:");
        String codEquipo = leerTexto("Código del equipo a incluir:");
        int horas = leerEntero("Cantidad de horas de uso:");

        resultado(empresa.agregarEquipoAReserva(codReserva, codEquipo, horas),
                "Equipo agregado a la reserva.",
                "No se pudo agregar: verifique que la reserva y el equipo existan, "
                        + "que el equipo esté disponible, no repetido, horas > 0 y la reserva esté SOLICITADA.");
    }

    private static void asignarOperadorAReserva() {
        String codReserva = leerTexto("Código de la reserva:");
        String idOperador = leerTexto("Identificación del operador:");

        resultado(empresa.asignarOperador(codReserva, idOperador),
                "Operador asignado a la reserva exitosamente.",
                "No se pudo asignar: revise si la reserva u operador existen, o si la reserva ya tenía operador.");
    }

    private static void cambiarEstadoReserva() {
        String codReserva = leerTexto("Código de la reserva:");
        String[] estados = {"Confirmar", "Cancelar", "Finalizar"};
        int op = JOptionPane.showOptionDialog(null, "Seleccione el nuevo estado:",
                "Cambio de Estado", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, estados, estados[0]);

        if (op == JOptionPane.CLOSED_OPTION) return;

        EstadoReserva nuevo = switch (op) {
            case 0 -> EstadoReserva.CONFIRMADA;
            case 1 -> EstadoReserva.CANCELADA;
            default -> EstadoReserva.FINALIZADA;
        };

        resultado(empresa.cambiarEstado(codReserva, nuevo),
                "Estado de la reserva cambiado a " + nuevo + ".",
                "No se pudo cambiar el estado: no se cumplen las condiciones del negocio.");
    }

    private static void consultarDisponibles() {
        LocalDate fecha = leerFecha();
        Turno turno = leerTurno();

        String[] opciones = {"Todos", "Cámaras", "Drones", "Kits Iluminación"};
        int filtro = JOptionPane.showOptionDialog(null, "Filtrar por tipo de equipo:",
                "Consulta de Disponibilidad", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        ArrayList<Equipo> lista = switch (filtro) {
            case 1 -> empresa.consultarDisponibles(fecha, turno, "Camara");
            case 2 -> empresa.consultarDisponibles(fecha, turno, "Dron");
            case 3 -> empresa.consultarDisponibles(fecha, turno, "KitIluminacion");
            default -> empresa.consultarDisponibles(fecha, turno);
        };

        if (lista.isEmpty()) {
            mostrarMensaje("No hay equipos disponibles para la fecha " + fecha + " y turno " + turno + ".");
        } else {
            StringBuilder sb = new StringBuilder("Equipos disponibles (" + lista.size() + "):\n\n");
            for (Equipo e : lista) {
                sb.append("• ").append(e).append("\n");
            }
            mostrarMensaje(sb.toString());
        }
    }

    private static void consultarConfirmadasPorCliente() {
        String id = leerTexto("Identificación del cliente:");
        ArrayList<Reserva> lista = empresa.consultarConfirmadasPorCliente(id);

        if (lista.isEmpty()) {
            mostrarMensaje("El cliente no posee reservas en estado CONFIRMADA.");
        } else {
            StringBuilder sb = new StringBuilder("Reservas Confirmadas (" + lista.size() + "):\n\n");
            for (Reserva r : lista) {
                sb.append("• ").append(r).append("\n");
            }
            mostrarMensaje(sb.toString());
        }
    }

    private static void calcularValorReserva() {
        String codigo = leerTexto("Código de la reserva:");
        double valor = empresa.calcularValorReserva(codigo);

        if (valor < 0) {
            mostrarMensaje("La reserva " + codigo + " no existe.");
        } else {
            mostrarMensaje("Valor total de la reserva " + codigo + ": " + formato(valor));
        }
    }

    private static void verReserva() {
        String codigo = leerTexto("Código de la reserva:");
        Reserva r = empresa.buscarReserva(codigo);

        if (r == null) {
            mostrarMensaje("La reserva " + codigo + " no existe.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(r).append("\n");
        sb.append("Operador asignado: ")
                .append(r.getOperador() == null ? "Sin asignar" : r.getOperador().nombreCompleto())
                .append("\n\n");

        sb.append("--- Equipos Incluidos ---\n");
        if (r.getListaDetallesReserva().isEmpty()) {
            sb.append("(Sin equipos agregados)\n");
        } else {
            for (DetalleReserva d : r.getListaDetallesReserva()) {
                sb.append("• ").append(d.equipo().getMarca()).append(" ").append(d.equipo().getModelo())
                        .append(" | ").append(d.horas()).append(" hrs | Subtotal: ")
                        .append(formato(d.calcularSubtotal())).append("\n");
            }
        }
        sb.append("\nTOTAL RESERVA: ").append(formato(r.calcularValorTotal()));

        mostrarMensaje(sb.toString());
    }

    private static void cargarDatosPrueba() {
        Sede sede = new Sede("S01", "Sede Centro", "Calle 10 # 5-20");
        Camara cam = new Camara("C001", "Canon", "R5", CondicionEquipo.DISPONIBLE, "8K", true);
        Dron dron = new Dron("D001", "DJI", "Mavic 3", CondicionEquipo.DISPONIBLE, 46, true);
        KitIluminacion kit = new KitIluminacion("K001", "Aputure", "300d", CondicionEquipo.DISPONIBLE, 4, true);

        Cliente ana = new Cliente("Ana Pérez", "1001", "3001112222", "ana@mail.com");
        Cliente luis = new Cliente("Luis Gómez", "1002", "3003334444", "luis@mail.com");
        Operador op = new Operador("2001", "Carlos Ruiz", "Piloto certificado", 5);
        LocalDate fecha = LocalDate.of(2026, 11, 20);

        if (!empresa.registrarSede(sede)) {
            mostrarMensaje("Los datos de prueba ya se encontraban cargados previamente.");
            return;
        }

        empresa.registrarEquipo(cam);
        empresa.registrarEquipo(dron);
        empresa.registrarEquipo(kit);
        empresa.asignarEquipoASede("S01", "C001");
        empresa.registrarCliente(ana);
        empresa.registrarCliente(luis);
        empresa.registrarOperador(op);

        empresa.registrarReserva(new Reserva("R001", ana, fecha, Turno.MANANA));
        empresa.registrarReserva(new Reserva("R002", luis, fecha, Turno.MANANA));

        empresa.agregarEquipoAReserva("R001", "C001", 3);
        empresa.agregarEquipoAReserva("R001", "D001", 2);
        empresa.agregarEquipoAReserva("R001", "K001", 4);

        mostrarMensaje("""
                ¡Datos de prueba cargados exitosamente!
                • Sede: S01
                • Equipos: C001 (Cámara), D001 (Dron), K001 (Kit)
                • Clientes: 1001 (Ana), 1002 (Luis)
                • Operador: 2001 (Carlos Ruiz)
                • Reservas: R001 y R002 (Fecha: 2026-11-20, Turno: Mañana)
                """);
    }

    // ================= MÉTODOS AUXILIARES DE ENTRADA Y SALIDA =================

    private static String leerTexto(String mensaje) {
        String entrada = JOptionPane.showInputDialog(null, mensaje, "PixelRent", JOptionPane.QUESTION_MESSAGE);
        return (entrada == null) ? "" : entrada.trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            String entrada = JOptionPane.showInputDialog(null, mensaje, "PixelRent", JOptionPane.QUESTION_MESSAGE);
            if (entrada == null) return 0; // Si presiona cancelar o cierra la ventana
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                mostrarMensaje("Error: Debe ingresar un número entero válido.");
            }
        }
    }

    private static boolean leerBoolean(String mensaje) {
        int resp = JOptionPane.showConfirmDialog(null, mensaje, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return resp == JOptionPane.YES_OPTION;
    }

    private static LocalDate leerFecha() {
        while (true) {
            String texto = leerTexto("Ingrese la fecha (AAAA-MM-DD):");
            if (texto.isEmpty()) return LocalDate.now();
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                mostrarMensaje("Fecha inválida. Respete el formato AAAA-MM-DD (ejemplo: 2026-11-20).");
            }
        }
    }

    private static Turno leerTurno() {
        Turno[] turnos = Turno.values();
        int seleccion = JOptionPane.showOptionDialog(null, "Seleccione el Turno:",
                "Turnos", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, turnos, turnos[0]);

        return (seleccion == JOptionPane.CLOSED_OPTION) ? Turno.MANANA : turnos[seleccion];
    }

    private static CondicionEquipo leerCondicion() {
        CondicionEquipo[] condiciones = CondicionEquipo.values();
        int seleccion = JOptionPane.showOptionDialog(null, "Seleccione la Condición del Equipo:",
                "Condición de Equipo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, condiciones, condiciones[0]);

        return (seleccion == JOptionPane.CLOSED_OPTION) ? CondicionEquipo.DISPONIBLE : condiciones[seleccion];
    }

    private static void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "PixelRent - Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void resultado(boolean ok, String exito, String fallo) {
        if (ok) {
            JOptionPane.showMessageDialog(null, exito, "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, fallo, "Atención / Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static String formato(double valor) {
        return String.format("$%,.0f", valor);
    }
}