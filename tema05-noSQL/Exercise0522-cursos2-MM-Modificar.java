package mongoCursos2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.util.JSON;

/**
 * 
 * Néstor Rosario Escolano
 * 14 / 02 / 2019
 *
 */
public class MongoCursos2 {
    private static Scanner scn = new Scanner(System.in);
    private static GestionMongo bd;
    
    private static final SimpleDateFormat FORMATO_FECHA = 
            new SimpleDateFormat("dd/MM/yyyy");
    
    private static String leerString(String mensaje) {
        
        System.out.print(mensaje);
        return scn.nextLine();
        
    }
    
    public static int leerInt(String mensaje) {
        
        String regex = "^-?\\d+$";
        String lectura;
        
        do {
            lectura = leerString(mensaje);
        } while (!lectura.matches(regex));
        
        return Integer.parseInt(lectura);
        
    }
    
    public static Date leerFecha(String mensaje) {
        
        String lectura;
        Date fecha;
        
        do {
            lectura = leerString(mensaje);
            fecha = fechaCorrecta(lectura);
        } while(fecha == null);
        
        return fecha;
        
    }
    
    public static Date fechaCorrecta(String lectura) {
        
        Date fecha;
        
        try {
            fecha = FORMATO_FECHA.parse(lectura);
        } 
        catch (ParseException pe) {
            System.out.println("Fecha incorrecta");
            fecha = null;
        }
        
        return fecha;
    }
    
    public static void menu() {
        
        System.out.println("1· Añadir curso");
        System.out.println("2· Asignar alumno a un curso");
        System.out.println("3· Ver alumnos de un curso");
        System.out.println("4· Ver cursos de un alumno");
        System.out.println("5· Modificar datos de curso");
        System.out.println("6· Modificar datos de alumno");
        System.out.println("S· Salir");
        
    }

    public static void main(String[] args) {
        
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        
        FORMATO_FECHA.setLenient(false);
        bd = new GestionMongo("localhost", 27017);
        
        boolean salir = false;
        String opcion;
        
        do {
            menu();
            opcion = leerString("Opción: ").toLowerCase();
            
            switch (opcion) {
            
                case "1":     
                    anyadirCurso();
                    break;
                    
                case "2":
                    asignarAlumnoACurso();
                    break;
                    
                case "3":
                    verAlumnosDeCurso();
                    break;
                    
                case "4":
                    verCursosDeAlumno();
                    break;
                    
                case "5":
                    modificarCurso();
                    break;
                    
                case "6":
                    modificarAlumno();
                    break;
                    
                case "s":
                    salir = true;
                    break;
            }
            
        } while (!salir);
        
    }
    
    private static void anyadirCurso() {
        
        int totalContenidos;
        String nombre;
        String contenido;
        Date fecha;
        List<String> contenidos = new ArrayList<>();
        
        nombre = leerString("Nombre: ");
        fecha = leerFecha("Fecha (formato DD/MM/YYYY): ");
        
        do {
            totalContenidos = leerInt("Nº de contenidos: ");
        } while (totalContenidos < 0);
        
        for(int i=0; i<totalContenidos; i++) {
            contenido = leerString("Contenido: ");
            contenidos.add(contenido);
        }
        
        if(bd.anyadirCurso(nombre, fecha, contenidos)) {
            System.out.println("¡Curso creado!");
        }
        else {
            System.out.println("Error al crear curso");
        }
        
    }
    
    private static void asignarAlumnoACurso() {
        
        int codigoAlumno, codigoCurso;
        String nombre;
        
        codigoCurso = leerInt("Código del curso: ");
        
        if(bd.cursoExiste(codigoCurso)) {
            codigoAlumno = leerInt("Código del alumno: ");
            nombre = leerString("Nombre del alumno: ");
            
            if(bd.anyadirAlumnoACurso(codigoAlumno, nombre, codigoCurso)) {
                System.out.println("¡Alumno creado!");
            }
            else {
                System.out.println("Error al asignar alumno");
            }
        }
        else {
            System.out.println("Código de curso incorrecto");
        }
        
    }
    
    private static void verAlumnosDeCurso() {
        
        int codigoCurso;
        
        codigoCurso = leerInt("Código del curso: ");
        
        if(bd.cursoExiste(codigoCurso)) {
            bd.mostrarAlumnosDeCurso(codigoCurso);
        }
        else {
            System.out.println("El curso no existe");
        }
        
    }
    
