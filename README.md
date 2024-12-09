

# Simulador de Restaurante - Concurrencia en Java con FXGL

Un simulador de restaurante interactivo que utiliza Java FXGL para gráficos y concurrencia, mostrando cómo administrar procesos complejos como asignación de mesas, atención a clientes y gestión de pedidos en tiempo real.

## Descripción

Este proyecto simula un restaurante en el que varios procesos concurrentes son manejados mediante monitores y buffers, aprovechando las capacidades de Java FXGL para la creación de interfaces interactivas y eficientes.  

- **Gestión de Procesos Concurrentes:** Uso de monitores y buffers para sincronizar operaciones como la llegada de clientes, preparación de pedidos y facturación.  
- **Interfaz Gráfica Dinámica:** Java FXGL potencia la representación visual con animaciones y gráficos avanzados.  
- **Optimización del Flujo de Trabajo:** Implementa lógica para evitar bloqueos y mejorar la experiencia del usuario simulando un entorno fluido.

## Insignias

[![Java](https://img.shields.io/badge/Java-21-brightgreen)](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)  
[![FXGL](https://img.shields.io/badge/FXGL-17.3-orange)](https://github.com/AlmasB/FXGL)  
[![Concurrencia](https://img.shields.io/badge/Concurrencia-Java-blue)](https://docs.oracle.com/javase/tutorial/essential/concurrency/)


## Empezando 🚀

Sigue estas instrucciones para configurar y ejecutar el proyecto en tu entorno local.

### Prerrequisitos 📋

- **Java 21**  
- **Apache Maven 3.8+**  
- **FXGL 17.3**  
- **Sistema Operativo:** Windows 10/11 o Ubuntu 20.04+  

### Instalación 🔧

Clona el repositorio e instala las dependencias:  

```bash
git clone https://github.com/tuusuario/proyectoC3.git
cd proyectoC3
mvn install
```

## Ejecutando las Pruebas ⚙️

Ejecuta las pruebas unitarias y de integración:  

```bash
mvn test
```

### Pruebas de Concurrencia 🔩

Estas pruebas verifican la correcta sincronización de procesos:  
- Llegada de clientes sin bloqueo.  
- Liberación adecuada de mesas y recursos.  

```bash
# Ejemplo de comando
java -jar target/demo-1.0-SNAPSHOT.jar
```


### **Implementación de Monitores y Buffers **  

En este proyecto, los **monitores** y **buffers** se implementaron para gestionar la concurrencia, asegurando que los recursos compartidos, como las mesas del restaurante y los pedidos, se manejen de manera eficiente y sin conflictos. A continuación, explicamos cómo funcionan y cómo se implementaron, acompañados de fragmentos de código relevantes.  

---

## **Monitor de Clientes**  

El monitor de clientes controla el acceso a las mesas del restaurante, asegurando que cada cliente obtenga una mesa disponible y evitando condiciones de carrera.  

### **Código de Implementación**  

```java
public class ClienteMonitor {
    private final int totalMesas;
    private int mesasDisponibles;

    public ClienteMonitor(int totalMesas) {
        this.totalMesas = totalMesas;
        this.mesasDisponibles = totalMesas;
    }

    public synchronized void entrarRestaurante(String clienteID) throws InterruptedException {
        while (mesasDisponibles == 0) {
            System.out.println(clienteID + " espera una mesa.");
            wait(); // El cliente espera si no hay mesas disponibles.
        }
        mesasDisponibles--;
        System.out.println(clienteID + " obtuvo una mesa. Mesas disponibles: " + mesasDisponibles);
    }

    public synchronized void salirRestaurante(String clienteID) {
        mesasDisponibles++;
        System.out.println(clienteID + " dejó la mesa. Mesas disponibles: " + mesasDisponibles);
        notify(); // Notifica a los clientes en espera que hay una mesa libre.
    }
}
```

### **Funcionamiento**  

1. **Entrada al Restaurante:**  
   - Si no hay mesas disponibles, el cliente queda en espera mediante `wait()`.  
   - Cuando una mesa está libre, el cliente toma la mesa y el contador de mesas disponibles se reduce.  

2. **Salida del Restaurante:**  
   - Cuando un cliente sale, el contador de mesas disponibles aumenta y se notifica a otros clientes en espera.  

---

## **Monitor de Pedidos**  

Este monitor sincroniza la preparación y entrega de pedidos, asegurando que los chefs procesen pedidos en orden y que el buffer no se sobrecargue.  

### **Código de Implementación**  

```java
public class PedidoMonitor {
    private final Queue<String> bufferPedidos = new LinkedList<>();
    private final int capacidadMaxima;

    public PedidoMonitor(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public synchronized void agregarPedido(String pedido) throws InterruptedException {
        while (bufferPedidos.size() == capacidadMaxima) {
            System.out.println("Buffer lleno. Esperando para agregar: " + pedido);
            wait(); // Espera si el buffer está lleno.
        }
        bufferPedidos.add(pedido);
        System.out.println("Pedido agregado: " + pedido);
        notifyAll(); // Notifica a los procesos esperando para procesar pedidos.
    }

    public synchronized String procesarPedido() throws InterruptedException {
        while (bufferPedidos.isEmpty()) {
            System.out.println("No hay pedidos para procesar. Chef en espera.");
            wait(); // Espera si el buffer está vacío.
        }
        String pedido = bufferPedidos.poll();
        System.out.println("Pedido procesado: " + pedido);
        notifyAll(); // Notifica a los procesos esperando para agregar pedidos.
        return pedido;
    }
}
```

### **Funcionamiento**  

1. **Agregar Pedido:**  
   - Si el buffer está lleno, el hilo que intenta agregar un pedido queda en espera mediante `wait()`.  
   - Cuando hay espacio, se agrega el pedido y se notifica a otros hilos en espera.  

2. **Procesar Pedido:**  
   - Si el buffer está vacío, el hilo que intenta procesar un pedido queda en espera mediante `wait()`.  
   - Cuando hay pedidos en el buffer, uno se elimina y se procesa, notificando a los hilos en espera para agregar más pedidos.  

---

## **Buffers**  

### **Buffer de Pedidos**  
El buffer de pedidos es una cola limitada que almacena temporalmente los pedidos antes de ser procesados. Implementa una **cola FIFO (First In, First Out)** para garantizar que los pedidos se procesen en el orden en que se reciben.  

### **Buffer de Clientes**  
En este caso, el monitor se asegura de que la asignación de mesas se maneje como un recurso crítico compartido. Aunque no se utiliza un buffer explícito, el mecanismo de espera y notificación del monitor garantiza que los clientes sean atendidos en orden.  

---

## **Sincronización en la Práctica**  

Aquí un ejemplo de cómo se utilizan los monitores en el flujo principal del simulador:  

### **Simulación Principal**  

```java
public class RestauranteSimulador {
    public static void main(String[] args) {
        ClienteMonitor clienteMonitor = new ClienteMonitor(5); // 5 mesas disponibles
        PedidoMonitor pedidoMonitor = new PedidoMonitor(10);  // Buffer con capacidad para 10 pedidos

        // Simula clientes que entran y hacen pedidos
        Runnable clienteRunnable = () -> {
            try {
                String clienteID = "Cliente-" + Thread.currentThread().getId();
                clienteMonitor.entrarRestaurante(clienteID);
                pedidoMonitor.agregarPedido(clienteID + ": Pedido");
                Thread.sleep(2000); // Simula el tiempo que el cliente pasa comiendo
                clienteMonitor.salirRestaurante(clienteID);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // Simula chefs procesando pedidos
        Runnable chefRunnable = () -> {
            try {
                while (true) {
                    String pedido = pedidoMonitor.procesarPedido();
                    Thread.sleep(3000); // Simula el tiempo para procesar un pedido
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // Crear hilos de clientes y chefs
        for (int i = 0; i < 10; i++) {
            new Thread(clienteRunnable).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(chefRunnable).start();
        }
    }
}
```

---

## **Beneficios de la Implementación**  

1. **Exclusión Mutua:** Los monitores garantizan que solo un hilo acceda al recurso crítico a la vez.  
2. **Bloqueo y Notificación:** Los métodos `wait()` y `notify()` aseguran que los hilos no desperdicien recursos mientras esperan.  
3. **Sincronización Ordenada:** Los buffers y monitores trabajan juntos para mantener el flujo de operaciones en orden y evitar sobrecargas.  


---

### **Funciones Clave de Java FXGL **

#### **1. Inicialización de la Aplicación con `GameApplication`**
La clase `GameApplication` es el punto de partida para cualquier proyecto basado en FXGL. Permite configurar la lógica central del juego y personalizar elementos esenciales como el tamaño de la ventana, el título y los ajustes iniciales.

- **Implementación en el proyecto:**  
  ```java
  @Override
  protected void initSettings(GameSettings settings) {
      settings.setWidth(800);
      settings.setHeight(600);
      settings.setTitle("Simulador de Restaurante");
      settings.setVersion("1.0");
  }
  ```
  Aquí, configuramos la ventana del simulador con dimensiones específicas, un título descriptivo y la versión del proyecto. Esta función permite personalizar rápidamente la experiencia visual del usuario.

---

#### **2. Creación y Gestión de Entidades con `Entity`**
Las entidades en FXGL representan los componentes principales del juego, como clientes, mesas y personal del restaurante. Utilizamos un enfoque basado en componentes para diseñar estas entidades.

- **Implementación en el proyecto:**  
  ```java
  Entity customer = entityBuilder()
      .at(50, 100)
      .view("customer.png")
      .with(new CustomerComponent())
      .buildAndAttach();
  ```
  Este código crea un cliente visual con una posición inicial específica y un componente personalizado `CustomerComponent` que maneja su lógica.
  Este ejemplo muestra cómo usar el patrón de diseño *Builder* para construir entidades dinámicamente y asociarlas con componentes que encapsulan su comportamiento.

---

#### **3. Manejo de Eventos con `EventBus`**
FXGL tiene un potente sistema de eventos que facilita la comunicación entre diferentes partes de la aplicación.

- **Implementación en el proyecto:**  
  ```java
  FXGL.getEventBus().addEventHandler(OrderEvent.ORDER_COMPLETED, event -> {
      log("Pedido completado: " + event.getOrderID());
  });
  ```
  Aquí, el sistema escucha eventos de tipo `ORDER_COMPLETED` para registrar pedidos completados en el log. Esto es esencial en aplicaciones concurrentes para sincronizar acciones entre múltiples procesos. 
  Los desarrolladores pueden utilizar `EventBus` para implementar sistemas reactivos, donde las acciones dependen de eventos específicos, mejorando la modularidad y escalabilidad.

---

#### **4. Interfaz de Usuario Dinámica con `UIFactoryService`**
La librería FXGL incluye herramientas para crear elementos visuales personalizados, como paneles de información, menús y botones.

- **Implementación en el proyecto:**  
  ```java
  var text = FXGL.getUIFactoryService().newText("Bienvenido al Restaurante", Color.BLACK, 18);
  addUINode(text, 100, 50);
  ```
  Este fragmento genera un texto estilizado y lo posiciona en la interfaz gráfica. Es ideal para mostrar información relevante al usuario, como mensajes de bienvenida o instrucciones.

---

#### **5. Animaciones y Efectos con `AnimationBuilder`**
FXGL incluye soporte para crear animaciones fluidas y visualmente atractivas, lo que mejora la experiencia del usuario.

- **Implementación en el proyecto:**  
  ```java
  FXGL.animationBuilder()
      .duration(Duration.seconds(2))
      .translate(customer)
      .from(new Point2D(50, 100))
      .to(new Point2D(400, 100))
      .buildAndPlay();
  ```
  Este código anima un cliente moviéndolo desde un punto inicial hasta su posición final en la mesa. Las animaciones permiten dar vida al simulador, haciendo que las interacciones sean más intuitivas y dinámicas.
---



## Construido Con 🛠️

- **[Java FXGL](https://github.com/AlmasB/FXGL):** Framework para crear aplicaciones gráficas dinámicas.  
- **Monitores:** Gestión de concurrencia con exclusión mutua y sincronización.  
- **Buffers:** Almacenamiento temporal para la interacción de procesos.

## Autores ✒️

- **Isaac Toledo Castillo** - _Creador del Proyecto_ - [Isaac Toledo](https://github.com/IsaacToledo123)
- **Jesus Hernandez Villatoro** - _Creador del Proyecto_ - [Jesus Hernandez](https://github.com/tuusuario)  
- 

## Licencia 📄

Este proyecto está bajo la Licencia MIT - consulta el archivo [LICENSE.md](LICENSE.md) para más detalles.

## Expresiones de Gratitud 🎁

- A la comunidad de FXGL por su framework poderoso.  
- A los entusiastas de la programación concurrente.  

--- 
