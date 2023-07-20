
# Broadcast vs Acumulador

Son conceptos antagónicos.

Acumulador:
    Variable que distribuyo a todos los nodos... y todos los nodos pueden escribir... aunque ninguno puede leer
    ... más concretamente, si la leen, cada uno tiene su valor independiente.
    Aunque todos pueden modificar la variable

Broadcast:
    Variable que distribuyo a todos los nodos... y todos los nodos pueden leer... aunque ninguno puede modificar
    ... más concretamente, si la modifican, nadie más que ellos se entera del cambio.
    Aunque todos comparten el valor de la variable