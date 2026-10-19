package Pruebas;

import Dominio.Casilla;
import Dominio.Jugador;
import Dominio.Tablero;

import java.util.ArrayList;
import java.util.List;

public class PruebasPreparacion {

    /**
     * Crea los jugadores y les agrega fichas
     */
    public List<Jugador>  crearJugadores() {

        Jugador j1 = new Jugador();
        j1.setIdJugador(1L);
        j1.setNombre("Jugador Rojo");
        j1.crearFichas(4);

        Jugador j2 = new Jugador();
        j2.setIdJugador(2L);
        j2.setNombre("Jugador Azul");
        j2.crearFichas(4);

        Jugador j3 = new Jugador();
        j3.setIdJugador(3L);
        j3.setNombre("Jugador Amarillo");
        j3.crearFichas(4);

        Jugador j4 = new Jugador();
        j4.setIdJugador(4L);
        j4.setNombre("Jugador Violeta");
        j4.crearFichas(4);

        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(j1);
        jugadores.add(j2);
        jugadores.add(j3);
        jugadores.add(j4);

        return jugadores;

    }

    public Tablero crearTablero() {
        Tablero tablero = new Tablero(1L, crearCasillas());
        tablero.setNumeroSalidaBase(5);
        return tablero;
    }

    private List<Casilla> crearCasillas() {
        List<Casilla> casillas = new ArrayList<>();

        // Creamos las 68 casillas numeradas del 1 al 68
        for (int i = 1; i <= 68; i++) {
            // Asignamos por defecto: maxFichas = 2, numeroCasilla = i, hayBarrera = false
            Casilla casilla = new Casilla(2, i, false);
            casillas.add(casilla);
        }

        return casillas;
    }

}
