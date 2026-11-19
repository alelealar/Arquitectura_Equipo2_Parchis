package Vista;

import Controlador.ControlPartida;
import Dominio.ConfiguracionPartida;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Turno;
import ENUM.ColorJugador;
import ENUM.EstadoPartida;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author oliro
 */
public class JuegoEnConsola {

    public void iniciar() {
        // Crear lista de jugadores y configuración
        List<Jugador> jugadores = crearJugadores();
        ConfiguracionPartida configuracion = new ConfiguracionPartida(4, 4, 5, 5);

        // Crear partida (ya construye el tablero y asigna el primer turno automáticamente)
        Partida partida = new Partida(1L, jugadores, configuracion);
        partida.setEstadoPartida(EstadoPartida.EN_CURSO);

        // Creamos el controlador al que se va dirigir la vista
        ControlPartida controlador = new ControlPartida(partida);
        // Creamos la vista que en este caso sera en consola y le agregamos el modelo y el controlador
        VistaConsola vista = new VistaConsola(partida, controlador);

        // Agregamos la vista a la lista de subscriptores del modelo
        partida.agregarObserver(vista);

        // Iniciamos el flujo de la consola
        vista.iniciar();
    }

    public List<Jugador> crearJugadores() {
        Jugador j1 = new Jugador();
        j1.setIdJugador(1L);
        j1.setNombre("Oliver");
        j1.setColor(ColorJugador.ROJO);

        Jugador j2 = new Jugador();
        j2.setIdJugador(2L);
        j2.setNombre("Alejandra");
        j2.setColor(ColorJugador.AZUL);

        Jugador j3 = new Jugador();
        j3.setIdJugador(3L);
        j3.setNombre("Valeria");
        j3.setColor(ColorJugador.AMARILLO);

        Jugador j4 = new Jugador();
        j4.setIdJugador(4L);
        j4.setNombre("Julian");
        j4.setColor(ColorJugador.VERDE);

        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(j1);
        jugadores.add(j2);
        jugadores.add(j3);
        jugadores.add(j4);

        return jugadores;
    }
}