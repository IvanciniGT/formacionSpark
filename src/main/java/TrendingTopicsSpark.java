import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TrendingTopicsSpark {
    public static void main(String[] args){
        final SparkConf configuracionDelClusterDeSpark =new SparkConf()
                .setAppName("calcularTrendingTopics")
                .setMaster("local[5]");
        final JavaSparkContext conexion = new JavaSparkContext(configuracionDelClusterDeSpark);
        // Vamos a tener una lista de palabras prohibidas
        // Caca Culo Pedo Pis Mierda
        final List<String> palabrotas = Arrays.asList("CACA", "CULO", "PEDO","PIS", "MIERDA");
        // Si tengo un hashtag que contenga alguna de esas palabras, lo elimino

        conexion.parallelize(Arrays.asList(  "En la piscina,#goodVibes#SummerLove",
                "En el trabajo.#goodVibes(#MierdaDeVerano)",
                "Dando un paseo al perro-#GooDVIBES",
                "En la playita !!!!"))
                .filter( tweet -> tweet.contains("#") ) // filtramos solo los que contengan almohadilla
                // Apuntar que en el rrd más adelante se aplique una función de filtro que acabamos de definir
                .map( tweet -> tweet.replaceAll("#", " #") ) // Añadir espacio delante del #
                .flatMap( tweet -> Arrays.asList(tweet.split("[^\\w#]+")).iterator())
                .filter( termino -> termino.startsWith("#") ) // Quedarme con los que empiezan por cuadradito
                .map( String::toUpperCase) // normalizarlo
                .filter( hashtag -> palabrotas.stream().filter( palabrota -> hashtag.contains(palabrota) ).count() == 0)

                .mapToPair ( hashTag -> new Tuple2<>(hashTag, 1) )// Añado a cada hashtag un 1
                .reduceByKey( Integer::sum )// Sumo los valores de los hashtag iguales
                .mapToPair( tupla -> new Tuple2<>(tupla._2, tupla._1) )// Uso como clave el numero
                .sortByKey(false)// Para ordenar por él
                .mapToPair( tupla -> new Tuple2<>(tupla._2, tupla._1) )// Y le doy la vuela para acabar
                .take(5)    // Me que con 5
                .forEach( System.out::println );

                // Tabla HASTAGS , columna hashtag
                // SELECT hashtag, COUNT(hashtag) FROM Hashtags ORDER BY COUNT(hashtag) DESC;
        // (a,b) -> a + b


                /*
                a   1
                a   1
                a   1
                b   1
                a   1
                .collect()
                // ^ Ese collect es el que manda por RED las funciones que hay que ejecutar en los nodos sobre los datos
                // Aquí también mandamos los datos !
                .forEach( System.out::println );*/

        conexion.close();
    }
}
