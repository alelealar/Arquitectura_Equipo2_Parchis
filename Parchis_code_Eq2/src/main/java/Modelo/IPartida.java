package Modelo;

import Dominio.Ficha;

/**
 * Interfaz que define el contrato del modelo principal para la gestión del flujo de Parchís.
 * Establece los métodos necesarios para la ejecución de tiradas, control de turnos,
 * aplicación de reglas (capturas, bonos y penalizaciones), movimiento de fichas
 * y evaluación de victoria.
 *
 * @author Equipo 2
 */
public interface IPartida {
    
    /**
     * Registra un nuevo observador para que comience a recibir notificaciones 
     * sobre los cambios en el estado de la partida.
     * 
     * @param observador El componente que se va a suscribir.
     */
    void agregarObserver(IObserver observador);

   /**
    * Notifica a los observadores registrados sobre cualquier cambio en el estado de la partida.
    */
   void notificarObservers();

   /**
    * Ejecuta el tiro del dado para obtener un valor aleatorio entre 1 y 6 y procesa sus reglas.
    *
    * @return El número obtenido en el dado.
    */
   int lanzarDado();

   /**
    * Concede un turno extra de lanzamiento al jugador activo.
    */
   void otorgarTurnoExtra();

   /**
    * Determina si el jugador activo mantiene su turno o se cede el control al siguiente participante.
    */
   void gestionarSiguienteTurno();

   /**
    * Solicita el desplazamiento de una ficha específica utilizando el valor de dado disponible.
    *
    * @param idFicha Identificador único de la ficha que se desea mover.
    */
   void solicitarMovimiento(int idFicha);

   /**
    * Evalúa qué fichas del jugador en turno tienen movimientos válidos según el resultado del dado.
    *
    * @param resultadoDado Puntos obtenidos en la tirada.
    */
   void calcularMovimientosDisponibles(int resultadoDado);

   /**
    * Aplica la regla de captura sobre la ficha de un rival, enviándola a la base y acreditando el bono.
    *
    * @param fichaRival Ficha del oponente capturada.
    */
   void aplicarCaptura(Ficha fichaRival);

   /**
    * Aplica una sanción a una ficha enviándola directamente a la base.
    *
    * @param ficha Ficha a penalizar.
    */
   void aplicarPenalizacion(Ficha ficha);

   /**
    * Asigna pasos de bonificación al turno activo.
    *
    * @param extra Cantidad de pasos extra por aplicar.
    */
   void aplicarBono(int extra);

   /**
    * Calcula y agrega pasos adicionales de bono a los movimientos pendientes del turno.
    *
    * @param distancia Cantidad de casillas/pasos a otorgar como bono.
    */
   void calcularFichasBono(int distancia);

   /**
    * Registra la entrada de una ficha a la meta y otorga las bonificaciones asociadas.
    *
    * @param fichaLlegada Ficha que alcanzó la meta.
    */
   void registrarLlegadaMeta(Ficha fichaLlegada);

   /**
    * Evalúa si se han cumplido las condiciones necesarias para concluir la partida.
    */
   void evaluarFinPartida();

   /**
    * Calcula el puntaje final acumulado por cada jugador y lo guarda en el marcador.
    */
   void calcularYGuardarEnMarcador();

   /**
    * Verifica si la partida ha concluido para efectuar el cierre del juego y la actualización del marcador.
    */
   void verificarFinalizarPartida();
   
   /**
     * Intenta sacar una ficha de la cárcel a la casilla de salida del jugador.
     */
   void incorporarFichaATablero(Long idFicha);
}