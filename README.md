# Call Center

En el siguiente proyecto se muestra una implementación de un Call Center. Tiene como objetivo asignar las llamadas telefónicas provenientes de clientes
a los distintos tipos de empleados. Para asignar estas llamadas, se utiliza el siguiente orden de asignación:
   1. Operador
   2. Supervisor
   3. Director
   
## Diseño
Para la implementación se utilizaron Threads, tanto para cada uno de los empleados, como para los clientes y el mismo dispatcher encargado de asignar las llamadas. Como recurso para manejar la 
concurrencia se utilizan dos semáforos: uno es un mutex para garantizar que no habrá más de un proceso modificando ningún objeto o recurso. El segundo semáforo se utiliza
para manejar la cantidad de llamadas concurrentes.
Se utilizan 3 listas de empleados: una para Operadores, otra para Supervisores y otra para Directores. Esto es útil a la hora de asignar las llamadas, ya que facilita manejar las prioridades.
También fue necesario tener una lista con las llamadas entrantes, guardando los ids de los clientes creados. Este tipo de lista utilizada, garantiza el orden de inserción de datos. 
Esta fue una solución pensada para resolver uno de los puntos extras del ejercicio.

### Employee
Es una interfaz cuyos dos métodos a implementar son takeCall y endCall.

### Operador, Director, Supervisor
Implementan la interfaz Employee. Cada clase es un Thread, el cual se despierta cuando hay una llamada para asignarle. Toma la llamada, espera un tiempo random entre 5 y 10 segundos, 
y finaliza la llamada. Tiene la responsabilidad de liberar el recurso que le fue asignado al tomar la llamada. En otras palabras, hace un release del semáforo.

### Dispatcher
Es un Thread encargado de asignar las llamadas telefónicas entrantes a cada tipo de empleado, según el criterio explicado anteriormente. Este maneja un mutex para bloquear la modificación
y asignación de llamadas.

### Client
Es un Thread que representa el cliente que intenta llamar al call center. Este proceso tiene un loop infinito que se "anota" para llamar, y espera un tiempo, con el fin de simular 
otra llamada.


### CallCenter
Esta clase es la que crea el Dispatcher, crea los empleados disponibles para tomar llamados y crea un set de clientes que van a generar un flujo de llamados continuo.
De las estructuras que maneja este objeto, encontramos un semáforo para administrar la cantidad de llamadas concurrentes y una lista de llamadas entrantes que están pendientes para se atendidas.


## Extra/Plus
* Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre.
Se utiliza una lista (calls) de llamadas entrantes, con la garantía de ser FIFO, para mantener el orden de llegada. Cuando no hay empleados libres, la llamada entrante se mantiene en la lista.
Esto permite que la siguiente llamada a ser tomada, sea la primera que está en la lista.

* Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes.
Para esta solución se utiliza un semáforo con una cantidad de recursos seteados al instanciar el call center. Cuando hay una llamada entrante y están todos los recursos tomados, es decir
que el semáforo no tiene permisos para asignar, el proceso intenta hacer un acquire del recurso y se queda esperando hasta que uno sea liberado.

## UML

![alt text](https://github.com/mriverov/call-center/raw/master/uml/call_center_uml.jpg)