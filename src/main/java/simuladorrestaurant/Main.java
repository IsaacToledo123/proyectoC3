package simuladorrestaurant;

import simuladorrestaurant.ui.RestaurantUI;

public class Main {
    public static void main(String[] args) {
        boolean usarInterfaz = true; // Cambiar según la necesidad

        if (usarInterfaz) {
            RestaurantUI.main(args); // Llama a la interfaz gráfica
        } else {
            System.out.println("Ejecutando lógica del restaurante...");
            // Aquí puedes iniciar el Restaurant.java de forma directa
        }
    }
}
