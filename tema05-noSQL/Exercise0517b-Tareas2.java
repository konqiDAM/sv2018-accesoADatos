// Cesar Martin Sogorb, mejoras por Nacho

package mongoTareas;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoTareas {
       
    private static void mostrarMenu() {
        System.out.println("MENU");
        System.out.println("1.- AÑADIR TAREA");
        System.out.println("2.- BUSCAR TAREAS EN UNA FECHA");
        System.out.println("3.- MODIFICAR TAREA");
        System.out.println("0.- SALIR");
    }

    private static void anyadirTarea() {
        Scanner sc = new Scanner(System.in);
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoClient mongoClient = null;
        int id = 0;

        try {
            mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("tareas");

            MongoCollection<Document> collection =
                    database.getCollection("tareas");

            
            System.out.println("Introduce la fecha: (YYYY-MM-DD)");
            String fecha = sc.nextLine();
            System.out.println("Introduce los detalles: ");
            String nombre = sc.nextLine();
            
            // Peligroso "id = (int) collection.count() + 1;" si hay borrados
            Document ultimaTarea =
                    collection.find().sort(Sorts.orderBy(Sorts.ascending("id"))).
                    first();
            id = ultimaTarea.getInteger("id") + 1;
            System.out.println("Cantidad de datos: " + collection.count() );
            System.out.println("Guardando con id: " + id);
            
            Document tarea = new Document();
            
            tarea.append("id", id);
            tarea.append("fecha", fecha);
            tarea.append("nombre", nombre);
            
            collection.insertOne(tarea);
            
            mongoClient.close();
        } catch (MongoCommandException e) {
            System.err.println(
                    "Se produjo un error ejecutando un comando de mongo");
        }
    }
    
    private static void modificarTarea() {
        Scanner sc = new Scanner(System.in);
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoClient mongoClient = null;

        try {
            mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase database = mongoClient.getDatabase("tareas");

            MongoCollection<Document> collection =
                    database.getCollection("tareas");

            System.out.println("Introduce el id: ");
            int id = sc.nextInt();
            sc.nextLine();
            
            Document tarea = collection.find(new Document("id", id)).first();
            String antiguaFecha = tarea.getString("fecha"); 
            String antiguoNombre = tarea.getString("nombre");
            
            System.out.println("Introduce la nueva fecha (era "
                    + antiguaFecha +"): ");
            String fecha = sc.nextLine();
            if (fecha.equals(""))
                fecha = antiguaFecha;
            System.out.println("Introduce los detalles (eran "
                    +antiguoNombre+"): ");
            String nombre = sc.nextLine();
            if (nombre.equals(""))
                nombre = antiguoNombre;
            
            collection.updateOne(Filters.eq("id", id), 
                    Updates.combine(
                            Updates.set("nombre", nombre),
                            Updates.set("fecha", fecha)));    
            System.out.println("Tarea actualizada!");
            
            mongoClient.close();
        } catch (MongoCommandException e) {
            System.err.println(
                    "Se produjo un error ejecutando un comando de mongo");
        }
    }
    
    private static void buscarPorFecha() {
        Scanner scn = new Scanner(System.in);
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoClient cliente = new MongoClient();
        MongoDatabase db = cliente.getDatabase("tareas");
        MongoCollection<Document> coleccion = db.getCollection("tareas");

        System.out.println("Fecha a buscar: (YYYY-MM-DD)");
        String fecha = scn.nextLine();
        if(coleccion.count(new Document("fecha", fecha)) == 0) {
            System.out.println("No encontrado");
        }
        else {
            for(Document doc : coleccion.find(new Document("fecha", fecha))){
                System.out.println("ID: " + doc.getInteger("id"));
                System.out.println("Fecha: " + doc.getString("fecha"));
                System.out.println("Detalles: " + doc.getString("nombre"));
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String opcion;
        boolean salir = false;
        
        do {
            mostrarMenu();
            opcion = sc.nextLine();
            switch(opcion) {
            case"1":
                anyadirTarea();
                break;
            case"2":
                buscarPorFecha();
                break;
            case"3":
                modificarTarea();
                break;
            case "0":
                salir = true;
                break;
            }
        } while (!salir);
    }
}
