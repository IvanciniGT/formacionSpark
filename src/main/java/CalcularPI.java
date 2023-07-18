import java.io.Serializable;
import java.util.stream.LongStream;

public class CalcularPI {
    public static void main(String[] args) {
        long tin = System.currentTimeMillis();
        final long NUMERO_TOTAL_DE_DARDOS = 10L * 1000 * 1000;
        // Tirar un numero Enorme de dardos aleatorios
        long dardosEnLaDiana = LongStream.range(0, NUMERO_TOTAL_DE_DARDOS)   // Para cada tirada
                .parallel()                                                 // Todos los dardos los quiero tirar en paralelo
                .mapToObj(numeroDeTirada -> new Dardo())                    // Tiro dardo
                .filter( Dardo:: estaEnLaDiana )                            // Me quedo con los que están dentro
                .count();                                                   // Los cuento
        double pi = 4. * dardosEnLaDiana / NUMERO_TOTAL_DE_DARDOS; // Calcular PI
        long tout = System.currentTimeMillis();
        System.out.println("El valor de PI es: "+ pi);
        System.out.println("hemos tardado: "+ (tout-tin) + "milisegundos");
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
