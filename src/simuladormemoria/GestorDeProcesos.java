package simuladormemoria;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GestorDeProcesos implements Runnable {
    private final Memoria memoria;
    private final Monitor monitor;
    private final BlockingQueue<Proceso> colaEspera;
    private final ConcurrentMap<Integer, Proceso> procesosEnEjecucion;

    public GestorDeProcesos(Memoria memoria, Monitor monitor) {
        this.memoria = memoria;
        this.monitor = monitor;
        this.colaEspera = new LinkedBlockingQueue<>();
        this.procesosEnEjecucion = new ConcurrentHashMap<>();
    }

    public void agregarProceso(Proceso proceso) throws InterruptedException {
        if (memoria.asignarMemoria(proceso.getMemoriaRequerida())) {
            proceso.setStatus("En ejecución");
            procesosEnEjecucion.put(proceso.getPid(), proceso);
            new Thread(new ProcesoExecutor(proceso, memoria, procesosEnEjecucion, monitor)).start();
            monitor.agregarEvento("Proceso " + proceso.getNombre() + " (PID: " + proceso.getPid() + ") iniciado.");
        } else {
            colaEspera.put(proceso);
            monitor.agregarEvento("No hay memoria suficiente. Proceso " + proceso.getNombre() + " (PID: " + proceso.getPid() + ") enviado a la cola.");
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
                Proceso procesoCandidato = colaEspera.take();
                monitor.agregarEvento("Proceso " + procesoCandidato.getNombre() + " (PID: " + procesoCandidato.getPid() + ") movido a ejecución.");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}