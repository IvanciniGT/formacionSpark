import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class TrendingTopicsSparkStreaming {
    public static void main(String[] args)  {

        JavaStreamingContext contextoDeStreaming =
                JavaStreamingContext.getOrCreate("checkpoint",
                        TrendingTopicsSparkStreaming::configurarUnStreamingContext);
        try {
            contextoDeStreaming.start(); // Pon en marcha ese contexto de Streaming ... en el cluster
            contextoDeStreaming.awaitTermination();
        }catch(InterruptedException ie){}
        contextoDeStreaming.close();
    }

    private static JavaStreamingContext configurarUnStreamingContext(){
        final SparkConf configuracionDelClusterDeSpark =new SparkConf()
                .setAppName("calcularTrendingTopics")
                .setMaster("local[5]");
        final JavaStreamingContext conexion = new JavaStreamingContext(configuracionDelClusterDeSpark, Durations.seconds(10));
        final List<String> palabrotas = Arrays.asList("CACA", "CULO", "PEDO","PIS", "MIERDA");

        JavaDStream<String> tweets = conexion.socketTextStream("localhost",9999);
        tweets
                .filter( tweet -> tweet.contains("#") )
                .map( tweet -> tweet.replaceAll("#", " #") )
                .flatMap( tweet -> Arrays.asList(tweet.split("[^\\w#]+")).iterator())
                .filter( termino -> termino.startsWith("#") )
                .map( String::toUpperCase)
                .filter( hashtag -> palabrotas.stream().filter( palabrota -> hashtag.contains(palabrota) ).count() == 0)
                .mapToPair ( hashTag -> new Tuple2<>(hashTag, 1) )
                .reduceByKey( Integer::sum )
                .mapToPair( tupla -> new Tuple2<>(tupla._2, tupla._1) )
                .foreachRDD(
                        rdd -> {
                            rdd.sortByKey(false)
                                .mapToPair(tupla -> new Tuple2<>(tupla._2, tupla._1))
                                .collect()
                                .forEach(System.out::println); // Aqui me lo llevo a una BBDD Tradicional
                        });
        return conexion;
    }
}
