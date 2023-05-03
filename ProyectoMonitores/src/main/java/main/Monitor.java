package main;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author keneth
 */
public class Monitor {
    
    public int reservadores=0, consultores=0, canceladores=0 ;
    public DefaultTableModel modeloTabla;
    public DefaultListModel modeloLista;
    public JTable tabla;
    public JList lista;

    public Monitor(DefaultTableModel modeloTabla, DefaultListModel modeloLista, JTable tabla, JList lista) {
        this.modeloTabla = modeloTabla;
        this.modeloLista = modeloLista;
        this.tabla = tabla;
        this.lista = lista;
    }
    
    
    

    synchronized void consultar(int indexHora, String nombre, Color color) throws InterruptedException{
        
        this.imprimir("Alumno " + nombre + " intentando consultar el horario " + (indexHora + 10)  + ":00 ", color);


            
        if (reservadores > 0 || canceladores > 0){
            wait();
        }
        
        consultores++;
        String consulta = String.valueOf( modeloTabla.getValueAt(indexHora, 1));

        
        this.imprimir("Alumno " + nombre + " ha consultado el estado de " + indexHora + ":00 -> " + consulta , color );

     
        consultores--;
        
        this.imprimir("Alumno " + nombre + " saliendo de consultar", color);
        

        notifyAll();
        
        
    }
    
     synchronized void reservar(int indexHora, String nombre, Color color) throws InterruptedException{
        
        this.imprimir("Alumno " + nombre + " intentando reservar el horario " + (indexHora + 10) + ":00 ", color );
  
         
        if (reservadores > 0 || canceladores > 0 || consultores > 0){
            
            wait();
        }
        
        reservadores++;
        
        
        String consulta = String.valueOf( modeloTabla.getValueAt(indexHora, 1));
        
         
        if (consulta.equals("Libre")) {
            modeloTabla.setValueAt("Reservado", indexHora , 1);
            modeloTabla.setValueAt(nombre, indexHora , 2);
            tabla.setModel(modeloTabla);
            
            this.imprimir("Alumno " + nombre + " ha reservado " + (indexHora + 10) + ":00", color  );

            
        } else {
            this.imprimir("Error: no se pudo reservar "+ (indexHora + 10) + ":00", color );
            
        }
        
        
     
        reservadores--;
        this.imprimir("Alumno " + nombre + " saliendo de reservar", color);
        
        notifyAll();  
    }
     
    synchronized void cancelar(String nombre, Color color) throws InterruptedException{
        this.imprimir("Alumno " + nombre + " intentando cancelar el horario sus horarios", color );
            
        if (reservadores > 0 || canceladores > 0 || consultores > 0){
            
            wait();
        }
        
        canceladores++;
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
        
        canceladores--;
        
        if (contador == 0) {
            this.imprimir("Error: No existe horarios asignados al Alumno " + nombre, color );

        } else {
            this.imprimir("Alumno " + nombre + " ha cancelado sus reservas", color);

        }

        notifyAll();  
    }
    
    
    private void imprimir (String texto, Color color){
        

        modeloLista.addElement(new celdaColoreada(texto, color));

        lista.setCellRenderer(new ListCellRenderer<celdaColoreada>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends celdaColoreada> list, celdaColoreada value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.getText());
                label.setOpaque(true);
                label.setBackground(value.getColor());
                return label;
            }
        });
        this.lista.setModel(modeloLista);
        
        System.out.println(texto);
    }
    
    private class celdaColoreada {
    private final String text;
    private final Color color;

    public celdaColoreada(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }
}
    
}
