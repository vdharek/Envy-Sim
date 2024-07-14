package org.example;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.CityGMLBuilder;
import org.citygml4j.model.citygml.CityGML;
import org.citygml4j.model.citygml.appearance.*;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.util.walker.GMLWalker;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReader;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class TexCoord {
    private static final Logger log = Logger.getLogger(GMLParser.class.getName());
    public static void main(String[] args) {
        try {

            CityGMLContext context = initializeCityGMLContext();
            log.info("initializeCityGMLContext()");
            CityGMLBuilder builder = context.createCityGMLBuilder();
            log.info("createCityGMLBuilder()");
            CityGMLInputFactory inputFactory = builder.createCityGMLInputFactory();
            log.info("createCityGMLInputFactory()");
            File file = new File("./gml-files/Frankfurt_Street_Setting_LOD3.gml");
            log.info("Accessing file");
            parseCityGMLFile(inputFactory, file);
            log.info("ProcessCityGmlFile()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CityGMLContext initializeCityGMLContext() throws Exception {
        return CityGMLContext.getInstance();
    }

    private static void parseCityGMLFile(CityGMLInputFactory inputFactory, File file) throws Exception {
        try (CityGMLReader reader = inputFactory.createCityGMLReader(file)) {
            log.info("inputFactory.createCityGMLReader(file)");
            while (reader.hasNext()) {

                CityGML citygml = reader.nextFeature();
                log.info("reader.nextFeature()");
                if (citygml instanceof CityModel cityModel) {
                    processAppearances(cityModel);
                    log.info("processAppearances()");
                }
            }
        }
    }

    private static void processAppearances(CityModel cityModel) {

        cityModel.accept(new GMLWalker() {
            @Override
            public void visit(Appearance appearance) {
                extractAndPrintTexData(appearance);
                super.visit(appearance);
            }
        });
    }

    private static void extractAndPrintTexData(Appearance appearance) {
        log.info("extractAndPrintTexData(appearance)");
        if(appearance.isSetSurfaceDataMember()){
            List<SurfaceDataProperty> surfaceDataMember = appearance.getSurfaceDataMember();
            for(SurfaceDataProperty surfaceDataProperty : surfaceDataMember){
                surfaceDataProperty.getSurfaceData().accept(new GMLWalker() {
                    @Override
                    public void visit(ParameterizedTexture parameterizedTexture) {
                        processParameterizedTexture(parameterizedTexture);
                        super.visit(parameterizedTexture);
                    }
                });
            }
        }
    }

    /*
    This method helps us to access <app:ParameterizedTexture>
     */
    private static void processParameterizedTexture(ParameterizedTexture parameterizedTexture) {
        log.info("processParameterizedTexture(parameterizedTexture)");
        if(parameterizedTexture.isSetTarget()){
            log.info("Target id: " + parameterizedTexture.getImageURI());
            parameterizedTexture.accept(new GMLWalker() {
                @Override
                public void visit(TexCoordList texCoordList) {
                    printCoordList(texCoordList);
                    super.visit(texCoordList);
                }
            });
        }
    }

    /*
    Access to coordinates
     */
    private static void printCoordList(TexCoordList texCoordList) {
        log.info("Tex coordinates: " + texCoordList.getGMLClass());
        if(texCoordList.isSetTextureCoordinates()) {
            for (TextureCoordinates textureCoordinate : texCoordList.getTextureCoordinates()) {
                log.info("textureCoordinate: " + textureCoordinate.getValue());
            }
        }
    }

    private static void processX3DMaterial(X3DMaterial material) {
        System.out.println("Diffuse Color: " + material.getDiffuseColor());
        System.out.println("Emissive Color: " + material.getEmissiveColor());
        System.out.println("Specular Color: " + material.getSpecularColor());

        for (String target : material.getTarget()) {
            System.out.println("Material Target: " + target);
        }
    }
}
