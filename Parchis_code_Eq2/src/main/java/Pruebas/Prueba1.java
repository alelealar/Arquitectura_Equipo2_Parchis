package Pruebas;

import Controlador.ControlPartida;
import Dominio.ConfiguracionPartida;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import ENUM.ColorJugador;
import ENUM.EstadoFicha;
import Vista.VistaJuego;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Equipo 2
 */
public class Prueba1 {
    public static void main(String[] args) {
        System.out.println("Cargando motor del juego...");

        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador(1L, "Jugador Rojo", ColorJugador.ROJO, "avatar1.png"));
        jugadores.add(new Jugador(2L, "Jugador Azul", ColorJugador.AZUL, "avatar2.png"));
        jugadores.add(new Jugador(3L, "Jugador Amarillo", ColorJugador.AMARILLO, "avatar3.png"));
        jugadores.add(new Jugador(4L, "Jugador Verde", ColorJugador.VERDE, "avatar4.png"));

        ConfiguracionPartida configuracion = new ConfiguracionPartida(4, 4, 5, 5);
        Partida modelo = new Partida(1L, jugadores, configuracion);

        VistaJuego vista = new VistaJuego();
        modelo.agregarObserver(vista);
        ControlPartida controlador = new ControlPartida(vista, modelo);

        Scanner scanner = new Scanner(System.in);
        boolean jugando = true;

        System.out.println("\n=======================================");
        System.out.println("    BIENVENIDO A PARCHIS   ");
        System.out.println("=======================================");

        while (jugando) {

            Jugador jugadorActivo = modelo.getTurnoActual().getJugadorActual();

            System.out.println("\n--- TURNO DE: " + jugadorActivo.getNombre() + " ---");
            System.out.println("Que accion deseas realizar?");
            System.out.println("1. Lanzar Dado");
            System.out.println("2. Ver estado actual de mis fichas");
            System.out.println("3. Salir del juego");
            System.out.print("Elige una opcion (1-3): ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    controlador.solicitarTirada();

                    int resultadoDado = modelo.getResultadoDado();
                    System.out.println("Sacaste un " + resultadoDado + "!");

                    List<Ficha> fichasMovibles = modelo.getFichasDisponibles();

                    if (fichasMovibles == null || fichasMovibles.isEmpty()) {
                        System.out.println("No tienes movimientos validos.");
                    } else {
                        System.out.println("\nFichas disponibles para este tiro:");
                        for (Ficha f : fichasMovibles) {
                            System.out.println("  ID: " + f.getIdFicha() + " [" + f.getEstado() + "]");
                        }

                        boolean eleccionValida = false;
                        int intentos = 0;
                        int maxIntentos = 2;

                        while (!eleccionValida && intentos < maxIntentos) {
                            System.out.print("\nEscribe el ID de la ficha a interactuar, o '0' para pasar turno: ");
                            String eleccionFicha = scanner.nextLine();

                            if (eleccionFicha.equals("0")) {
                                modelo.getTurnoActual().getMovimientosPendientes().clear();
                                modelo.gestionarSiguienteTurno();
                                eleccionValida = true;
                            } else {
                                try {
                                    Long idFichaElegida = Long.parseLong(eleccionFicha);

                                    Ficha fichaSeleccionada = null;
                                    for (Ficha f : fichasMovibles) {
                                        if (f.getIdFicha().equals(idFichaElegida)) {
                                            fichaSeleccionada = f;
                                            break;
                                        }
                                    }

                                    if (fichaSeleccionada == null) {
                                        intentos++;
                                        System.out.println("Error: Esa ficha no existe o no se puede mover en este turno.");
                                        if (intentos < maxIntentos) {
                                            System.out.println("Te quedan " + (maxIntentos - intentos) + " intentos.");
                                        }
                                    } else {
                                        eleccionValida = true;

                                        if (fichaSeleccionada.getEstado() == EstadoFicha.EN_BASE) {
                                            controlador.solicitarIncorporarFicha(idFichaElegida);
                                        } else {
                                            controlador.solicitarMovimiento(idFichaElegida.intValue());
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    intentos++;
                                    System.out.println("Error: Debes escribir numeros.");
                                    if (intentos < maxIntentos) {
                                        System.out.println("Te quedan " + (maxIntentos - intentos) + " intentos.");
                                    }
                                }
                            }
                        }

                        if (!eleccionValida) {
                            modelo.getTurnoActual().getMovimientosPendientes().clear();
                            modelo.gestionarSiguienteTurno();
                        }
                    }

                    break;

                case "2":
                    System.out.println("\nESTADO DE FICHAS:");
                    for (Ficha f : jugadorActivo.getFichas()) {
                        String pos = "";
                        if (f.getCasillaActual() != null) {
                            pos = " (Casilla #" + f.getCasillaActual().getNumeroCasilla() + ", Pasos: " + f.getPasosDados() + ")";
                        }
                        System.out.println("   Ficha " + f.getIdFicha() + " -> " + f.getEstado() + pos);
                    }
                    break;

                case "3":
                    jugando = false;
                    break;

                default:
                    System.out.println("\nOpcion no valida. Escribe 1, 2 o 3.");
            }
        }
        scanner.close();
    }
}