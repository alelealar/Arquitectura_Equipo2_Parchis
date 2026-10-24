package Pruebas;

import Controlador.ControlPartida;
import Dominio.Casilla;
import Dominio.CasillaSalida;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Tablero;
import ENUM.ColorJugador;

import java.util.ArrayList;
import java.util.List;

/**
 * Prueba de escritorio exhaustiva e informativa para el caso de uso: Incorporar Ficha al Tablero
 * @author valeria
 */
public class Prueba_IncorporarFichaAlTablero {

    public static void main(String[] args) {
        // 1. Crear jugadores
        Jugador jugadorRojo = new Jugador(1L, "Carlos", ColorJugador.ROJO, "avatar1.png");
        jugadorRojo.crearFichas(4);
        for (Ficha f : jugadorRojo.getFichas()) f.setJugador(jugadorRojo);

        Jugador jugadorAzul = new Jugador(2L, "Ana", ColorJugador.AZUL, "avatar2.png");
        jugadorAzul.crearFichas(4);
        for (Ficha f : jugadorAzul.getFichas()) f.setJugador(jugadorAzul);

        List<Jugador> jugadores = List.of(jugadorRojo, jugadorAzul);

        // 2. Armar casillas del tablero
        List<Casilla> casillasParchis = new ArrayList<>();
        for (int i = 1; i <= 68; i++) {
            if (i == 5) {
                casillasParchis.add(new CasillaSalida(5L, 2, i, false, ColorJugador.ROJO));
            } else {
                casillasParchis.add(new Casilla((long) i, 2, i, false));
            }
        }

        Tablero tableroConfigurado = new Tablero(1L, casillasParchis);
        Partida modelo = new Partida(100L);
        modelo.prepararPartida(tableroConfigurado, jugadores);
        modelo.forzarInicio(); // Activa turno del jugador Rojo

        ControlPartida controlador = new ControlPartida(modelo);

        // Referencia a la Casilla de Salida (Casilla 5) para monitorear su estado
        CasillaSalida casillaSalidaRoja = (CasillaSalida) casillasParchis.get(4); // Índice 4 = Casilla 5

        Ficha fichaRoja1 = jugadorRojo.getFichas().get(0);
        Ficha fichaRoja2 = jugadorRojo.getFichas().get(1);
        Ficha fichaRoja3 = jugadorRojo.getFichas().get(2);

        System.out.println("==================================================");
        System.out.println("     PRUEBAS DE INCORPORACIÓN AL TABLERO");
        System.out.println("==================================================");
        imprimirEstadoCasilla(casillaSalidaRoja);

        // ---------------------------------------------------------------------
        // PRUEBA A: Dado = 3 (Inválido)
        // ---------------------------------------------------------------------
        System.out.println("\n--- PRUEBA A: Intentar incorporar con Dado = 3 (Inválido) ---");
        modelo.setResultadoDado(3);
        controlador.solicitarIncorporarFicha(fichaRoja1.getIdFicha());

        System.out.println("Estado Ficha 1 (Esperado: EN_BASE) -> Real: " + fichaRoja1.getEstado());
        imprimirEstadoCasilla(casillaSalidaRoja);

        // ---------------------------------------------------------------------
        // PRUEBA B: Dado = 5 (Válido - 1ra Ficha)
        // ---------------------------------------------------------------------
        System.out.println("\n--- PRUEBA B: Incorporar 1ra ficha con Dado = 5 (Válido) ---");
        modelo.setResultadoDado(5);
        controlador.solicitarIncorporarFicha(fichaRoja1.getIdFicha());

        System.out.println("Estado Ficha 1 (Esperado: EN_TABLERO) -> Real: " + fichaRoja1.getEstado());
        imprimirEstadoCasilla(casillaSalidaRoja);

        // ---------------------------------------------------------------------
        // PRUEBA C: Dado = 5 (Válido - 2da Ficha / Forma Barrera)
        // ---------------------------------------------------------------------
        System.out.println("\n--- PRUEBA C: Incorporar 2da ficha con Dado = 5 (Forma Barrera) ---");
        modelo.setResultadoDado(5);
        controlador.solicitarIncorporarFicha(fichaRoja2.getIdFicha());

        System.out.println("Estado Ficha 2 (Esperado: EN_TABLERO) -> Real: " + fichaRoja2.getEstado());
        imprimirEstadoCasilla(casillaSalidaRoja);

        // ---------------------------------------------------------------------
        // PRUEBA D: Dado = 5 (3ra Ficha cuando ya hay Barrera/Límite de 2)
        // ---------------------------------------------------------------------
        System.out.println("\n--- PRUEBA D: Intentar incorporar 3ra ficha habiendo BARRERA ---");
        modelo.setResultadoDado(5);
        controlador.solicitarIncorporarFicha(fichaRoja3.getIdFicha());

        System.out.println("Estado Ficha 3 (Esperado: EN_BASE) -> Real: " + fichaRoja3.getEstado());
        imprimirEstadoCasilla(casillaSalidaRoja);
    }

    /**
     * Método auxiliar para imprimir de forma clara el estado actual de la casilla de salida
     */
    private static void imprimirEstadoCasilla(CasillaSalida casilla) {
        System.out.println(" > [ESTADO CASILLA " + casilla.getNumeroCasilla() + "]: "
                + "Fichas contenidas: " + casilla.getFichas().size() + "/2 | "
                + "Barrera activa: " + casilla.isHayBarrera());
    }
}