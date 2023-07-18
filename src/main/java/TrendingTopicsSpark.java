import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.stream.Stream;

public class TrendingTopicsSpark {
    public static void main(String[] args){
        final SparkConf configuracionDelClusterDeSpark =new SparkConf()
                .setAppName("calcularTrendingTopics")
                .setMaster("local[5]");
        final JavaSparkContext conexion = new JavaSparkContext(configuracionDelClusterDeSpark);

        conexion.parallelize(Arrays.asList(  "En la piscina,#goodVibes#SummerLove",
                "En el trabajo.#goodVibes(#MierdaDeVerano)",
                "Dando un paseo al perro-#GooDVIBES",
                "En la playita !!!!"))
                .filter( tweet -> tweet.contains("#") ) // filtramos solo los que contengan almohadilla
                .map( tweet -> tweet.replaceAll("#", " #") ) // Añadir espacio delante del #
                .flatMap( tweet -> Arrays.asList(tweet.split("[^\\w#]+")).iterator())
                .filter( termino -> termino.startsWith("#") ) // Quedarme con los que empiezan por cuadradito
                .map( String::toUpperCase) // normalizarlos
                .collect()
                .forEach( System.out::println );

        conexion.close();
    }
}
