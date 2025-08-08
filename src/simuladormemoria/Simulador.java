package simuladormemoria;

public class Simulador {
    /**
     * El método main es el punto de entrada de la aplicación.
     * Orquesta la creación de los componentes principales de la simulación
     * y los pone en marcha.
     * @param args Argumentos de la línea de comandos (no utilizados).
     * @throws InterruptedException Si el hilo principal es interrumpido durante la espera.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Iniciando simulador...");

        // 1. Creación de los componentes principales del simulador.
        // Se crea un objeto Memoria que gestionará los recursos de RAM.
        Memoria memoria = new Memoria();
        // Se crea un Monitor, que es la interfaz visual de la simulación,
        // pasándole la referencia a la memoria para que pueda mostrar su estado.
        Monitor monitor = new Monitor(memoria);
        // Se crea el GestorDeProcesos, que es el "sistema operativo" de la simulación.
        // Se le pasan la memoria y el monitor para que pueda interactuar con ellos.
        GestorDeProcesos gestor = new GestorDeProcesos(memoria, monitor);

        // 2. Establecer la conexión entre el Monitor y el Gestor.
        // El monitor necesita saber del gestor para mostrar la lista de procesos.
        monitor.setGestor(gestor);

        // 3. Iniciar los hilos principales de la simulación.
        // El gestor de procesos se ejecuta en un hilo para gestionar la cola de espera.
        new Thread(gestor).start();
        // El monitor se ejecuta en otro hilo para refrescar la pantalla continuamente.
        new Thread(monitor).start();

        // 4. Pausa inicial para que la interfaz se cargue.
        Thread.sleep(2000);

        // 5. Creación y adición de procesos de ejemplo.
        // Se crean varios procesos y se agregan al gestor.
        // El gestor decidirá si ejecutarlos de inmediato o enviarlos a la cola de espera.
        gestor.agregarProceso(new Proceso("Navegador Web", 350, 10)); // Memoria: 350MB, Duración: 10s
        gestor.agregarProceso(new Proceso("Editor de Código", 250, 15)); // Memoria: 250MB, Duración: 15s

        // Pausa para simular la llegada de nuevos procesos en momentos diferentes.
        Thread.sleep(1000);

        gestor.agregarProceso(new Proceso("Juego", 700, 20)); // Este proceso probablemente irá a la cola por falta de memoria.

        Thread.sleep(1000);

        gestor.agregarProceso(new Proceso("Reproductor de Música", 100, 5)); // Este podría ejecutarse o esperar, dependiendo del estado de la RAM.
    }
}