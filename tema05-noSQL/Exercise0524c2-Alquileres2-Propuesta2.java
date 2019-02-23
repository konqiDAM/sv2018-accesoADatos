package Principal;

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
import com.mongodb.client.model.Updates;
import com.mongodb.util.JSON;

//David Bohez Oscar
public class Main0524 {

    private static Scanner sc;
    private static Scanner sc2;
    private static Scanner sc3;
    
    public static String leerDatos() {
        String  info;
        sc = new Scanner(System.in);
        info = sc.nextLine();
        return info;
    }
    
    public static int leerDatosInt() {
        int info;
        sc2 = new Scanner(System.in);
        info = sc2.nextInt();
        sc2.nextLine();
        return info;
    }
    
    public static float leerDatosF() {
        float info;
        sc3 = new Scanner(System.in);
        info = sc3.nextFloat();
        sc3.nextLine();
        return info;
    }
    
    public static void anadirVehiculo() {
        
         MongoClient mongoClient = null;
         
         try {
             mongoClient = new MongoClient("localhost",27017);
             MongoDatabase database = mongoClient.getDatabase("taller2");
             MongoCollection<Document> collection = 
             database.getCollection("vehiculos");
             
             Document doc = new Document();
             
             System.out.println("matricula: ");
             String matricula=leerDatos();
             doc.append("matricula", matricula);
             System.out.println("marca: ");
             String marca=leerDatos();
             doc.append("marca", marca);
             System.out.println("modelo: ");
             String modelo=leerDatos();
             doc.append("modelo", modelo);
             System.out.println("grupo: ");
             String grupo=leerDatos();
             doc.append("grupo", grupo);
             System.out.println("precio: ");
             float precio=leerDatosF();
             doc.append("precio", precio);
             
             System.out.println("tiene carga(si/no): ");
             String c=leerDatos();
             if(c.equals("si")) {
                 
                 System.out.println("carga: ");
                 String carga=leerDatos();
                 doc.append("carga", carga);
             }
             collection.insertOne(doc);
         } 
         catch (Exception e) {}
         finally {
             mongoClient.close();
         }
        
        
    }
    
    public static void anadirAlquiler(String matricula) throws ParseException {
        
        MongoClient mongoClient = null;
        
         try {
             mongoClient = new MongoClient("localhost",27017);
             MongoDatabase database = mongoClient.getDatabase("taller2");
             MongoCollection<Document> collection = 
             database.getCollection("vehiculos");
             Document doc = new Document();
             
             
             System.out.println("Fecha salida: ");
             String fechaSalida=leerDatos();
             Date fecha = new Date();
             fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaSalida);
             doc.append("fechasalida", fecha);
             
             System.out.println("Fecha prevista: ");
             String fechaPrevista=leerDatos();
             Date fecha2 = new Date();
             fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaPrevista);
             doc.append("fechaprevista", fecha2);
             
             System.out.println("km iniciales: ");
             float km=leerDatosF();
             doc.append("kminiciales", km);
             
             System.out.println("DNI: ");
             String dni=leerDatos();
             doc.append("dni", dni);
             System.out.println("Nombre: ");
             String nombre=leerDatos();
             doc.append("nombre", nombre);
             System.out.println("Apellidos: ");
             String apellidos=leerDatos();
             doc.append("apellidos", apellidos);
             System.out.println("Direccion: ");
             String dir=leerDatos();
             doc.append("direccion", dir);
             
             collection.updateOne(
                     Filters.eq("matricula", matricula), 
                     Updates.addToSet("alquiler", doc)
             );
            
        } 
        catch (MongoCommandException mce) {}
        finally {
            mongoClient.close();
        }
        
    }
    
    public static void devolver(String matricula) throws ParseException {
        
        MongoClient mongoClient = null;
        
         try {
             mongoClient = new MongoClient("localhost",27017);
             MongoDatabase database = mongoClient.getDatabase("taller2");
             MongoCollection<Document> collection = 
             database.getCollection("vehiculos");
             Document doc = new Document();
             
             
             System.out.println("Fecha devolucion: ");
             String fechaSalida=leerDatos();
             Date fecha = new Date();
             fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaSalida);
             doc.append("fechadevolucion", fecha);
             
             System.out.println("km finales: ");
             float km=leerDatosF();
             doc.append("kmfinales", km);
           
             
             collection.updateOne(
                     Filters.eq("matricula", matricula), 
                     Updates.addToSet("alquiler", doc)
             );
            
        } 
        catch (MongoCommandException mce) {}
        finally {
            mongoClient.close();
        }
        
    }
    
    public static void buscarVehiculo(String matricula) {
        
         MongoClient mongoClient = null;
        try {
         mongoClient = new MongoClient("localhost",27017);
             MongoDatabase database = mongoClient.getDatabase("taller2");
             MongoCollection<Document> collection = 
             database.getCollection("vehiculos");
             
            BasicDBObject filtro = 
                    (BasicDBObject) JSON.parse("{vehiculos.matricula:'" + matricula + "'}");
            for(Document curso : collection.find(filtro)) {
                
                System.out.println("Matricula: " + curso.getString("matricula"));
                List<Document> dir = 
                        (List<Document>) curso.get("alquiler");
                System.out.println("Alquiler: ");
                for(Document contenido : dir) {
                    System.out.println("\t" + contenido.toString());
                }
                
                System.out.println();
            }
        } 
        catch (MongoCommandException mce) {
            System.out.println("Error al leer datos");
        }
    }
    
    public static void main(String[] args) throws ParseException {
        
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        
        
        boolean salir = false;
        
        do {
            
            System.out.println("Opcion: ");
            String opcion=leerDatos();
            
            switch(opcion) {
                case "1":
                    anadirVehiculo();
                    break;
                case "2":
                    System.out.println("Matricula del vehiculo: ");
                    String matricula=leerDatos();
                    anadirAlquiler(matricula);
                    break;
                case "3":
                    
                case "4":
                    System.out.println("Matricula del vehiculo: ");
                    String matricula2=leerDatos();
                    buscarVehiculo(matricula2);
                    break;
                case "5":
                    
                    break;
                case "0":
                    salir = true;
                    break;
            }
        }while(!salir);

    }

}
