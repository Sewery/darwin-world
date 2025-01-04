package agh.ics.oop.core;

public class AppState {
    private static AppState INSTANCE;
    private Configuration config;
    private Statistics stats;
    private AppState() {
    }

    public synchronized  static AppState getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AppState();
        }

        return INSTANCE;
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public Statistics getStats() {
        return stats;
    }

    public void setStats(Statistics stats) {
        this.stats = stats;
    }

}
