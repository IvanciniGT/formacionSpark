import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.internal.config.R;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.util.LongAccumulator;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.udf;

public class DNIsStreaming {

    public static void main(String[] args) throws TimeoutException, StreamingQueryException {
        // Abrir una conexión con un cluster
        SparkSession conexion = SparkSession.builder()
                                            .appName("EjemploSQL")
                                            .master("local[4]")
                                            .getOrCreate();

        final Broadcast<Map<Integer, String>> letrasDNI = JavaSparkContext.fromSparkContext(conexion.sparkContext()).broadcast(letrasDNI());
        final LongAccumulator invalidos = conexion.sparkContext().longAccumulator();
        //Dataset<Row> personas = conexion.read().json("src/main/resources/personas.json");
        StructType esquema = conexion.read().json("src/main/resources/personas.json").schema();
        Dataset<Row> personas = conexion.readStream().schema(esquema).json("src/main/resources/"); // De esta ruta hablaremos!
        // Configuraré operaciones
        UserDefinedFunction validadorDni = udf( (String dni)-> {
            try {
                int numero = Integer.parseInt(dni.substring(0, dni.length() - 1));
                if( letrasDNI.getValue().get(numero%23).equals(dni.substring(dni.length() - 1)) )
                    return numero;
            }catch (Exception e){}
            invalidos.add(1);
            return -1;
        }, DataTypes.IntegerType);

        Dataset<Row> personas2 = personas.select(col("nombre"), validadorDni.apply(col("dni")).as("dni"))
                .filter(col("dni").notEqual(-1));

        //personas2.show();
        System.out.println("Invalidos "+invalidos.count());

        personas2.writeStream()
                .format("console")
                .start()
                .awaitTermination();

    }

    private static Map<Integer, String> letrasDNI(){
        Map<Integer, String> letrasControl = new HashMap<>();
        letrasControl.put(0,"T");
        letrasControl.put(1,"R");
        letrasControl.put(2,"W");
        letrasControl.put(3,"A");
        letrasControl.put(4,"G");
        letrasControl.put(5,"M");
        letrasControl.put(6,"Y");
        letrasControl.put(7,"F");
        letrasControl.put(8,"P");
        letrasControl.put(9,"D");
        letrasControl.put(10,"X");
        letrasControl.put(11,"B");
        letrasControl.put(12,"N");
        letrasControl.put(13,"J");
        letrasControl.put(14,"Z");
        letrasControl.put(15,"S");
        letrasControl.put(16,"Q");
        letrasControl.put(17,"V");
        letrasControl.put(18,"H");
        letrasControl.put(19,"L");
        letrasControl.put(20,"C");
        letrasControl.put(21,"K");
        letrasControl.put(22,"E");
        return letrasControl;
    }
}
