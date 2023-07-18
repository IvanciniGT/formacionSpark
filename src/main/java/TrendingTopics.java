import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TrendingTopics {

    public static void main(String[] args){
        Stream<String> tweets = Arrays.asList(  "En la piscina,#goodVibes#SummerLove",
                        "En el trabajo.#goodVibes(#MierdaDeVerano)",
                        "Dando un paseo al perro-#GooDVIBES",
                        "En la playita !!!!").stream();

        Stream<String> tweetsConHashtags = tweets                 // Para cada tweet
                .filter( tweet -> tweet.contains("#") ); // filtramos solo los que contengan almohadilla

        Stream<String> tweetsConHashtagsSeparados =
                tweetsConHashtags.map( tweet -> tweet.replaceAll("#", " #") ); // Añadir espacio delante del #

        Stream<String> tweetsConTerminosSeparados=          // Stream<String>
                tweetsConHashtagsSeparados.flatMap( tweet -> Arrays.stream(tweet.split("[^\\w#]+")));

        Stream<String> hashtags =
            tweetsConTerminosSeparados.filter( termino -> termino.startsWith("#") ); // Quedarme con los que empiezan por cuadradito

        Stream<String> hashtagsNormalizados =
            hashtags.map( String::toUpperCase); // normalizarlos

        hashtagsNormalizados.forEach( System.out::println );
    }

}
