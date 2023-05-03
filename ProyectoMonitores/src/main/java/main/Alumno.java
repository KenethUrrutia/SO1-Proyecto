package main;

import java.awt.Color;

/**
 *
 * @author keneth
 */
public class Alumno extends Thread {
    public Monitor m;
    public Color color;
    

    public Alumno(Monitor m, String name, Color color) {
        super(name);
        this.m = m;
        this.color = color;
    }
    
    public void run () {
        try {
            for (int i = 0; i < 4; i++){
                
                
                switch ( (int) (Math.random()*4 + 1)) {
                    case 1:
                        m.reservar((int) (Math.random()*12 ), this.getName(), color);
                        break;
                    case 2:
                        m.reservar((int) (Math.random()*12 ), this.getName(), color);
                        break;
                    case 3:
                        m.consultar((int) (Math.random()*12 ), this.getName(), color);
                        break;
                    case 4:
                        m.cancelar(this.getName(), color);
                        break;
                    default:
                        throw new AssertionError();
                }
                sleep(500);
            }
            
        }  catch (Exception e) {}
   }

    @Override
    public String toString() {
        return super.getName() + " " + this.color + " " ;
    }
    
    
    
}
