package simuladormemoria;

import java.util.Queue;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * La clase Monitor actúa como la interfaz de usuario de la simulación.
 * Se ejecuta en un hilo separado (implementa Runnable) para refrescar
 * continuamente la pantalla con el estado actual del sistema.
 */
public class Monitor implements Runnable {
    // Referencia al GestorDeProcesos para obtener el estado de los procesos.
    private GestorDeProcesos gestor;
    // Referencia a la Memoria para mostrar su estado de uso.
    private final Memoria memoria;
    // Cola concurrente para almacenar los eventos más recientes.
    private final Queue<String> eventosRecientes;
    // Límite de eventos a mostrar en pantalla.
    private static final int MAX_EVENTOS = 5;

    /**
     * Constructor del Monitor.
     * @param memoria Objeto Memoria para monitorear su estado.
     */
    public Monitor(Memoria memoria) {
        this.memoria = memoria;
        // Se usa ConcurrentLinkedQueue para operaciones seguras en hilos.
        this.eventosRecientes = new ConcurrentLinkedQueue<>();
    }

    /**
     * Establece la referencia al GestorDeProcesos.
     * Se usa un setter para evitar dependencias circulares en el constructor.
     * @param gestor Objeto GestorDeProcesos.
     */
    public void setGestor(GestorDeProcesos gestor) {
        this.gestor = gestor;
    }

    /**
     * Agrega un nuevo evento a la cola de eventos recientes.
     * Si la cola excede el tamaño máximo, elimina el evento más antiguo.
     * @param evento El mensaje del evento a registrar.
     */
    public void agregarEvento(String evento) {
        // Añade la fecha y hora al evento antes de agregarlo a la cola.
        eventosRecientes.add(String.format("[%s] %s", new Date(), evento));
        // Mantiene la cola con un máximo de MAX_EVENTOS.
        if (eventosRecientes.size() > MAX_EVENTOS) {
            eventosRecientes.poll(); // Elimina el evento que entró primero.
        }
    }

    /**
     * El método run() es el corazón del monitor. Se ejecuta en un bucle infinito
     * para refrescar la pantalla cada 2 segundos.
     */
    @Override
    public void run() {
        while (true) {
            // Limpia la consola para una visualización limpia.
            limpiarConsola();
            // Imprime cada sección de la interfaz.
            imprimirEncabezado();
            imprimirEstadoRAM();
            // Solo imprime la información del gestor si ya ha sido inicializado.
            if (gestor != null) {
                imprimirProcesosEnEjecucion();
                imprimirColaDeEspera();
            }
            imprimirEventosRecientes();

            try {
                // Espera 2 segundos antes de refrescar la pantalla de nuevo.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Si el hilo es interrumpido, restaura su estado.
                Thread.currentThread().interrupt();
            }
        }
    }

    // --- Métodos privados para imprimir las distintas secciones ---

    private void imprimirEncabezado() {
        // Imprime el título del simulador.
        System.out.println("+------------------------------------------------------+");
        System.out.println("| SIMULADOR DE GESTIÓN DE PROCESOS EN MEMORIA (Java)   |");
        System.out.println("+------------------------------------------------------+");
    }

    private void imprimirEstadoRAM() {
        // Obtiene el estado actual de la memoria.
        int ramTotal = memoria.getRamTotal();
        int ramDisponible = memoria.getRamDisponible();
        int ramUsada = memoria.getRamUsada();
        String barraProgreso = generarBarra(ramUsada, ramTotal);

        // Aplica colores ANSI a la barra de progreso según el uso de la RAM.
        String colorRAM = "\u001B[32m"; // Verde (por defecto)
        if (ramUsada > ramTotal * 0.75) {
            colorRAM = "\u001B[31m"; // Rojo
        } else if (ramUsada > ramTotal * 0.5) {
            colorRAM = "\u001B[33m"; // Amarillo
        }

        // Imprime el estado detallado de la memoria.
        System.out.printf("| RAM Total: %d MB | RAM Disponible: %d MB\n", ramTotal, ramDisponible);
        System.out.printf("| RAM Usada: %d MB  | %s%s\u001B[0m\n", ramUsada, colorRAM, barraProgreso);
        System.out.println("+------------------------------------------------------+");
    }

    /**
     * Genera una cadena de texto que representa una barra de progreso.
     * @param actual El valor actual (RAM usada).
     * @param total El valor total (RAM total).
     * @return Una barra de progreso en formato String.
     */
    private String generarBarra(int actual, int total) {
        int porcentaje = (int) (((double) actual / total) * 100);
        int bloques = porcentaje / 5; // Cada bloque representa el 5%.
        StringBuilder sb = new StringBuilder("[");
        // Llena la barra con '#' o espacios.
        for (int i = 0; i < 20; i++) {
            if (i < bloques) {
                sb.append("#");
            } else {
                sb.append(" ");
            }
        }
        sb.append("] ").append(porcentaje).append("%");
        return sb.toString();
    }

    private void imprimirProcesosEnEjecucion() {
        // Muestra el número de procesos en ejecución.
        System.out.printf("| PROCESOS EN EJECUCIÓN (%d)\n", gestor.getProcesosEnEjecucion().size());
        System.out.println("+------------------------------------------------------+");
        // Itera sobre los procesos en ejecución para mostrar sus detalles.
        if (!gestor.getProcesosEnEjecucion().isEmpty()) {
            gestor.getProcesosEnEjecucion().forEach((pid, proc) -> {
                // Calcula el tiempo restante de ejecución.
                long tiempoRestante = proc.getDuracion() - (System.currentTimeMillis() - proc.getTiempoInicio()) / 1000;
                System.out.printf("|   - %s (PID: %d)\n", proc.getNombre(), pid);
                System.out.printf("|     Memoria: %d MB | Duración: %ds | Restante: %ds\n",
                        proc.getMemoriaRequerida(), proc.getDuracion(), tiempoRestante);
            });
        } else {
            System.out.println("|   (Ninguno)");
        }
        System.out.println("+------------------------------------------------------+");
    }

    private void imprimirColaDeEspera() {
        // Muestra el número de procesos en la cola de espera.
        System.out.printf("| COLA DE ESPERA (%d)\n", gestor.getColaEspera().size());
        System.out.println("+------------------------------------------------------+");
        // Itera sobre los procesos en la cola para mostrarlos.
        if (!gestor.getColaEspera().isEmpty()) {
            gestor.getColaEspera().forEach(proc -> System.out.printf("|   - %s (PID: %d) -> Memoria requerida: %d MB\n",
                    proc.getNombre(), proc.getPid(), proc.getMemoriaRequerida()));
        } else {
            System.out.println("|   (Ninguno)");
        }
        System.out.println("+------------------------------------------------------+");
    }

    private void imprimirEventosRecientes() {
        // Muestra los eventos del sistema.
        System.out.println("| EVENTOS RECIENTES                                    |");
        System.out.println("+------------------------------------------------------+");
        // Itera sobre la cola de eventos y los imprime.
        if (!eventosRecientes.isEmpty()) {
            eventosRecientes.forEach(evento -> System.out.printf("| %s\n", evento));
        } else {
            System.out.println("|   (Ninguno)");
        }
        System.out.println("+------------------------------------------------------+");
    }

    /**
     * Imprime múltiples líneas en blanco para simular la limpieza de la consola.
     */
    public void limpiarConsola() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}