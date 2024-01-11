# Sistema de Adquisición de Datos Concurrente

El proyecto aborda un sistema de adquisición de datos concurrente que incluye dos buffers y tres tipos de actores: Creadores de Datos, Revisores de Datos y Consumidores de Datos. El ciclo de operación implica la creación, revisión y consumo de datos.

## Estructura del Código

### Clases Principales:

- **Data**: Representa la estructura de datos utilizada en el programa.
- **Buffer**: Clase abstracta que modela un espacio de almacenamiento de datos.
- **InitBuffer**: Extiende Buffer y representa el almacenamiento inicial de datos.
- **ValidBuffer**: Extiende Buffer y almacena datos validados.
- **Creator**: Implementa la interfaz Runnable y se encarga de crear datos.
- **Reviewer**: Implementa la interfaz Runnable y revisa datos almacenados.
- **Consumer**: Implementa la interfaz Runnable y consume datos validados.
- **Log**: Extiende Thread y registra estadísticas durante la ejecución.

### Funcionamiento del Código

1. **Inicialización**: Se crean instancias de las clases y se inician los hilos correspondientes a los actores (creadores, revisores, consumidores) y al log.

2. **Creación de Datos**: Los creadores generan datos y los depositan en el Buffer Inicial (`InitBuffer`).

3. **Revisión de Datos**: Los revisores verifican los datos en el Buffer Inicial. Cuando todos los revisores han revisado un dato, el último revisor lo copia al Buffer de Validados (`ValidBuffer`).

4. **Consumo de Datos**: Los consumidores retiran datos del Buffer de Validados y eliminan la copia en el Buffer Inicial.

5. **Registro y Estadísticas**: El log registra estadísticas detalladas, incluyendo el tiempo de ejecución, la cantidad de datos en cada buffer y las acciones de cada actor.

## Diagrama de Clases

La estructura del programa se representa en el siguiente diagrama de clases:

<p align="center">
  <img src="img/Diagrama%20de%20Clases.jpg" alt="Diagrama de Clases">
</p>

## Diagrama de Secuencia

El proceso de creación, revisión y consumo de un dato se ilustra en el siguiente diagrama de secuencia:

<p align="center">
  <img src="img/Diagrama%20de%20Secuencia.jpg" alt="Diagrama de Secuencia">
</p>