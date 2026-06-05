import java.util.List;

public class FSS {
    private final int[][] processingTime;
    private final int jobCount;
    private final int machineCount;

    public FSS(int[][] processingTime, int jobCount, int machineCount) {
        this.processingTime = processingTime;
        this.jobCount = jobCount;
        this.machineCount = machineCount;
    }

    public boolean isFitnessBetter(FitnessValue current, FitnessValue prev) {
        int prevMakespan = prev.makespan();
        int currMakespan = current.makespan();

        int prevTotalFlowTime = prev.totalFlowTime();
        int currTotalFlowTime = current.totalFlowTime();
        
        double minMakespan = Math.min(currMakespan, prevMakespan);
        double minTotalFlowTime = Math.min(currTotalFlowTime, prevTotalFlowTime);

        double prevFitness = (prevMakespan - minMakespan) / minMakespan + (prevTotalFlowTime - minTotalFlowTime) / minTotalFlowTime;
        double currentFitness = (currMakespan - minMakespan) / minMakespan + (currTotalFlowTime - minTotalFlowTime) / minTotalFlowTime;
        return Double.compare(currentFitness, prevFitness) < 0;
    }

    public FitnessValue calculateFitness(List<Integer> jobSchedule) {
        int[][] completionTime = new int[jobCount][machineCount];
        completionTime[0][0] = processingTime[jobSchedule.getFirst()][0];
        for (int i = 1; i < machineCount; i++) {
            completionTime[0][i] = completionTime[0][i - 1] + processingTime[jobSchedule.getFirst()][i];
        }

        for (int i = 1; i < jobCount; i++) {
            int curJob = jobSchedule.get(i);
            completionTime[i][0] = completionTime[i - 1][0] + processingTime[curJob][0];
            for (int j = 1; j < machineCount; j++) {
                completionTime[i][j] = Math.max(completionTime[i - 1][j], completionTime[i][j - 1]) + processingTime[curJob][j];
            }
        }

        int makespan = calculateMakespan(completionTime);
        int totalFlowTime = calculateTotalFlowTime(completionTime);
        return new FitnessValue(makespan, totalFlowTime);
    }
    
    private int calculateMakespan(int[][] completionTime) {
        return completionTime[jobCount - 1][machineCount - 1];
    }

    private int calculateTotalFlowTime(int[][] completionTime) {
        int totalFlowTime = 0;
        for (int i = 0; i < jobCount; i++) {
            totalFlowTime += completionTime[i][machineCount - 1];
        }
        return totalFlowTime;
    }
}
