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
public class PruebaCUIncorporarFichaATablero {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // ----------------------------------------------
        // PRUEBA CASO DE USO "MOVER FICHA"
        // ----------------------------------------------
        
        // Crear datos de prueba
        Jugador jugadorPrueba = new Jugador();
        jugadorPrueba.setIdJugador(1L);
        jugadorPrueba.setNombre("Jugador 1");
        
        // Creamos una ficha y la encerramos en la base/cárcel
        Ficha ficha1 = new Ficha();
        ficha1.setIdFicha(1L);
        ficha1.setEstadoFicha(EstadoFicha.EN_BASE); 
        
        List<Ficha> fichasJugador = new ArrayList<>();
        fichasJugador.add(ficha1);
        jugadorPrueba.setFichas(fichasJugador); 
        
        List<Jugador> jugadoresPrueba = new ArrayList<>();
        jugadoresPrueba.add(jugadorPrueba);
        
        // Nace el modelo
        IPartida modelo = new Partida(1L, jugadoresPrueba); 
        
        // Nace la vista
        VistaJuego vista = new VistaJuego();
        modelo.agregarObserver(vista);
        
        // Nace el controlador
        JuegoController controlador = new JuegoController(vista, modelo);
        
        //
        System.out.println("--- INICIANDO PRUEBA: INCORPORAR FICHA ---");
        
        // Simulamos el Mensaje 1 y 2: El jugador da clic en su ficha en la cárcel
        //controlador.solicitarIncorporarFicha();
        
        // Imprimimos el estado final para comprobar si realmente cambió
        System.out.println("ESTADO FINAL DE LA FICHA: " + ficha1.getEstado());
    }
    
}
