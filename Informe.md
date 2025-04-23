# Informe Proyecto 1 - Algoritmos de Planificacion de Procesos

---
## Integrantes
- Agustín Argüello
- Andrés Chyrnia

---
## Descripción
El Proyecto N° 1 consiste en la creación de un programa escrito en el 
lenguaje Java, que simula el trabajo de un despachador de procesos.
El programa recibe una lista de procesos (en un .csv), con la cual produce un 
nuevo .csv que contiene diagramas de Gantt trazando la ejecución de cada 
proceso.
Cuenta con una interfaz gráfica que permite la previsualización de la lista 
de procesos, y la selección de los algoritmos a ser utilizados.

Las clases más importantes son:
- **Parser**: recibe un arreglo de strings, instancia procesos y los almacena 
en una lista
- **Dispatcher**: el planificador del hipotético OS, en el método `run()` 
simula el paso del tiempo con un bucle, también tiene los métodos 
`updateBCP()` y `manageGanttEntries()`, que gestionan los procesos y el 
diagrama de Gantt respectivamente.
- **Algoritmo**: interfaz que declara el método `apply()` que todo algoritmo 
debe implementar
- **Gantt**: Almacena un ArrayList con intervalos de ejecución, se encarga de 
la creación del diagrama de Gantt, y del cálculo de promedios.

---
## Problemas en el camino

- [x] El primer problema con el que nos cruzamos fue el no comprender realmente 
qué se nos pedía e ir corriendo a las computadoras a escribir código. La 
versión 1.0 del programa es un programa en Java que recibe una lista de 
procesos en formato .csv y devuelve otro .csv con el diagrama Gantt trazando 
la ejecución de los mismos. Aún así, es un programa que sólo vagamente ha
oído hablar de qué es la orientación a objetos, apenas soporta un par de 
algoritmos, y lo último que jamás le preocupó fue el modo en que un
despachador funciona por dentro. Es decir, la v1.0 hace todo lo que tiene que 
hacer de todas las formas equivocadas. 


- [x] El siguiente problema fue la definición de clases y la apropiada 
asignación de responsabilidades (esto ya cuando sí recordamos utilizar 
orientación a objetos). Un problema habitual en cualquier proyecto, que nos
siguió durante gran parte del desarrollo, pues incluso hacia el final pueden
verse commits redistribuyendo tareas a tal o cual clase según juzgásemos más
apropiado quién debía hacer qué.


- [x] También están los problemas relacionados con git. Un simple vistazo al
historial de commits en el repositorio de github bastará al profesor para notar
que, si bien el proyecto tuvo definido un esquema de commits aceptable desde el
principio, ambos integrantes de éste grupo somos un par de neófitos a la hora de 
trabajar con git, y puede notarse en las varias torpezas registradas.


- [x] Los problemas relacionados con conseguir que el BCP se actualizara
correctamente en cada interacción con Dispatcher no se mencionan, porque tenemos
entendido que ése era el *quid de la cuestión*. Es ése el problema que estamos
solucionando al implementar el programa. No es una dificultad extra, sino la que
se suponía que debíamos tener.

---
## Qué se logró, qué no

Creemos haber logrado todos los ítems mencionados en el .docx del profesor, dado
que pese a lo que esté escrito en la lista de indicadores, en clases fue acordado 
que el output se presentaría en un archivo .csv, no en la interfaz gráfica.
Más allá de eso: 
- BCP se halla correctamente implementado, según nuestra mejor comprensión del mismo.
- Todos los algoritmos solicitados fueron implementados y son funcionales.

---
## El video

El link del video con la demostración:
[Click] [https://drive.google.com/file/d/1NvH69HLR2_WNsoYLYKLrgccDTx-idWVK/view?usp=sharing] 

