package simulador_memoria;

import java.util.concurrent.ConcurrentMap;
import java.util.Date;

public class ProcesoExecutor implements Runnable {
    private final Proceso proceso;
    private final Memoria memoria;
    private final ConcurrentMap<Integer, Proceso> procesosEnEjecucion;

    public ProcesoExecutor(Proceso proceso, Memoria memoria, ConcurrentMap<Integer, Proceso> procesosEnEjecucion) {
        this.proceso = proceso;
        this.memoria = memoria;
        this.procesosEnEjecucion = procesosEnEjecucion;
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
            System.out.printf("[%s] <--- Finalizado y liberando memoria del proceso: %s (PID: %d)\n",
                    new Date(), proceso.getNombre(), proceso.getPid());
        }
    }
}