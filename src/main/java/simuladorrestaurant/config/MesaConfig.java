package simuladorrestaurant.config;

public class MesaConfig {

    // Lógica para verificar si una celda es una mesa
    public static boolean isMesa(int row, int col) {
        return (row >= 14 && row <= 15) && (col == 8) ||
                (row >= 14 && row <= 15) && (col >= 11 && col <= 12) ||
                (row >= 14 && row <= 15) && (col >= 18 && col <= 19) ||
                ((row >= 18 && row <= 19) && (col >= 28 && col <= 29)) ||
                (row >= 20 && row <= 23) && (col >= 13 && col <= 15) ||
                (row >= 20 && row <= 23) && (col >= 20 && col <= 22) ||
                ((row >= 25 && row <= 26) && (col >= 28 && col <= 29)) ||
                ((row >= 32 && row <= 33) && (col >= 28 && col <= 29)) ||
                (row >= 22 && row <= 26) && (col >= 4 && col <= 5) ||
                (row >= 29 && row <= 33) && (col >= 4 && col <= 5) ||
                (row >= 29 && row <= 33) && (col >= 14 && col <= 17);
    }

    // Calcular la posición X de una mesa en la pantalla
    public static double calcularX(int col, double cellWidth) {
        return col * cellWidth;
    }

    // Calcular la posición Y de una mesa en la pantalla
    public static double calcularY(int row, double cellHeight) {
        return row * cellHeight;
    }
}
