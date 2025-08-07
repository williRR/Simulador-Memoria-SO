package simulador_memoria;

import java.util.concurrent.atomic.AtomicInteger;

public class Memoria {
    private static final int RAM_TOTAL = 1024;
    private final AtomicInteger ramDisponible;

    public Memoria() {
        this.ramDisponible = new AtomicInteger(RAM_TOTAL);
    }

    public int getRamTotal() {
        return RAM_TOTAL;
    }

    public int getRamDisponible() {
        return ramDisponible.get();
    }

    public int getRamUsada() {
        return RAM_TOTAL - ramDisponible.get();
    }

    public synchronized boolean asignarMemoria(int cantidad) {
        if (ramDisponible.get() >= cantidad) {
            ramDisponible.addAndGet(-cantidad);
            return true;
        }
        return false;
    }

    public synchronized void liberarMemoria(int cantidad) {
        ramDisponible.addAndGet(cantidad);
    }
}

