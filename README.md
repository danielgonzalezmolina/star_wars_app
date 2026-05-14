Star Wars 
Proyecto Android desarrollado con Jetpack Compose que permite gestionar una base de datos de 
planetas de Star Wars. La aplicación aplica arquitectura moderna de Android, inyección de 
dependencias y flujos reactivos


El PlanetRepository es un Singleton que contiene el dataset de planetas. 
Pasa los datos a través de un Flow<List<Planet>>

Solo hay un viewmodel compartido: PlanetViewModel. Inyectado mediante Hilt en el NavHost. 
Al pulsar un planeta en la lista, se guarda en el estado selectedPlanet del ViewModel
para que lo lea la pantalla de edición al abrirse


La navegación se gestiona mediante NavHost, con transiciones laterales al cambiar de pantalla

El FloatingActionButton se define en el Scaffold de la MainActivity y solo aparece en la ruta "LIST"
 mediante currentBackStackEntryAsState(). Esta funcion he tenido que investigarla por internet

A PlanetListScreen le he añadido un buscador y filtros para mejorar la interfaz y como práctica 
personal. Cada tarjeta permite editar o borrar el planeta que representa. El borrado muestra un 
alertDialog para confirmar y un snackbar informativo

Como contendrían exactamente los mismos campos, he mezclado las pantallas de crear y editar en
PlanetEditScreen. La pantalla detecta si está en modo "Crear" o "Editar" basándose en el parámetro
del planeta inicial que recibe en el ViewModel. Si recibe un planeta sin datos interpreta que esta 
en el modo creacion, y los textos cambian. 
Ademas, he modificado los estilos que ya había con respecto a la última práctica para hacerla mas
visual.

Tambien he cambiado los estilos de la pantalla de about us para aprovechar los colores y el estilo 
de las demas pantallas.
