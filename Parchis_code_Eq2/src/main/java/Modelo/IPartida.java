
package Modelo;

import Dominio.Ficha;

/**
 * 
 * @author Julian
 */
public interface IPartida {
   void notificarObservers();
   void actualizarContadorSeises(int resultadoDado);
   void otorgarTurnoExtra();
   void aplicarCaptura(Ficha fichaRival);
   void calcularFichasBono(int distancia);
   int lanzarDado();
   void aplicarPenalizacion(Ficha ficha);
   void calcularMovimientosDispobibles(int resultadoDado);
   void registrarLlegadaMeta(Ficha fichaLlegada);
   void aplicarBono(int extra);
   void gestionarSiguienteTurno();
   void solicitarMovimiento(int idFicha);
   void evaluarFinPartida();
   void calcularYGuardarEnMarcador();
   void verificarFinalizarPartida();
}
