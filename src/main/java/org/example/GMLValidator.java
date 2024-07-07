package org.example;

import org.citygml4j.xml.CityGMLContext;
import org.citygml4j.xml.CityGMLContextException;
import org.citygml4j.xml.schema.CityGMLSchemaHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmlobjects.schema.SchemaHandlerException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class GMLValidator {

    private static final Logger log = Logger.getLogger(GMLValidator.class.getName());

    public void GMLValidation(Path file) throws CityGMLContextException, SchemaHandlerException, SAXException, IOException {
        CityGMLContext context = CityGMLContext.newInstance();

        log.info("Getting default CityGML schemas from schema handler");
        CityGMLSchemaHandler schemaHandler = context.getDefaultSchemaHandler();
        Source[] schemas = schemaHandler.getSchemas();

        log.info("Creating XML schema factory and validator");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemas);

        Validator validator = schema.newValidator();

        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) {
                String message = "[" + exception.getLineNumber() + ", " +
                        exception.getColumnNumber() + "] " +
                        exception.getMessage();

                log.info(message);
            }

            @Override
            public void warning(SAXParseException exception) {
                error(exception);
            }

            @Override
            public void fatalError(SAXParseException exception) {
                error(exception);
            }
        });
        log.info("Validating the file " + file);

        validator.validate(new StreamSource(file.toFile()));

        log.info("Finished validating");
    }

}
