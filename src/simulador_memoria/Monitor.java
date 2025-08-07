package simulador_memoria;

public class Monitor implements Runnable {
    private final GestorDeProcesos gestor;
    private final Memoria memoria;

    public Monitor(GestorDeProcesos gestor, Memoria memoria) {
        this.gestor = gestor;
        this.memoria = memoria;
    }

    @Override
    public void run() {
        while (true) {
            limpiarConsola();
            System.out.println("==================================================");
            System.out.println("SIMULADOR DE GESTIÓN DE PROCESOS EN MEMORIA (Java)");
            System.out.println("==================================================");

            System.out.printf("RAM Total: %d MB | RAM Disponible: %d MB | RAM Usada: %d MB\n",
                    memoria.getRamTotal(), memoria.getRamDisponible(), memoria.getRamUsada());
            System.out.println("--------------------------------------------------");

            System.out.println("Procesos en Ejecución:");
            if (!gestor.getProcesosEnEjecucion().isEmpty()) {
                gestor.getProcesosEnEjecucion().forEach((pid, proc) -> {
                    long tiempoRestante = proc.getDuracion() - (System.currentTimeMillis() - proc.getTiempoInicio()) / 1000;
                    System.out.printf("  -> %s (PID: %d) | Memoria: %d MB | Tiempo restante: %ds\n",
                            proc.getNombre(), pid, proc.getMemoriaRequerida(), tiempoRestante);
                });
            } else {
                System.out.println("  (Ninguno)");
            }

            System.out.println("--------------------------------------------------");

            System.out.println("Procesos en Cola de Espera:");
            if (!gestor.getColaEspera().isEmpty()) {
                gestor.getColaEspera().forEach(proc -> System.out.printf("  -> %s (PID: %d) | Memoria requerida: %d MB\n",
                        proc.getNombre(), proc.getPid(), proc.getMemoriaRequerida()));
            } else {
                System.out.println("  (Ninguno)");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void limpiarConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Silenciar el error si no se puede limpiar la consola
        }
    }
}