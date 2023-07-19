import lombok.Data;

import java.io.Serializable;

@Data
public class Persona implements Serializable {

    private String nombre;
    private String apellidos;
    private int edad;
    private String email;
    private String dni;
}
