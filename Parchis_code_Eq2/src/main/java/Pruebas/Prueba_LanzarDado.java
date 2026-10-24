
package Pruebas;

import Controlador.ControlPartida;
import Dominio.*;
import ENUM.ColorJugador;
import ENUM.EstadoFicha;
import Vista.VistaJuego;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author valeria
 */
public class Prueba_LanzarDado {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBA DEL CASO DE USO: LANZAR DADO ===");

        // 1. Crear los jugadores usando su constructor
        Jugador j1 = new Jugador(1L, "Carlos", ColorJugador.ROJO, "avatar1.png");
        Jugador j2 = new Jugador(2L, "Ana", ColorJugador.AZUL, "avatar2.png");

        // IMPORTANTE: Inicializar las fichas para que no sea null en calcularMovimientosDisponibles
        j1.crearFichas(4);
        j2.crearFichas(4);

        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(j1);
        jugadores.add(j2);

        // 2. Instanciar el Modelo (Partida)
        Partida modelo = new Partida(100L, jugadores);

        // 3. Instanciar el Controlador pasándole el modelo
        ControlPartida controlador = new ControlPartida(modelo);

        // 4. EJECUTAR EL CASO DE USO
        System.out.println("\n--> Invocando: controlador.solicitarTirada()...");
        controlador.solicitarTirada();

        // 5. VERIFICAR RESULTADOS EN CONSOLA
        System.out.println("\n=== RESULTADOS DE LA PRUEBA ===");
        System.out.println("Dado obtenido: " + modelo.getResultadoDado());

        if (modelo.getTurnoActual() != null) {
            // Nota: ajustado a getJugador() si tu clase Turno retorna la referencia directa del Jugador
            System.out.println("Jugador en turno: " + modelo.getTurnoActual().getJugadorActual().getNombre());
        }

        System.out.println("¿Otorga turno extra?: " + modelo.isTurnoExtra());
        System.out.println("=== FIN DE LA PRUEBA ===");
    }

}


