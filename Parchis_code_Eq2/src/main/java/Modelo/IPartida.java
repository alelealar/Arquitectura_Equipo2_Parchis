package Modelo;

import Dominio.Ficha;

/**
 * Interfaz que define las operaciones del modelo principal para la gestión de la partida de Parchís.
 * Establece el contrato para el control de turnos, ejecución de dados, aplicación de reglas de negocio
 * (capturas, bonos y penalizaciones), actualización del marcador y notificación a observadores.
 *
 * @author Equipo 2
 */
public interface IPartida {

   /**
    * Notifica a los observadores registrados sobre cualquier cambio en el estado de la partida.
    */
   void notificarObservers();

   /**
    * Concede un turno extra de lanzamiento al jugador activo.
    */
   void otorgarTurnoExtra();

   /**
    * Aplica la regla de captura sobre la ficha de un rival, enviándola a la base y acreditando el bono correspondiente.
    *
    * @param fichaRival Ficha del oponente capturada.
    */
   void aplicarCaptura(Ficha fichaRival);

   /**
    * Calcula y agrega pasos adicionales de bono a los movimientos pendientes del turno.
    *
    * @param distancia Cantidad de casillas/pasos a otorgar como bono.
    */
   void calcularFichasBono(int distancia);

   /**
    * Ejecuta el tiro del dado para obtener un valor aleatorio entre 1 y 6 y procesa sus reglas.
    *
    * @return El número obtenido en el dado.
    */
   int lanzarDado();

   /**
    * Aplica una sanción a una ficha enviándola directamente a la base.
    *
    * @param ficha Ficha a penalizar.
    */
   void aplicarPenalizacion(Ficha ficha);

   /**
    * Evalúa qué fichas del jugador en turno tienen movimientos válidos según el resultado del dado.
    *
    * @param resultadoDado Puntos obtenidos en la tirada.
    */
   void calcularMovimientosDispobibles(int resultadoDado);

   /**
    * Registra la entrada de una ficha a la meta y otorga las bonificaciones asociadas.
    *
    * @param fichaLlegada Ficha que alcanzó la meta.
    */
   void registrarLlegadaMeta(Ficha fichaLlegada);

   /**
    * Asigna pasos de bonificación al turno activo.
    *
    * @param extra Cantidad de pasos extra por aplicar.
    */
   void aplicarBono(int extra);

   /**
    * Determina si se mantiene el turno del jugador actual o se concede el turno al siguiente participante.
    */
   void gestionarSiguienteTurno();

   /**
    * Solicita el desplazamieto de una ficha específica utilizando el valor de dado disponible.
    *
    * @param idFicha Identificador único de la ficha que se desea mover.
    */
   void solicitarMovimiento(int idFicha);

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
}