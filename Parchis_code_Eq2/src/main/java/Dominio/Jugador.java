
package Dominio;

import ENUM.ColorJugador;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un Jugador dentro de la partida de parchis.
 * @author Julian
 */
public class Jugador {
    
    /**
     * Identificador unico del jugador 
     */
    private Long idJugador;
    
    /**
     * Nombre del jugador dentro de la partida 
     */
    private String nombre;
    
    /**
     * Color que representara al jugador dentro de la partida 
     */
    private ColorJugador color;
    
    /**
     * Avatar que el jugador seleccione para representarlo  
     */
    private String avatar;
    
    /**
     * Las fichas que le corresponden al jugador para jugar la partida
     */
    private List<Ficha> fichas; //Aqui es la composicion de jugador hacia ficha 
    
    /**
     * Constructor default
     */
    public Jugador() {}
    
    /**
     * Constructor completo para inicializar un Jugador, con su ID, nombre, color y avatar. 
     * @param idJugador Identificador unico del jugador 
     * @param nombre Nombre del jugador
     * @param color Color que representara al jugador en la partida
     * @param avatar Avatar que representara al jugador en la partida
     */
    public Jugador(Long idJugador, String nombre, ColorJugador color, String avatar) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.color = color;
        this.avatar = avatar;
    }

    /**
     * Metodo para obtener el identificador unico del jugador
     * @return el ID del jugador 
     */
    public Long getIdJugador() {
        return idJugador;
    }
    
    /**
     * Metodo para asignarle un ID a un jugador 
     * @param idJugador el ID que se le asignara al jugador
     */
    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    /**
     * Metodo para obtener el nombre del jugador
     * @return el nombre del Jugador
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Metodo para asignarle el nombre a un jugador
     * @param nombre que se le asignara al jugador
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo para obtener el color que representa 
     * la base y las fichas del jugador
     * @return el color que represente al jugador 
     */
    public ColorJugador getColor() {
        return color;
    }
    
    /**
     * Metodo para asignarle el color que representara a un jugador
     * @param color que se le asignara al jugador 
     */
    public void setColor(ColorJugador color) {
        this.color = color;
    }

    /**
     * Metodo para obtener el avatar del jugador
     * @return el avatar que represente al jugador
     */
    public String getAvatar() {
        return avatar;
    }
    
    /**
     * Metodo para asignarle el avatar a un jugador
     * @param avatar que se le desea asignar al jugador
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    /**
     * Metodo para obtener las fichas del jugador
     * @return una lista con las fichas correspondientes al jugador
     */
    public List<Ficha> getFichas() {
        return fichas;
    }
    
    /**
     * Crea las fichas que pertenecen al jugador.
     * Metodo que se utilizara en configurar partida, al momento que el anfitrion seleccione
     * la cantidad de fichas por jugador para asignarselas.
     * @param cantidad cantidad de fichas que tendrá el jugador
     */
    public void crearFichas(int cantidad) {
        fichas = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            Ficha ficha = new Ficha();
            ficha.setIdFicha((idJugador * 10) + i + 1); 
            fichas.add(ficha);
        }
    }
}
