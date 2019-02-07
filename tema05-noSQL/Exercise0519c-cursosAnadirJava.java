import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class Exercise0519 {
    private static Scanner scn = new Scanner(System.in);

    private static String pedir(String mensaje) {
        System.out.print(mensaje + " ");
        return scn.nextLine();
    }

    private static void anyadir() {
        MongoClient mongoClient = null;
        int id = 0;

        try {
            mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("cursos");

            MongoCollection<Document> collection =
                    database.getCollection("cursos");

            Document curso = new Document();

            double codigo = Double.parseDouble(pedir("Código:"));
            String nombre = pedir("Nombre del curso:");

            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaString = pedir("Introduce la fecha(DD/MM/YYYY):");
            Date fecha = sf.parse(fechaString);

            curso.append("codigo", codigo);
            curso.append("nombre", nombre);
            curso.append("comienzo", fecha);

            ArrayList<Document> contenidos = new ArrayList<>();

            boolean salir = false;
            do {
                salir = pedir("¿Desea añadir un nuevo contenido?(s/n):")
                        .equalsIgnoreCase("n");

                if (!salir) {
                    Document contenido = new Document();

                    String nombreContenido = pedir("nombre del contenido:");

                    contenido.append("contenido", nombreContenido);
                    contenidos.add(contenido);
                }

            } while (!salir);

            for (Document doc : contenidos) {
                String descripcion = pedir(
                        "descripción de " + doc.getString("nombre") + ":");

                if (!descripcion.equals("")) {
                    doc.append("descripcion", descripcion);
                }
            }

            curso.append("contenidos", contenidos);
            collection.insertOne(curso);

            mongoClient.close();
        } catch (MongoCommandException e) {
            System.out.println(
                    "Se produjo un error ejecutando un comando de mongo");
        } catch (ParseException e) {
            System.out.println("Se produjo un error con la fecha");
        } finally {
            if (mongoClient != null)
                mongoClient.close();
        }
    }

    private static void buscar() {
        try {
            MongoClient cliente = new MongoClient();
            MongoDatabase db = cliente.getDatabase("cursos");
            MongoCollection<Document> coleccion = db.getCollection("cursos");

            System.out.println("codigo: " + doc.getDouble("codigo").intValue());

            if (coleccion.count(new Document("codigo", codigo)) == 0) {
                System.out.println("No encontrado");
            } else {
                for (Document doc : coleccion
                        .find(new Document("codigo", codigo))) {
                    System.out.println("codigo: " + doc.getDouble("codigo"));

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    System.out.println(
                            "Fecha: " + df.format(doc.getDate("comienzo")));

                    System.out.println("Contenidos");
                    List<Document> contenidos =
                            (List<Document>) doc.get("contenidos");
                    for (Document c : contenidos) {
                        System.out.println("nombre = " + c.getString("contenido"));
                    }

                }
            }
        } catch (MongoCommandException e) {
            System.out.println(
                    "Se produjo un error ejecutando un comando de mongo");
        }
    }

    private static String menu() {
        System.out.println("Menú");
        System.out.println("1.- Buscar curso.");
        System.out.println("2.- Añadir un curso.");
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
                buscar();
                break;
            case "2":
                anyadir();
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
