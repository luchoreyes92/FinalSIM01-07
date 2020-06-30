package Distribuciones;

import java.text.DecimalFormat;

public class ExponencialNegativa {

    public static final Float variableAleatoria(Integer random,Double media) {
        float rnd= (float) random/100;
        return Float.parseFloat(new DecimalFormat("0.00").format(-media * Math.log(1 - rnd)).replace(',', '.'));
    }

}
