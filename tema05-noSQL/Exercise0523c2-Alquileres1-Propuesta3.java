package Ex0523;

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
import com.mongodb.client.model.Sorts;
//David Bohez Oscar
import com.mongodb.util.JSON;

public class Main {

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
	
	public int proximoId(MongoCollection<Document> collection) {
        Document ultimaTarea = collection.find()
                .sort(Sorts.orderBy(Sorts.descending("codigo")))
                .first();
        
        if(ultimaTarea != null) {
            return ultimaTarea.getInteger("codigo") + 1;
        }
        else {
            return 1;
        }
    }
	
	 public static void anadirCliente() {
         
         List<String> lista =new ArrayList<>();
         List<Document> listaDoc =new ArrayList<>();
         MongoClient mongoClient = null;
	     try {
	    	 mongoClient = new MongoClient("localhost",27017);
	         MongoDatabase database = mongoClient.getDatabase("taller");
	         MongoCollection<Document> collection = 
	         database.getCollection("clientes");
             
             Document doc = new Document();
             
             System.out.println("DNI: ");
             String dni=leerDatos();
             doc.append("dni", dni);
             System.out.println("Nombre: ");
             String nombre=leerDatos();
             doc.append("nombre", nombre);
             System.out.println("Apellidos: ");
             String apellidos=leerDatos();
             doc.append("apellidos", apellidos);
             
             boolean salir=false;
             
             do
             {
            	 System.out.println("desea añadir direccion(si/no): ");
            	 String resp=leerDatos();
            	 if(resp.equals("no")) {
            		 salir=true;
            	 }else {
            		 System.out.println("direccion: ");
                	 String dir=leerDatos();
                	 lista.add(dir);
            	 }
            	 
             }while(!salir);
             
             
             if(lista.size() > 0) {
            	 
                 for(String contenido : lista) {
                     Document cont = new Document();
                     cont.append("nombre", contenido);
                     listaDoc.add(cont);
                 }
                 
                 doc.append("direccion", listaDoc);
             }
             
             
             collection.insertOne(doc);
             
         } 
         catch (Exception e) {
             
         }
         finally {
        	 mongoClient.close();
         }
         
     }

	 public static void anadirVehiculo() {
         
         List<String> lista =new ArrayList<>();
         List<Document> listaDoc =new ArrayList<>();
         MongoClient mongoClient = null;
	     try {
	    	 mongoClient = new MongoClient("localhost",27017);
	         MongoDatabase database = mongoClient.getDatabase("taller");
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
             
             
             boolean salir=false;
             
             do
             {
            	 System.out.println("desea añadir extras(si/no): ");
            	 String resp=leerDatos();
            	 if(resp.equals("no")) {
            		 salir=true;
            	 }else {
            		 System.out.println("extra: ");
                	 String ext=leerDatos();
                	 lista.add(ext);
            	 }
            	 
             }while(!salir);
             
             
             if(lista.size() > 0) {
            	 
                 for(String contenido : lista) {
                     Document cont = new Document();
                     cont.append("nombre", contenido);
                     listaDoc.add(cont);
                 }
                 
                 doc.append("extras", listaDoc);
             }
             
             
             collection.insertOne(doc);
             
         } 
         catch (Exception e) {
             
         }
         finally {
        	 mongoClient.close();
         }
         
     }
	
	 public static void alquilar() {
         
         List<String> lista =new ArrayList<>();
         List<Document> listaDoc =new ArrayList<>();
         MongoClient mongoClient = null;
         
	     try {
	    	 mongoClient = new MongoClient("localhost",27017);
	         MongoDatabase database = mongoClient.getDatabase("taller");
	         MongoCollection<Document> collection = 
	         database.getCollection("alquiler");
             
             Document doc = new Document();
             
             System.out.println("DNI cliente: ");
             String cliente=leerDatos();
             doc.append("cliente", cliente);
             System.out.println("Matricula vehiculo: ");
             String vehiculo=leerDatos();
             doc.append("vehiculo", vehiculo);
             
             
             System.out.println("Fecha salida: ");
             
             
             String fechaSalida=leerDatos();
             Date fecha = new Date();
	         fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaSalida);
	         doc.append("fechasalida", fecha);
             
             System.out.println("km iniciales: ");
             float km=leerDatosF();
             doc.append("kminiciales", km);
             
             
             boolean salir=false;
             
             do
             {
            	 System.out.println("desea añadir incidencias(si/no): ");
            	 String resp=leerDatos();
            	 if(resp.equals("no")) {
            		 salir=true;
            	 }else {
            		 System.out.println("incidencia: ");
                	 String dir=leerDatos();
                	 lista.add(dir);
            	 }
            	 
             }while(!salir);
             
             
             if(lista.size() > 0) {
            	 
                 for(String contenido : lista) {
                     Document cont = new Document();
                     cont.append("nombre", contenido);
                     listaDoc.add(cont);
                 }
                 
                 doc.append("incidencias", listaDoc);
             }
             
             
             collection.insertOne(doc);
             
         } 
         catch (Exception e) {
             
         }
         finally {
        	 mongoClient.close();
         }
         
     }
	 public static void buscarCliente(String dni) {
         
		 MongoClient mongoClient = null;
         try {
        	 mongoClient = new MongoClient("localhost",27017);
	         MongoDatabase database = mongoClient.getDatabase("taller");
	         MongoCollection<Document> collection = 
	         database.getCollection("clientes");
	         
             BasicDBObject filtro = 
                     (BasicDBObject) JSON.parse("{clientes.dni:'" + dni + "'}");
             for(Document curso : collection.find(filtro)) {
                 
            	 System.out.println("DNI: " + curso.getString("dni"));
                 System.out.println("Nombre: " + curso.getString("nombre"));
                 System.out.println("Apellidos: " + curso.getString("apellidos"));
                 List<Document> dir = 
                         (List<Document>) curso.get("direccion");
                 System.out.println("Direccion: ");
                 for(Document contenido : dir) {
                     System.out.println("\t" + contenido.getString("nombre"));
                 }
                 
                 System.out.println();
             }
         } 
         catch (MongoCommandException mce) {
             System.out.println("Error al leer datos");
         }
     }
	 
	public static void main(String[] args) {
		
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        
        
        boolean salir = false;
        
        do {
            
        	System.out.println("Opcion: ");
            String opcion=leerDatos();
            
            switch(opcion) {
                case "1":
                	anadirCliente();
                    break;
                case "2":
                	anadirVehiculo();
                    break;
                case "3":
                	alquilar();;
                case "4":
                  
                    break;
                case "5":
                    System.out.println("DNI a buscar: ");
                    String dni=leerDatos();
                    buscarCliente(dni);
                    break;
                case "0":
                    salir = true;
                    break;
            }
        }while(!salir);

	}

}
