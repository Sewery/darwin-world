package agh.ics.oop.util;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.Statistics;

import javax.swing.filechooser.FileSystemView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.function.Consumer;

//public abstract class CSVWriter<T> {
//    protected String filePath;
//    protected PrintWriter writer;
//    protected T data;
//    protected CSVWriter(String filename, T data) {
//        String DOCUMENTS_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
//        this.data = data;
//        try {
//            filePath=DOCUMENTS_PATH+'/'+filename+".csv";
//            this.writer = new PrintWriter(filePath);
//        } catch (FileNotFoundException e) {
//            System.err.println(e.getMessage());
//        }
//    }
//    public abstract void writeHeader();
//    public abstract void writeRow();
//    public void close() {
//        writer.close();
//    }

public  class CSVWriter{
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
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(DOCUMENTS_PATH+'/'+filename,true))) {
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
            writer.println("WorldID,Day number,Number of animals,Number of plants,Number of free fields,Most popular genotypes" +
                    ",Average animals energy," +
                    "Average animals age,Average animals children,AnimalID,Genotype,Current Gene," +
                    "Energy,Eaten plants,Children,Descendants,Age,Date of death");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
    public static void writeStatisticsLine(Statistics statistics, String filename) {
        String DOCUMENTS_PATH = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(DOCUMENTS_PATH+'/'+filename,true))) {
            int day = statistics.getNumberOfDay();
            writer.printf("%d,%d,%d,%d,%d,\"%s\",%d,%d,%d,%s,\"%s\",%s,%s,%s,%s,%s,%s,%s\n",
                    0,
                    day,
                    statistics.getNumberOfAllAnimals().get(day),
                    statistics.getNumberOfAllPlants().get(day),
                    statistics.getEmptySpaces().get(day),
                    statistics.getMostPopularGenotypes().toString(),
                    statistics.getAverageEnergy().get(day),
                    statistics.getAverageLifespan().get(day),
                    statistics.getAverageNumberOfChildren().get(day),
                    "N/A",
                    statistics.getGenome()==null?"N/A":statistics.getGenome(),
                    statistics.getCurrentGene()==null?"":statistics.getCurrentGene(),
                    statistics.getCurrentEnergy()==null?"":   statistics.getCurrentEnergy(),
                    statistics.getPlantsEaten()==null?"":statistics.getPlantsEaten(),
                    statistics.getNumberOfChildren()==null?"":statistics.getNumberOfChildren(),
                    statistics.getNumberOfDescendants()==null?"":  statistics.getNumberOfDescendants(),
                    statistics.getAge()==null?"":  statistics.getAge(),
                    statistics.getDayOfDeath()==null?"":  statistics.getDayOfDeath()
            );
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
