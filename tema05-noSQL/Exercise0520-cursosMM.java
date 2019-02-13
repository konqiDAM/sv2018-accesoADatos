import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * @author Jorge Calabuig Bartual <IES. SAN VICENTE - 2º DAM>
 *
 */
public class Exercise0520 {
    private static Scanner scn = new Scanner(System.in);

    private static String pedir(String mensaje) {
        System.out.print(mensaje + " ");
        return scn.nextLine();
    }

    private static Document getAlumno() {
        Document alumno = new Document();

        double codigo = Double.parseDouble(pedir("Código:"));
        String nombre = pedir("Nombre del alumno:");
        alumno.append("codigo", codigo);
        alumno.append("nombre", nombre);

        return alumno;
    }

    private static void anyadirAlumno() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("cursos");

            MongoCollection<Document> collection =
                    database.getCollection("alumnos");

            Document alumno = getAlumno();

            collection.insertOne(alumno);

            mongoClient.close();
        } catch (MongoCommandException e) {
            System.out.println(
                    "Se produjo un error ejecutando un comando de mongo");
        } finally {
            if (mongoClient != null)
                mongoClient.close();
        }
    }

    private static void anyadirAsistencia() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("cursos");

            MongoCollection<Document> collection =
                    database.getCollection("asistir");

            Document asistencia = new Document();

            Double curso = Double.parseDouble(pedir("ID Curso:"));
            Double alumno = Double.parseDouble(pedir("ID Alumno:"));

            asistencia.append("curso", curso);
            asistencia.append("alumno", alumno);

            collection.insertOne(asistencia);

            mongoClient.close();
        } catch (MongoCommandException e) {
            System.out.println(
                    "Se produjo un error ejecutando un comando de mongo");
        } finally {
            if (mongoClient != null)
                mongoClient.close();
        }
    }

    private static void verAlumnosCurso() {
        Scanner scn = new Scanner(System.in);
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoClient cliente = new MongoClient();
        MongoDatabase db = cliente.getDatabase("cursos");
        MongoCollection<Document> coleccion = db.getCollection("asistir");
        MongoCollection<Document> coleccionAlu = db.getCollection("alumnos");

        double codigo = Double.parseDouble(pedir("ID curso"));

        if (coleccion.count(new Document("curso", codigo)) == 0) {
            System.out.println("No encontrado");
        } else {
            
            for (Document detallesCurso : coleccion
                    .find(new Document("curso", codigo))) {

                Document alumno = coleccionAlu
                        .find(new Document("codigo", detallesCurso.getDouble("alumno")))
                        .first();
                System.out.println(alumno.getString("nombre"));
            }
        }
    }
    
    
    private static void verCursosAlumno() {
        Scanner scn = new Scanner(System.in);
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoClient cliente = new MongoClient();
        MongoDatabase db = cliente.getDatabase("cursos");
        MongoCollection<Document> asistir = db.getCollection("asistir");
        MongoCollection<Document> cursos = db.getCollection("cursos");

        double codigo = Double.parseDouble(pedir("ID alumno"));

        if (asistir.count(new Document("alumno", codigo)) == 0) {
            System.out.println("No encontrado");
        } else {

            for (Document asistencia : asistir
                    .find(new Document("alumno", codigo))) {

                Document curso = cursos
                        .find(new Document("codigo", asistencia.getDouble("curso")))
                        .first();
                System.out.println(curso.getString("nombre"));
            }
        }
    }

    private static String menu() {
        System.out.println("Menú");
        System.out.println("1.- Añadir un alumno.");
        System.out.println("2.- Asignar un alumno a un curso.");
        System.out.println("3.- Ver los alumnos de un curso.");
        System.out.println("4.- Ver los cursos de un curso.");
        System.out.println("0.- Salir.");
        return pedir("Opción:");
    }

    public static void main(String[] args) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        boolean salir = false;
        String opcion;
        do {
            opcion = menu();

            switch (opcion) {
            case "1":
                anyadirAlumno();
                break;
            case "2":
                anyadirAsistencia();
                break;
            case "3":
                verAlumnosCurso();
                break;
            case "4":
                verCursosAlumno();
                break;
            case "0":
                salir = true;
                break;
            default:
                System.out.println("Opción incorrecta");
                break;
            }

        } while (!salir);
    }

}

/*
 * // JORGE CALABUIG BARTUAL

use cursos;

use cursos
db.cursos.save([
    {
        nombre: 'Monogame',
        codigo: 1, 
        comienzo: new Date('2019-02-07'), 
        contenidos: [
            {contenido: 'Origen: XNA'},
            {contenido: 'El bucle de juego'}, 
            {contenido: 'Assets'}, 
            {contenido: 'Acciones del usuario'}, 
            {contenido: 'Varias pantallas'}
            ]
    },
    {
        nombre: 'Monogame 2',
        codigo: 2, 
        comienzo: new Date('2019-02-07'), 
        contenidos: [
            {contenido: 'Desenlace: XNA'}
        ]
    }
]
);

db.alumnos.save(
[
    {
        codigo: 1,
        nombre: "Pablo Gutierrez"
    },
    {
        codigo: 2,
        nombre: "Marga Garcia"
    },
]
);

db.asistir.save( 
[
    {
        curso:1,
        alumno:1
    },
    {
        curso:1,
        alumno:2
    }
]);
 * */
