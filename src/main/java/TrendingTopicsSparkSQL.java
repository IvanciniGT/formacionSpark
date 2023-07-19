import lombok.Data;
import org.apache.hadoop.util.hash.Hash;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

public class TrendingTopicsSparkSQL {
    public static void main(String[] args){
        SparkSession conexion = SparkSession.builder()
                .appName("TrendingTopicSQL")
                .master("local[4]")
                .getOrCreate();
        // Vamos a tener una lista de palabras prohibidas
        // Caca Culo Pedo Pis Mierda
        final List<String> palabrotas = Arrays.asList("CACA", "CULO", "PEDO","PIS", "MIERDA");
        // Si tengo un hashtag que contenga alguna de esas palabras, lo elimino

        Dataset<Row> tweets = conexion.createDataFrame(Arrays.asList(  "En la piscina,#goodVibes#SummerLove",
                "En el trabajo.#goodVibes(#MierdaDeVerano)",
                "Dando un paseo al perro-#GooDVIBES",
                "En la playita !!!!").stream().map( texto -> new Tweet(texto)).collect(Collectors.toList()),
                Tweet.class
        );

        JavaRDD<Hashtag> hashtags = tweets.toJavaRDD().map(fila -> fila.get(0).toString() )
                .filter( tweet -> tweet.contains("#") ) // filtramos solo los que contengan almohadilla
                // Apuntar que en el rrd más adelante se aplique una función de filtro que acabamos de definir
                .map( tweet -> tweet.replaceAll("#", " #") ) // Añadir espacio delante del #
                .flatMap( tweet -> Arrays.asList(tweet.split("[^\\w#]+")).iterator())
                .filter( termino -> termino.startsWith("#") ) // Quedarme con los que empiezan por cuadradito
                .map( String::toUpperCase) // normalizarlo
                .filter( hashtag -> palabrotas.stream().filter( palabrota -> hashtag.contains(palabrota) ).count() == 0)
                .map(Hashtag::new);
        Dataset<Row> hashtagsDataset =  conexion.createDataFrame(hashtags, Hashtag.class);


        hashtagsDataset.groupBy("hashtag").count().sort(col("count").desc()).show();

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

    @Data
    public static class Hashtag {
        public Hashtag(String hashtag){
            this.hashtag= hashtag  ;
        }
        private String hashtag;
    }
    @Data
    public static class Tweet {
        public Tweet(String tweet){
            this.tweet= tweet  ;
        }
        private String tweet;
    }
}
