

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

## Visuales

Capturas de pantalla o GIFs mostrando:  
- Clientes entrando al restaurante.  
- Preparación y entrega de pedidos.  
- Representación gráfica de la concurrencia en acción.  

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
cd simulador-restaurante
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

## Construido Con 🛠️

- **[Java FXGL](https://github.com/AlmasB/FXGL):** Framework para crear aplicaciones gráficas dinámicas.  
- **Monitores:** Gestión de concurrencia con exclusión mutua y sincronización.  
- **Buffers:** Almacenamiento temporal para la interacción de procesos.

## Autores ✒️

- **Isaac Toledo Castillo** - _Creador del Proyecto_ - [Isaac Toledo](https://github.com/tuusuario)  

## Licencia 📄

Este proyecto está bajo la Licencia MIT - consulta el archivo [LICENSE.md](LICENSE.md) para más detalles.

## Expresiones de Gratitud 🎁

- A la comunidad de FXGL por su framework poderoso.  
- A los entusiastas de la programación concurrente.  

--- 
