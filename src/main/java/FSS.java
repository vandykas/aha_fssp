import java.util.ArrayList;

public class FSS {
    private final int[][] processingTime;
    private final int jobCount;
    private final int machineCount;
    private int[][] completionTime;

    public FSS(int[][] processingTime, int jobCount, int machineCount) {
        this.processingTime = processingTime;
        this.jobCount = jobCount;
        this.machineCount = machineCount;
    }

    public void calculateCompletionTime(ArrayList<Integer> jobSchedule) {
        this.completionTime = new int[jobCount][machineCount];
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
    }
    
    public int calculateMakespan() {
        return this.completionTime[jobCount - 1][machineCount - 1];
    }

    public int calculateTotalFlowTime() {
        int totalFlowTime = 0;
        for (int i = 0; i < jobCount; i++) {
            totalFlowTime += this.completionTime[i][machineCount - 1];
        }
        return totalFlowTime;
    }
}
