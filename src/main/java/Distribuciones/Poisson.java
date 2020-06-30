
package Distribuciones;

public class Poisson {
    
    public static final Float variableAleatoria(Double media) {
        Float x=-1.0f;
        Double p=1.0;
        Double a = Math.exp(-1/media);
        while(p >= a){
            Double u = Math.random();
            p = p * u;
            x++;
        }
        return x;
    }
    
}
