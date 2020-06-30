/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribuciones;

import java.text.DecimalFormat;

public class Uniforme {

    public static final Float variableAleatoria(Integer random,Integer inferior, Integer superior) {
        float rnd = (float) random /100;
        Float x =Float.parseFloat(new DecimalFormat("0.00").format(inferior + (rnd * (superior - inferior))).replace(',', '.'));
        return x;
    }
    
  

}
