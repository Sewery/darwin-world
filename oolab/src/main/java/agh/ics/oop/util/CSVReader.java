package agh.ics.oop.util;

import agh.ics.oop.model.util.Configuration;
import agh.ics.oop.model.util.ConfigurationInvalidException;

import java.io.File;
import java.util.Scanner;
import java.util.function.Consumer;

public class CSVReader {
    private static final String HEADER="width,height,initialNumberOfGrasses,numberOfNewGrassesEachDay,energyPerGrass," +
            "initialNumberOfAnimals,initialEnergyOfAnimals,energyToReproduce,minNumberOfMutations," +
            "maxNumberOfMutations,genotypeLength,mapStrategy,animalsBehaviourStrategy";
    private static Configuration toConfig(String[] args) {
        if(args.length != 13) {
            throw new ConfigurationInvalidException("Invalid CSV file data");
        }
        return new Configuration(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                Integer.parseInt(args[6]),
                Integer.parseInt(args[7]),
                Integer.parseInt(args[8]),
                Integer.parseInt(args[9]),
                Integer.parseInt(args[10]),
                Configuration.MapStrategy.fromString(args[11]),
                Configuration.AnimalsBehaviourStrategy.fromString(args[12])
        );
    }
    public static Configuration readConfiguration(File file, Consumer<Exception> onError) {
        Configuration config=null;
        try(Scanner scanner = new Scanner(file)){
            if (scanner.hasNextLine()
                    && scanner.nextLine().equals(HEADER) && scanner.hasNextLine()) {
                config = toConfig(scanner.nextLine().split(","));
                System.out.println(config);
            }else{
                throw new ConfigurationInvalidException("Invalid CSV file data");
            }
        }catch (Exception e){
            onError.accept(e);
        }
        return config;
    }
}
