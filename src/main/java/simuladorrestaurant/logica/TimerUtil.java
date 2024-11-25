package simuladorrestaurant.logica;

public class TimerUtil {
    // Método para simular tiempos de espera
    public static void esperar(int tiempoMs) {
        try {
            Thread.sleep(tiempoMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para generar un tiempo aleatorio
    public static int tiempoAleatorio(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }
}
