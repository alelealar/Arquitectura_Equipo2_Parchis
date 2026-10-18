
package Dominio;

import ENUM.EstadoPartida;
import Modelo.IPartida;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Julian
 */
public class Partida implements IPartida{
    private Long idPartida;
    private EstadoPartida estadoPartida;
    private int votosInicio;
    private int indiceJugadorActual;
    private List<Jugador> ordenJugadores;
    private Map<Jugador, Integer> marcador;
    //Atributod que agregue porque no estan en el diagrama de clases
    private int contadorSeises;
    private Turno turnoActual;
    private boolean turnoExtra;
    private int resultadoDado;
    
    public Partida(Long idPartida, List<Jugador> jugadores){
        this.idPartida = idPartida;
        this.estadoPartida = EstadoPartida.EN_ESPERA;
        this.votosInicio = 0;
        this.indiceJugadorActual = 0;
        this.turnoExtra = false;
        this.contadorSeises = 0;
        this.resultadoDado = 1;
        
        this.ordenJugadores = new ArrayList<>(jugadores); 
        this.marcador = new HashMap<>();

        for (Jugador jugador : ordenJugadores) {
            marcador.put(jugador, 0); //agregar los jugadores al marcador
        }

        if (!ordenJugadores.isEmpty()) {
            this.turnoActual = new Turno(ordenJugadores.get(0)); //se asigna al primer jugador en la lista de jugadores el turno actual
        }
    }
    @Override
    public void notificarObservers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizarContadorSeises(int resultadoDado) {
        if(resultadoDado == 6){
            contadorSeises++;           //primero se aumenta al contador el seis o se restablece
        }else{
            contadorSeises = 0;
        }
        if(contadorSeises >= 3){
            Ficha fichaPenalizada = turnoActual.getUltimaFichaMovida();   //despues se verifica que no se penalizacion para el jugador
            if(fichaPenalizada != null){
                aplicarPenalizacion(fichaPenalizada);
            }
            contadorSeises = 0;         //si hay penalizacion sale del flujo y penaliza al jugador
            return;
        }
        if(resultadoDado == 6){
            otorgarTurnoExtra();            //por ultimo si no hay penalizacion se le otorga el turno extra al jugador
        }
    }

    @Override
    public void otorgarTurnoExtra() {
        turnoExtra = true;
    }

    @Override
    public void aplicarCaptura(Ficha fichaRival) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void calcularFichasBono(int distancia) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int lanzarDado() {
        resultadoDado = (int) (Math.random() * 6) + 1;
        actualizarContadorSeises(resultadoDado);
        return resultadoDado;
    }

    @Override
    public void aplicarPenalizacion(Ficha ficha) {
        if (ficha == null) {
            return;
        }
        ficha.cambiarEstado(ENUM.EstadoFicha.EN_BASE);
        ficha.cambiarPosicion(null);
        notificarObservers();
    }

    @Override
    public void calcularMovimientosDispobibles(int resultadoDado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void registrarLlegadaMeta(Ficha fichaLlegada) {
        if (fichaLlegada == null) {
            return;
        }
        fichaLlegada.cambiarEstado(ENUM.EstadoFicha.EN_META);
        evaluarFinPartida();
        notificarObservers();
    }

    @Override
    public void aplicarBono(int extra) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void gestionarSiguienteTurno() {
        if (ordenJugadores == null || ordenJugadores.isEmpty()) {  //validar que haya jugadores en la lista
            return; 
        }
        if (turnoExtra) {
            turnoExtra = false; //si el jugador tiene turno extra, no se cambia el turno al siguiente jugador
            return;             
        }
        indiceJugadorActual++;
        if (indiceJugadorActual >= ordenJugadores.size()) { //se va aumentando en uno cada que pase de turno
            indiceJugadorActual = 0;                        //una vez ya recorrio vuelve otra vez al primer jugador en la lista 
        }
        contadorSeises = 0; //reiniciar el contador de seises
        turnoActual.setJugadorActual(ordenJugadores.get(indiceJugadorActual));  //otorgarle el turno al jugador 
        turnoActual.reiniciarTurno();                                           //reiniciar los valores del turno para que el jugador lo empiece nuevamente
        
        notificarObservers();     
    }

    @Override
    public void solicitarMovimiento(int idFicha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void evaluarFinPartida() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void calcularYGuardarEnMarcador() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void verificarFinalizarPartida() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
