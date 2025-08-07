package simuladormemoria;

import java.util.concurrent.atomic.AtomicInteger;

public class Proceso {
    private static final AtomicInteger pidCounter = new AtomicInteger(1000);

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

    // Getters y Setters
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