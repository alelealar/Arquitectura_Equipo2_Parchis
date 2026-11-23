package com.eq2.parchis.servidor;

import com.eq2.parchis.comunes.red.ConfiguracionRed;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Punto de entrada de ParchisServidor.jar.
 *
 * <p>Uso: {@code java -jar ParchisServidor.jar [puerto]}</p>
 *
 * <p>ESQUELETO: por ahora solo abre el puerto, avisa que está listo y
 * registra las conexiones. Aquí se armará después el ensamblado:
 * BusEventosServidor, ProxyServidor, ManejadorEventosServidor, FachadaServidor
 * y gestores.</p>
 */
public class ServidorMain {

    public static void main(String[] args) {
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : ConfiguracionRed.PUERTO_POR_DEFECTO;

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            // El LanzadorServidor del cliente espera esta línea exacta
            System.out.println(ConfiguracionRed.MENSAJE_SERVIDOR_LISTO + " " + puerto);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Conexión recibida de " + socket.getRemoteSocketAddress());
                // TODO: entregar el socket al ProxyServidor
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor en el puerto " + puerto + ": " + e.getMessage());
            System.exit(1);
        }
    }
}
