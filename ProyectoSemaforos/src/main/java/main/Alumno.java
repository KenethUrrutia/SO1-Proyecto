package main;


/**
 * Clase Alumno que hereda de Thread
 * @author keneth
 */
public class Alumno extends Thread {
    public Monitor m;
    
    /**
     * Contructor
     * @param m Monitor que controla el comportamiento
     * @param name String del nombre del alumno
     */
    public Alumno(Monitor m, String name) {
        super(name);
        this.m = m;
    }
    
    /**
     * Metodo run, heredado de Thread.
     * Se realizan 4 interaciones, cada una con una accion
     * Reservar, Consultar y Cancelar
     * Cada una con probabilidad de 50%, 25% y 25% respectivamente
     * Las acciones reservar y consultar requieren elegir de una hora, elegida con un numero aleatorio de 0 a 11
     */
    public void run () {
        try {
            for (int i = 0; i < 4; i++){
                switch ( (int) (Math.random()*4 + 1)) {
                    case 1:
                        m.reservar((int) (Math.random()*12 ), this.getName());
                        break;
                    case 2:
                        m.reservar((int) (Math.random()*12 ), this.getName());
                        break;
                    case 3:
                        m.consultar((int) (Math.random()*12 ), this.getName());
                        break;
                    case 4:
                        m.cancelar(this.getName());
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        }  catch (Exception e) {}
   }   
}
