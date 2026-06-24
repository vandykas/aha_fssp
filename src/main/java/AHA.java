import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AHA {
    private FSS fss;
    private Random rand;
    private int[][] visitTable;
    private List<Hummingbird> population;
    private int N;
    private int maxIter;
    private int jobCount;

    public AHA(FSS fss, Random rand, int N, int maxIter, int jobCount) {
        this.fss = fss;
        this.rand = rand;
        this.N = N;
        this.maxIter = maxIter;
        this.jobCount = jobCount;
        this.visitTable = new int[N][N];
        this.population = new ArrayList<>();
    }

    public Hummingbird run() {
        initializePopulation();
        initializeVisitTable();

        Hummingbird best = new Hummingbird(population.get(0));
        for (int t = 1; t <= maxIter; t++) {
            for (int i = 0; i < N; i++) {
                double prob = this.rand.nextDouble();
                if (prob <= 0.5) {
                    int target = findTarget(i);
                    Hummingbird newHummingbird = guidedForaging(population.get(i), population.get(target).getFoodSource());
                    evaluate(newHummingbird);
                    
                    boolean isForagingSuccess = (population.get(i).getFoodSource().compareTo(newHummingbird.getFoodSource()) > 0);
                    updateVisitTableGuidedForaging(i, target, isForagingSuccess);
                    if (isForagingSuccess) {
                        population.set(i, newHummingbird);
                    }
                }
                else {
                    Hummingbird newHummingbird = territorialForaging(population.get(i));
                    evaluate(newHummingbird);

                    boolean isForagingSuccess = (population.get(i).getFoodSource().compareTo(newHummingbird.getFoodSource()) > 0);
                    updateVisitTableTerritorialForaging(i, isForagingSuccess);
                    if (isForagingSuccess) {
                        population.set(i, newHummingbird);
                    }
                }

                if (best.getFoodSource().compareTo(population.get(i).getFoodSource()) > 0){
                    best = new Hummingbird(population.get(i));
                }
            }

            if (t % (2 * N) == 0) {
                migrationForaging();
            }
        }
        return best;
    }

    private void initializePopulation() {
        for (int i = 0; i < N; i++) {
            Hummingbird hummingbird = new Hummingbird(new FoodSource(rand, jobCount));
            evaluate(hummingbird);
            population.add(hummingbird);
        }
    }

    private void initializeVisitTable() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                visitTable[i][j] = (i == j) ? -1 : 0;
            }
        }
    }

    private int findTarget(int currHummingBird) {
        int timeLastVisited = visitTable[currHummingBird][0];
        int pickedFoodSource = 0;
        for (int i = 1; i < N; i++) {
            if (visitTable[currHummingBird][i] > timeLastVisited) {
                timeLastVisited = visitTable[currHummingBird][i];
                pickedFoodSource = i;
            } 
        }
        return pickedFoodSource;
    }

    private void evaluate(Hummingbird hummingbird) {
        fss.calculateCompletionTime(hummingbird.getFoodSource().getJobSchedule());
        hummingbird.getFoodSource().setMakespan(fss.calculateMakespan());
        hummingbird.getFoodSource().setTotalFlowTime(fss.calculateTotalFlowTime());
    }

    private Hummingbird guidedForaging(Hummingbird hummingbird, FoodSource foodSource) {
        int swapCount = calculateSwapCount();
        ArrayList<Integer> source = (ArrayList<Integer>) hummingbird.getFoodSource().getJobSchedule().clone();
        ArrayList<Integer> target = foodSource.getJobSchedule();

        for (int i = 0; i < swapCount; i++) {
            List<Integer> diffPos = new ArrayList<>();
            for (int j = 0; j < jobCount; j++) {
                if (target.get(j) != source.get(j))
                    diffPos.add(j);
            }
            if (diffPos.isEmpty())
                break;

            int pos = diffPos.get(rand.nextInt(diffPos.size()));
            int targetVal = target.get(pos);
            int targetIdx = source.indexOf(targetVal);

            Collections.swap(source, pos, targetIdx);
        }

        return new Hummingbird(new FoodSource(source));
    }

    private Hummingbird territorialForaging(Hummingbird hummingbird) {
        int swapCount = calculateSwapCount();
        ArrayList<Integer> candidateSchedule = (ArrayList<Integer>) hummingbird.getFoodSource().getJobSchedule()
                .clone();

        for (int i = 0; i < swapCount; i++) {
            int pos1 = rand.nextInt(jobCount);
            int pos2 = rand.nextInt(jobCount);

            if (pos1 != pos2) {
                Collections.swap(candidateSchedule, pos1, pos2);
            }
        }

        return new Hummingbird(new FoodSource(candidateSchedule));
    }

    private void migrationForaging() {
        int worstIdx = 0;
        FoodSource worstFoodSource = population.get(0).getFoodSource();
        for (int i = 1; i < N; i++) {
            if (worstFoodSource.compareTo(population.get(i).getFoodSource()) < 0) {// aaaaaaaaaaaaaa
                worstFoodSource = population.get(i).getFoodSource();
                worstIdx = i;
            }
        }

        FoodSource newFoodSource = new FoodSource(rand, jobCount);
        Hummingbird newHummingbird = new Hummingbird(newFoodSource);
        evaluate(newHummingbird);
        population.set(worstIdx, newHummingbird);

        for (int i = 0; i < N; i++) {
            if (i == worstIdx)
                continue;
            visitTable[worstIdx][i]++;
        }

        for (int i = 0; i < N; i++) {
            if (worstIdx == i)
                continue;
            int maxL = 0;
            for (int j = 0; j < N; j++) {
                if (i == j)
                    continue;
                if (visitTable[i][j] > maxL)
                    maxL = visitTable[i][j];
            }
            visitTable[i][worstIdx] = maxL + 1;
        }
    }

    private int calculateSwapCount() {
        double rFlight = rand.nextDouble();
        int swapCount;
        if (rFlight < 1.0 / 3.0) {
            swapCount = 1;
        } else if (rFlight < 2.0 / 3.0) {
            swapCount = 2 + rand.nextInt(Math.max(1, jobCount / 2 - 1));
        } else {
            swapCount = jobCount / 2 + rand.nextInt(
                    Math.max(1, jobCount - jobCount / 2));
        }
        return swapCount;
    }

    private void updateVisitTableTerritorialForaging(int i, boolean success) {
        for (int j = 0; j < N; j++) {
            if (j == i)
                continue;
            visitTable[i][j]++;
        }

        if (success) {
            for (int j = 0; j < N; j++) {
                if (j == i)
                    continue;
                int maxL = 0;
                for (int l = 0; l < N; l++) {
                    if (l == j)
                        continue;
                    if (visitTable[j][l] > maxL)
                        maxL = visitTable[j][l];
                }
                visitTable[j][i] = maxL + 1;
            }
        }
    }

    private void updateVisitTableGuidedForaging(int i, int target, boolean success) {
        for (int j = 0; j < N; j++) {
            if (j == i || j == target)
                continue;
            visitTable[i][j]++;
        }
        visitTable[i][target] = 0;

        if (success) {
            for (int j = 0; j < N; j++) {
                if (j == i)
                    continue;
                int maxL = 0;
                for (int l = 0; l < N; l++) {
                    if (l == j)
                        continue;
                    if (visitTable[j][l] > maxL)
                        maxL = visitTable[j][l];
                }
                visitTable[j][i] = maxL + 1;
            }
        }
    }
}
