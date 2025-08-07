/*
package simulador_memoria;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Iniciando simulador...");

        Memoria memoria = new Memoria();
        GestorDeProcesos gestor = new GestorDeProcesos(memoria);
        Monitor monitor = new Monitor(gestor, memoria);

        // Iniciar los hilos principales
        new Thread(gestor).start();
        new Thread(monitor).start();

        Thread.sleep(2000);

        // Ejemplo de creación de procesos
        gestor.agregarProceso(new Proceso("Navegador Web", 350, 10));
        gestor.agregarProceso(new Proceso("Editor de Código", 250, 15));
        Thread.sleep(1000);
        gestor.agregarProceso(new Proceso("Juego", 700, 20));
        Thread.sleep(1000);
        gestor.agregarProceso(new Proceso("Reproductor de Música", 100, 5));
    }
}
*/