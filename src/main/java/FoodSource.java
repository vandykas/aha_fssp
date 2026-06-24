import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class FoodSource implements Comparable<FoodSource> {
    private ArrayList<Integer> jobSchedule;
    private int makespan;
    private int totalFlowTime;

    public FoodSource(Random rand, int jobCount) {
        initializeJobSchedule(rand, jobCount);
        this.makespan = Integer.MAX_VALUE;
        this.totalFlowTime = Integer.MAX_VALUE;
    }

    private void initializeJobSchedule(Random rand, int jobCount) {
        this.jobSchedule = new ArrayList<>();
        for (int i = 0; i < jobCount; i++) {
            this.jobSchedule.add(i);
        }
        Collections.shuffle(this.jobSchedule, rand);
    }

    public FoodSource(ArrayList<Integer> jobSchedule) {
        this.jobSchedule = jobSchedule;
        this.makespan = Integer.MAX_VALUE;
        this.totalFlowTime = Integer.MAX_VALUE;
    }

    @Override
    public int compareTo(FoodSource other) {
        int currMakespan = this.getMakespan();
        int otherMakespan = other.getMakespan();

        int currTotalFlowTime = this.getTotalFlowTime();
        int otherTotalFlowTime = other.getTotalFlowTime();
        
        int minMakespan = Math.min(otherMakespan, currMakespan);
        int minTotalFlowTime = Math.min(otherTotalFlowTime, currTotalFlowTime);

        double currFitness = calculateFitness(currMakespan, minMakespan, currTotalFlowTime, minTotalFlowTime);
        double otherFitness = calculateFitness(otherMakespan, minMakespan, otherTotalFlowTime, minTotalFlowTime);
        return Double.compare(currFitness, otherFitness);
    }

    public int getMakespan() {
        return makespan;
    }

    public void setMakespan(int makespan) {
        this.makespan = makespan;
    }

    public int getTotalFlowTime() {
        return totalFlowTime;
    }

    public void setTotalFlowTime(int totalFlowTime) {
        this.totalFlowTime = totalFlowTime;
    }

    public ArrayList<Integer> getJobSchedule() {
        return jobSchedule;
    }

    public double calculateFitness(int makespan, int minMakespan, int totalFlowTime, int minTotalFlowTime) {
        return (double) (makespan - minMakespan) / minMakespan + (totalFlowTime - minTotalFlowTime) / minTotalFlowTime;
    }
}