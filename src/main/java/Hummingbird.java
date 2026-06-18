import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Hummingbird implements Comparable<Hummingbird> {
    private ArrayList<Integer> jobSchedule;
    private int makespan;
    private int totalFlowTime;

    public Hummingbird(Random rand, int jobCount) {
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

    public Hummingbird(ArrayList<Integer> jobSchedule) {
        this.jobSchedule = jobSchedule;
        this.makespan = Integer.MAX_VALUE;
        this.totalFlowTime = Integer.MAX_VALUE;
    }

    @Override
    public int compareTo(Hummingbird other) {
        int currMakespan = this.getMakespan();
        int otherMakespan = other.getMakespan();

        int currTotalFlowTime = this.getTotalFlowTime();
        int otherTotalFlowTime = other.getTotalFlowTime();
        
        double minMakespan = Math.min(otherMakespan, currMakespan);
        double minTotalFlowTime = Math.min(otherTotalFlowTime, currTotalFlowTime);

        double currFitness = (currMakespan - minMakespan) / minMakespan + (currTotalFlowTime - minTotalFlowTime) / minTotalFlowTime;
        double otherFitness = (otherMakespan - minMakespan) / minMakespan + (otherTotalFlowTime - minTotalFlowTime) / minTotalFlowTime;
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
}
