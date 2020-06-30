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
import java.util.Collections;

/**
 *
 * @author lucho
 */
public class Simular {

    private ArrayList<Empleado> empleados = new ArrayList<>();
    private ArrayList<Solicitante> solicitantes = new ArrayList<>();
    private ArrayList<Solicitante> colaSolicitantes = new ArrayList<>();
    private ArrayList<Solicitante> colaFormularioSolicitantes = new ArrayList<>();
    private Object[] filaNueva = new Object[20];

    private Integer nroSolicitante = 1;

    private Double mediaPoissonLlegada, mediaExpLlenarFormulario;
    private Integer infUnifAtencion, supUnifAtencion;

    public Simular(Double mediaPoissonLlegada, Double mediaExpLlenarFormulario, Integer infUnifAtencion, Integer supUnifAtencion) {
        this.mediaPoissonLlegada = mediaPoissonLlegada;
        this.mediaExpLlenarFormulario = mediaExpLlenarFormulario;
        this.infUnifAtencion = infUnifAtencion;
        this.supUnifAtencion = supUnifAtencion;
        crearEmpleados();
    }

    public Object[] simularA(Object[] filaAnterior) {
        inicializarFila();
        prioridadColaFormularioSolicitante();
        quitarSolicitanteFueraSistema();
        if (esLlegadaSolicitante((Float) filaAnterior[3])) {
            this.filaNueva[0] = (Float) filaAnterior[3];
            Solicitante solicitante = new Solicitante(this.nroSolicitante, (Float) this.filaNueva[0]);
            this.nroSolicitante++;
            this.filaNueva[1] = Estado.LLEGADA_SOLICITANTE + " " + solicitante.getNumero();
            this.filaNueva[2] = (Float) Poisson.variableAleatoria(this.mediaPoissonLlegada);
            this.filaNueva[3] = (Float) this.filaNueva[0] + (Float) this.filaNueva[2];
            this.filaNueva[4] = generarRandom();
            this.filaNueva[5] = ExponencialNegativa.variableAleatoria((Integer) this.filaNueva[4], this.mediaExpLlenarFormulario);
            this.filaNueva[6] = (Float) this.filaNueva[0] + (Float) this.filaNueva[5];
            solicitante.setFinFormulario((Float) this.filaNueva[6]);
            solicitante.setEstado(Estado.LLENANDO_FORMULARIO);
            this.colaFormularioSolicitantes.add(solicitante);
            this.filaNueva[7] = "-";
            this.filaNueva[8] = "-";
            this.filaNueva[9] = filaAnterior[9];
            this.filaNueva[10] = filaAnterior[10];
            this.filaNueva[11] = filaAnterior[11];
            this.filaNueva[12] = filaAnterior[12];
            this.filaNueva[13] = filaAnterior[13];
            this.filaNueva[14] = filaAnterior[14];
            this.filaNueva[15] = filaAnterior[15];
            this.filaNueva[16] = filaAnterior[16];
            this.filaNueva[17] = filaAnterior[17];
            this.filaNueva[18] = filaAnterior[18];
            this.filaNueva[19] = filaAnterior[19];
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
                this.filaNueva[4] = "-";
                this.filaNueva[5] = "-";
                this.filaNueva[6] = "-";
                Integer indexSolicitante = setearSolicitante(this.empleados.get(index).getNroSolicitante());
                this.empleados.get(index).setNroSolicitante(null);
                if (existeColaSolicitante()) {
                    this.filaNueva[7] = generarRandom();
                    this.filaNueva[8] = Uniforme.variableAleatoria((Integer) this.filaNueva[7], this.infUnifAtencion, this.supUnifAtencion);
                    this.empleados.get(index).setEstado(Estado.OCUPADO);
                    this.empleados.get(index).setFinAtencion((Float) this.filaNueva[0] + (Float) this.filaNueva[8]);
                    this.empleados.get(index).setNroSolicitante(this.colaSolicitantes.get(0).getNumero());
                    llenarFilaEmpleados(index, filaAnterior);
                    this.filaNueva[17] = (Integer) filaAnterior[17] - 1;
                    this.colaSolicitantes.remove(0);
                } else {  // ES EMPLEADO LIBRE
                    this.empleados.get(index).setEstado(Estado.LIBRE);
                    this.empleados.get(index).setFinAtencion(null);
                    llenarFilaEmpleados(index, filaAnterior);
                    this.filaNueva[7] = "-";
                    this.filaNueva[8] = "-";
                    this.filaNueva[17] = (Integer) filaAnterior[17];
                }
                this.filaNueva[18] = (Integer) filaAnterior[18] + 1;
                this.filaNueva[19] = (Float) filaAnterior[19] + 
                        this.solicitantes.get(indexSolicitante).getHoraSalida() -
                        this.solicitantes.get(indexSolicitante).getHoraLlegada();
            } else { //FIN FORMULARIO
                Solicitante solicitante = this.colaFormularioSolicitantes.get(0);
                this.filaNueva[0] = (Float) solicitante.getFinFormulario();
                this.filaNueva[1] = Estado.FIN_FORMULARIO + " " + solicitante.getNumero();
                this.filaNueva[2] = "-";
                this.filaNueva[3] = filaAnterior[3];
                this.filaNueva[4] = "-";
                this.filaNueva[5] = "-";
                this.filaNueva[6] = "-";
                if (existeColaFinFormularioSolicitante()) {
                    solicitante.setEstado(Estado.EN_COLA);
                    solicitante.setFinFormulario(null);
                    this.filaNueva[7] = "-";
                    this.filaNueva[8] = "-";
                    this.filaNueva[9] = filaAnterior[9];
                    this.filaNueva[10] = filaAnterior[10];
                    this.filaNueva[11] = filaAnterior[11];
                    this.filaNueva[12] = filaAnterior[12];
                    this.filaNueva[13] = filaAnterior[13];
                    this.filaNueva[14] = filaAnterior[14];
                    this.filaNueva[15] = filaAnterior[15];
                    this.filaNueva[16] = filaAnterior[16];
                    this.filaNueva[17] = (Integer) filaAnterior[17] + 1;
                    this.colaSolicitantes.add(solicitante);
                } else {
                    solicitante.setEstado(Estado.SIENDO_ATENDIDO);
                    this.filaNueva[7] = generarRandom();
                    this.filaNueva[8] = Uniforme.variableAleatoria((Integer) this.filaNueva[7], this.infUnifAtencion, this.supUnifAtencion);
                    Integer index = this.empleados.indexOf(this.empleados.stream().
                            filter(x -> x.getEstado().equals(Estado.LIBRE)).findFirst().get());
                    this.empleados.get(index).setEstado(Estado.OCUPADO);
                    this.empleados.get(index).setFinAtencion((Float) this.filaNueva[0] + (Float) this.filaNueva[8]);
                    this.empleados.get(index).setNroSolicitante(solicitante.getNumero());
                    llenarFilaEmpleados(index, filaAnterior);
                    this.filaNueva[17] = (Integer) filaAnterior[17];

                }
                this.filaNueva[18] = filaAnterior[18];
                this.filaNueva[19] = filaAnterior[19];               
                this.colaFormularioSolicitantes.remove(0);
            }

        }

