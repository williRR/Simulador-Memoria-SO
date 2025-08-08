# Simulador de Memoria - Sistemas Operativos

Este proyecto es un **simulador de gestión de memoria RAM**, desarrollado en Java, que emula cómo un sistema operativo maneja los procesos y su asignación de memoria. Fue creado con fines académicos para practicar conceptos de administración de memoria en sistemas operativos, como asignación, concurrencia y control de recursos compartidos.

##  Descripción

El simulador implementa una memoria RAM total de 1024 MB (1 GB), donde se controlan procesos que consumen memoria de forma concurrente. Utiliza estructuras seguras para múltiples hilos como `AtomicInteger` y `ConcurrentHashMap` para simular un entorno multitarea realista.

Los objetivos clave del simulador son:
- Simular la asignación y liberación de memoria.
- Controlar múltiples procesos que solicitan RAM.
- Utilizar estructuras de sincronización para simular entornos concurrentes.


## Lenguaje de programación utilizado

- **Java** (versión 8 o superior)

## Librerías o frameworks empleados

Este proyecto **no utiliza frameworks externos**. Sin embargo, hace uso de librerías estándar del JDK, incluyendo:

- `java.util.concurrent`
  - `AtomicInteger`
  - `ConcurrentHashMap`
  - `ExecutorService`
  - `ThreadPoolExecutor`
- `java.util`
  - Colecciones y estructuras básicas

Estas clases permiten trabajar con concurrencia y gestión de múltiples hilos de forma segura y eficiente.

## Cómo ejecutar

### Requisitos:
- Java JDK 8 o superior

### Pasos:
1. 
Instalar IntelliJ IDEA, el entorno de desarrollo que utilizaremos para simular el programa.







 <img width="442" height="163" alt="image" src="https://github.com/user-attachments/assets/2fec4353-bfc9-485e-a51a-1625c2fa0747" />

### Pasos: 
2.
Abrir IntelliJ IDEA.





<img width="283" height="159" alt="image" src="https://github.com/user-attachments/assets/66a0b81b-0e0c-40c6-a358-6a5919a21d60" />
 


### Pasos:
3.
Seleccionar "Nuevo Proyecto". En el campo "Location", elegir la carpeta donde se encuentra el código fuente del programa. Si se desea clonar desde un repositorio, se puede utilizar el siguiente comando de Git:
git clone https://github.com/tuusuario/Simulador-Memoria-SO.git 
Luego, hacer clic en la opción "Crear".





<img width="277" height="151" alt="image" src="https://github.com/user-attachments/assets/1e6d9f54-9f1c-43d3-9091-5add73375abf" />
 

### Pasos:
4.
Seleccionar el proyecto "Simulador-Memoria-SO" para simularlo.





<img width="401" height="167" alt="image" src="https://github.com/user-attachments/assets/551eb0a5-d99b-4a59-acb1-83bfd8b01c6e" /> 


El programa se abrirá en el entorno de desarrollo.




<img width="354" height="210" alt="image" src="https://github.com/user-attachments/assets/15f735ed-35d3-4c1b-91a3-8df39686959d" />






 
### Pasos:
5.
Descripción de la Simulación del Programa
Al ejecutar el programa, se simula el monitoreo del uso de la memoria RAM del sistema en tiempo real. En la ventana principal se muestra:
Memoria total, usada y libre, expresada en megabytes (MB).




<img width="425" height="226" alt="image" src="https://github.com/user-attachments/assets/224f0273-eac6-4cd6-b337-75d97c5b7ee6" />




•	Un gráfico de barras que cambia dinámicamente según el porcentaje de uso.



<img width="425" height="226" alt="image" src="https://github.com/user-attachments/assets/b63944a0-03d2-42cc-b635-904d6f01a00b" />





•	El valor del uso actual de RAM se actualiza continuamente, permitiendo observar cómo varía al ejecutarse diferentes procesos.



<img width="425" height="226" alt="image" src="https://github.com/user-attachments/assets/3b7fd8ee-baab-47b6-a38d-661b2724efb0" />



Este programa permite visualizar de forma clara y sencilla el comportamiento del consumo de memoria en el sistema.


