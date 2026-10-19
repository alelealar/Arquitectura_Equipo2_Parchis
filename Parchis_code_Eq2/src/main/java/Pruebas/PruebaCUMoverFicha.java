/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.JuegoController;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import ENUM.EstadoFicha;
import Modelo.IPartida;
import Vista.VistaJuego;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author valeria
 */
public class PruebaCUMoverFicha {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // ----------------------------------------------
        // PRUEBA CASO DE USO "MOVER FICHA"
        // ----------------------------------------------
        
        // Crear datos de prueba
        Jugador jugadorPrueba = new Jugador();
        Ficha ficha1 = new Ficha();
        ficha1.setIdFicha(1L);
        ficha1.setEstadoFicha(EstadoFicha.EN_TABLERO); 
        
        List<Ficha> fichasJugador = new ArrayList<>();
        fichasJugador.add(ficha1);
        jugadorPrueba.setFichas(fichasJugador); // Le entregamos la ficha al jugador
        
        List<Jugador> jugadoresPrueba = new ArrayList<>();
        jugadoresPrueba.add(jugadorPrueba);
        
         // Nace el modelo
        IPartida modelo = new Partida(1L, jugadoresPrueba); 
        
         // Nace la vista
        VistaJuego vista = new VistaJuego();
        modelo.agregarObserver(vista);
        
        // Nace el controlador
        JuegoController controlador = new JuegoController(vista, modelo);
        
        // Llamada a la vista
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
    
}
