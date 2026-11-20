package Pruebas;

import Controlador.ControlPartida;
import Dominio.ConfiguracionPartida;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import ENUM.ColorJugador;
import ENUM.EstadoFicha;
import ENUM.EstadoPartida;
import Vista.VistaJuego;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Equipo 2
 */
public class Prueba_view {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Jugador> jugadores = new ArrayList<>();
            jugadores.add(new Jugador(1L, "Oliver", ColorJugador.ROJO, "avatar1.png"));
            jugadores.add(new Jugador(2L, "Vale", ColorJugador.AZUL, "avatar2.png"));
            jugadores.add(new Jugador(3L, "Julián", ColorJugador.AMARILLO, "avatar3.png"));
            jugadores.add(new Jugador(4L, "Ale", ColorJugador.VERDE, "avatar4.png"));

            ConfiguracionPartida configuracion = new ConfiguracionPartida(4, 4, 5, 5);
            Partida modelo = new Partida(1L, jugadores, configuracion);
            modelo.setEstadoPartida(EstadoPartida.EN_CURSO);

            VistaJuego vista = new VistaJuego(modelo);
            modelo.agregarObserver(vista);

            ControlPartida controlador = new ControlPartida(vista, modelo);
            vista.setControlador(controlador);

            vista.setVisible(true);
            vista.actualizar("INICIO");
        });
    }
}