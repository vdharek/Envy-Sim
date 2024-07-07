package org.example;

import org.citygml4j.xml.CityGMLContextException;
import org.xml.sax.SAXException;
import org.xmlobjects.schema.SchemaHandlerException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class App {
    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException, SchemaHandlerException, SAXException {

        Path file = Paths.get("./gml-files/PockelStrasse.gml");

        GMLValidator gmlValidator = new GMLValidator();

        try {
            gmlValidator.GMLValidation(file);
        } catch (CityGMLContextException e) {
            throw new RuntimeException(e);
        }



    }
}
