package com.eq2.parchis.comunes.red;

/**
 * Constantes de red que cliente y servidor deben compartir.
 */
public final class ConfiguracionRed {

    /** Puerto que usa el servidor si no se le pasa otro como argumento. */
    public static final int PUERTO_POR_DEFECTO = 5000;

    /**
     * Línea que el servidor imprime en consola cuando ya está escuchando.
     * El LanzadorServidor del cliente espera esta línea antes de conectarse.
     */
    public static final String MENSAJE_SERVIDOR_LISTO = "SERVIDOR_LISTO";

    private ConfiguracionRed() {
    }
}
