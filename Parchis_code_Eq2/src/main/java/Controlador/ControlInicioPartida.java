package Controlador;

import Modelo.IPartida;

public class ControlInicioPartida {

    private final IPartida partida;
    private final Long idJugadorLocal;

    public ControlInicioPartida(IPartida partida, Long idJugadorLocal) {
        this.partida = partida;
        this.idJugadorLocal = idJugadorLocal;
    }

    public void confirmarInicio(boolean listo) {
        if (partida != null && idJugadorLocal != null) {
            partida.confirmarInicioJugador(idJugadorLocal, listo);
        }
    }

    public Long getIdJugadorLocal() {
        return idJugadorLocal;
    }
}
