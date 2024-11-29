package simuladorrestaurant;

import simuladorrestaurant.logica.Restaurant;
import simuladorrestaurant.ui.RestaurantUI;

public class Main {
    public static void main(String[] args) {
        boolean usarInterfaz = true; // Cambiar según la necesidad

        if (usarInterfaz) {
            // Iniciar la interfaz gráfica de FXGL
            RestaurantUI.main(args); // Asegúrate de tener un método startGame() en RestaurantUI
        } else {
            System.out.println("Ejecutando lógica del restaurante...");
            // Aquí puedes iniciar la lógica del restaurante directamente
            Restaurant restaurant = new Restaurant(3, 2, 5); // Ejemplo de parámetros
            restaurant.iniciarSimulacion();
            restaurant.llegarComensales();
        }
    }
}
