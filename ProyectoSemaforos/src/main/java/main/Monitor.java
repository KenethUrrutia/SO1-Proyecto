package main;

import java.awt.*;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 *Clase Monitor que controlara los interbloqueos y acciones de hilos
 * @author keneth
 */
public class Monitor {
    public Semaphore semaforo;
    public DefaultTableModel modeloTabla;
    public DefaultListModel modeloLista;
    public JTable tabla;
    public JList lista;

    /**
     * Contructor
     * @param modeloTabla DefaultTableModel para la tabla de reservas
     * @param modeloLista DefaultListModel para la lista que funge como terminal
     * @param tabla JTable para la visualizacion de reservas
     * @param lista JList que funge como Terminal
     */
    public Monitor(DefaultTableModel modeloTabla, DefaultListModel modeloLista, JTable tabla, JList lista) {
        semaforo = new Semaphore(25);
        this.modeloTabla = modeloTabla;
        this.modeloLista = modeloLista;
        this.tabla = tabla;
        this.lista = lista;
    }
    
    
    
    /**
     * Metodo para que un alumno pueda realizar una consulta de una hora especifica
     * @param indexHora hora a consultar
     * @param nombre nombre del alumno que consulta
     * @throws InterruptedException 
     */
    synchronized void consultar(int indexHora, String nombre) throws InterruptedException{
        
        this.imprimir(nombre + " intentando consultar el horario " + (indexHora + 10)  + ":00 ", new Color(255,128, 0) );
        
        semaforo.acquire();
        Thread.sleep( (long) Math.random()*3000 + 500);
        String consulta = String.valueOf( modeloTabla.getValueAt(indexHora, 1));
        this.imprimir(nombre + " ha consultado el estado de " + indexHora + ":00 -> " + consulta , new Color(255,178, 102));

        semaforo.release();
        this.imprimir(nombre + " saliendo de consultar", new Color(255,229, 204));
        
    }
    
    /**
     * Metodo para que un alumno pueda realizar una reservacion de una hora especifica
     * @param indexHora hora a reservar
     * @param nombre nombre del alumno que reserva
     * @throws InterruptedException 
     */
    synchronized void reservar(int indexHora, String nombre) throws InterruptedException{
        
        this.imprimir(nombre + " intentando reservar el horario " + (indexHora + 10) + ":00 ", new Color(0, 204, 0) );

        semaforo.acquire(25);
        String consulta = String.valueOf( modeloTabla.getValueAt(indexHora, 1));
        Thread.sleep( (long) Math.random()*3000 + 500);
        if (consulta.equals("Libre")) {
            modeloTabla.setValueAt("Reservado", indexHora , 1);
            modeloTabla.setValueAt(nombre, indexHora , 2);
            tabla.setModel(modeloTabla);
            this.imprimir(nombre + " ha reservado " + (indexHora + 10) + ":00", new Color(51, 255, 51)  );
            
        } else {
            this.imprimir("Error: no se pudo reservar a " + nombre + " la hora "+ (indexHora + 10) + ":00", new Color(51, 255, 51)  );
        }
        
        semaforo.release(25);
        
        this.imprimir(nombre + " saliendo de reservar", new Color(204, 255, 204));
         
    }
     
    /**
     * Metodo para que un alumno pueda realizar una cancelacion de una hora especifica
     * @param nombre nombre del alumno que cancela
     * @throws InterruptedException 
     */
    synchronized void cancelar(String nombre) throws InterruptedException{
        
        this.imprimir(nombre + " intentando cancelar el horario sus horarios", new Color(102, 178, 255) );
            
        semaforo.acquire(25);
        Thread.sleep( (long) Math.random()*3000 + 500);
        int contador = 0;
        for (int i = 0; i < 12; i++) {
            String consultaNombre = String.valueOf( modeloTabla.getValueAt(i, 2));
            if (consultaNombre.equals(nombre)) {
                modeloTabla.setValueAt("Libre", i, 1);
                modeloTabla.setValueAt(" ", i, 2);
                tabla.setModel(modeloTabla);
                contador++;  
            }
        }
        
        if (contador == 0) {
            this.imprimir("Error: No existe horarios asignados al Alumno " + nombre, new Color(153,255, 255) );

        } else {
            this.imprimir(nombre + " ha cancelado sus reservas", new Color(153,255, 255) );
        }
        
        semaforo.release(25);

        this.imprimir(nombre + " saliendo de cancelar", new Color(200,255, 255) );


    }
    
    
    /**
     * Funcion para mostrar en el JList las operaciones realizadas
     * Haciendo la funcion de Terminal en el GUI
     * @param texto Texto a imprimir
     * @param color Color de fondo del texto
     */
    private void imprimir (String texto, Color color){
        
        modeloLista.addElement(new CeldaColoreada(texto, color));

        lista.setCellRenderer(new ListCellRenderer<CeldaColoreada>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends CeldaColoreada> list, CeldaColoreada celda, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(celda.getText());
                label.setOpaque(true);
                label.setBackground(celda.getColor());
                return label;
            }
        });
        this.lista.setModel(modeloLista);
        System.out.println(texto);
    }
    
    /**
     * Clase privada para uso exclusivo de hacer celdas con color en la Terminal JList
     */
    private class CeldaColoreada {
        private final String text;
        private final Color color;

        /**
         * Contructor
         * @param text Texto de la celda
         * @param color Color de la celda
         */
        public CeldaColoreada(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        /**
         * 
         * @return Texto de la celda
         */
        public String getText() {
            return text;
        }

        /**
         * 
         * @return Color de la celda 
         */
        public Color getColor() {
            return color;
        }
    }
    
}
