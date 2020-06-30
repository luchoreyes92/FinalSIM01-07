package Logica;

public class Solicitante {

    private Integer numero;
    private Float horaLlegada;
    private Float horaSalida;
    private String estado;
    private Float finFormulario;


    public Solicitante(Integer numero, Float horaLlegada) {
        this.numero = numero;
        this.horaLlegada = horaLlegada;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Float getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(Float horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public Float getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Float horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Float getFinFormulario() {
        return finFormulario;
    }

    public void setFinFormulario(Float finFormulario) {
        this.finFormulario = finFormulario;
    }

}
