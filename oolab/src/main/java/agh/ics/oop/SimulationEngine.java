package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for (Simulation simulation : simulations) {
            simulation.run();
        }
    }

    public void runAsync(){

        for (Simulation simulation : simulations){
            threads.add(new Thread(simulation));
            threads.getLast().start();
        }


    }

    public void runASyncInThreadPool(){

        for (Simulation simulation : simulations){
            executor.submit(simulation);
        }

    }

    public void awaitSimulationsEnd() throws InterruptedException {

        for (Thread thread: threads) thread.join();

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.SECONDS)){
            executor.shutdownNow();
        }

    }
}
