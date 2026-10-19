package Vista;

import Controlador.JuegoController;
import Dominio.Ficha;
import Dominio.Jugador;
import ENUM.EstadoFicha;
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
            // Informacion del jugador en turno
            mostrarPanel();
            System.out.println("1. Lanzar dado");
            System.out.println("2. Mover ficha");
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
                case "0":
                    System.out.println("Saliendo del juego...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Muestra un panel resumen de la persona en turno
    private void mostrarPanel() {
        Jugador j = modelo.getTurnoActual().getJugadorActual();
        System.out.println();
        System.out.println("--- TURNO DE: " + j.getNombre() + " (" + j.getColor() + ") ---");
        System.out.println("Fase: " + modelo.getTurnoActual().getEstadoTurno()
                + " | Tiradas pendientes: " + modelo.getTurnoActual().getLanzamientosPendientes()
                + " | Movimientos por usar: " + modelo.getTurnoActual().getMovimientosPendientes());
        System.out.println("Fichas de " + j.getNombre() + ":");
        for (Ficha f : j.getFichas()) {

            if (f.getEstado() != EstadoFicha.EN_BASE) {
                System.out.println("   Ficha " + f.getIdFicha() + " -> " + f.getCasillaActual().getNumeroCasilla());
            }

            System.out.println("   Ficha " + f.getIdFicha() + " -> " + f.getEstado());

        }
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
    public void actualizar(String evento) {

        switch (evento) {
            case "DADO_LANZADO":
                System.out.println("🎲 " + modelo.getTurnoActual().getJugadorActual().getNombre() + " ha lanzado un " + modelo.getResultadoDado());
                break;
            case "SIN_MOVIMIENTOS":
                System.out.println(modelo.getTurnoActual().getJugadorActual().getNombre() + " no puede realizar movimientos");
                break;


            //case "FICHA_MOVIDA":
            //    MovimientoDTO movDto = (MovimientoDTO) datos;
            //    System.out.println("♟️ " + movDto.getJugador() + " movió la ficha " + movDto.getFichaId() + " a la casilla " + movDto.getNuevaPosicion());
            //    break;

        }

    }

}
