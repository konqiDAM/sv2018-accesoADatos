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
public class Exercise0516 {

	private static String pedir(String mensaje) {
		Scanner scn = new Scanner(System.in);
		System.out.print(mensaje + " ");
		return scn.nextLine();
	}
	private static String menu() {
		System.out.println("MENU");
		System.out.println("1.- AÑADIR ALUMNO");
		System.out.println("0.- SALIR");
		return pedir("Opción:");
	}
	
	private static void anyadirAlumno() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);

		MongoClient mongoClient = null;

		try {
			mongoClient = new MongoClient("localhost", 27017);
			MongoDatabase database = mongoClient.getDatabase("alumnos");

			MongoCollection<Document> collection =
			        database.getCollection("alumnos");

			String nombre = pedir("Nombre del alumno:");
			double nacido = Double.parseDouble(pedir("Nacido:"));
			String empresaActual = pedir("Empresa actual:");
			String cargo = pedir("Cargo:");
			
			Document alumno = new Document();
			
			alumno.append("nombre", nombre);
			alumno.append("nacido", nacido);
			alumno.append("empresaActual", empresaActual);
			alumno.append("cargo", cargo);
			
			collection.insertOne(alumno);
			
			mongoClient.close();
		} catch (MongoCommandException e) {
			System.err.println(
			        "Se produjo un error ejecutando un comando de mongo");
		}
	}
	public static void main(String[] args) {
		String opcion;
		boolean salir = false;
		
		do {
			opcion = menu();
			switch(opcion) {
			case"1":
				anyadirAlumno();
				break;
			case "0":
				salir = true;
				break;
			}
		} while (!salir);
	}

}
