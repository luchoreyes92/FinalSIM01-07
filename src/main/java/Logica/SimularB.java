/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Distribuciones.ExponencialNegativa;
import Distribuciones.Poisson;
import Distribuciones.Uniforme;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author lucho
 */
public class SimularB {

    private ArrayList<Empleado> empleados = new ArrayList<>();
    private ArrayList<Solicitante> solicitantes = new ArrayList<>();
    private ArrayList<Solicitante> colaSolicitantes = new ArrayList<>();
    private ArrayList<Solicitante> colaFormularioSolicitantes = new ArrayList<>();
    private Object[] filaNueva = new Object[17];

    private Integer nroSolicitante = 1;

    private final Double mediaPoissonLlegada, mediaAtencion;

    public SimularB(Double mediaPoissonLlegada, Double mediaExpAtencion) {
        this.mediaPoissonLlegada = mediaPoissonLlegada;
        this.mediaAtencion = mediaExpAtencion;
        crearEmpleados();
    }

    public Object[] simularB(Object[] filaAnterior) {
        inicializarFila();
        prioridadColaFormularioSolicitante();
        if (esLlegadaSolicitante((Float) filaAnterior[3])) {
            this.filaNueva[0] = (Float) filaAnterior[3];
            Solicitante solicitante = new Solicitante(this.nroSolicitante, (Float) this.filaNueva[0]);
            this.nroSolicitante++;
            this.filaNueva[1] = Estado.LLEGADA_SOLICITANTE + " " + solicitante.getNumero();
            this.filaNueva[2] = (Float) Poisson.variableAleatoria(this.mediaPoissonLlegada);
            this.filaNueva[3] = (Float) this.filaNueva[0] + (Float) this.filaNueva[2];
            solicitante.setEstado(Estado.LLEGADA_SOLICITANTE);
            if (existeColaSolicitante(false)) {
                solicitante.setEstado(Estado.EN_COLA);
                this.filaNueva[4] = "-";
                this.filaNueva[5] = "-";
                this.filaNueva[6] = filaAnterior[6];
                this.filaNueva[7] = filaAnterior[7];
                this.filaNueva[8] = filaAnterior[8];
                this.filaNueva[9] = filaAnterior[9];
                this.filaNueva[10] = filaAnterior[10];
                this.filaNueva[11] = filaAnterior[11];
                this.filaNueva[12] = filaAnterior[12];
                this.filaNueva[13] = filaAnterior[13];
                this.filaNueva[14] =(Integer) filaAnterior[14] + 1;
                this.filaNueva[15] =  filaAnterior[15];
                this.colaSolicitantes.add(solicitante);
            } else { // PASA A SER ATENDIDO.
                solicitante.setEstado(Estado.SIENDO_ATENDIDO);
                this.filaNueva[4] = generarRandom();
                this.filaNueva[5] = ExponencialNegativa.
                        variableAleatoria((Integer) this.filaNueva[4], this.mediaAtencion);
                Integer index = this.empleados.indexOf(this.empleados.stream().
                        filter(x -> x.getEstado().
                        equals(Estado.LIBRE)).findFirst().get());
                this.empleados.get(index).setEstado(Estado.OCUPADO);
                this.empleados.get(index).setFinAtencion((Float) this.filaNueva[0] + (Float) this.filaNueva[5]);
                this.empleados.get(index).setNroSolicitante(solicitante.getNumero());
                llenarFilaEmpleados(index, filaAnterior);
                this.filaNueva[14] = filaAnterior[14];
                this.filaNueva[15] = filaAnterior[15];
            }
            this.filaNueva[16] = filaAnterior[16];
            this.solicitantes.add(solicitante);
        } else {
            if (esFinAtencion()) {
                this.filaNueva[0] = obtenerEmpleadoFinAtencion();
                Integer index = this.empleados.indexOf(this.empleados.stream().
                        filter(x -> x.getFinAtencion() != null).
                        filter(x -> x.getFinAtencion().
                        equals((Float) this.filaNueva[0])).findFirst().get());
                this.filaNueva[1] = Estado.FIN_ATENCION + " " + this.empleados.get(index).getNroSolicitante();
                this.filaNueva[2] = "-";
                this.filaNueva[3] = filaAnterior[3];
                Integer indexSolicitante = setearSolicitante(this.empleados.get(index).getNroSolicitante());
                this.empleados.get(index).setNroSolicitante(null);
                if (existeColaSolicitante(true)) {
                    this.filaNueva[4] = generarRandom();
                    this.filaNueva[5] = ExponencialNegativa.variableAleatoria((Integer) this.filaNueva[4], this.mediaAtencion);
                    this.empleados.get(index).setEstado(Estado.OCUPADO);
                    this.empleados.get(index).setFinAtencion((Float) this.filaNueva[0] + (Float) this.filaNueva[6]);
                    this.empleados.get(index).setNroSolicitante(this.colaSolicitantes.get(0).getNumero());
                    llenarFilaEmpleados(index, filaAnterior);
                    this.filaNueva[14] = (Integer) filaAnterior[14] - 1;
                    this.colaSolicitantes.remove(0);
                } else {  // ES EMPLEADO LIBRE
                    this.empleados.get(index).setEstado(Estado.LIBRE);
                    this.empleados.get(index).setFinAtencion(null);
                    llenarFilaEmpleados(index, filaAnterior);
                }
                this.filaNueva[15] = (Integer) filaAnterior[15] + 1;
                this.filaNueva[16] = (Float) filaAnterior[16]
                        + this.solicitantes.get(indexSolicitante).getHoraSalida()
                        - this.solicitantes.get(indexSolicitante).getHoraLlegada();
            }

        }

        return this.filaNueva;
    }

