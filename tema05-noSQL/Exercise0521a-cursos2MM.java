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

/*
use cursos2;

db.cursos.insert(
	{
		codigo:1,
		nombre:"Curso Unity",
		fecha_inicio:new Date('2018-01-01'),
		contenidos:[
			{nombre:"Introduccion"},
			{nombre:"La vida moderna"}
		],
		alumnos:[
			{codigo:1,nombre:"Jehová"},
			{codigo:2,nombre:"Adrian"}
		]
	}
);
*/

public class Main {

    public static void menu() {
        System.out.println("----MENÚ----");
        System.out.println("1. Mostrar curso");
        System.out.println("2. Añadir curso");
        System.out.println("3. Asignar alumno a curso");
        System.out.println("4. Ver alumnos de un curso");
        System.out.println("5. Ver cursos de un alumno");
        System.out.println("0. Salir");
        System.out.println("Opción: ");
    }
    
    public static void mostrarDatosCurso(Document curso) {
        System.out.println("Codigo: "+curso.getDouble("codigo"));
        System.out.println("Nombre: "+curso.getString("nombre"));
        
        Date fecha = curso.getDate("fecha_inicio");
        System.out.println("Fecha: " 
            + new SimpleDateFormat("dd/MM/yyyy").format(fecha));
        
        System.out.println("\nContenidos: ");
        List<Document> contenidos = (List<Document>) 
                curso.get("contenidos");
        for(Document contenido : contenidos) {
            System.out.println("Nombre: "+contenido.
                    getString("nombre"));
        }
        System.out.println("\nAlumnos: ");
        List<Document> alumnos = (List<Document>) curso.get("alumnos");
        for(Document alumno : alumnos) {
            System.out.println("Codigo: "+alumno.getDouble("codigo"));
            System.out.println("Nombre: "+alumno.getString("nombre"));
        }
        System.out.println("----------");
    }
    
    public static void mostrarCurso() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("cursos2");
            MongoCollection<Document> collectionCursos = 
                    database.getCollection("cursos");
            
            System.out.println("Introduce el código del curso: ");
            double codigoCurso = Double.parseDouble(sc.nextLine());
            
            for(Document curso : collectionCursos.find(
                    new Document("codigo",codigoCurso))) {
                
                mostrarDatosCurso(curso);
               
            }
        }catch(MongoCommandException e) {
            System.err.println("Error al mostrar los cursos");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void anyadirCurso() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("cursos2");
            
            MongoCollection<Document> collectionCursos = 
                    database.getCollection("cursos");
            
            System.out.println("Introduce el código: ");
            double codigo = Double.parseDouble(sc.nextLine());
            System.out.println("Introduce el nombre: ");
            String nombre = sc.nextLine();
            System.out.println("Introduce la fecha: ");

            Date fecha = new Date();
            try {
                fecha = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
            } catch (ParseException e) {}
            
            System.out.println("Introduce la cantidad de contenidos: ");
            int cantidad = Integer.parseInt(sc.nextLine());
            ArrayList<Document> contenidos = new ArrayList<Document>();
            for(int i=0;i<cantidad;i++) {
                System.out.println("Introduce el contenido nº"+(i+1)+": ");
                contenidos.add(new Document("nombre",sc.nextLine()));
            }
            
            System.out.println("Introduce la cantidad de alumnos que "
                    + "quieres añadir: ");
            cantidad = Integer.parseInt(sc.nextLine());
            ArrayList<Document> alumnos = introducirAlumnos(cantidad);
            Document docCursos = new Document();
            docCursos.append("codigo", codigo);
            docCursos.append("nombre", nombre);
            docCursos.append("fecha_inicio", fecha);
            docCursos.append("contenidos",contenidos);
            docCursos.append("alumnos", alumnos);
            
            collectionCursos.insertOne(docCursos);
            System.out.println("Curso añadido con éxito");
        }catch(MongoCommandException e) {
            System.err.println("Error al añadir el curso");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static ArrayList<Document> introducirAlumnos(int cantidadInsertar) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Document> alumnos = new ArrayList<Document>();
        double codigoAlumno;
        String nombreAlumno;
        Document docAlumnos;
        for(int i=0;i<cantidadInsertar;i++) {
            System.out.println("Introduce el código del alumno: ");
            codigoAlumno = Double.parseDouble(sc.nextLine());
            System.out.println("Introduce el nombre del alumno: ");
            nombreAlumno = sc.nextLine();
            docAlumnos = new Document();
            docAlumnos.append("codigo", codigoAlumno);
            docAlumnos.append("nombre", nombreAlumno);
            alumnos.add(docAlumnos);
        }
        return alumnos;
    }
    
    public static void asignarAlumnoCurso() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("cursos2");
            MongoCollection<Document> collectionCursos = 
                    database.getCollection("cursos");
            
            System.out.println("Introduce el codigo del curso: ");
            double codCurso = Double.parseDouble(sc.nextLine());
            Document alumno = introducirAlumnos(1).get(0);
            
            collectionCursos.updateOne(
                        Filters.eq("codigo",codCurso),
                        Updates.addToSet("alumnos", alumno)
                    );
            System.out.println("Alumno asignado con éxito");
        }catch(MongoCommandException e) {
            System.err.println("Error al añadir el alumno al curso");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void verAlumnosCurso() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("cursos2");
            MongoCollection<Document> collectionCursos = 
                    database.getCollection("cursos");
            
            System.out.println("Introduce el codigo del curso: ");
            double codCurso = Double.parseDouble(sc.nextLine());
            
            for(Document curso : collectionCursos.find(
                    new Document("codigo",codCurso))) {
                ArrayList<Document> alumnos = (ArrayList<Document>)
                        curso.get("alumnos");
                for(Document alumno : alumnos) {
                    System.out.println("Codigo: "+alumno.getDouble("codigo"));
                    System.out.println("Nombre: "+alumno.getString("nombre"));
                    System.out.println("---------");
                }
            }
        }catch(MongoCommandException e) {
            System.err.println("Error al mostrar los alumnos del curso");
        }finally {
            if(mongoClient != null)
                mongoClient.close();
        }
    }
    
    public static void verCursosAlumno() {
        Scanner sc = new Scanner(System.in);
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient("localhost",27017);
            MongoDatabase database = mongoClient.getDatabase("cursos2");
            MongoCollection<Document> collectionCursos = 
                    database.getCollection("cursos");
            
            Document alumno = introducirAlumnos(1).get(0);
            
            for(Document curso : collectionCursos.find(
                    new Document("alumnos",alumno))) {
                
                mostrarDatosCurso(curso);
            }
        }catch(MongoCommandException e) {
            System.err.println("Error al mostrar los cursos del alumno");
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
                    mostrarCurso();
                    break;
                case "2":
                    anyadirCurso();
                    break;
                case "3":
                    asignarAlumnoCurso();
                    break;
                case "4":
                    verAlumnosCurso();
                    break;
                case "5":
                    verCursosAlumno();
                    break;
                case "0":
                    salir = true;
                    break;
            }
        }while(!salir);
    }

}
