package com.eq2.parchis.cliente;

import com.eq2.parchis.comunes.red.ConfiguracionRed;

/**
 * Punto de entrada de ParchisCliente.jar.
 *
 * <p>ESQUELETO: aquí se armará el ensamblado del cliente:
 * BusEventosCliente, ProxyCliente, ManejadorEventosCliente,
 * ControladorCliente, ModeloCliente y la primera pantalla.</p>
 */
public class ClienteMain {

    public static void main(String[] args) {
        System.out.println("Cliente Parchis - se conectará por defecto al puerto "
                + ConfiguracionRed.PUERTO_POR_DEFECTO);
        // TODO: mostrar pantalla inicial (crear partida / unirse a partida)
    }
}
