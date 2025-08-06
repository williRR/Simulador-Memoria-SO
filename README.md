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
1. Clona el repositorio:
   ```bash
   git clone https://github.com/tuusuario/Simulador-Memoria-SO.git




