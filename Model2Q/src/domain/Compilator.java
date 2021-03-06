package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import domain.parsers.AbstractParser;
import domain.parsers.JsonParser;
import domain.templates.AbstractTemplate;
import domain.templates.QSharpTemplate;
import domain.templates.QiskitTemplate;
import persistence.FileBroker;

public class Compilator {

	AbstractParser parser;
	AbstractTemplate template;
	String code = "";
	
	public void translate(String inputPath, String inputFormat, String outputLanguaje) throws JsonSyntaxException, JsonIOException, FileNotFoundException, Exception {
		// Instanciamos el parser adecuado para el formato del que queremos traducir.
		instanceParser("JSON");
		// Instanciamos la plantilla de código fijo adecuada para el lenguaje al que queremos traducir.
		instanceTemplate(outputLanguaje);
		// Parseamos el archivo JSON recibido.
		ProductFeatures productFeatures = parser.readFile(inputPath);
		// Generamos el código por medio de la plantilla y el traductor.
		code = template.generateCode(productFeatures);
		// Creamos el archivo de salida y lo escribimos.
		FileBroker.write(inputPath, productFeatures.getProductName(),code, outputLanguaje);
		System.out.println("Generated code:");
		System.out.println("\t" + code.replaceAll("\r\n", "\r\n\t"));
		System.out.println("\r\nCode successfully written!");
	}

	private void instanceParser(String format) throws Exception {
		if(format.equals("JSON")) {
			parser = new JsonParser();
		}else {
			Exception e = new Exception("Formato de entrada incorrecto");
			throw e;
		}
	}
	
	private void instanceTemplate(String languaje) throws Exception {
		if(languaje.equals("Q#")) {
			template = new QSharpTemplate();
		}else if(languaje.equals("Qiskit")) {
			template = new QiskitTemplate();
		}else {
			Exception e = new Exception("Lenguaje de salida incorrecto");
			throw e;
		}
	}
}