    private static void verCursosDeAlumno() {
        
        int codigoAlumno;
        
        codigoAlumno = leerInt("Código del alumno: ");
        
        bd.mostrarCursosDeAlumno(codigoAlumno);
        
    }
    
    private static void modificarCurso() {
        
        int codigoCurso;
        String nombre, fechaCadena;
        Date fecha;
        List<String> contenidos = new ArrayList<>();
        Document curso = null;
        
        codigoCurso = leerInt("Código del curso: ");
            
        curso = bd.cursoModificable(codigoCurso);
        
        if(curso != null) {
            
            nombre = leerString("Nombre: ");
            if(!nombre.trim().isEmpty()) {
                curso.replace("nombre", nombre);
            }
            
            fechaCadena = leerString("Fecha (formato DD/MM/YYYY): ");
            
            if(!fechaCadena.trim().isEmpty()) {
                try {
                    fecha = FORMATO_FECHA.parse(fechaCadena);
                    curso.replace("fechaInicio", fecha);
                }
                catch (ParseException pe) {
                    System.out.println("Fecha incorrecta. Se mantiene la anterior");
                }
            }
            
            if(bd.actualizarCurso(curso)) {
                System.out.println("¡Curso actualizado!");
            }
            else {
                System.out.println("Error al actualizar el curso");
            }
            
        }
        else {
            System.out.println("ID de curso incorrecto");
        }
        
    }
    
    private static void modificarAlumno() {
        
        int codigoAlumno;
        String nombre;
        
        codigoAlumno = leerInt("Código del alumno: ");
        nombre = leerString("Nuevo nombre del alumno: ");
        
        if(bd.actualizarAlumno(codigoAlumno, nombre)) {
            System.out.println("¡Alumno actualizado!");
        }
        else {
            System.out.println("Error al actualizar datos");
        }
        
        
    }
}

    // ---------------------
class GestionMongo {    
     private static SimpleDateFormat formatoFecha = 
                new SimpleDateFormat("dd/MM/yyyy");
        
        private MongoClient cliente;
        private MongoDatabase bd;
        private String host;
        private int puerto;
        
        public GestionMongo(String host, int port) {
            
            this.host = host;
            this.puerto = port;
            
        }
        
        public int proximoId(MongoCollection<Document> coleccionMongo) {
            Document ultimaTarea = coleccionMongo.find()
                    .sort(Sorts.orderBy(Sorts.descending("codigo")))
                    .first();
            
            if(ultimaTarea != null) {
                return ultimaTarea.getInteger("codigo") + 1;
            }
            else {
                return 1;
            }
        }
        
        public boolean anyadirCurso(String nombre, Date fecha,
                List<String> contenidos) {
            
            boolean datosGuardados = false;
            List<Document> listaContenidos = null;
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = 
                        bd.getCollection("cursos");
                
                Document curso = new Document();
                curso.append("codigo", proximoId(coleccionCursos));
                curso.append("nombre", nombre);
                curso.append("fechaInicio", fecha);
                
                if(contenidos.size() > 0) {
                    listaContenidos = new ArrayList<>();
                    
                    for(String contenido : contenidos) {
                        Document cont = new Document();
                        cont.append("nombre", contenido);
                        listaContenidos.add(cont);
                    }
                    
                    curso.append("contenidos", listaContenidos);
                }
                
                
                coleccionCursos.insertOne(curso);
                
                datosGuardados = true;
            } 
            catch (Exception e) {
                datosGuardados = false;
            }
            finally {
                if(cliente != null)
                    cliente.close();
            }
            
