# BigData

Conjunto de técnicas que utilizamos cuando las técnicas tradicionales que hemos venido usando décadas
ya no me funcionan a la hora de trabajar con datos.

Quiero llevar un listado de las cosas que voy a comprar al mercadona! Excel
Y si quiero comprar 100k cosas... que dice el Excel?  Empieza a sudar!
Pues al Access.... Y si tengo 2.000.000 -... que dice el Access? Empieza a sudar !
Sin problema... me voy al MariaDB... y si tengo 10M de datos? Empieza a sudar!
Pero tengo el MS SQL Server... que no tenga 50M... que empieza a sudar!
Siempre me quedará OracleDB... que no tengo 1000kM de datos.. que me la cargo!
Y ahora qué?

Tengo una película que me creado yo, responsablemente.. que ocupa 5Gbs... y tengo un USB de 16Gbs vacio...
Me entra? No necesariamente ... Dependerá del formato de archivos.
FAT16, FAT32... 2Gbs
Siempre me quedará NTFS....
A no ser que tenga un archivo de 16eB... NTFS... se hace popo!

ClashRoyale... 2v2
1 jugador en 1 seg puede facilmente hacer 2 movimientos -> viajar -> 3 telefonos de los openentes/compañero.
De cada movimiento acaban saliendo 3 mensajes.
1 seg/2 movimientos... 6 mensajes/segundo (mios)... pero somos 4 jugando = 24 mensajes /segundo / partida
50k partidas = 1.2M de mensajes / segundo... Qué máquina contrato que permita gestionar esa cantidad de mensajes?

Llega un momento en el cual, bien debido a:
- El volumen de información que estoy generando
- El ritmo de generación de información y su ventana de oportunidad
- Complejidad de la información (video, foto, audio)
  las técnicas tradicionales (y herramientas tradicionales) no me solucionan el problema de:
- Almacenar datos
- Analizar los datos
- Transmitir datos
- Procesar datos

El mundo del BigData, no tiene que ver con el ANALISIS DE DATOS.
Tiene que ver con el empleo de clusters de commodity Hardware (Maquinas de mierda) trabajando como si fueran uno solo.

Todo arranca con la gente de Google.. cuando empiezan a scrappear la web.... y empiezan a tener cantidades ingentes de datos ! Esta gente saca un paper llamado BigTable.

Como una implementación de ese BigTable de Google, sale en un momento dado HADOOP.

Qué es Hadoop? (más allá de un proyecto de la fundación Apache)

Básicamente es el equivalente a un SO distribuido, que permite controlar un cluster de máquinas y aprovechar (usar) sus recursos (CPU, RAM, HDD) como si fueran uno solo:
- Implementación MAP-REDUCE: Patrón de diseño de aplicaciones, pensado para el tratamiento paralelizado de información
- HDFS: Un sistema de archivos distribuido, pensado para almacenar fichero grandes,... pero grandes grandes... del orden de exabytes. (por defecto el tamaño de partición de un archivo era 64Mbs)
  Cada trozo de archivo se guarda en al menos 3 equipos diferentes:
    - HA
    - Escalabilidad

HDD 160 Mb/s
SSD 650 Mb/s            << quien me limita ahora? Velocidad de la RED: 10Gbit/seg: 150M/s
NVME 2500 Mb/s          << quien me limita ahora?

Al final el SO para que me vale?  Para poco...
Pero encima de eso puedo instalar más programas... programas que me aportarán valor... en tanto y cuanto
me permitan hacer las gestiones que necesito con unos datos!

En el mundo BigData hay muchos programas que operan sobre Hadoop:
- BBDD: Mongo, Hive, Cassandra, HBase
- Sistemas de mensajería: Kafka
- Frameworks de procesamiento de datos: Spark, Storm...

# Qué es Spark?

Framework para el procesamiento de datos, sobre una infra Bigdata: Un conjunto de maquinitas de mierda que tienen instalado Hadoop.

