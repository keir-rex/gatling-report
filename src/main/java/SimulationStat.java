import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationStat {
    private String filePath;
    private String simulationName;
    private Stat simStat;
    private Map<String, Stat> reqStats;

    private static final String ALL_REQUESTS = "_all";
    private long start;

    public SimulationStat(String filePath) {
        this.filePath = filePath;
        this.simStat = new Stat(filePath, ALL_REQUESTS, 0);
        reqStats = new HashMap<>();
    }

    public void addRequest(String requestName, long start, long end, boolean success) {
        Stat request = reqStats.get(requestName);
        if (request == null) {
            request = new Stat(simulationName, requestName, this.start);
            reqStats.put(requestName, request);
        }
        request.add(start, end, success);
        simStat.add(start, end, success);
    }

    public void computeStat() {
        simStat.computeStat();
        reqStats.values().forEach(request -> request.computeStat(simStat.getDuration()));
    }

    @Override
    public String toString() {
        return simStat.toString() + "\n" + reqStats.values().stream().map(Stat::toString).collect(Collectors
                .joining("\n"));
    }

    public void setSimulationName(String name) {
        this.simulationName = name;
        simStat.setScenario(name);
    }

    public void setStart(long start) {
        this.start = start;
        simStat.setStart(start);
    }
}