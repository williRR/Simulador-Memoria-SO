package simuladormemoria;

import java.util.concurrent.ConcurrentMap;
import java.util.Date;

/**
 * La clase ProcesoExecutor es la responsable de la ejecución de un proceso.
 * Cada instancia de esta clase se ejecuta en un hilo separado y simula
 * la vida útil de un proceso, desde su inicio hasta su finalización.
 */
public class ProcesoExecutor implements Runnable {
    // Referencia al objeto Proceso que esta clase va a ejecutar.
    private final Proceso proceso;
    // Referencia a la memoria para poder liberar el espacio al finalizar.
    private final Memoria memoria;
    // Mapa concurrente de procesos en ejecución. Es necesario para eliminar
    // el proceso de la lista cuando termine.
    private final ConcurrentMap<Integer, Proceso> procesosEnEjecucion;
    // Referencia al monitor para registrar el evento de finalización del proceso.
    private final Monitor monitor;

    /**
     * Constructor que inicializa el ejecutor con las dependencias necesarias.
     * @param proceso El proceso que se va a ejecutar.
     * @param memoria El objeto Memoria para la gestión de la memoria.
     * @param procesosEnEjecucion El mapa de procesos en ejecución para poder eliminarse de él.
     * @param monitor El monitor para registrar eventos.
     */
    public ProcesoExecutor(Proceso proceso, Memoria memoria, ConcurrentMap<Integer, Proceso> procesosEnEjecucion, Monitor monitor) {
        this.proceso = proceso;
        this.memoria = memoria;
        this.procesosEnEjecucion = procesosEnEjecucion;
        this.monitor = monitor;
    }

    /**
     * El método run() contiene la lógica de ejecución del proceso.
     * Este método es llamado cuando se inicia el hilo.
     */
    @Override
    public void run() {
        // Imprime un mensaje en la consola indicando que el proceso ha comenzado.
        System.out.printf("[%s] ---> Iniciando ejecución del proceso: %s (PID: %d)\n",
                new Date(), proceso.getNombre(), proceso.getPid());
        // Establece el tiempo de inicio del proceso para calcular el tiempo restante.
        proceso.setTiempoInicio(System.currentTimeMillis());

        try {
            // Simula el tiempo de ejecución del proceso durmiendo el hilo.
            // La duración del proceso se multiplica por 1000 para convertir segundos a milisegundos.
            Thread.sleep(proceso.getDuracion() * 1000L);
        } catch (InterruptedException e) {
            // Si el hilo es interrumpido durante la espera, se restaura el estado de interrupción.
            Thread.currentThread().interrupt();
        } finally {
            // Este bloque se ejecuta siempre, tanto si el proceso termina con éxito
            // como si es interrumpido, asegurando que la limpieza se haga.

            // 1. Libera la memoria que el proceso había ocupado.
            memoria.liberarMemoria(proceso.getMemoriaRequerida());

            // 2. Elimina el proceso del mapa de procesos en ejecución.
            procesosEnEjecucion.remove(proceso.getPid());

            // 3. Registra el evento de finalización en el monitor.
            monitor.agregarEvento("Proceso " + proceso.getNombre() + " (PID: " + proceso.getPid() + ") finalizado y memoria liberada.");
        }
    }
}