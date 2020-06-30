/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

/**
 *
 * @author lucho
 */
public class Empleado {
    private Integer numero;
    private String estado;
    private Float finAtencion;
    private Integer nroSolicitante;
    
    public Empleado(Integer numero, String estado) {
        this.numero = numero;
        this.estado = estado;
    }

    
    
    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    

    public Float getFinAtencion() {
        return finAtencion;
    }

    public void setFinAtencion(Float finAtencion) {
        this.finAtencion = finAtencion;
    }

    public Integer getNroSolicitante() {
        return nroSolicitante;
    }

    public void setNroSolicitante(Integer nroSolicitante) {
        this.nroSolicitante = nroSolicitante;
    }

    
    
}
