/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.IPartida;
import Vista.VistaJuego;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Capa de Control (Controlador - Patrón MVC) que actúa como puente de comunicación 
 * entre la VistaJuego y el Modelo (IPartida).
 *
 * Su responsabilidad es escuchar las interacciones del usuario en la interfaz gráfica
 * y delegar o enrutar esas peticiones hacia los métodos correspondientes del Modelo. 
 * Cumple estrictamente con el principio "Thin Controller", por lo que carece por 
 * completo de lógica de negocio, no evalúa reglas del Parchís, ni guarda estados.
 *
 * @author Equipo 2
 */
public class JuegoController {
    /** Referencia a la capa de presentación. */
    private VistaJuego vista;
    
    /** Referencia al contrato de la capa de dominio. */
    private IPartida modelo; 

    /**
     * Construye el controlador inyectando sus dos dependencias principales.
     * Al instanciarse, el controlador enlaza automáticamente los eventos visuales.
     * 
     * @param vista  La ventana interactiva del usuario.
     * @param modelo La instancia que maneja la lógica y estado de la partida.
     */
    public JuegoController(VistaJuego vista, IPartida modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        inicializarEventos();
    }

    /**
     * Establece las conexiones físicas entre los componentes de la Vista 
     * y los métodos de respuesta de este Controlador (Listeners).
     */
    private void inicializarEventos() {
        // "Cable' para botón de CU: Lanzar Dado
        this.vista.addLanzarDadoListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solicitarTirada();
            }
        });
        
        // "Cable' para botón de CU: Mover Ficha
        this.vista.addMoverFichaListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    // Extraemos y convertimos el texto aquí mismo en el escuchador
                    String textoIngresado = vista.getIdFichaTexto();
                    int idFicha = Integer.parseInt(textoIngresado);
                    
                    // Llamamos al método respetando EXACTAMENTE el UML
                    solicitarMovimiento(idFicha);
                    
                } catch (NumberFormatException ex) {
                    System.out.println("CONTROLADOR: Error. Ingresa un número válido.");
                }
            }
        });
    }

    /**
     * Método de enrutamiento para el caso de uso "Lanzar Dado".
     * Atrapa el clic del usuario en la interfaz y delega la ejecución al modelo.
     * Cumple con la regla de oro: el controlador NO genera los números aleatorios.
     */
    public void solicitarTirada() {
        System.out.println("CONTROLADOR: Clic detectado. Avisando a la Partida...");
        
        // Delega el cálculo de las reglas de negocio exclusivamente al Modelo.
        modelo.lanzarDado(); 
    }
    
    /**
     * Método 2 del Diagrama de Secuencia: solicitarMovimiento(idFicha: int)
     * Atrapa la petición de la vista y delega la ejecución al modelo.
     * 
     * @param idFicha Identificador de la ficha a mover.
     */
    public void solicitarMovimiento(int idFicha) {
        System.out.println("CONTROLADOR: Ordenando al modelo mover la ficha ID: " + idFicha);
        modelo.solicitarMovimiento(idFicha);
    }
    
    /**
     * Mensaje 2 del Diagrama: solicitarIncorporarFicha()
     * Atrapa la petición de la vista de sacar una ficha de la cárcel.
     */
    public void solicitarIncorporarFicha(Long idFicha) {
        System.out.println("CONTROLADOR: Solicitud de incorporar ficha " + idFicha + " detectada.");
        modelo.incorporarFichaATablero(idFicha); 
    }
}
