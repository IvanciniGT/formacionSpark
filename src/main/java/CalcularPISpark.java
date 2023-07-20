import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class CalcularPISpark {
    public static void main(String[] args) {

//      PASO 1: Abro una conexión con un cluster de Spark (con quien hablo, mi interlocutor, es el maestro)
        final SparkConf configuracionDelClusterDeSpark =new SparkConf()
                .setAppName("calcularPI");
                // Aun no tenemos una instalación real de un cluster de Spark.
                // GRacias a los chavalillos y chavalillas que hace Spark,
                // tenemos un truquito, guay!!
                // Cuando estoy desarrollando puedo poner como maestro: local
                //                                                  o   local[?]
                //                                                      local[3]
                //                                                      local[*]  = poner solo local
                // siendo el número el número de cores que quiero que se usen de mi máquina en un cluster
                // de juguete que se me va a automontar para pruebas
                //.setMaster("spark://3.253.61.214:7077");
        // Esta es la conexión con el cluster!
        final JavaSparkContext conexion = new JavaSparkContext(configuracionDelClusterDeSpark);

        long tin = System.currentTimeMillis();
        final long NUMERO_TOTAL_DE_DARDOS = 10L * 1000 * 1000;

//      PASO 2: Creo un RDD
        // collection.stream() -> Stream
        // Stream.collect( COLECTOR EN FUNCION DEL TIPO DE COLLECTION) -> Collection
        int trozos = 50; // -200
        // Quiero optimizar el uso de los recursos del cluster... por ende...
        // Si pongo pocas particiones que puede pasar?
        //      Si al final salen menos particiones que nodos trabajadores (contando sus cores) tengo
        //      Habrá workers que se quedan sin trabajar
        // Me interesa al menos un número igual o superior al número de workers que haya
        //      dato que en general desconozco
        // Que pasa si parto aquello en demasiados trozos?
        //      Sobrecarga de trabajo... muchas comunicaciones... muchas consolidaciones de datos...
        //      En el extremo podría hacer tanto trozos como elementos tenga en el conjunto de datos de partida
        // Será el número de divisiones que hará de los datos... Cada división (trozo) se mandará a un Worker
        // long -> Long
        // int  -> Integer
        // boolean -> Boolean
        // byte -> Byte
        // double -> Double

        // int numero = (int)3L;
        // char c = 'a';
        // Character C = (Character)c; // No es necesario hacer conversión.... Se hace en auto. por java: AUTOBOXIN
        // Convertir tipos simples en sus respectivos objetos
        // Esto es necesario al trabajar con genéricos!

        List<Long> listadoDeTiradas = LongStream.range(0, NUMERO_TOTAL_DE_DARDOS)
                .boxed()
                .collect(Collectors.toList());
        JavaRDD<Long> datosDePartida = conexion.parallelize(listadoDeTiradas, trozos );

//      PASO 3: Configuro el trabajo en el RDD (map+map+map+map+map...+map...reduce)

        // Tirar un numero Enorme de dardos aleatorios
        long dardosEnLaDiana = datosDePartida               // Todos los dardos los quiero tirar en paralelo
                .map(numeroDeTirada -> new Dardo())         // Tiro dardo
                .filter( Dardo:: estaEnLaDiana )            // Me quedo con los que están dentro
                .count();                                   // Los cuento

//      PASO 4: Acabo... con lo que sea
        double pi = 4. * dardosEnLaDiana / NUMERO_TOTAL_DE_DARDOS; // Calcular PI
        long tout = System.currentTimeMillis();
        System.out.println("El valor de PI es: "+ pi);
        System.out.println("hemos tardado: "+ (tout-tin) + "milisegundos");

//      PASO 5: Cierro conexión con el cluster de Spark.
        conexion.close();

    }

    private static class Dardo implements Serializable {
        private final double x= Math.random();
        private final double y = Math.random();
        public boolean estaEnLaDiana(){
            return Math.sqrt( x * x + y * y ) <= 1;
        }
    }
}


/*
 * Stream de partida
 *   0
 *   1
 *   ...
 *   99999
 * */
