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
Ir a la opción "Git" y seleccionar "Commit".




<img width="425" height="209" alt="image" src="https://github.com/user-attachments/assets/6e2b6e27-028f-41b4-8e79-2eaaafe04838" />
 
parecerá una ventana donde se puede escribir un mensaje descriptivo sobre los cambios realizados (por ejemplo: "Agrega saludo inicial y bucle simple").





<img width="402" height="220" alt="image" src="https://github.com/user-attachments/assets/fc4a41cb-8a7f-47e3-9fd1-4c8fc3356687" />
 
Finalmente, se puede elegir:
•	"Commit" para guardar los cambios localmente.
•	"Commit and Push..." si se desea subir los cambios al repositorio remoto.




