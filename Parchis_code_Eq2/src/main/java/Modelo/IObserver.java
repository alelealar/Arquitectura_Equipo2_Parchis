/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Modelo;

/**
 * Contrato del Patrón Observer.
 * Cualquier clase gráfica que quiera enterarse de los cambios del juego,
 * debe implementar esta interfaz.
 * @author Equipo 2
 */
public interface IObserver {
    public void actualizar(String evento);
}
