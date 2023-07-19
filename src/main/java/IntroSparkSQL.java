import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;

public class IntroSparkSQL {

    public static void main(String[] args){
        // Abrir una conexión con un cluster
        SparkSession conexion = SparkSession.builder()
                                            .appName("EjemploSQL")
                                            .master("local[4]")
                                            .getOrCreate();
        // Necesitaré hacerme con un objeto de datos en el cluster (RDD, Stream-> Dataset
        Dataset<Row> personas = conexion.read().json("src/main/resources/personas.json"); // De esta ruta hablaremos!
        // Configuraré operaciones
        personas.show();
        personas.printSchema();

        personas.select("apellidos", "nombre", "edad").show();
        personas.select(col("apellidos").startsWith("Garcia"),col("nombre"),col("edad").plus(15)).show();

        personas.filter(col("edad").gt(30)).show();

        personas.orderBy(col("edad").desc()).show();

        personas.groupBy("nombre").sum("edad").show();

        personas.createOrReplaceTempView("personas");
        Dataset<Row> resultado = conexion.sql("SELECT nombre, apellidos, edad+5 FROM personas WHERE edad > 30 ORDER BY edad desc");
        resultado.show();

        // Cerrar la conexión
        conexion.stop();
    }

}
