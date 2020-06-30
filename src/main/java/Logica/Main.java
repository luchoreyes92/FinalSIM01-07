package Logica;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuario Principal
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        int x=-1;
        Double p=1.0;
        float lambda = 0.5f;
        System.out.println(lambda);
        Double a = Math.exp(-lambda);
        System.out.println("esto es a: " +a);
        while(p >= a){
            Double u = Math.random();
            System.out.println("esto es u: "+u);
            p = p * u;
            System.out.println("esto es p: "+p);
            x++;
        }
        System.out.println("asdasdada"+x);
        
    }
    
}
