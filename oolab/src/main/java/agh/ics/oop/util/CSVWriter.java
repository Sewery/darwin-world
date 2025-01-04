package agh.ics.oop.util;

import agh.ics.oop.core.Configuration;

import javax.swing.filechooser.FileSystemView;
import java.io.PrintWriter;
import java.util.function.Consumer;

public class CSVWriter {
    private static void writeConfigurationHeader(PrintWriter writer) {
        writer.println("width,height,initialNumberOfGrasses,numberOfNewGrassesEachDay,energyPerGrass," +
                "initialNumberOfAnimals,initialEnergyOfAnimals,energyToReproduce,minNumberOfMutations," +
                "maxNumberOfMutations,genotypeLength,mapStrategy,animalsBehaviourStrategy");
    }
    private static void writeConfigurationContent(PrintWriter writer,Configuration config) {
        writer.printf("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s,%s%n",
                config.width(),
                config.height(),
                config.initialNumberOfGrasses(),
                config.numberOfNewGrassesEachDay(),
                config.energyPerGrass(),
                config.initialNumberOfAnimals(),
                config.initialEnergyOfAnimals(),
                config.energyToReproduce(),
                config.minNumberOfMutations(),
                config.maxNumberOfMutations(),
                config.genotypeLength(),
                config.mapStrategy(),
                config.animalsBehaviourStrategy()
        );
    }
    public static void writeConfiguration(Configuration config , String filename, Consumer<String> onSuccess) {
        String DOCUMENTS_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        try (PrintWriter writer = new PrintWriter(DOCUMENTS_PATH+'/'+filename)) {
            writeConfigurationHeader(writer);
            writeConfigurationContent(writer, config);
            onSuccess.accept("Configuration was saved successfully in:\n"+DOCUMENTS_PATH+'\\'+filename);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }
}
