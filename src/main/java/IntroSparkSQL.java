import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
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

        // Dataset<Row> -> JavaRDD<Persona>
        // Cerrar la conexión
        JavaRDD<Persona> personasRDD = personas.toJavaRDD().map( IntroSparkSQL::row2Persona );
        personasRDD.collect().forEach(System.out::println);

        Dataset<Row> personas2 = conexion.createDataFrame(personasRDD, Persona.class);
        personas2.show();

        JavaSparkContext miContexto = JavaSparkContext.fromSparkContext(conexion.sparkContext());
        SparkSession session2 = SparkSession.builder().config(miContexto.getConf()).getOrCreate();
        conexion.stop();
    }

    // Mapeador
    public static Persona row2Persona(Row fila){
        Persona p = new Persona();
        p.setNombre(fila.get(fila.fieldIndex("nombre")).toString());
        p.setApellidos(fila.get(fila.fieldIndex("apellidos")).toString());
        p.setEmail(fila.get(fila.fieldIndex("email")).toString());
        p.setEdad((int) fila.getLong(fila.fieldIndex("edad")));
        p.setDni(fila.get(fila.fieldIndex("dni")).toString());
        return p;
    }

}
