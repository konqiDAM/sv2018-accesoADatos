// Cesar Martin Sogorb + Moisés Encinas

import java.util.Scanner;
import java.util.logging.Level;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.Format;
import java.util.Date;

public class ConexionMongo {

    private static void menu() {
        System.out.println("MENU");
        System.out.println("1.- Ver curso por codigo");
        System.out.println("0.- SALIR");
    }
    
    private static void verCursos() {
        Scanner scn = new Scanner(System.in);
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        MongoClient cliente = new MongoClient();
        MongoDatabase db = cliente.getDatabase("cursos");
        MongoCollection<Document> coleccion = db.getCollection("cursos");

        System.out.print("ID del curso que desea ver: ");
        int id = scn.nextInt();
        scn.nextLine();
        if(coleccion.count(new Document("codigo", id)) == 0) {
            System.out.println("No encontrado");
        }
        else {
            for(Document doc : coleccion.find(new Document("codigo", id))){
                System.out.println("ID: " + doc.getDouble("codigo"));
                System.out.println("Nombre: " + doc.getString("nombre"));
                
                System.out.print("Fecha Inicio: ");
                Format formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date fecha = (Date)doc.get("comienzo");
                System.out.println(formatter.format(fecha));

                System.out.println("Contenidos:");
                List<Document> contenidos = (List<Document>) doc.get("contenidos");
                for (Document cont : contenidos) {
                    System.out.println("- " + cont.getString("contenido"));
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String opcion;
        boolean salir = false;
        
        do {
            menu();
            opcion = sc.nextLine();
            switch(opcion) {
                case"1":
                    verCursos();
                    break;
                case "0":
                    salir = true;
                    break;
            }
        } while (!salir);

    }
}
