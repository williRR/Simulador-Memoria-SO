package simuladormemoria;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * La clase GestorDeProcesos maneja el ciclo de vida de los procesos,
 * desde su creación hasta su finalización. Implementa la interfaz Runnable
 * para ejecutarse en un hilo separado, monitorizando la cola de espera de
 * procesos.
 */
public class GestorDeProcesos implements Runnable {
    // Referencia a la memoria del sistema para asignar y liberar espacio.
    private final Memoria memoria;
    // Monitor para registrar y visualizar los eventos del sistema.
    private final Monitor monitor;
    // Cola de espera de procesos. Utiliza una BlockingQueue para manejar
    // de forma segura los procesos en un entorno concurrente.
    private final BlockingQueue<Proceso> colaEspera;
    // Mapa de procesos que están actualmente en ejecución.
    // ConcurrentMap garantiza que múltiples hilos puedan acceder y modificar
    // este mapa de forma segura.
    private final ConcurrentMap<Integer, Proceso> procesosEnEjecucion;

    /**
     * Constructor para inicializar el gestor de procesos.
     *
     * @param memoria Objeto Memoria para la gestión de la memoria.
     * @param monitor Objeto Monitor para el registro de eventos.
     */
    public GestorDeProcesos(Memoria memoria, Monitor monitor) {
        this.memoria = memoria;
        this.monitor = monitor;
        this.colaEspera = new LinkedBlockingQueue<>();
        this.procesosEnEjecucion = new ConcurrentHashMap<>();
    }

    /**
     * Método para agregar un nuevo proceso al sistema.
     * Intenta asignar memoria al proceso. Si hay suficiente, lo inicia.
     * De lo contrario, lo envía a la cola de espera.
     *
     * @param proceso El proceso a agregar.
     * @throws InterruptedException Si el hilo es interrumpido mientras espera.
     */
    public void agregarProceso(Proceso proceso) throws InterruptedException {
        // Intenta asignar la memoria requerida por el proceso.
        if (memoria.asignarMemoria(proceso.getMemoriaRequerida())) {
            // Si hay memoria, cambia el estado del proceso a "En ejecución".
            proceso.setStatus("En ejecución");
            // Agrega el proceso al mapa de procesos en ejecución.
            procesosEnEjecucion.put(proceso.getPid(), proceso);
            // Crea y ejecuta un nuevo hilo para el proceso.
            new Thread(new ProcesoExecutor(proceso, memoria, procesosEnEjecucion, monitor)).start();
            // Registra el evento en el monitor.
            monitor.agregarEvento("Proceso " + proceso.getNombre() + " (PID: " + proceso.getPid() + ") iniciado.");
        } else {
            // Si no hay memoria, agrega el proceso a la cola de espera.
            colaEspera.put(proceso);
            // Registra el evento de que el proceso ha sido enviado a la cola.
            monitor.agregarEvento("No hay memoria suficiente. Proceso " + proceso.getNombre() + " (PID: " + proceso.getPid() + ") enviado a la cola.");
        }
    }

    /**
     * Obtiene el mapa de procesos actualmente en ejecución.
     *
     * @return Un ConcurrentMap de procesos en ejecución.
     */
    public ConcurrentMap<Integer, Proceso> getProcesosEnEjecucion() {
        return procesosEnEjecucion;
    }

    /**
     * Obtiene la cola de procesos en espera.
     *
     * @return Una BlockingQueue de procesos en espera.
     */
    public BlockingQueue<Proceso> getColaEspera() {
        return colaEspera;
    }

    /**
     * El método 'run' se ejecuta en un hilo separado.
     * En este caso, el gestor de procesos monitorea la cola de espera
     * y, cuando un proceso está disponible, lo mueve para su ejecución.
     */
    @Override
    public void run() {
        try {
            // Bucle infinito para monitorear la cola de espera.
            while (true) {
                // 'take()' es un método que bloquea el hilo hasta que
                // un proceso esté disponible en la cola.
                Proceso procesoCandidato = colaEspera.take();
                // Registra el evento de que un proceso se mueve a ejecución.
                monitor.agregarEvento("Proceso " + procesoCandidato.getNombre() + " (PID: " + procesoCandidato.getPid() + ") movido a ejecución.");
                // Simula un tiempo de procesamiento para el evento.
                Thread.sleep(1000);
                // Nota: Actualmente, este código toma un proceso de la cola
                // pero no lo inicia. Esto puede ser un error de lógica
                // que necesitarías corregir. La lógica de 'agregarProceso'
                // es la que realmente inicia los hilos.
            }
        } catch (InterruptedException e) {
            // Si el hilo es interrumpido, se restaura el estado de interrupción.
            Thread.currentThread().interrupt();
        }
    }
}