        return this.filaNueva;
    }

    private Boolean esLlegadaSolicitante(Float proximaLlegada) {
        if (this.empleados.stream().allMatch(x -> x.getEstado().equals(Estado.LIBRE))) {
            if (this.colaFormularioSolicitantes.size() > 0) {
                return proximaLlegada <= this.colaFormularioSolicitantes.get(0).getFinFormulario();
            } else {
                return true;
            }
        } else {
            if (this.empleados.stream().anyMatch(x -> x.getFinAtencion() != null && x.getFinAtencion() < proximaLlegada)) {
                return false;
            } else {
                if (this.colaFormularioSolicitantes.size() > 0) {
                    return proximaLlegada <= this.colaFormularioSolicitantes.get(0).getFinFormulario();
                }
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

    private Boolean existeColaSolicitante() {
        return this.colaSolicitantes.size() > 0;
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
        this.filaNueva[5] = 0.0f; // Tiempo Formulario
        this.filaNueva[6] = 0.0f; // Fin Formulario
        this.filaNueva[7] = "-"; //RND
        this.filaNueva[8] = 0.0f; //tiempo Atencion
        this.filaNueva[9] = 0.0f;//Fin Atencion;
        this.filaNueva[10] = ""; // Estado
        this.filaNueva[11] = 0.0f;//Fin Atencion;
        this.filaNueva[12] = ""; // Estado
        this.filaNueva[13] = 0.0f;//Fin Atencion;
        this.filaNueva[14] = ""; // Estado
        this.filaNueva[15] = 0.0f;//Fin Atencion;
        this.filaNueva[16] = ""; // Estado
        this.filaNueva[17] = (Integer) 0;  //Cola
        this.filaNueva[18] = (Integer) 0; // Acumulador Solicitantes
        this.filaNueva[19] = 0.0f; //Acumulador de tiempoAtencion
    }

    private void llenarFilaEmpleados(Integer index, Object[] filaAnterior) {
        //EMPLEADO 1
        this.filaNueva[9] = filaAnterior[9];//Fin Atencion;
        this.filaNueva[10] = filaAnterior[10];// Estado
        //EMPLEADO 2
        this.filaNueva[11] = filaAnterior[11];//Fin Atencion;
        this.filaNueva[12] = filaAnterior[12]; // Estado
        //EMPLEADO 3
        this.filaNueva[13] = filaAnterior[13];//Fin Atencion;
        this.filaNueva[14] = filaAnterior[14]; // Estado
        //EMPLEADO 4
        this.filaNueva[15] = filaAnterior[15];//Fin Atencion;
        this.filaNueva[16] = filaAnterior[16];
        if (index == 0) {
            this.filaNueva[9] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";
            this.filaNueva[10] = this.empleados.get(index).getEstado();
        }
        if (index == 1) {
            this.filaNueva[11] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";;
            this.filaNueva[12] = this.empleados.get(index).getEstado();
        }
        if (index == 2) {
            this.filaNueva[13] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";;
            this.filaNueva[14] = this.empleados.get(index).getEstado();
        }
        if (index == 3) {
            this.filaNueva[15] = this.empleados.get(index).getFinAtencion() != null
                    ? this.empleados.get(index).getFinAtencion() : "-";;
            this.filaNueva[16] = this.empleados.get(index).getEstado();
        }

    }
    
    public ArrayList<Solicitante> obtenerSolicitantes(){
        return this.solicitantes;
    }

    private void crearEmpleados() {
        this.empleados.add(new Empleado(1, Estado.LIBRE));
        this.empleados.add(new Empleado(2, Estado.LIBRE));
        this.empleados.add(new Empleado(3, Estado.LIBRE));
        this.empleados.add(new Empleado(4, Estado.LIBRE));
    }

    private void quitarSolicitanteFueraSistema() {
        this.solicitantes.removeIf(x-> x.getEstado().equals(Estado.FUERA_SISTEMA)); //To change body of generated methods, choose Tools | Templates.
    }

}
