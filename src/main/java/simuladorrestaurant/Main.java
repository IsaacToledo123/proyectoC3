package simuladorrestaurant;

import simuladorrestaurant.logica.Restaurant;
import simuladorrestaurant.ui.RestaurantUI;
import simuladorrestaurant.concurrencia.MonitorMesas;

public class Main {

    private static int mesasDisponibles = 10;

    public static void main(String[] args) {
        boolean usarInterfaz = true; // Cambiar según la necesidad

        if (usarInterfaz) {
            // Crear una instancia de MonitorMesas
            MonitorMesas monitorMesas = new MonitorMesas(mesasDisponibles);

            // Iniciar la interfaz gráfica de FXGL pasándole MonitorMesas
            RestaurantUI restaurantUI = new RestaurantUI(monitorMesas); // Pasa monitorMesas al constructor
            restaurantUI.main(args); // Asegúrate de que main(args) esté correctamente implementado en RestaurantUI
        } else {
            System.out.println("Ejecutando lógica del restaurante...");
            // Crear una instancia de MonitorMesas
            MonitorMesas monitorMesas = new MonitorMesas(mesasDisponibles);

            // Aquí puedes iniciar la lógica del restaurante directamente, pasando los 4 parámetros
            Restaurant restaurant = new Restaurant(3, 2, monitorMesas); // Asegúrate de pasar todos los parámetros
            restaurant.iniciarSimulacion();
            restaurant.llegarComensales();
        }
    }
}
