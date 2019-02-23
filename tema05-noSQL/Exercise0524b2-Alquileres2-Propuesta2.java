/*
Nacho, lo de actualizar el alquiler no me funciona por lo que se ve y la regex 
no sé qué es lo que falla, desde la consola esto último me va:

db.vehiculos.find({'alquileres.cliente.apellidos':{$regex:/nombreABuscar/}})
*/

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
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

//Adrián Fernández Arnal
public class Main {

    public static void menu() {
        System.out.println("----MENÚ----");
        System.out.println("1. Añadir vehículo");
        System.out.println("2. Añadir alquiler");
        System.out.println("3. Devolver alquiler");
        System.out.println("4. Mostrar historial de vehículo");
        System.out.println("5. Ver cliente");
        System.out.println("0. Salir");
        System.out.println("Opción: ");
    }
    
    public static void anyadirVehiculo() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("alquileres2");
            MongoCollection<Document> collectionVehiculos = 
                    database.getCollection("vehiculos");
            
            String matricula,marca,modelo,grupo,volumenString;
            Double precioDia;
            ArrayList<Document> extras = new ArrayList<>();
            
            System.out.println("Introduce la matrícula: ");
            matricula = sc.nextLine();
            System.out.println("Introduce la marca: ");
            marca = sc.nextLine();
            System.out.println("Introduce el modelo: ");
            modelo = sc.nextLine();
            System.out.println("Introduce el grupo: ");
            grupo = sc.nextLine();
            System.out.println("Introduce el precio diario: ");
            precioDia = Double.parseDouble(sc.nextLine());
            System.out.println("Introduce el volumen de la carga"
                    + " (intro para ignorar): ");
            volumenString = sc.nextLine();
            System.out.println("Introduce la cantidad de extras: ");
            int cantidadExtras = Integer.parseInt(sc.nextLine());
            
            for(int i=0;i<cantidadExtras;i++) {
                System.out.println("Introduce el "+(i+1)+"º extra: ");
                extras.add(new Document("nombre",sc.nextLine()));
            }
            Document documentVehiculos = new Document();
            documentVehiculos.append("matricula", matricula);
            documentVehiculos.append("marca", marca);
            documentVehiculos.append("modelo", modelo);
            documentVehiculos.append("grupo", grupo);
            documentVehiculos.append("precioDia", precioDia);
            if(!volumenString.equals(""))
                documentVehiculos.append("volumenCarga", 
                        Double.parseDouble(volumenString));
            if(extras.size()>0)
                documentVehiculos.append("extras", extras);
            