            return datosGuardados;
            
        }
        
        public boolean anyadirAlumnoACurso(int codigoAlumno, String nombreAlumno,
                int codigoCurso) {
            
            boolean datosGuardados = false;
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = 
                        bd.getCollection("cursos");
                BasicDBObject filtroCurso = 
                        (BasicDBObject) JSON.parse("{codigo: " + codigoCurso + "}");
                
                Document alumno = new Document();
                alumno.append("codigo", codigoAlumno);
                alumno.append("nombre", nombreAlumno);
                
                coleccionCursos.updateOne(
                        Filters.eq("codigo", codigoCurso), 
                        Updates.addToSet("alumnos", alumno));
                
                datosGuardados = true;
            } 
            catch (MongoCommandException mce) {
                datosGuardados = false;
            }
            finally {
                if(cliente != null)
                    cliente.close();
            }
            
            return datosGuardados;
        }
        
        public boolean cursoExiste(int codigo) {
            
            boolean cursoExiste = false;
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = bd
                        .getCollection("cursos");
                BasicDBObject filtro = 
                        (BasicDBObject) JSON.parse("{codigo: " + codigo + "}");
                
                if(coleccionCursos.count(filtro) > 0) {
                    cursoExiste = true;
                }
            } 
            catch (MongoCommandException mce) {
                cursoExiste = false;
            }
            finally {
                if(cliente != null)
                    cliente.close();
            }
            
            return cursoExiste;
            
        }
        
        public void mostrarAlumnosDeCurso(int codigoCurso) {
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = bd
                        .getCollection("cursos");
                BasicDBObject filtroCurso = 
                        (BasicDBObject) JSON.parse("{codigo: " + codigoCurso + "}");
                    
                Document curso = coleccionCursos.find(filtroCurso).first();
                System.out.println("Alumnos del curso '" + 
                        curso.getString("nombre") + "':");
                List<Document> alumnos = (List<Document>) curso.get("alumnos");
                
                if(alumnos != null && alumnos.size() > 0) {
                    for(Document alumno : alumnos) {
                        System.out.println("\t" + alumno.getString("nombre"));
                    }
                }
                else {
                    System.out.println("\t(Sin alumnos)");
                }
            } 
            catch (MongoCommandException mce) {
                System.out.println("Error al leer datos");
            }
            finally {
                if(cliente != null)
                    cliente.close();
            }
            
        }

        
        public void mostrarCursosDeAlumno(int codigoAlumno) {
            
            boolean alumnoEnCurso = false;
            String nombreAlumno = "";
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = bd
                        .getCollection("cursos");
                BasicDBObject filtroAlumno = 
                        (BasicDBObject) JSON.parse("{alumnos.codigo: " + codigoAlumno + "}");
                for(Document curso : coleccionCursos.find(filtroAlumno)) {
                    
                    System.out.println("Nombre: " + curso.getString("nombre"));
                    System.out.println("Fecha de inicio: " 
                            + formatoFecha.format(curso.getDate("fechaInicio")));
                    
                    List<Document> contenidos = 
                            (List<Document>) curso.get("contenidos");
                    System.out.println("Contenidos: ");
                    for(Document contenido : contenidos) {
                        System.out.println("\t" + contenido.getString("nombre"));
                    }
                    
                    System.out.println();
                }
            } 
            catch (MongoCommandException mce) {
                System.out.println("Error al leer datos");
            }
        }
        
        
        public Document cursoModificable(int codigo) {
            
            Document curso = null;
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = bd
                        .getCollection("cursos");
                BasicDBObject filtroCurso = 
                        (BasicDBObject) JSON.parse("{codigo: " + codigo + "}");
                
                curso = coleccionCursos.find(filtroCurso).first();
            } 
            catch (MongoCommandException mce) {
                curso = null;
            }
            finally {
                if(cliente != null)
                    cliente.close();
            }
            
            return curso;
        }
        
        public boolean actualizarCurso(Document curso) {
            
            boolean datosActualizados = false;
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = bd
                        .getCollection("cursos");
                
                coleccionCursos.updateOne(
                        Filters.eq("codigo", curso.getInteger("codigo")), 
                        Updates.combine(
                                Updates.set("nombre", curso.getString("nombre")),
                                Updates.set("fechaInicio", curso.getDate("fechaInicio"))));
                
                datosActualizados = true;
            } 
            catch (MongoCommandException mce) {
                datosActualizados = false;
            }
            
            return datosActualizados;
        }
        
        public boolean actualizarAlumno(int codigoAlumno, String nuevoNombre) {
            
            boolean datosActualizados = false;
            
            try {
                cliente = new MongoClient(host, puerto);
                bd = cliente.getDatabase("cursos2");
                MongoCollection<Document> coleccionCursos = bd
                        .getCollection("cursos");
                
                coleccionCursos.updateMany(
                        Filters.eq("alumnos.codigo", codigoAlumno), 
                        Updates.set("alumnos.$.nombre", nuevoNombre));
                
                datosActualizados = true;
            } 
            catch (Exception e) {
                datosActualizados = false;
                System.out.println(e.getMessage());
            }
            
            return datosActualizados;
            
        }
}
