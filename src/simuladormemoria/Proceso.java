package simuladormemoria;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * La clase Proceso representa una tarea o programa en la simulación.
 * Contiene información clave como su identificador, memoria y duración.
 */
public class Proceso {
    // Un contador estático y atómico para generar PIDs únicos y seguros en entornos concurrentes.
    private static final AtomicInteger pidCounter = new AtomicInteger(1000);

    // Identificador único del proceso.
    private final int pid;
    // Nombre descriptivo del proceso.
    private final String nombre;
    // Cantidad de memoria RAM que el proceso necesita.
    private final int memoriaRequerida;
    // Duración de la ejecución del proceso en segundos.
    private final int duracion;
    // Momento en milisegundos en que el proceso comenzó a ejecutarse.
    private long tiempoInicio;
    // Estado actual del proceso (ej. "En cola", "En ejecución", "Finalizado").
    private String status;

    /**
     * Constructor para crear un nuevo proceso.
     * @param nombre El nombre del proceso.
     * @param memoriaRequerida La memoria que el proceso necesita en MB.
     * @param duracion La duración del proceso en segundos.
     */
    public Proceso(String nombre, int memoriaRequerida, int duracion) {
        // Asigna un PID único y lo incrementa para el siguiente proceso.
        this.pid = pidCounter.getAndIncrement();
        this.nombre = nombre;
        this.memoriaRequerida = memoriaRequerida;
        this.duracion = duracion;
        // El estado inicial de todo proceso es "En cola".
        this.status = "En cola";
    }

    // Getters y Setters
    // Métodos para acceder y modificar las propiedades del proceso.
    public int getPid() { return pid; }
    public String getNombre() { return nombre; }
    public int getMemoriaRequerida() { return memoriaRequerida; }
    public int getDuracion() { return duracion; }
    public long getTiempoInicio() { return tiempoInicio; }
    public void setTiempoInicio(long tiempoInicio) { this.tiempoInicio = tiempoInicio; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Sobrescribe el método toString para una representación en cadena más útil.
     * @return Una cadena formateada con los detalles del proceso.
     */
    @Override
    public String toString() {
        return String.format("PID: %d | Nombre: %s | Memoria: %d MB | Duración: %ds",
                pid, nombre, memoriaRequerida, duracion);
    }
}