    private Boolean esLlegadaSolicitante(Float proximaLlegada) {
        if (this.empleados.stream().allMatch(x -> x.getEstado().equals(Estado.LIBRE))) {
            return true;
        } else {
            if (this.empleados.stream().anyMatch(x -> x.getFinAtencion() != null && x.getFinAtencion() < proximaLlegada)) {
                return false;
            } else {
                return true;
            }
        }
    }

    private Solicitante obtenerSolicitanteCola() {
        return this.colaSolicitantes.get(0);
    }

    private Integer setearSolicitante(Integer nroSolicitante) {
        Integer index = this.solicitantes.indexOf(this.solicitantes.stream().
                filter(x -> x.getNumero().equals(nroSolicitante)).findFirst().get());
        this.solicitantes.get(index).setEstado(Estado.FUERA_SISTEMA);
        this.solicitantes.get(index).setHoraSalida((Float) this.filaNueva[0]);
        return index;
    }

    private Boolean esFinAtencion() {
        if (this.colaFormularioSolicitantes.size() > 0) {
            if (this.empleados.stream().allMatch(x -> x.getEstado().equals(Estado.LIBRE))) {
                return false;
            }
            if (this.empleados.stream().anyMatch(x
                    -> x.getFinAtencion() != null && x.getFinAtencion() < this.colaFormularioSolicitantes.get(0).getFinFormulario())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private Float obtenerEmpleadoFinAtencion() {
        return this.empleados.stream().
                filter(x -> x.getFinAtencion() != null).
                min((x, y) -> x.getFinAtencion().
                compareTo(y.getFinAtencion())).get().getFinAtencion();
    }

    private Boolean existeColaFinFormularioSolicitante() {
        return this.empleados.stream().allMatch(x -> x.getEstado().equals(Estado.OCUPADO));

    }

    private Boolean existeColaSolicitante(Boolean esFinAtencion) {
        return esFinAtencion ? this.colaSolicitantes.size() > 0
                : this.empleados.stream().allMatch(x -> x.getEstado().equals(Estado.OCUPADO));
    }

    private Empleado ocuparProximoEmpleado() {
        for (Empleado empleado : empleados) {
            if (empleado.getEstado().equals(Estado.LIBRE)) {
                return empleado;
            }
        }
        return null;
    }

    private Integer obtenerNuevoSolicitante() {
        return this.colaFormularioSolicitantes.size() > 0
                ? this.colaFormularioSolicitantes.
                        get(this.colaFormularioSolicitantes.size() - 1).
                        getNumero() + 1 : 1;
    }

    private Integer generarRandom() {
        Integer random = Integer.parseInt(new DecimalFormat("0").format(Math.random() * 100));
        if (random == 100) {
            random--;
        }
        return random;
    }

    private void prioridadColaFormularioSolicitante() {
        if (this.colaFormularioSolicitantes.size() > 1) {
            this.colaFormularioSolicitantes.sort((x, y) -> x.getFinFormulario().compareTo(y.getFinFormulario()));
        }
    }

    private void inicializarFila() {
        this.filaNueva[0] = 0.0f; // Reloj
        this.filaNueva[1] = ""; //Evento
        this.filaNueva[2] = (Integer) 0; //Tiempo Llegada
        this.filaNueva[3] = 0.0f; //Proxima Llegada.
        this.filaNueva[4] = "-"; //RND
        this.filaNueva[5] = 0.0f; //tiempo Atencion
        this.filaNueva[6] = 0.0f;//Fin Atencion;
        this.filaNueva[7] = ""; // Estado
        this.filaNueva[8] = 0.0f;//Fin Atencion;
        this.filaNueva[9] = ""; // Estado
        this.filaNueva[10] = 0.0f;//Fin Atencion;
        this.filaNueva[11] = ""; // Estado
        this.filaNueva[12] = 0.0f;//Fin Atencion;
        this.filaNueva[13] = ""; // Estado
        this.filaNueva[14] = (Integer) 0;  //Cola
        this.filaNueva[15] = (Integer) 0; // Acumulador Solicitantes
        this.filaNueva[16] = 0.0f; //Acumulador de tiempoAtencion
    }

    private void llenarFilaEmpleados(Integer index, Object[] filaAnterior) {
        //EMPLEADO 1
        this.filaNueva[6] = filaAnterior[6];//Fin Atencion;
        this.filaNueva[7] = filaAnterior[7];// Estado
        //EMPLEADO 2
        this.filaNueva[8] = filaAnterior[8];//Fin Atencion;
        this.filaNueva[9] = filaAnterior[9]; // Estado
        //EMPLEADO 3
        this.filaNueva[10] = filaAnterior[10];//Fin Atencion;
        this.filaNueva[11] = filaAnterior[11]; // Estado
        //EMPLEADO 4
        this.filaNueva[12] = filaAnterior[12];//Fin Atencion;
        this.filaNueva[13] = filaAnterior[13]; // Estado

        if (index == 0) {
            this.filaNueva[6] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";
            this.filaNueva[7] = this.empleados.get(index).getEstado();
        }
        if (index == 1) {
            this.filaNueva[8] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";;
            this.filaNueva[9] = this.empleados.get(index).getEstado();
        }
        if (index == 2) {
            this.filaNueva[10] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";;
            this.filaNueva[11] = this.empleados.get(index).getEstado();
        }
        if (index == 3) {
            this.filaNueva[12] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";;
            this.filaNueva[13] = this.empleados.get(index).getEstado();
        }

    }

    public ArrayList<Solicitante> obtenerSolicitantes() {
        return this.solicitantes;
    }

    private void crearEmpleados() {
        this.empleados.add(new Empleado(1, Estado.LIBRE));
        this.empleados.add(new Empleado(2, Estado.LIBRE));
        this.empleados.add(new Empleado(3, Estado.LIBRE));
        this.empleados.add(new Empleado(4, Estado.LIBRE));
    }
}
