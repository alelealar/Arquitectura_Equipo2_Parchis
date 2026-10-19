/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.JuegoController;
import Dominio.Jugador;
import Dominio.Partida;
import Modelo.IPartida;
import Vista.VistaJuego;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author valeria
 */
public class PruebaCULanzarDado {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // ----------------------------------------------
        // PRUEBA CASO DE USO "LANZAR DADO"
        // ----------------------------------------------
    
        // Crear datos de prueba
        List<Jugador> jugadoresPrueba = new ArrayList<>();
        jugadoresPrueba.add(new Jugador()); 
        
        // Nace el modelo
        IPartida modelo = new Partida(1L, jugadoresPrueba); 
        
        // Nace la vista
        VistaJuego vista = new VistaJuego();
        
        // Conexión con Observer
        modelo.agregarObserver(vista);
        
        // Nace el controlador
        JuegoController controlador = new JuegoController(vista, modelo);
        
        // Llamada a la vista
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        
    }
    
}
