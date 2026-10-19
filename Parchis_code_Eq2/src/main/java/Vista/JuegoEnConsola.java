
package Vista;

import Controlador.JuegoController;
import Dominio.Jugador;
import Dominio.Partida;
import ENUM.ColorJugador;
import Modelo.IObserver;
import Modelo.IPartida;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author oliro
 */
public class JuegoEnConsola{

    public void iniciar() {
        Partida partida = new Partida(1L, crearJugadores());
        JuegoController controlador = new JuegoController(partida);
        VistaConsola vista = new VistaConsola(partida, controlador);

        partida.agregarObserver(vista);
        // partida.iniciarPartida(); TODO
        vista.iniciar();
    }

    public List<Jugador>  crearJugadores() {

        Jugador j1 = new Jugador();
        j1.setIdJugador(1L);
        j1.setNombre("Oliver");
        j1.crearFichas(4);

        Jugador j2 = new Jugador();
        j2.setIdJugador(2L);
        j2.setNombre("Alejandra");
        j2.crearFichas(4);

        Jugador j3 = new Jugador();
        j3.setIdJugador(3L);
        j3.setNombre("Valeria");
        j3.crearFichas(4);

        Jugador j4 = new Jugador();
        j4.setIdJugador(4L);
        j4.setNombre("Julian");
        j4.crearFichas(4);

        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(j1);
        jugadores.add(j2);
        jugadores.add(j3);
        jugadores.add(j4);

        return jugadores;

    }


}
