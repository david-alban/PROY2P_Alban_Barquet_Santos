package Modelo;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

import Excepciones.DatosIncompletosException;

/**
 * Clase encargada de gestionar la persistencia de datos en el almacenamiento interno de la aplicación.
 * Proporciona métodos para leer y escribir archivos de texto, así como para serializar y deserializar objetos.
 */

public class ManejadorArchivos implements Serializable {

    /**
     * Lee las líneas de un archivo de texto desde el almacenamiento interno.
     * Si el archivo no existe, intenta copiarlo desde la carpeta de 'assets' antes de leerlo.
     * Omite la primera línea del archivo, asumiendo que es un encabezado.
     *
     * @param context       Contexto de la aplicación utilizado para acceder a los archivos.
     * @param nombreArchivo Nombre del archivo de texto a leer.
     * @return Una lista de cadenas con las líneas del archivo.
     */

    public ArrayList<String> leerLineas(Context context, String nombreArchivo) {
        ArrayList<String> lineas = new ArrayList<>();
        File archivoInterno = new File(context.getFilesDir(), nombreArchivo);

        if (!archivoInterno.exists()) {
            try (InputStream entrada = context.getAssets().open(nombreArchivo); OutputStream salida = context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE)) {
                byte[] buffer = new byte[1024];
                int cantidadBytes;
                while ((cantidadBytes = entrada.read(buffer)) != -1) {
                    salida.write(buffer, 0, cantidadBytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return lineas;
            }
        }

        try (FileInputStream fis = new FileInputStream(archivoInterno); BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            //Saltar el encabezado
            String linea = reader.readLine();
            //Recorrer las lineas
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineas;
    }

    /**
     * Carga y procesa la información de todos los usuarios registrados en el sistema.
     * Asocia los puntajes para los participantes y los cargos para los administradores.
     *
     * @param context Contexto de la aplicación.
     * @return Una lista de objetos {@link Usuario} instanciados según su tipo.
     */

    public ArrayList<Usuario> leerUsuarios(Context context) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        ArrayList<String> lineas = leerLineas(context, "usuarios.txt");

        ArrayList<String> lineasParticipantes = leerParticipantes(context);
        ArrayList<String> lineasAdmins = leerAdmins(context);

        if (lineas == null || lineas.isEmpty()) {
            return usuarios;
        }

        int inicio = 0;
        if (lineas.get(0).toLowerCase().startsWith("idusuario")) {
            inicio = 1;
        }

        for (int index = inicio; index < lineas.size(); index++) {
            String linea = lineas.get(index);

            if (linea == null || linea.trim().isEmpty()) {
                continue;
            }

            String[] datos = linea.split(";");

            if (datos.length >= 5) {
                String idUsuario = datos[0].trim();
                String nombreUsuario = datos[1].trim();
                String contrasena = datos[2].trim();
                String nombreCompleto = datos[3].trim();

                Modelo.TipoUsuario tipoUsuario;
                try {
                    tipoUsuario = Modelo.TipoUsuario.valueOf(datos[4].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    continue;
                }

                Usuario usuario;

                if (tipoUsuario == TipoUsuario.PARTICIPANTE) {
                    int puntos = 0;
                    boolean encontrado = false;
                    int i = 0;

                    if (lineasParticipantes != null) {
                        while (i < lineasParticipantes.size() && !encontrado) {
                            String line = lineasParticipantes.get(i);
                            if (line != null && !line.trim().isEmpty()) {
                                String[] partes = line.split(";");

                                if (partes.length >= 2) {
                                    String idEnArchivo = partes[0].trim();

                                    if (idEnArchivo.equals(idUsuario)) {
                                        try {
                                            puntos = Integer.parseInt(partes[1].trim());
                                        } catch (NumberFormatException e) {
                                            puntos = 0;
                                        }
                                        encontrado = true;
                                    }
                                }
                            }
                            i++;
                        }
                    }

                    Participante participante = new Participante(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
                    participante.setPuntajeAcumulado(puntos);
                    usuario = participante;

                } else {
                    String cargo = "Sin Cargo";
                    boolean encontrado = false;
                    int i = 0;

                    if (lineasAdmins != null) {
                        while (i < lineasAdmins.size() && !encontrado) {
                            String line = lineasAdmins.get(i);
                            if (line != null && !line.trim().isEmpty()) {
                                String[] partes = line.split(";");

                                if (partes.length >= 2) {
                                    String idEnArchivo = partes[0].trim();

                                    if (idEnArchivo.equals(idUsuario)) {
                                        cargo = partes[1].trim();
                                        encontrado = true;
                                    }
                                }
                            }
                            i++;
                        }
                    }

                    Administrador admin = new Administrador(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
                    admin.setCargo(cargo);
                    usuario = admin;
                }

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    /**
     * Lee el archivo de partidos y construye una lista de objetos {@link Partido}.
     *
     * @param context Contexto de la aplicación.
     * @return Una lista de partidos programados.
     */

    public ArrayList<Partido> leerPartidos(Context context) {
        ArrayList<Partido> partidos = new ArrayList<>();
        ArrayList<String> lineas = leerLineas(context, "partidos.txt");

        for (String linea : lineas) {
            String[] datos = linea.split(";");
            String id = datos[0];
            String fase = datos[1];
            String fecha = datos[2];
            String horaUTC = datos[3];
            String estadio = datos[4];
            String seleccion1 = datos[5];
            String seleccion2 = datos[6];
            EstadoPartido estado = EstadoPartido.valueOf(datos[7]);

            Partido partido = new Partido(id, fase, fecha, horaUTC, estadio, estado, seleccion1, seleccion2);
            partidos.add(partido);
        }
        return partidos;
    }

    /**
     * Lee las líneas correspondientes a los participantes registrados.
     *
     * @param context Contexto de la aplicación.
     * @return Lista de cadenas con los datos de los participantes.
     */

    public ArrayList<String> leerParticipantes(Context context) {
        ArrayList<String> resultado;
        resultado = leerLineas(context, "participantes.txt");
        return resultado;
    }

    /**
     * Lee las líneas correspondientes a los administradores registrados.
     *
     * @param context Contexto de la aplicación.
     * @return Lista de cadenas con los datos de los administradores.
     */

    public ArrayList<String> leerAdmins(Context context) {
        ArrayList<String> resultado;
        resultado = leerLineas(context, "administradores.txt");
        return resultado;
    }

    /**
     * Lee los resultados oficiales guardados y actualiza la lista de partidos con los goles registrados.
     * Muestra un mensaje de error si los datos de un resultado están incompletos.
     *
     * @param context  Contexto de la aplicación.
     * @param partidos Lista de partidos actualmente cargados en memoria.
     * @return Lista de cadenas extraídas del archivo de resultados.
     */

    public ArrayList<String> leerResultados(Context context, ArrayList<Partido> partidos) {

        ArrayList<String> resultado;
        resultado = leerLineas(context, "resultados.txt");
        if (partidos == null) {
            return resultado;
        }
        for (String linea : resultado) {
            String datos[] = linea.trim().split(";");

            if (datos.length == 4) {
                try {

                    String idPartido = datos[1];
                    int goles1 = Integer.parseInt(datos[2]);
                    int goles2 = Integer.parseInt(datos[3]);

                    for (Partido partido : partidos) {
                        if (partido.getId().equals(idPartido)) {
                            partido.registrarResultadoOficial(goles1, goles2);
                            break;
                        }
                    }


                } catch (DatosIncompletosException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }
        return resultado;
    }

    /**
     * Deserializa y consolida todos los pronósticos realizados por un usuario en específico
     * a través de todas las fases del torneo.
     *
     * @param context   Contexto de la aplicación.
     * @param idUsuario Identificador único del participante.
     * @return Lista con todos los pronósticos creados por el usuario.
     */

    public ArrayList<Pronostico> leerTodosLosPronosticos(Context context, String idUsuario) {
        ArrayList<Pronostico> todosLosPronosticos = new ArrayList<>();

        String[] fases = {"fase_de_grupos", "dieciseisavos_de_final", "octavos_de_final", "cuartos_de_final", "semifinales", "tercer_lugar", "final"};

        for (String fase : fases) {
            String nombreArchivo = "pronostico_" + idUsuario + "_" + fase + ".dat";

            try {
                Object obj = deserializar(context, nombreArchivo);
                if (obj instanceof ArrayList<?>) {
                    ArrayList<Pronostico> pronosticosFase = (ArrayList<Pronostico>) obj;
                    todosLosPronosticos.addAll(pronosticosFase);
                }
            } catch (FileNotFoundException e) {
                // como el archivo en este punto no ha sido creado no se realiza ninguna accion
            } catch (IOException e) {
                Toast.makeText(context, "Error de lectura en: " + nombreArchivo, Toast.LENGTH_SHORT).show();
            }
        }

        return todosLosPronosticos;
    }

    /**
     * Escribe una serie de líneas en un archivo de texto en el almacenamiento interno.
     *
     * @param context       Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo de destino.
     * @param lineas        Lista de cadenas de texto a escribir.
     * @param append        true para añadir información al final del archivo, false para sobreescribirlo.
     * @return true si la escritura fue exitosa; false si ocurrió un error.
     */

    public boolean escribirLineas(Context context, String nombreArchivo, ArrayList<String> lineas, boolean append) {

        int modo;
        if (append) {
            modo = Context.MODE_APPEND;
        } else {
            modo = Context.MODE_PRIVATE;
        }

        try (FileOutputStream fos = context.openFileOutput(nombreArchivo, modo); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            for (int i = 0; i < lineas.size(); i++) {
                writer.write(lineas.get(i));
                if (i < lineas.size() - 1) {
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Guarda (serializa) un objeto en un archivo binario dentro del almacenamiento interno.
     *
     * @param context       Contexto de la aplicación.
     * @param objeto        Objeto serializable a guardar.
     * @param nombreArchivo Nombre del archivo de destino.
     * @return true si el objeto fue serializado correctamente; false en caso contrario.
     */

    public boolean serializar(Context context, Object objeto, String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE))) {
            oos.writeObject(objeto);
            return true;
        } catch (IOException e) {
            Toast.makeText(context, "Error al guardar el archivo: " + nombreArchivo, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lee y reconstruye (deserializa) un objeto almacenado previamente en un archivo binario.
     *
     * @param context       Contexto de la aplicación.
     * @param nombreArchivo Nombre del archivo que contiene los datos serializados.
     * @return El objeto recuperado desde el archivo.
     * @throws FileNotFoundException Si el archivo especificado no existe.
     * @throws IOException           Si ocurre un error durante el proceso de lectura.
     */

    public Object deserializar(Context context, String nombreArchivo) throws FileNotFoundException, IOException {
        Object salida = null;

        try (ObjectInputStream ois = new ObjectInputStream(context.openFileInput(nombreArchivo))) {
            salida = ois.readObject();
        } catch (ClassNotFoundException e) {
            Toast.makeText(context, "Error: Clase del objeto no encontrada", Toast.LENGTH_SHORT).show();
        }

        return salida;
    }

    /**
     * Actualiza el estado de un partido específico directamente en el archivo "partidos.txt".
     *
     * @param context           Contexto de la aplicación.
     * @param partidoModificado Instancia del partido con su información actualizada (ej. estado de ABIERTO a CERRADO).
     */

    public static void actualizarEstadoPartido(Context context, Partido partidoModificado) {
        ArrayList<String> lineasNuevas = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput("partidos.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String linea = reader.readLine();
            if (linea != null) {
                lineasNuevas.add(linea);
            }

            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                int idActual = Integer.parseInt(datos[0].trim());
                if (idActual == Integer.parseInt(partidoModificado.getId())) {
                    String nuevaLinea = partidoModificado.getId() + ";" + partidoModificado.getFase() + ";" + partidoModificado.getFecha() + ";" + partidoModificado.getHora() + ";" + partidoModificado.getEstadio() + ";" + partidoModificado.getSeleccion1() + ";" + partidoModificado.getSeleccion2() + ";" + partidoModificado.getEstado().toString();

                    lineasNuevas.add(nuevaLinea);
                } else {
                    lineasNuevas.add(linea);
                }
            }
            reader.close();

            FileOutputStream fos = context.openFileOutput("partidos.txt", Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fos);
            for (String l : lineasNuevas) {
                writer.println(l);
            }
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe y actualiza el archivo "participantes.txt" con los puntajes acumulados actuales
     * de los usuarios tipo Participante.
     *
     * @param context  Contexto de la aplicación.
     * @param usuarios Lista de usuarios del sistema.
     */

    public static void guardarPuntajesParticipantes(Context context, ArrayList<Usuario> usuarios) {

        try {
            FileOutputStream fos = context.openFileOutput("participantes.txt", MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(fos);
            writer.println("idUsuario;puntajeAcumulado");

            for (Usuario usuario : usuarios) {
                if (usuario instanceof Participante) {

                    Participante participante = (Participante) usuario;
                    writer.println(participante.getIdUsuario() + ";" + participante.getPuntajeAcumulado());
                }

            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Agrega un nuevo registro de resultado oficial para un partido al final del archivo "resultados.txt".
     *
     * @param context   Contexto de la aplicación.
     * @param idPartido Identificador numérico del partido concluido.
     * @param goles1    Goles anotados por la primera selección.
     * @param goles2    Goles anotados por la segunda selección.
     */

    public static void guardarResultadoOficial(Context context, int idPartido, int goles1, int goles2) {
        try {
            FileOutputStream fos = context.openFileOutput("resultados.txt", Context.MODE_APPEND);
            PrintWriter writer = new PrintWriter(fos);
            long idResultado = System.currentTimeMillis();
            String linea = idResultado + ";" + idPartido + ";" + goles1 + ";" + goles2;

            writer.println(linea);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
