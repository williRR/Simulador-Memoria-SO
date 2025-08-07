package simuladormemoria;

import java.util.concurrent.ConcurrentMap;
import java.util.Date;

public class ProcesoExecutor implements Runnable {
    private final Proceso proceso;
    private final Memoria memoria;
    private final ConcurrentMap<Integer, Proceso> procesosEnEjecucion;
    private final Monitor monitor;

    public ProcesoExecutor(Proceso proceso, Memoria memoria, ConcurrentMap<Integer, Proceso> procesosEnEjecucion, Monitor monitor) {
        this.proceso = proceso;
        this.memoria = memoria;
        this.procesosEnEjecucion = procesosEnEjecucion;
        this.monitor = monitor;
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
            memoria.liberarMemoria(proceso.getMemoriaRequerida());
            procesosEnEjecucion.remove(proceso.getPid());
            monitor.agregarEvento("Proceso " + proceso.getNombre() + " (PID: " + proceso.getPid() + ") finalizado y memoria liberada.");
        }
    }
}