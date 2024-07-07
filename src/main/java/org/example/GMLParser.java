package org.example;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.CityGMLBuilder;
import org.citygml4j.model.citygml.CityGML;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.citygml.transportation.TrafficArea;
import org.citygml4j.model.gml.geometry.aggregates.MultiSurface;
import org.citygml4j.model.gml.geometry.primitives.LinearRing;
import org.citygml4j.model.gml.geometry.primitives.SurfaceProperty;
import org.citygml4j.util.walker.GMLWalker;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReader;

import java.io.File;
import java.util.List;

public class GMLParser {
    public static void main(String[] args) {
        try {
            CityGMLContext context = CityGMLContext.getInstance();
            CityGMLBuilder builder = context.createCityGMLBuilder();

            processGMLFile(builder, "./gml-files/PockelStrasse.gml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processGMLFile(CityGMLBuilder builder, String filePath) throws Exception {
        CityGMLInputFactory inputFactory = builder.createCityGMLInputFactory();
        try (CityGMLReader reader = inputFactory.createCityGMLReader(new File(filePath))) {
            while (reader.hasNext()) {
                CityGML cityGML = reader.nextFeature();
                if (cityGML instanceof CityModel) {
                    processTrafficAreas((CityModel) cityGML);
                }
            }
        }
    }

    private static void processTrafficAreas(CityModel cityModel) {
        cityModel.accept(new GMLWalker() {
            @Override
            public void visit(TrafficArea trafficArea) {
                extractAndPrintLinearRingData(trafficArea);
                super.visit(trafficArea);
            }
        });
    }

    private static void extractAndPrintLinearRingData(TrafficArea trafficArea) {
        if (trafficArea.isSetLod3MultiSurface()) {
            MultiSurface multiSurface = trafficArea.getLod3MultiSurface().getMultiSurface();
            if (multiSurface != null) {
                for (SurfaceProperty surfaceProperty : multiSurface.getSurfaceMember()) {
                    surfaceProperty.getGeometry().accept(new GMLWalker() {
                        @Override
                        public void visit(LinearRing linearRing) {
                            printLinearRingData(linearRing);
                        }
                    });
                }
            }
        }
    }

    private static void printLinearRingData(LinearRing linearRing) {
        System.out.println("LinearRing ID: " + linearRing.getId());

        List<Double> coordinates = linearRing.toList3d();
        for (int i = 0; i < coordinates.size(); i += 3) {
            double x = coordinates.get(i);
            double y = coordinates.get(i + 1);
            double z = coordinates.get(i + 2);
            System.out.println("Position: " + x + " " + y + " " + z);
        }
    }
}


