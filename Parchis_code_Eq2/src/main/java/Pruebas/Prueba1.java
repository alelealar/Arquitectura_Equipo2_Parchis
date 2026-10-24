
package Pruebas;

import Controlador.ControlPartida;
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
public class Prueba1 {
    public static void main(String[] args) {
        System.out.println("Cargando motor del juego...");

        PruebasPreparacion preparar = new PruebasPreparacion();

        List<Jugador> jugadores = preparar.crearJugadores();

        Tablero tablero = preparar.crearTablero();

        Partida modelo = new Partida(1L, jugadores);
        modelo.setTablero(tablero);

        Turno turnoPrueba = new Turno(jugadores.get(0));
        modelo.setTurnoActual(turnoPrueba);

        VistaJuego vista = new VistaJuego();
        modelo.agregarObserver(vista);
        ControlPartida controlador = new ControlPartida(vista, modelo);

        // --- 3. CICLO DEL JUEGO (GAME LOOP) ---
        Scanner scanner = new Scanner(System.in);
        boolean jugando = true;

        System.out.println("\n=======================================");
        System.out.println("    BIENVENIDO A PARCHÍS   ");
        System.out.println("=======================================");

        while (jugando) {

            Jugador jugadorActivo = modelo.getTurnoActual().getJugadorActual();

            System.out.println("\n--- TURNO DE: " + jugadorActivo.getNombre() + " ---");
            System.out.println("¿Qué acción deseas realizar?");
            System.out.println("1. Lanzar Dado");
            System.out.println("2. Ver estado actual de mis fichas");
            System.out.println("3. Salir del juego");
            System.out.print("Elige una opción (1-3): ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":

                    System.out.println("\n🎲 Solicitando al sistema lanzar el dado...");

                    controlador.solicitarTirada();

                    // Extrae el valor oficial de tu Modelo
                    int resultadoDado = modelo.getResultadoDado();
                    System.out.println("¡Sacaste un " + resultadoDado + "!");

                    // --- 2. FILTRO INTELIGENTE DE FICHAS ---
                    // Separamos solo las fichas que realmente se pueden mover con este tiro
                    List<Ficha> fichasMovibles = new ArrayList<>();
                    for (Ficha f : jugadorActivo.getFichas()) {
                        if (f.getEstado() == EstadoFicha.EN_BASE) {
                            // Si está en base, solo se mueve si sacaste el número clave (5)
                            if (resultadoDado == tablero.getNumeroSalidaBase()) {
                                fichasMovibles.add(f);
                            }
                        } else {
                            // Si ya está afuera (ej. EN_TABLERO), se puede mover con cualquier número
                            fichasMovibles.add(f);
                        }
                    }

                    if (fichasMovibles.isEmpty()) {
                        System.out.println(">> No tienes fichas fuera y no sacaste un " + tablero.getNumeroSalidaBase() + ". No tienes movimientos válidos.");
                        System.out.println(">> Turno perdido automáticamente.");
                    } else {

                        System.out.println("\n¿Qué ficha deseas usar?");
                        System.out.println("Fichas disponibles para este tiro: ");
                        for (Ficha f : fichasMovibles) {
                            System.out.println("  ID: " + f.getIdFicha() + " [" + f.getEstado()+ "]");
                        }

                        boolean eleccionValida = false;
                        int intentos = 0;
                        int maxIntentos = 2;

                        while (!eleccionValida && intentos < maxIntentos) {
                            System.out.print("\nEscribe el ID de la ficha a interactuar, o '0' para pasar turno: ");
                            String eleccionFicha = scanner.nextLine();

                            if (eleccionFicha.equals("0")) {
                                System.out.println(">> Pasando turno voluntariamente.");
                                eleccionValida = true;
                            } else {
                                try {
                                    Long idFichaElegida = Long.parseLong(eleccionFicha);

                                    // Validamos verificando la lista filtrada
                                    boolean fichaEsMia = fichasMovibles.stream()
                                            .anyMatch(f -> f.getIdFicha() == idFichaElegida);

                                    if (!fichaEsMia) {
                                        intentos++;
                                        System.out.println(" Error: Esa ficha no existe o no se puede mover en este turno.");
                                        if (intentos < maxIntentos) {
                                            System.out.println("Te quedan " + (maxIntentos - intentos) + " intentos.");
                                        }
                                    } else {
                                        // Elección correcta
                                        eleccionValida = true;

                                        if (resultadoDado == tablero.getNumeroSalidaBase()) {
                                            System.out.println("\n>> ¡Número clave! Intentando incorporar la ficha " + idFichaElegida + " a la salida...");
                                            controlador.solicitarIncorporarFicha(idFichaElegida);
                                        } else {
                                            System.out.println(">> Intentando avanzar la ficha " + idFichaElegida + "...");

                                            // controlador.solicitarMovimiento(idFichaElegida);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    intentos++;
                                    System.out.println(" Error: Debes escribir números.");
                                    if (intentos < maxIntentos) {
                                        System.out.println("Te quedan " + (maxIntentos - intentos) + " intentos.");
                                    }
                                }
                            }
                        }

                        if (!eleccionValida) {
                            System.out.println("\n>>  Agotaste tus " + maxIntentos + " intentos. Pierdes tu turno por inactividad.");
                        }
                    }


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
    

