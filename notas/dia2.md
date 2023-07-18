# Que es maven?

Es una herramienta de automatización de tareas habituales de mi proyecto:
- Compilación
    - Es necesario descargar Dependencias
- Ejecución de Pruebas
- Empaquetado
- Ejecución
- Mandar el código al Sonar

# Quien compila un proyecto java?

javac

Usar javac es horrible

javac -cp ".jar 10 lineas o 20 de codigo" -d classes ponte aqui paquetes

Nosotros nunca compilamos hoy en día llamando directamemnte a javac... sino mediante MAVEN?  NO... mediante el entorno de desarrollo: ECLIPSE, INTELLIJ

Entonces... pa' que quiero el maven?
Porque lo que vosotros compilais no vale pa mierda !!!!

Vosotros subís el código fuente a git...
Y de donde sale el compilao'?
De un proceso de Integración continua (lanzado a través de un Jenkins o similar)
que descarga el código de git... y lo compila a través de MAVEN !

---

# SPARK

Nuestra computadora... que solicita a un cluster la ejecución de un trabajo
vvvv ^^^^^
Cluster:
Maestro, que recibe el encargo... y lo distribuye a unos:
Trabajadores... que son los que realmente lo ejecutan

Cómo va a ser un programa de Spark... sea el programa que sea:

- PASO 1: Abro una conexión con un cluster de Spark (con quien hablo, mi interlocutor, es el maestro)
- PASO 2: Creo un RDD
- PASO 3: Configuro el trabajo en el RDD (map+map+map+map+map...+map...reduce)
- PASO 4: Acabo... con lo que sea
- PASO 5: Cierro conexión con el cluster de Spark.

## RDD

Básicamente es como llama Spark a los Stream de java.

Eso está bien cuando trabajo con la librería spark-core. NUNCA JAMAS TRABAJAMOS CON LA LIBRERIA SPARK CORE.
REALMENTE trabajamos con la librería: spark-sql

Cuando trabajamos con la librería SparkSQL, los pasos serán:

- PASO 1: Abro una conexión con un cluster de Spark (con quien hablo, mi interlocutor, es el maestro)
- PASO 2: Creo un DataSet
- PASO 3: Configuro el trabajo en el DataSet (map+map+map+map+map...+map...reduce)
- PASO 4: Acabo... con lo que sea
- PASO 5: Cierro conexión con el cluster de Spark.

---

Queremos montar el sistema de trending topics de Twitter

Vamos a recibir un montón de textos.... podemos empezar con 3
Y queremos mirar esos textos y quedarnos con las palabras que comienzan por #
Y contabilizarlas!

Se acabó !
"En la piscina,#goodVibes#SummerLove"
"En el trabajo.#goodVibes(#MierdaDeVerano)"
"Dando un paseo al perro-#GooDVIBES"

Pero sin Spark... trabajando con Java pelao y Streams.... Cuando lo tengamos lo llevamos a Spark

#goodVibes 3
#SummerLove 1
#MierdaDeVerano 1

1º Filtramos los textos que contengan #
2º Reemplazo "#" por " #"
3º Split por qué carácter: cualquier carácter que no sea un carácter típico de palabra ni #

En
la
piscina
#goodVibes
#SummerLove

4º Me quedo solo con las que empiezan por #
5º normalizar case
6º Cuento de alguna forma 