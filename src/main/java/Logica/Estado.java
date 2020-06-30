
package Logica;

public final class Estado {
    
    //EMPLEADO
    public final static String LIBRE = "LIBRE";
    public final static String OCUPADO = "OCUPADO";
    
    //SOLICITANTE
    public final static String EN_COLA = "EN_COLA";
    public final static String SIENDO_ATENDIDO = "SIENDO_ATENDIDO";
    public final static String FUERA_SISTEMA = "FUERA_SISTEMA";
    public final static String LLENANDO_FORMULARIO = "LLENANDO_FORMULARIO";
    
    
    //EVENTO
    public final static String LLEGADA_SOLICITANTE = "LLEGADA_SOLICITANTE";
    public final static String FIN_FORMULARIO = "FIN_FORMULARIO";
    public final static String FIN_ATENCION = "FIN_ATENCION";
}
