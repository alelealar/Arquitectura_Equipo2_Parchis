
package Vista;

import Controlador.JuegoController;
import Dominio.Casilla;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Tablero;
import ENUM.ColorJugador;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oliro
 */
public class JuegoEnConsola{

    public void iniciar() {
        // Crear partida
        Partida partida = new Partida(1L);

        // Crear Tablero
        Tablero tablero = crearTablero();
        // Crear lista de jugadores
        List<Jugador> jugadores = crearJugadores();
        // Preparar partida agregando tablero y jugadores
        partida.prepararPartida(tablero, jugadores);

        // Creamos el controlador al que se va dirigir la vista
        JuegoController controlador = new JuegoController(partida);
        // Creamos la vista que en este caso sera en consola y le agregamos el modelo y el controlador
        VistaConsola vista = new VistaConsola(partida, controlador);

        // Agregamos la vista a la lista de subscriptores del modelo
        partida.agregarObserver(vista);

        // Forzamos inicio de partida
        partida.forzarInicio();
        // Iniciamos el flujo de la consola
        vista.iniciar();
    }

    // TODO Hay que hacer mejor la creacion de jugadores, de momento se inyectan fichas desde un metodo en dominio
    public List<Jugador>  crearJugadores() {

        Jugador j1 = new Jugador();
        j1.setIdJugador(1L);
        j1.setNombre("Oliver");
        j1.setColor(ColorJugador.ROJO);
        j1.crearFichas(4);

        Jugador j2 = new Jugador();
        j2.setIdJugador(2L);
        j2.setNombre("Alejandra");
        j2.setColor(ColorJugador.AZUL);
        j2.crearFichas(4);

        Jugador j3 = new Jugador();
        j3.setIdJugador(3L);
        j3.setNombre("Valeria");
        j3.setColor(ColorJugador.AMARILLO);
        j3.crearFichas(4);

        Jugador j4 = new Jugador();
        j4.setIdJugador(4L);
        j4.setNombre("Julian");
        j4.setColor(ColorJugador.VERDE);
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
