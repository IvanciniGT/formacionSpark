import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;
import scala.Tuple2;

import java.io.Serializable;
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
        final LongAccumulator palabrasEliminadas = conexion.sc().longAccumulator();
        JavaPairRDD<String,Integer> hashtags = conexion.parallelize(Arrays.asList(  "En la piscina,#goodVibes#SummerLove",
                        "En el trabajo.#goodVibes(#MierdaDeVerano)",
                        "En el trabajo.#goodVibes(#CacaDeVerano)",
                        "En el trabajo.#goodVibes(#VeranoDePis)",
                        "En el trabajo.#goodVibes(#CuloCacaPedoPis)",
                "Dando un paseo al perro-#GooDVIBES",
                "En la playita !!!!"))
                .filter( tweet -> tweet.contains("#") ) // filtramos solo los que contengan almohadilla
                // Apuntar que en el rrd más adelante se aplique una función de filtro que acabamos de definir
                .map( tweet -> tweet.replaceAll("#", " #") ) // Añadir espacio delante del #
                .flatMap( tweet -> Arrays.asList(tweet.split("[^\\w#]+")).iterator())
                .filter( termino -> termino.startsWith("#") ) // Quedarme con los que empiezan por cuadradito
                .map( String::toUpperCase) // normalizarlo
                .filter( hashtag -> palabrotas.stream().filter( palabrota -> {
                    boolean contienePalabrota = hashtag.contains(palabrota);
                    if(contienePalabrota) {
                        palabrasEliminadas.add(1);
                        System.out.println("Palabrota detectada: "+ palabrota+" llevamos: "+palabrasEliminadas.count()+" palabrotas eliminadas");
                    }
                    return contienePalabrota;
                } ).count() == 0)
                .mapToPair ( hashTag -> new Tuple2<>(hashTag, 1) )// Añado a cada hashtag un 1
                .reduceByKey( Integer::sum )// Sumo los valores de los hashtag iguales
                .mapToPair( tupla -> new Tuple2<>(tupla._2, tupla._1) )// Uso como clave el numero
                .sortByKey(false)// Para ordenar por él
                .mapToPair( tupla -> new Tuple2<>(tupla._2, tupla._1) )// Y le doy la vuela para acabar
                //.take(5)    // Me que con 5
                //.forEach( System.out::println );
                ;

        hashtags.repartition(1).saveAsTextFile("fichero.txt");

        // Queremos el número de palabrotas detectadas y eliminadas
        System.out.println("Eliminadas:  " + palabrasEliminadas.count());

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
/*
    public static class Contador implements Serializable {
        private int cantidad;
        public Contador() {
            cantidad = 0;
        }
        public void incrementar(){
            this.cantidad++;
        }
        public int valor(){
            return this.cantidad;
        }

    }*/
}
