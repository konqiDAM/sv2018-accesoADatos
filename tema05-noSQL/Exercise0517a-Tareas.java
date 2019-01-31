// Cesar Martin Sogorb

package mongoTareas;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoTareas {
	   
    private static void showMenu() {
        System.out.println("MENU");
        System.out.println("1.- AÑADIR TAREA");
        System.out.println("2.- BUSCAR TAREA");
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

            id = (int) collection.count() + 1;
            System.out.println("Introduce la fecha: (YYYY-MM-DD)");
            String fecha = sc.nextLine();
            System.out.println("Introduce los detalles: ");
            String nombre = sc.nextLine();
            
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
            System.out.println("Introduce los detalles: ");
            String nombre = sc.nextLine();
            
            Document tarea = new Document();            
            collection.updateOne(Filters.eq("id", id), Updates.set("nombre", nombre));            
            System.out.println("Tarea actualizada!");
            
            mongoClient.close();
        } catch (MongoCommandException e) {
            System.err.println(
                    "Se produjo un error ejecutando un comando de mongo");
        }
    }
    
    private static void buscarTarea() {
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
            showMenu();
            opcion = sc.nextLine();
            switch(opcion) {
            case"1":
                anyadirTarea();
                break;
            case"2":
                buscarTarea();
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
