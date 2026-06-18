import java.util.ArrayList;
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
        this.rand = rand;
        this.visitTable = new int[N][N];
        this.population = new ArrayList<>();
        this.jobCount = jobCount;
    }

    public void run() {
        initializePopulation();
        for (int i = 0; i < maxIter; i++) {

        }
    }

    private void initializePopulation() {
        for (int i = 0; i < N; i++) {
            Hummingbird hummingbird = new Hummingbird(new FoodSource(rand, jobCount));
            evaluate(hummingbird);
            population.add(hummingbird);
        }
    }

    private void evaluate(Hummingbird hummingbird) {
        fss.calculateCompletionTime(hummingbird.getFoodSource().getJobSchedule());
        hummingbird.getFoodSource().setMakespan(fss.calculateMakespan());
        hummingbird.getFoodSource().setTotalFlowTime(fss.calculateTotalFlowTime());
    }

    private Hummingbird foraging(Hummingbird hummingbird, FoodSource foodSource) {
        double prob = this.rand.nextDouble();
        if (prob < 0.33)
            return guidedForaging(hummingbird, foodSource);
        if (prob < 0.67)
            return territorialForaging(hummingbird, foodSource);
        else
            return migrationForaging(hummingbird, foodSource);
    }

    private Hummingbird guidedForaging(Hummingbird hummingbird, FoodSource foodSource) {
        // Tentukan jenis terbang secara acak (1/3 masing-masing)
        double rFlight = rand.nextDouble();
        int jumlahSwap;
        if (rFlight < 1.0 / 3.0) {
            // Axial: swap 1 posisi
            jumlahSwap = 1;
        } else if (rFlight < 2.0 / 3.0) {
            // Diagonal: swap 2 hingga (n/2) posisi
            jumlahSwap = 2 + rand.nextInt(Math.max(1, jobCount / 2 - 1));
        } else {
            // Omnidirectional: swap banyak posisi (mendekati target sepenuhnya)
            jumlahSwap = jobCount / 2 + rand.nextInt(
                    Math.max(1, jobCount - jobCount / 2));
        }

        ArrayList<Integer> source = (ArrayList<Integer>) hummingbird.getFoodSource().getJobSchedule().clone();
        ArrayList<Integer> target = foodSource.getJobSchedule();

        // Lakukan sejumlah swap agar mendekati x_i secara parsial
        // (analogi: a * D * (x_i - x_tar) → bergerak sebagian ke arah x_i)
        for (int s = 0; s < jumlahSwap; s++) {
            // Pilih posisi acak yang berbeda antara urutan dan asal
            List<Integer> posBeda = new ArrayList<>();
            for (int k = 0; k < jobCount; k++) {
                if (target.get(k) != source.get(k))
                    posBeda.add(k);
            }
            if (posBeda.isEmpty())
                break;

            // Pilih satu posisi beda secara acak, swap elemen agar mendekati asal
            int pos = posBeda.get(rand.nextInt(posBeda.size()));
            int nilaiTarget = source.get(pos);
            // Cari di mana nilaiTarget berada di urutan saat ini
            int posNilai = -1;
            for (int k = 0; k < jobCount; k++) {
                if (source.get(k) == nilaiTarget) {
                    posNilai = k;
                    break;
                }
            }
            if (posNilai != -1 && posNilai != pos) {
                int temp = source.get(pos);
                source.set(pos, source.get(posNilai));
                source.set(posNilai, temp);
            }
        }

        return new Hummingbird(new FoodSource(source));
    }

    private Hummingbird territorialForaging(Hummingbird hummingbird, FoodSource foodSource) {
        // TODO: Buat algoritma 
        return null;
    }

    private Hummingbird migrationForaging(Hummingbird hummingbird, FoodSource foodSource) {
        // TODO: Buat algoritma 
        return null;
    }
}
