package Pruebas;

import Controlador.ControlInicioPartida;
import Controlador.ControlPartida;
import Dominio.Jugador;
import Dominio.Partida;
import ENUM.ColorJugador;
import ENUM.EstadoPartida;
import Vista.VistaJuego;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class PruebaInicioPartida {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Jugador j1 = new Jugador(1L, "Oliver", ColorJugador.AMARILLO, "avatar1");
            Jugador j2 = new Jugador(2L, "Vale", ColorJugador.AZUL, "avatar2");
            Jugador j3 = new Jugador(3L, "Julian", ColorJugador.VERDE, "avatar3");
            Jugador j4 = new Jugador(4L, "Ale", ColorJugador.ROJO, "avatar4");

            List<Jugador> jugadores = Arrays.asList(j1);

            Partida partida = new Partida(100L, jugadores);
            VistaJuego vista = new VistaJuego(partida);

            ControlPartida controlPartida = new ControlPartida(vista, partida);
            vista.setControlador(controlPartida);

            for (Jugador j : jugadores) {
                vista.registrarControladorInicio(new ControlInicioPartida(partida, j.getIdJugador()));
            }

            partida.agregarObserver(vista);
            if (partida.getEstadoPartida() == EstadoPartida.EN_CURSO) {
                vista.actualizar("PARTIDA_INICIADA");
            } else {
                vista.actualizar("JUGADOR_LISTO");
            }
            vista.setVisible(true);
        });
    }
}