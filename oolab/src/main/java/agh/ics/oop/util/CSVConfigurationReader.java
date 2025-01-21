package agh.ics.oop.util;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.ConfigurationInvalidException;

import java.io.File;
import java.util.Scanner;
import java.util.function.Consumer;

public class CSVConfigurationReader {
    private static final Integer NUMBER_OF_COLUMNS = 13;
    private static final String HEADER="Width, Height, Initial Number Of Grasses, Number Of New Grasses Each Day, Energy Per Grass, " +
            "Initial Number Of Animals, Initial Energy Of Animals, Energy To Reproduce, Minimum Number Of Mutations, " +
            "Maximum Number Of Mutations, Genotype Length, Map Strategy, Animals Behaviour Strategy";
    private static Configuration toConfig(String[] args) {
        if(args.length != NUMBER_OF_COLUMNS) {
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
    public static Configuration readConfiguration(File file, Consumer<Exception> onError,Consumer<String> onSuccess) {
        Configuration config=null;
        try(Scanner scanner = new Scanner(file)){
            if (scanner.hasNextLine()
                    && scanner.nextLine().equals(HEADER) && scanner.hasNextLine()) {
                config = toConfig(scanner.nextLine().split(","));
            }else{
                throw new ConfigurationInvalidException("Invalid CSV file data\\nConfiguration could not be loaded");
            }
            onSuccess.accept("Configuration has been read successfully");
        }catch (Exception e){
            onError.accept(e);
        }
        return config;
    }
}
