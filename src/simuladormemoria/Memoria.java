package simuladormemoria;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que simula la gestión de la memoria RAM.
 * Utiliza AtomicInteger para manejar la concurrencia de forma segura.
 */
public class Memoria {
    // Constante para el tamaño total de la memoria RAM en MB.
    private static final int RAM_TOTAL = 1024;
    // Variable atómica para rastrear la RAM disponible, garantizando
    // operaciones seguras entre múltiples hilos.
    private final AtomicInteger ramDisponible;

    /**
     * Constructor que inicializa la memoria disponible con la RAM total.
     */
    public Memoria() {
        this.ramDisponible = new AtomicInteger(RAM_TOTAL);
    }

    /**
     * Devuelve el tamaño total de la RAM.
     * @return El total de RAM.
     */
    public int getRamTotal() {
        return RAM_TOTAL;
    }

    /**
     * Devuelve la cantidad de RAM disponible en este momento.
     * @return La RAM disponible.
     */
    public int getRamDisponible() {
        return ramDisponible.get();
    }

    /**
     * Calcula y devuelve la cantidad de RAM que está en uso.
     * @return La RAM usada.
     */
    public int getRamUsada() {
        return RAM_TOTAL - ramDisponible.get();
    }

    /**
     * Intenta asignar una cantidad de memoria.
     * El método es sincronizado para evitar condiciones de carrera.
     *
     * @param cantidad La memoria a asignar.
     * @return true si la asignación fue exitosa, false en caso contrario.
     */
    public synchronized boolean asignarMemoria(int cantidad) {
        // Verifica si hay suficiente RAM disponible.
        if (ramDisponible.get() >= cantidad) {
            // Asigna la memoria y actualiza la cantidad disponible.
            ramDisponible.addAndGet(-cantidad);
            return true;
        }
        return false;
    }

    /**
     * Libera una cantidad de memoria.
     * También es un método sincronizado para garantizar la seguridad.
     *
     * @param cantidad La memoria a liberar.
     */
    public synchronized void liberarMemoria(int cantidad) {
        // Devuelve la memoria y actualiza la cantidad disponible.
        ramDisponible.addAndGet(cantidad);
    }
}