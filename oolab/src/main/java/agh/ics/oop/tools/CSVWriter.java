package agh.ics.oop.tools;

import agh.ics.oop.model.core.Configuration;
import agh.ics.oop.model.core.Statistics;

import javax.swing.filechooser.FileSystemView;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.function.Consumer;

public class CSVWriter{
    private static void writeConfigurationHeader(PrintWriter writer) {
        writer.println("Width, Height, Initial Number Of Grasses, Number Of New Grasses Each Day, Energy Per Grass, " +
                "Initial Number Of Animals, Initial Energy Of Animals, Energy To Reproduce, Minimum Number Of Mutations, " +
                "Maximum Number Of Mutations, Genotype Length, Map Strategy, Animals Behaviour Strategy");
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
    public static void writeStatisticsHeader(Statistics statistics, String filename) {
        String DOCUMENTS_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        try (PrintWriter writer = new PrintWriter(DOCUMENTS_PATH+'/'+filename)) {
            writer.println("Day number,Number of animals,Number of plants,Number of free fields,Most popular genotypes" +
                    ",Average animals energy," +
                    "Average animals age,Average animals children,Genotype,Current Gene," +
                    "Energy,Eaten plants,Children,Descendants,Age,Date of death");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
    public static void writeStatisticsLine(Statistics statistics, String filename) {
        String DOCUMENTS_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(DOCUMENTS_PATH+'/'+filename,true))) {
            int day = statistics.getNumberOfDay();
            writer.printf("%d,%d,%d,%d,\"%s\",%d,%d,%d,\"%s\",%s,%s,%s,%s,%s,%s,%s\n",
                    day,
                    statistics.getNumberOfAllAnimals(),
                    statistics.getNumberOfAllPlants(),
                    statistics.getEmptySpaces(),
                    statistics.getMostPopularGenotypes().toString(),
                    statistics.getAverageEnergy(),
                    statistics.getAverageLifespan(),
                    statistics.getAverageNumberOfChildren(),
                    statistics.getGenome()==null?"N/A":statistics.getGenome(),
                    statistics.getCurrentGene()==null?"N/A":statistics.getCurrentGene(),
                    statistics.getCurrentEnergy()==null?"N/A":   statistics.getCurrentEnergy(),
                    statistics.getPlantsEaten()==null?"N/A":statistics.getPlantsEaten(),
                    statistics.getNumberOfChildren()==null?"N/A":statistics.getNumberOfChildren(),
                    statistics.getNumberOfDescendants()==null?"N/A":  statistics.getNumberOfDescendants(),
                    statistics.getAge()==null?"N/A":  statistics.getAge(),
                    statistics.getDayOfDeath()==null?"N/A":  statistics.getDayOfDeath()
            );
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
