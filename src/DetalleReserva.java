public record DetalleReserva(Equipo equipo, int horas) {

    public DetalleReserva{
        if(equipo == null || horas <=0){
            throw new IllegalArgumentException("Detalle inválido");
        }
    }
    public double calcularSubtotal(){
        return  equipo.calcularCosto(horas);
    }
}
