import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

// Function         Función que tiene argumentos y devuelve un dato
// Predicate        Función que tiene argumentos y devuelve un boolean
// Consumer         Función que tiene argumentos y no devuelve ni la hueva (void)
// Producer         Función que no tiene argumentos pero devuelve un valor
public class ProgramacionFuncional {

    public static void main (String[] args) {
        // Asignar la variable texto al valor HOLA, inicializándola
        String texto = "HOLA";
        // 1º "HOLA" -> Coloca un objeto de tipo String en la RAM con el valor "HOLA"
        // 2º Crea una variable que puede apuntar a objetos de tipo String, llamada texto
        // 3º Asigna la variable al objeto HOLA
        texto = "Adios";
        // 1º "ADIOS" -> Coloca un objeto de tipo String en la RAM con el valor "ADIOS"
                        // Dónde? en el mismo sitio donde ponía "Hola"? NO... en otro
                        // En este momento en RAM tengo 2 objetos String: "Hola" y "Adios"
        // 2º Toma la variable (arranca el posit de donde estaba pegao!)
        // 3º Pega el posit al lado del "Adios"... Reasigna la variable al nuevo valor"
            // En este momento, a "Hola" nadie le apunta... se ha convertido en BASURA (Garbage)
            // Y en algún momento o no... entrará el Recolector de Basura a trincarselo !

        System.out.println(ProgramacionFuncional.doblar(5));
        // Esto se puede hacer desde Java 1,.8... antes no.
        Function<Integer, Integer> miFuncion = ProgramacionFuncional::doblar;
                                // En Java sale el operador :: que permite REFERENCIAR funciones
        System.out.println(miFuncion.apply(10));
        // En esta linea delego la ejecución de miFuncion a la función Imprimir
        ProgramacionFuncional.imprimirResultadoOperacion(miFuncion, 20);
        ProgramacionFuncional.imprimirResultadoOperacion(ProgramacionFuncional::doblar, 14);
        // Adicionalmente en Java 1.8 sale otro operador -> que permite definir una expresión lambda
        // Que es una expresión?
        System.out.println("HOLA"); // STATEMENT - Sentencia = Frase
        //int numero = 5+6;           // Otro Statement
                    //^//              Una expresión: Una porción de código que devuelve un valor: Lo que en español llamaríamos un SINTAGMA
        // Qué es una expresión lambda? Una poción de código que devuelve una función anónima
        // creada en linea, dentro de un statement.

        Function<Integer, Integer> miFuncion2 = (Integer numero) -> {
            return numero*10;
        };

        ProgramacionFuncional.imprimirResultadoOperacion(miFuncion2, 4);

        Function<Integer, Integer> miFuncion3 = (numero) -> {
            return numero*3;
        };

        ProgramacionFuncional.imprimirResultadoOperacion(miFuncion3, 4);

        Function<Integer, Integer> miFuncion4 = numero -> {
            return numero*4;
        };

        ProgramacionFuncional.imprimirResultadoOperacion(miFuncion4, 4);

        Function<Integer, Integer> miFuncion5 = numero -> numero*5;

        ProgramacionFuncional.imprimirResultadoOperacion(miFuncion5, 4);

        ProgramacionFuncional.imprimirResultadoOperacion(  numero -> numero*7  , 4);
        // Las funciones anónimas tiene una característica muy especial...
        // no están asociadas a ninguna clase. y eso tendrá su repercusión en Spark... que ya os contaré!


        // Como consecuencia de la inclusión de programación funcional dentro de Java 1.8, se añade otra cosita...
        // REPITO: CONSECUENCIA DIRECTA de tener por fin... 17 años después... programación funcional
        //  Los Stream: Definidos dentro del paquete java.util.stream
        // Que es un Stream
        // Básicamente lo mismo que un Set... Una colección secuencial (ordenada) de datos.... sobre la que puedo iterar.
        // El Stream es igual... solo que está preparado para aplicarle operaciones Map-Reduce... programación funcional!
        // Cualquier Collection de Java lo puedo transformar a un Stream, mediante la función stream()
        // Cualquier Stream lo puedo convertir a una Collection, mediante la función .collect(Collector)
        // Collector, que básicamente me dice a que tipo de Collection quiero convertir el Stream

        List<String> miLista = List.of("hola1","HOLA2", "Hola3");

        // Hasta Java 1.5 , cómo itero eso, para mostrarlo por pantalla?
        for(int i = 0;i<miLista.size();i++) {
            System.out.println(miLista.get(i));
        }

        // En Java 1,5...  aparecen los Iterables... y los foreach
        for(String elemento : miLista){
            System.out.println(elemento);
        }
        // En java 1.8, se añade la función foreach a todas las colecciones:
        miLista.forEach(System.out::println); // Esta implementación es la más eficiente!

        //miLista.forEach( elemento -> System.out.println(elemento) );

        miLista.stream()                                    // Para cada texto
                .map( String::toUpperCase )                 // Lo pongo en mayúsculas
                .map( txt -> "Tengo el texto: " + txt )     // Lo pongo en mayúsculas
                .filter( txt -> !txt.endsWith("2"))         // Quita los que acaban en 2
                .forEach(System.out::println);              // Lo imprimo

        // Funciones de tipo map:  map, flatMap, filter... Dado un stream, generan otro stream
        //          Stream Origen           map(doblar)         Stream Destino
        //              1               --->    doblar ---->        2
        //              2                                           4
        //              3                                           6
        // Básicamente me permite transformar datos
        // Funciones tipo reduce: reduce, forEach: Funciones que dado un stream generan algo que no es un Stream(lo cual incluye no devolver nada)
        // Las funciones de tipo map, se ejecutan en modo Lazy, siempre!... y ahora os aclaro esto!

        List<Integer> numeritos = List.of(1,2,3,4,5,6,7,8,9);
        List<Integer> nuevaLista = numeritos.parallelStream() // Quiero que que crees tantos hilos com ocores tengo en la máquina y repartas las tareas entre ellos... y esperes al resultado conjunto, cuando todos acaben,
                .filter( numero -> numero %2 == 0) // Que hace esta linea realmente?
                                                    // Crea un nuevo stream con
                                                    // los pares == Quitando los impares
                                                    // NOP !!!!
                                                    // Lo que realmente hace es APUNTAR que esa operación debe realizarse. No hacerla.
                                                    // Apunta que solo quiero los pares!
                                                    // Ejecución en modo Lazy... vago... solo cuando sea necesario
                .map( numero -> numero * 2)
                .collect(Collectors.toList());      // Las funciones de tipo reduce, son las disparan la aplicación de las anotaciones que se hacen sobre un Stream.
        // Este proceso que es pura CPU, en mi caso que tengo un i7 de 4x2 cores = 8 cores... pondría la CPU al 12.5%
        // Quedate con los elementos de la lista cuyo triple es impart
        // 1,2,3,4,5,6,7,8,9

        nuevaLista.forEach(System.out::println);
    }

    public static void imprimirResultadoOperacion(Function<Integer, Integer> operacion, int numero){
        System.out.println(operacion.apply(numero));
    }
    public static int multiplicarPorSiete(int numero) {
        return numero * 7;
    }
    public static int doblar(int numero){
        return numero*2;
    }
}
