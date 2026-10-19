/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Controlador.JuegoController;
import Dominio.Casilla;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Tablero;
import Dominio.Turno;
import ENUM.EstadoFicha;
import Vista.VistaJuego;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author valeria
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Cargando motor del juego...");

        // --- 1. PREPARAR MUNDO (MOCK) ---
        // Crear primer jugador
        Jugador j1 = new Jugador();
        j1.setIdJugador(1L);
        j1.setNombre("Jugador Rojo");
        
        // Crearle fichas al primer jugador (Método de tu equipo)
        j1.crearFichas(4);
        
        // Crear segundo jugador
        Jugador j2 = new Jugador();
        j2.setIdJugador(2L);
        j2.setNombre("Jugador Azul");
        
        // Crearle fichas al segundo jugador (Método de tu equipo)
        j2.crearFichas(4);

        // Crear lista de jugadores
        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(j1);
        jugadores.add(j2);

        // Crear Tablero 
        List<Casilla> casillasMock = new ArrayList<>();
        Tablero tableroMock = new Tablero(1L, casillasMock);
        tableroMock.setNumeroSalidaBase(5);

        // --- 2. CONECTAR ARQUITECTURA MVC ---
        Partida modelo = new Partida(1L, jugadores);
        modelo.setTablero(tableroMock); 

        // Creamos un turno manual de arranque
        Turno turnoPrueba = new Turno(j1);
        modelo.setTurnoActual(turnoPrueba);

        VistaJuego vista = new VistaJuego();
        modelo.agregarObserver(vista);
        JuegoController controlador = new JuegoController(vista, modelo);

        // --- 3. CICLO DEL JUEGO (GAME LOOP) ---
        Scanner scanner = new Scanner(System.in);
        boolean jugando = true;

        System.out.println("\n=======================================");
        System.out.println("    BIENVENIDO A PARCHÍS   ");
        System.out.println("=======================================");

        while (jugando) {
            System.out.println("\n--- TURNO DE: " + modelo.getTurnoActual().getJugadorActual().getNombre() + " ---");
            System.out.println("¿Qué acción deseas realizar?");
            System.out.println("1. Lanzar Dado");
            System.out.println("2. Ver estado actual de mis fichas");
            System.out.println("3. Salir del juego");
            System.out.print("Elige una opción (1-3): ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println("\nSolicitando al sistema lanzar el dado...");
           
                    // Usa el método oficial de tu Controlador
                    controlador.solicitarTirada();
                    
                    // Extrae el valor oficial de tu Modelo
                    int resultadoDado = modelo.getResultadoDado(); 
                    System.out.println("¡Sacaste un " + resultadoDado + "!");
                    
                    // LÓGICA DE INTERACCIÓN DEL USUARIO
                    System.out.println("\n¿Qué ficha deseas usar?");
                    System.out.println("Tus fichas son: ");
                    for (Ficha f : modelo.getTurnoActual().getJugadorActual().getFichas()) {
                        System.out.println("  ID: " + f.getIdFicha() + " [" + f.getEstado()+ "]");
                    }
                    
                    System.out.print("Escribe el ID de la ficha a interactuar (ej. 11, 22), o '0' para pasar turno: ");
                    String eleccionFicha = scanner.nextLine();
                    
                    if (!eleccionFicha.equals("0")) {
                        try {
                            Long idFichaElegida = Long.parseLong(eleccionFicha);
                            
                            boolean fichaEsMia = modelo.getTurnoActual().getJugadorActual().getFichas()
                                    .stream()
                                    .anyMatch(f -> f.getIdFicha() == idFichaElegida);
                            
                            if (!fichaEsMia) {
                                System.out.println("\n>> Error: La ficha " + idFichaElegida + " no es tuya o no existe. Turno perdido por movimiento ilegal.");
                            } else {
                                // Si la ficha sí es tuya, procedemos
                                if (resultadoDado == tableroMock.getNumeroSalidaBase()) {
                                    System.out.println("\n>> ¡Número clave! Intentando incorporar la ficha " + idFichaElegida + " a la salida...");
                                    controlador.solicitarIncorporarFicha(idFichaElegida); 
                                } else {
                                    System.out.println("\n>> Lástima, necesitas un " + tableroMock.getNumeroSalidaBase() + " para salir de la base.");
                                    System.out.println(">> Intentando avanzar la ficha " + idFichaElegida + "...");
                                    // controlador.solicitarMovimiento(idFichaElegida);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\n>>  Error: Debes escribir números. Pierdes tu turno.");
                        }
                    } else {
                        System.out.println(">> Pasando turno voluntariamente.");
                    }

                    // --- CAMBIO DE TURNO CON MÉTODO OFICIAL DEL EQUIPO ---
                    System.out.println(">> Fin de las acciones. Evaluando si el turno cambia...");
                    modelo.gestionarSiguienteTurno();
                    
                    break;
                    
                case "2":
                    System.out.println("\n>> ESTADO DE FICHAS:");
                    for (Ficha f : modelo.getTurnoActual().getJugadorActual().getFichas()) {
                        System.out.println("   Ficha " + f.getIdFicha() + " -> " + f.getEstado());
                    }
                    break;
                    
                case "3":
                    System.out.println("\nCerrando el motor del juego... ¡Hasta luego!");
                    jugando = false;
                    break;
                    
                default:
                    System.out.println("\n Opción no válida. Escribe 1, 2 o 3.");
            }
        }
        scanner.close();
    }
}
    

