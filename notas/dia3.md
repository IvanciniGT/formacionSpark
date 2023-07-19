Dentro de Spark hay varias librerías.

Spark-core          es la que ofrece una implementación map-reduce
    ^
Spark-sql           esta librería es la que se ha impuesto!
                    Nos permite trabajar con conjuntos grandes de datos,
                    usando un lenguaje al que estamos muy acostumbrados...
                    que nos sirve también cuando trabajamos con conjuntos no tan grandes: SQL
    Tiene sus propias funciones para crear una conexion con un cluster de spark


---

Todos estos procesos que hemos montado, son procesos BATCH.
Son procesos que parten de un conjunto de datos cerrado (fichero, query...)
y proceso información hasta que no quede... y ahí el programa muere... hasta mañana... o pasado... o dentro de una hora.

En muchas ocasiones esto es suficiente. En otras ocasiones sno es suficiente... 
y queremos montar procesos en Streaming... donde los datos se procesan en caliente... según llegan.

Cuando mandamos datos... en caliente, tenemos un problema potencial: 
Que el destinatario del dato, no esté disponible!
Qué pasa ahí? 

Tengo un Servidor WEB soy un banco, tienda!
Ese servidor web genera access logs 
A la empresa, le sirven para algo?

AMAZON... Cúantos servidores web puede tener amazon trabajando en cluster para ofrecer la tienda: centenares a miles!

Servidores WEB            flume         Sis. de Mensajería                 Spark                    MariaDB
    -> accesslog1 50kbs   fluentd   --->     Kafka(10)    <-- Procesar la info en tiempo real -----> BBDD ----> Dashboard
         v     ^          filebeat                              1 maestro y 200 trabajadores
       accesslog2 50kbs
        Tiene persistencia en RAM


        Toda la info que se haya recopilado se procesa
            ^....   ^....
    |------>|------>|--------------------------------------------------------------------> TIEMPO
    ^       ^       ^
    Empezamos a capturar información  cada 5 segundos

Por un lado Spark ha tenido siempre una librería llamada spark-streaming.
Pero la deprecaron hace poco.... y hoy en día ha sido reemplazada por spark-sql.

La librería SparkStreaming se basaba en RDDs, estaba montada sobre SparkCope
Lo nuevo, os imaginareis, trabaja con Datasets

El punto es que cuando trabajamos en modo Streaming, no vamos a tener 1 RDD o 1 Dataset
Tendremos una colección de RDDs o de Datasets
                            v           v
                          DStream    Dataframe


DStream de Spark... no se parece en nada a un Stream de Java
El Stream de Java es equivalente en Spark al RDD

En general, no me interesan los RDDs pasados... solo el actual... ya que a los pasados, ya les habré dado su trámite.

        Toda la info que se haya recopilado se procesa          CATAPLASH
            ^....   ^....                                          v
    |---1-->|---2-->|---3-->|---4-->|---5-->|---6-->|---7-->|---8--X-------------------------------------> TIEMPO
    ^       ^       ^
    Empezamos a capturar tweets cada 5 segundos y quiero sacar los trending topic.

    En la ventana de tiempo 1, me llegarán 1000 tweets, que procesaré y sacar el listado de hashtags... contabilizados
    posiblementye ni lo ordene... y el resultado que será una tabla del tipo:
            goodVibes       123
            mierdaVerano     77         ===> TABLA DE UNA BBDD
            calorDeCojones  577

    Y así lo hire haciendo cada 5 segundos...

    Quizás cada hora, lanzo otro proceso... de consolidación de todas esas mediciones -> Tabla ordenada hashtags

QUE PASA SI CATAPLASH? |---8--X-------
Pierdo los datos de esa ventana de tiempo... Me los volverían a mandar? NO... ya me los mandaron!
Qué pasa con los datos después de la X... los pierdo? NO
Cuando vuelva a arrancar el programa, contactaré con la fuente de datos... y me los mandará
Para evitar esta situación, puedo solicitar a Spark que vaya generando un registro
persistente en disco de toda la información que va recibiendo.
Al iniciar el proceso, quiero que se mire si ya había un contexto en funcionamiento... con datos ptes de procesar...
En cuyo caso, no generaré un contexto nuevo, partiré del que tenía, del que Spark ha persistido en disco.

