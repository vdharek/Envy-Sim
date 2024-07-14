package org.example;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.CityGMLBuilder;
import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.citygml.core.AbstractCityObject;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.citygml.transportation.Road;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReader;
import org.citygml4j.model.citygml.core.CityObjectMember;

import java.io.File;

public class GMLRoads {

    public static void main(String[] args) {
        try {
            // Initialize CityGML context and builder
            CityGMLContext context = CityGMLContext.getInstance();
            CityGMLBuilder builder = context.createCityGMLBuilder();

            // Create CityGML input factory
            CityGMLInputFactory inputFactory = builder.createCityGMLInputFactory();

            // Read the CityGML file
            CityGMLReader reader = inputFactory.createCityGMLReader(new File("./gml-files/PockelStrasse.gml"));

            int roadCount = 0;

            while (reader.hasNext()) {
                // Read the next feature (typically CityModel)
                Object cityObject = reader.nextFeature();

                if (cityObject instanceof CityModel cityModel) {

                    // Iterate through CityObjectMembers in the CityModel
                    for (CityObjectMember cityObjectMember : cityModel.getCityObjectMember()) {
                        // Check if the CityObject is a Road
                        if (cityObjectMember.isSetCityObject() && cityObjectMember.getCityObject().getCityGMLClass() == CityGMLClass.ROAD) {
                            Road road = (Road) cityObjectMember.getCityObject();
                            AbstractCityObject feature = cityObjectMember.getFeature();
                            roadCount++;
                            // Optionally, you can print Road ID or other attributes
                            System.out.println("Found Road with ID: " + road.getId());
                        }
                    }
                }
            }

            reader.close();

            // Print the total number of <tran:Road> elements found
            System.out.println("Total number of <tran:Road> elements: " + roadCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