Básicamente, Spark me ofrece una implementación alternativa (y mucho más eficiente) al Map-reduce de hadoop.
Ya que en la implementación de hadoop, cada operación de entrada/salida a un computador del cñluster se persiste a HDD... Mientras que en Spark queda en RAM... los datos viajan de RAM a RAM

-------------- y quiero hacer sobre ellos distintas transformaciones (procesarlos)
Dato 1
Dato 2
Dato 3
Dato 4
...
Dato 10000
--------------
Fecha de nacimiento > Años > Mirar si son más de 18     Quiero saber si una persona nacida ese día es mayor de edad
10-10-1979  -> 34,8 años   > Mayor de edad? SI
10-11-2002  -> 21,4 años   > Mayor de edad? SI
10-07-2008  -> 15 años     > Mayor de edad? NO
....
Para acelerar ese proceso podría optar por 2 vías:
- Repartir los datos:                                               SPARK
  Nodo 1: dato 1...1000
  Nodo 2: dato 1001...2000
  ...
- Repartir las tareas:                                              STORM
    - Nodo 1 le pongo a calcular edades
    - Nodo 2 le pongo a mirar si las edades son mayores de 18

200 sensores x 2 medidas = 400 medidas / 5 minutos: 12 : 4800 medidas /hora x 24

---

Scala: Es un lenguaje de programación... que ofrece una sintaxis alternativa a JAVA para generar ficheros
.class, que corren sobre la máquina virtual de Java

No es el único en este sentido

Kotlin: Es un lenguaje de programación... que ofrece una sintaxis alternativa a JAVA para generar ficheros
.class, que corren sobre la máquina virtual de Java

Y por qué? Joder... es que JAVA está muy mal pensado... vaya kk de gramática que tiene!!!!

Java es un lenguaje compilado o interpretado? Las 2 cosas a la vez!

---

En JAVA 1.8, se añade el soporte para programación funcional:

Paradigmas de programación:
- Imperativo:           Le damos a la computadora instrucciones que debe ejecutar de forma secuencial.
  En ocasiones me interesa romper esa secuencialidad... y hacemos uso de
  distintas palabras que tenemos en estos lenguajes a tal efecto:
  if, else, for, while, switch
- Procedural:           Cuando el lenguaje me permite definir mis propias funciones/procedimientos/métodos
  y solicitar posteriormente su ejecución.
- Funcional:            Cuando el lenguaje me permite que una variable apunte a una función.
  Y posteriormente ejecutar esa función desde la variable.
  La cuestión no es el concepto... que es bien sencillo...
  El tema es lo que puedo hacer con eso:
  - Puedo crear funciones que acepten otras funciones como argumentos
  - Puedo crear funciones que devuelvan funciones.
- Orientado a Objetos:  Cuando el lenguaje me permite definir mis propios tipos de datos
  con sus propiedades y funciones particulares.

                        String:         secuencia de caracteres             toUpperCase() .trim()
                        ZonedDateTime:  año, mes, dia, hora, minuto, milisegundos, segundos
                                                                            formar, si el año es bisiesto
                                                                            en qué día de la semana cae
                        List
                        Map
                        Usuario         nombre, id, edad, apellidos...      esMayorDeEdad()


Es una forma de escribir (traducir) las instrucciones que queremos darle a la computadora...


---

Spark

Lo que vamos a montar es un cluster de máquinas que tendrán instalado Hadoop + Spark.

Yo tendré mi maquina desde donde solicito la ejecución de un programa
Tendré un conjunto de máquinas TRABAJADORAS de Spark
Tendré un maestro en el cluster de Spark

                                    Cluster de Spark
                        |--------------------------------------------------------------------------|

MI MAQUINA          >    Nodo Maestro      >    Nodo 1      20000 -> Cuenta ( a nivel del nodo )
(1000000)        >    Nodo 2      20000
sumará las      >    Nodo 3      20000
<      cantidades de   >    Nodo...     20000
cada nodo       >    Nodo 50     20000
_________
1000000

Para mandar un DATO por RED necesito que el DATO sea Serializable