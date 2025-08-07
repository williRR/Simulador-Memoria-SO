package simulador_memoria;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Date;

public class GestorDeProcesos implements Runnable {
    private final Memoria memoria;
    private final BlockingQueue<Proceso> colaEspera;
    private final ConcurrentMap<Integer, Proceso> procesosEnEjecucion;

    public GestorDeProcesos(Memoria memoria) {
        this.memoria = memoria;
        this.colaEspera = new LinkedBlockingQueue<>();
        this.procesosEnEjecucion = new ConcurrentHashMap<>();
    }

    public void agregarProceso(Proceso proceso) throws InterruptedException {
        if (memoria.asignarMemoria(proceso.getMemoriaRequerida())) {
            proceso.setStatus("En ejecución");
            procesosEnEjecucion.put(proceso.getPid(), proceso);
            new Thread(new ProcesoExecutor(proceso, memoria, procesosEnEjecucion)).start();
        } else {
            colaEspera.put(proceso);
            System.out.printf("[%s] !!! No hay memoria suficiente. Proceso %s (PID: %d) enviado a la cola.\n",
                    new Date(), proceso.getNombre(), proceso.getPid());
        }
    }

    public ConcurrentMap<Integer, Proceso> getProcesosEnEjecucion() {
        return procesosEnEjecucion;
    }

    public BlockingQueue<Proceso> getColaEspera() {
        return colaEspera;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Proceso procesoCandidato = colaEspera.peek();
                if (procesoCandidato != null && memoria.asignarMemoria(procesoCandidato.getMemoriaRequerida())) {
                    procesoCandidato = colaEspera.take(); // Quitar de la cola
                    procesoCandidato.setStatus("En ejecución");
                    procesosEnEjecucion.put(procesoCandidato.getPid(), procesoCandidato);
                    new Thread(new ProcesoExecutor(procesoCandidato, memoria, procesosEnEjecucion)).start();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}