            collectionVehiculos.insertOne(documentVehiculos);
            System.out.println("Vehículo insertado con éxito");
        }catch(MongoCommandException e) {
            System.err.println("Error al insertar el vehículo");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void anyadirAlquiler() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("alquileres2");
            MongoCollection<Document> collectionVehiculos = 
                    database.getCollection("vehiculos");
            
            String matricula,fechaSalidaCadena, fechaPrevistaCadena, dni, 
            nombre, apellidos, direccion;   
            Double kmIniciales,importe;
            Date fechaSalida = new Date(),fechaPrevista = new Date();
            
            //Pedir matrícula para identificar el vehículo
            System.out.println("Introduce la matrícula del coche a alquilar: ");
            matricula = sc.nextLine();
            
            //Pedir datos del alquiler
            System.out.println("Introduce la fecha de salida: ");
            fechaSalidaCadena = sc.nextLine();
            try {
                fechaSalida = new SimpleDateFormat("dd/MM/yyyy")
                        .parse(fechaSalidaCadena);
            } catch (ParseException e) { 
                System.out.println(
                        "La fecha de salida no es válida");
            }
            System.out.println("Introduce la fecha prevista de devolución: ");
            fechaPrevistaCadena = sc.nextLine();
            try {
                fechaPrevista = new SimpleDateFormat("dd/MM/yyyy")
                        .parse(fechaPrevistaCadena);
            } catch (ParseException e) { 
                System.out.println(
                        "La fecha de devolución prevista no es válida");
            }
            System.out.println("Introduce los KM iniciales: ");
            kmIniciales = Double.parseDouble(sc.nextLine());
            System.out.println("Introduce el importe: ");
            importe = Double.parseDouble(sc.nextLine());
            
            //Pedir datos del cliente que alquila
            System.out.println("Introduce el DNI del cliente: ");
            dni = sc.nextLine();
            System.out.println("Introduce el nombre del cliente: ");
            nombre = sc.nextLine();
            System.out.println("Introduce los apellidos del cliente: ");
            apellidos = sc.nextLine();
            System.out.println("Introduce la dirección del cliente: ");
            direccion = sc.nextLine();
            
            Document docCliente = new Document();
            docCliente.append("dni", dni);
            docCliente.append("nombre", nombre);
            docCliente.append("apellidos", apellidos);
            docCliente.append("direccion", direccion);
            Document docAlquiler = new Document();
            docAlquiler.append("fechaSalida", fechaSalida);
            docAlquiler.append("fechaDevolucionPrevista", fechaPrevista);
            docAlquiler.append("kmIniciales", kmIniciales);
            docAlquiler.append("importe", importe);
            docAlquiler.append("cliente", docCliente);
            
            collectionVehiculos.updateOne(
                    Filters.eq("matricula",matricula),
                    Updates.addToSet("alquileres", docAlquiler)
            );
            System.out.println("Alquiler registrado con éxito");
        }catch(MongoCommandException e) {
            System.err.println("Error al insertar el alquiler");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void devolverAlquiler() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("alquileres2");
            MongoCollection<Document> collectionVehiculos = 
                    database.getCollection("vehiculos");
            
            String matricula,fechaSalidaCadena,fechaRealCadena;
            Double kmFinales;
            Date fechaReal = new Date(), fechaSalida = new Date();
            
            //Pedir datos para identificar el alquiler
            System.out.println("Introduce la matrícula: ");
            matricula = sc.nextLine();
            System.out.println("Introduce la fecha de salida: ");
            fechaSalidaCadena = sc.nextLine();
            try {
                fechaSalida = new SimpleDateFormat("dd/MM/yyyy")
                        .parse(fechaSalidaCadena);
            } catch (ParseException e1) {
                System.out.println("La fecha de salida no es válida");
            }
            
            //Pedir datos a actualizar
            System.out.println("Introduce los KM finales: ");
            kmFinales = Double.parseDouble(sc.nextLine());
            
            Document docAlquiler = new Document();
            docAlquiler.put("fechaDevolucionReal", fechaReal);
            docAlquiler.put("kmFinales", kmFinales);
            
            collectionVehiculos.updateOne(
                    Filters.and(
                            Filters.eq("matricula", matricula),
                            Filters.eq("alquileres.fechaSalida", fechaSalida)
                    ),
                    Updates.combine(
                            Updates.set("alquileres.kmFinales", kmFinales),
                            Updates.set("alquileres.fechaDevolucionReal", 
                                    fechaReal)
                    )
            );
            System.out.println("Vehículo devuelto con éxito");
        }catch(MongoCommandException e) {
            System.err.println("Error al devolver el vehículo");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void mostrarHistorialVehiculo() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("alquileres2");
            MongoCollection<Document> collectionVehiculos = 
                    database.getCollection("vehiculos");
            
            String matricula;
            Double volumenCarga;
            System.out.println("Introduce la matrícula del vehículo: ");
            matricula = sc.nextLine();
            
            for(Document vehiculo : collectionVehiculos.find(
                    new Document("matricula",matricula))) {
                
                System.out.println("Marca: "+vehiculo.getString("marca"));
                System.out.println("Modelo: "+vehiculo.getString("modelo"));
                System.out.println("Grupo: "+vehiculo.getString("grupo"));
                System.out.println("Precio/dia: "+vehiculo
                        .getDouble("precioDia"));
                volumenCarga = vehiculo.getDouble("volumenCarga");
                if(volumenCarga!=null)
                    System.out.println("Volumen de carga: "+volumenCarga);
                
                List<Document> extras = (List<Document>) vehiculo.get("extras");
                if(extras != null) {
                    System.out.println("Extras: ");
                    for(Document extra : extras) {
                        System.out.println("- Nombre: "+extra.get("nombre"));
                    }
                }
                
                List<Document> alquileres = (List<Document>) vehiculo
                        .get("alquileres");
                
                if(alquileres != null) {
                    System.out.println("Alquileres: ");
                    for(Document alquiler : alquileres) {
                        System.out.println("- Fecha salida: "+alquiler
                                .getDate("fechaSalida"));
                        System.out.println("- Fecha devolución prevista: "+
                                alquiler.getDate("fechaDevolucionPrevista"));
                        System.out.println("- Fecha devolución real: "+alquiler
                                .getDate("fechaDevolucionReal"));
                        System.out.println("- Km iniciales: "+alquiler
                                .getDouble("kmIniciales"));
                        System.out.println("- Km finales: "+alquiler
                                .getDouble("kmFinales"));
                        System.out.println("- Importe: "+alquiler
                                .getDouble("importe"));
                        
                        Document cliente = (Document)alquiler.get("cliente");
                        System.out.println("-- DNI: "+cliente.getString("dni"));
                        System.out.println("-- Nombre: "+cliente
                                .getString("nombre"));
                        System.out.println("-- Apellidos: "+cliente
                                .getString("apellidos"));
                        System.out.println("-- Dirección: "+cliente
                                .getString("direccion"));
                        
                    }
                }
            }
            
        }catch(MongoCommandException e) {
            System.err.println("Error al mostrar el historial del vehículo");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void mostrarCliente() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("alquileres2");
            MongoCollection<Document> collectionVehiculos = 
                    database.getCollection("vehiculos");
            
            String apellido;
            System.out.println("Introduce el apellido: ");
            apellido = sc.nextLine();
            
            Document regexQuery = new Document();
            regexQuery.put("alquileres.cliente.apellidos",
                   new Document("$regex", "/"+apellido+"/"));
            
            for(Document cliente : collectionVehiculos.find(regexQuery)) {
                System.out.println("DNI: "+cliente.getString("dni"));
                System.out.println("Nombre: "+cliente.getString("nombre"));
                System.out.println("Apellidos: "+cliente
                        .getString("apellidos"));
                System.out.println("Dirección: "+cliente
                        .getString("direccion"));
            }
            
        }catch(MongoCommandException e) {
            System.err.println("Error al mostrar el historial del vehículo");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void main(String[] args) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        do {
            menu();
            switch(sc.nextLine()) {
                case "1":
                    anyadirVehiculo();
                    break;
                case "2":
                    anyadirAlquiler();
                    break;
                case "3":
                    devolverAlquiler();
                    break;
                case "4":
                    mostrarHistorialVehiculo();
                    break;
                case "5":
                    mostrarCliente();
                    break;
                case "0":
                    salir = true;
                    break;
            }
        }while(!salir);
    }

}
