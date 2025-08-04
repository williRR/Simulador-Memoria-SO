package simulador_memoria;

import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimuladorMemoria {
    // Constantes del sistema
    private static final int RAM_TOTAL = 1024; // 1 GB en MB

    // Estructuras de datos compartidas y seguras para hilos
    private static final AtomicInteger ramDisponible = new AtomicInteger(RAM_TOTAL);
    private static final ConcurrentMap<Integer, Proceso> procesosEnEjecucion = new ConcurrentHashMap<>();
    private static final BlockingQueue<Proceso> colaEspera = new LinkedBlockingQueue<>();
    private static final AtomicInteger pidCounter = new AtomicInteger(1000);

    // Clase para representar un proceso
    static class Proceso {
        private final int pid;
        private final String nombre;
        private final int memoriaRequerida;
        private final int duracion;
        private long tiempoInicio;
        private String status;

        public Proceso(String nombre, int memoriaRequerida, int duracion) {
            this.pid = pidCounter.getAndIncrement();
            this.nombre = nombre;
            this.memoriaRequerida = memoriaRequerida;
            this.duracion = duracion;
            this.status = "En cola";
        }

        // Getters
        public int getPid() { return pid; }
        public String getNombre() { return nombre; }
        public int getMemoriaRequerida() { return memoriaRequerida; }
        public int getDuracion() { return duracion; }
        public long getTiempoInicio() { return tiempoInicio; }
        public void setTiempoInicio(long tiempoInicio) { this.tiempoInicio = tiempoInicio; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        @Override
        public String toString() {
            return String.format("PID: %d | Nombre: %s | Memoria: %d MB | Duración: %ds",
                    pid, nombre, memoriaRequerida, duracion);
        }
    }

    // Tarea para ejecutar un proceso en un hilo
    static class ProcesoExecutor implements Runnable {
        private final Proceso proceso;

        public ProcesoExecutor(Proceso proceso) {
            this.proceso = proceso;
        }

        @Override
        public void run() {
            System.out.printf("[%s] ---> Iniciando ejecución del proceso: %s (PID: %d)\n",
                    new Date(), proceso.getNombre(), proceso.getPid());
            proceso.setTiempoInicio(System.currentTimeMillis());

            try {
                Thread.sleep(proceso.getDuracion() * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // Liberar memoria y limpiar el estado del proceso
                ramDisponible.addAndGet(proceso.getMemoriaRequerida());
                procesosEnEjecucion.remove(proceso.getPid());
                System.out.printf("[%s] <--- Finalizado y liberando memoria del proceso: %s (PID: %d)\n",
                        new Date(), proceso.getNombre(), proceso.getPid());
            }
        }
    }

    // Tarea para el gestor de procesos
    static class GestorDeProcesos implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Proceso procesoCandidato = colaEspera.peek();
                    if (procesoCandidato != null && ramDisponible.get() >= procesoCandidato.getMemoriaRequerida()) {
                        procesoCandidato = colaEspera.take();
                        if (ramDisponible.get() >= procesoCandidato.getMemoriaRequerida()) {
                            ramDisponible.addAndGet(-procesoCandidato.getMemoriaRequerida());
                            procesoCandidato.setStatus("En ejecución");
                            procesosEnEjecucion.put(procesoCandidato.getPid(), procesoCandidato);
                            new Thread(new ProcesoExecutor(procesoCandidato)).start();
                        } else {
                            colaEspera.put(procesoCandidato); // Volver a poner en la cola si la memoria ya no es suficiente
                        }
                    }
                    Thread.sleep(1000); // Pausa para no consumir CPU
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Tarea para el monitor del sistema
    static class Monitor implements Runnable {
        @Override
        public void run() {
            while (true) {
                limpiarConsola();
                System.out.println("==================================================");
                System.out.println("SIMULADOR DE GESTIÓN DE PROCESOS EN MEMORIA (Java)");
                System.out.println("==================================================");

                int ramUsada = RAM_TOTAL - ramDisponible.get();
                System.out.printf("RAM Total: %d MB | RAM Disponible: %d MB | RAM Usada: %d MB\n",
                        RAM_TOTAL, ramDisponible.get(), ramUsada);
                System.out.println("--------------------------------------------------");

                System.out.println("Procesos en Ejecución:");
                if (!procesosEnEjecucion.isEmpty()) {
                    procesosEnEjecucion.forEach((pid, proc) -> {
                        long tiempoRestante = proc.getDuracion() - (System.currentTimeMillis() - proc.getTiempoInicio()) / 1000;
                        System.out.printf("  -> %s (PID: %d) | Memoria: %d MB | Tiempo restante: %ds\n",
                                proc.getNombre(), pid, proc.getMemoriaRequerida(), tiempoRestante);
                    });
                } else {
                    System.out.println("  (Ninguno)");
                }

                System.out.println("--------------------------------------------------");

                System.out.println("Procesos en Cola de Espera:");
                if (!colaEspera.isEmpty()) {
                    colaEspera.forEach(proc -> System.out.printf("  -> %s (PID: %d) | Memoria requerida: %d MB\n",
                            proc.getNombre(), proc.getPid(), proc.getMemoriaRequerida()));
                } else {
                    System.out.println("  (Ninguno)");
                }

                try {
                    Thread.sleep(2000); // Actualizar cada 2 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void limpiarConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Manejar errores
        }
    }

    public static void crearProceso(String nombre, int memoria, int duracion) throws InterruptedException {
        Proceso nuevoProceso = new Proceso(nombre, memoria, duracion);
        if (ramDisponible.get() >= nuevoProceso.getMemoriaRequerida()) {
            ramDisponible.addAndGet(-nuevoProceso.getMemoriaRequerida());
            nuevoProceso.setStatus("En ejecución");
            procesosEnEjecucion.put(nuevoProceso.getPid(), nuevoProceso);
            new Thread(new ProcesoExecutor(nuevoProceso)).start();
        } else {
            colaEspera.put(nuevoProceso);
            System.out.printf("[%s] !!! No hay memoria suficiente. Proceso %s (PID: %d) enviado a la cola.\n",
                    new Date(), nuevoProceso.getNombre(), nuevoProceso.getPid());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Iniciando simulador...");

        // Iniciar los hilos principales
        new Thread(new GestorDeProcesos()).start();
        new Thread(new Monitor()).start();

        // Pausa inicial para que los hilos se estabilicen
        Thread.sleep(2000);

        // Ejemplo de creación de procesos
        crearProceso("Navegador Web", 350, 10);
        crearProceso("Editor de Código", 250, 15);
        Thread.sleep(1000);
        crearProceso("Juego", 700, 20);
        Thread.sleep(1000);
        crearProceso("Reproductor de Música", 100, 5);
    }
}