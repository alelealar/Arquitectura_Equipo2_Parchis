package Vista;

import Controlador.JuegoController;
import Dominio.Ficha;
import Dominio.Jugador;
import ENUM.EstadoPartida;
import Modelo.IObserver;
import Modelo.IPartida;

import java.util.Scanner;

public class VistaConsola implements IObserver{

    private final IPartida modelo;
    private final JuegoController controlador;

    private final Scanner entrada = new Scanner(System.in);

    public VistaConsola(IPartida modelo, JuegoController controlador) {
        this.controlador = controlador;
        this.modelo = modelo;
    }

    public void iniciar() {
        System.out.println("=======================================");
        System.out.println("            PARCHÍS (consola)          ");
        System.out.println("=======================================");

        while (true) {
            mostrarPanel();
            System.out.println("1. Lanzar dado");
            System.out.println("2. Mover ficha");
            System.out.println("3. Incorporar ficha (sacar de la cárcel)");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            String opcion = leer();
            if (opcion == null) break;
            switch (opcion.trim()) {
                case "1":
                    controlador.solicitarTirada();
                    break;
                case "2": {
                    Integer id = leerEntero("ID de la ficha a mover: ");
                    if (id != null) controlador.solicitarMovimiento(id);
                    break;
                }
                case "3": {
                    Integer id = leerEntero("ID de la ficha a sacar de la cárcel: ");
                    if (id != null) controlador.solicitarIncorporarFicha(id.longValue());
                    break;
                }
                case "0":
                    System.out.println("Saliendo del juego...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void mostrarPanel() {
        Jugador j = modelo.getTurnoActual().getJugadorActual();
        System.out.println();
        System.out.println("--- TURNO DE: " + j.getNombre() + " (" + j.getColor() + ") ---");
        System.out.println("Numero de dado: " + modelo.getTurnoActual().getMovimientosPendientes().toString()); // TODO mostrar numero de dado
        System.out.println("Fase: " + modelo.getTurnoActual().getEstadoTurno()
                + " | Tiradas pendientes: " + modelo.getTurnoActual().getLanzamientosPendientes()
                + " | Movimientos por usar: " + modelo.getTurnoActual().getMovimientosPendientes());
        // System.out.println("Fichas de " + j.getNombre() + ":");
        // for (Ficha f : j.getFichas()) {
        //     System.out.println("   Ficha " + f.getIdFicha() + " -> " + f.getCasillaActual().getNumeroCasilla());
        // }
        if (!modelo.getFichasDisponibles().isEmpty()) {
            StringBuilder sb = new StringBuilder("Fichas que pueden moverse: ");
            for (Ficha f : modelo.getFichasDisponibles()) sb.append(f.getIdFicha()).append(' ');
            System.out.println(sb);
        }
    }

    private String leer() {
        return entrada.hasNextLine() ? entrada.nextLine() : null;
    }

    private Integer leerEntero(String pregunta) {
        System.out.print(pregunta);
        String texto = leer();
        if (texto == null) return null;
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            System.out.println("Escribe un número válido.");
            return null;
        }
    }

    @Override
    public void actualizar() {
        mostrarPanel();
    }

}
