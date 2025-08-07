package simuladormemoria;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;

public class Monitor implements Runnable {
    private final GestorDeProcesos gestor;
    private final Memoria memoria;
    private final Queue<String> eventosRecientes = new LinkedList<>();
    private static final int MAX_EVENTOS = 5;

    public Monitor(GestorDeProcesos gestor, Memoria memoria) {
        this.gestor = gestor;
        this.memoria = memoria;
    }

    public void agregarEvento(String evento) {
        if (eventosRecientes.size() >= MAX_EVENTOS) {
            eventosRecientes.poll(); // Elimina el evento más antiguo
        }
        eventosRecientes.add(String.format("[%s] %s", new Date(), evento));
    }

    @Override
    public void run() {
        while (true) {
            limpiarConsola();
            imprimirEncabezado();
            imprimirEstadoRAM();
            imprimirProcesosEnEjecucion();
            imprimirColaDeEspera();
            imprimirEventosRecientes();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void imprimirEncabezado() {
        System.out.println("+------------------------------------------------------+");
        System.out.println("| SIMULADOR DE GESTIÓN DE PROCESOS EN MEMORIA (Java)   |");
        System.out.println("+------------------------------------------------------+");
    }

    private void imprimirEstadoRAM() {
        int ramTotal = memoria.getRamTotal();
        int ramDisponible = memoria.getRamDisponible();
        int ramUsada = memoria.getRamUsada();
        String barraProgreso = generarBarra(ramUsada, ramTotal);

        String colorRAM = "\u001B[32m"; // Verde
        if (ramUsada > ramTotal * 0.75) {
            colorRAM = "\u001B[31m"; // Rojo
        } else if (ramUsada > ramTotal * 0.5) {
            colorRAM = "\u001B[33m"; // Amarillo
        }

        System.out.printf("| RAM Total: %d MB | RAM Disponible: %d MB\n", ramTotal, ramDisponible);
        System.out.printf("| RAM Usada: %d MB  | %s%s\u001B[0m\n", ramUsada, colorRAM, barraProgreso);
        System.out.println("+------------------------------------------------------+");
    }

    private String generarBarra(int actual, int total) {
        int porcentaje = (int) (((double) actual / total) * 100);
        int bloques = porcentaje / 5;
        StringBuilder sb = new StringBuilder("[");
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
        System.out.printf("| PROCESOS EN EJECUCIÓN (%d)\n", gestor.getProcesosEnEjecucion().size());
        System.out.println("+------------------------------------------------------+");
        if (!gestor.getProcesosEnEjecucion().isEmpty()) {
            gestor.getProcesosEnEjecucion().forEach((pid, proc) -> {
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
        System.out.printf("| COLA DE ESPERA (%d)\n", gestor.getColaEspera().size());
        System.out.println("+------------------------------------------------------+");
        if (!gestor.getColaEspera().isEmpty()) {
            gestor.getColaEspera().forEach(proc -> System.out.printf("|   - %s (PID: %d) -> Memoria requerida: %d MB\n",
                    proc.getNombre(), proc.getPid(), proc.getMemoriaRequerida()));
        } else {
            System.out.println("|   (Ninguno)");
        }
        System.out.println("+------------------------------------------------------+");
    }

    private void imprimirEventosRecientes() {
        System.out.println("| EVENTOS RECIENTES                                    |");
        System.out.println("+------------------------------------------------------+");
        if (!eventosRecientes.isEmpty()) {
            eventosRecientes.forEach(evento -> System.out.printf("| %s\n", evento));
        } else {
            System.out.println("|   (Ninguno)");
        }
        System.out.println("+------------------------------------------------------+");
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