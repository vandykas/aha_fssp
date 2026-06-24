import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Kelas yang merepresentasikan kandidat solusi (sumber makanan) pada AHA
 * 
 * Kelas ini menyimpan urutan jadwal pekerjaan jobSchedule beserta nilai fungsi 
 * objektifnya makespan dan totalFlowTime. 
 * 
 * Kelas ini mengimplementasikan Comparable untuk memungkinkan proses 
 * pengurutan atau seleksi kandidat berdasarkan fitness (makespan dan totalFlowTime)
 * 
 * Sumber:https://img1.wsimg.com/blobby/go/e8abc963-7b19-40d6-a270-eed55d317dba/AHA.pdf 
 * 
 * @author Axel, Vandyka
 * 
 */
public class FoodSource implements Comparable<FoodSource> {
    private ArrayList<Integer> jobSchedule;
    private int makespan;
    private int totalFlowTime;

    /**
     * Menginisialisasi objek dengan jadwal pekerjaan yang dihasilkan secara acak.
     * 
     * Nilai awal fungsi objektif (makespan dan total flow time) akan diatur ke 
     * nilai minimum integer karena evaluasi belum dilakukan
     *
     * @param rand     objek random
     * @param jobCount total jumlah pekerjaan yang akan dijadwalkan
     */
    public FoodSource(Random rand, int jobCount) {
        initializeJobSchedule(rand, jobCount);
        this.makespan = Integer.MAX_VALUE;
        this.totalFlowTime = Integer.MAX_VALUE;
    }

    /**
     * constructor untuk menduplikasi objek FoodSource
     *
     * @param other  FoodSource referensi yang akan diduplikasi
     */
    public FoodSource(FoodSource other) {
        this.jobSchedule = new ArrayList<>(other.jobSchedule);
        this.makespan = other.makespan;
        this.totalFlowTime = other.totalFlowTime;
    }

    /**
     * Menginisialisasi urutan jadwal pekerjaan secara acak.
     * 
     * Metode ini menghasilkan urutan indeks pekerjaan dari 0 hingga jobCount - 1, 
     * dan mengacak urutannya menggunakanCollections.shuffle.
     *
     * @param rand     objek random, digunakan untuk proses pengacakan.
     * @param jobCount total jumlah pekerjaan.
     */
    private void initializeJobSchedule(Random rand, int jobCount) {
        this.jobSchedule = new ArrayList<>();
        for (int i = 0; i < jobCount; i++) {
            this.jobSchedule.add(i);
        }
        Collections.shuffle(this.jobSchedule, rand);
    }

    /**
     * Menginisialisasi objek berdasarkan urutan jadwal pekerjaan yang sudah didefinisikan
     * 
     * Nilai awal fungsi objektif akan diatur ke minimum integer value
     * 
     * @param jobSchedule urutan jadwal pekerjaan
     */
    public FoodSource(ArrayList<Integer> jobSchedule) {
        this.jobSchedule = jobSchedule;
        this.makespan = Integer.MAX_VALUE;
        this.totalFlowTime = Integer.MAX_VALUE;
    }

    /**
     * Membandingkan sumber makanan ini dengan sumber makanan lain untuk menentukan prioritas.
     *
     * Perbandingan dilakukan dengan menghitung deviasi relatif nilai makespan dan total flow time 
     * terhadap nilai minimum dari kedua kandidat. 
     * 
     * Foodsource yang mempunyai nilai fitness yang lebih rendah dianggap lebih unggul
     *
     * @param other foodsource lain yang akan dibandingkan.
     * @return nilai negatif, nol, atau positif yang menunjukkan apakah objek ini 
     * lebih kecil, sama dengan, atau lebih besar dari objek sekarang
     * 
     */
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

    /**
     * Method untuk menghitung nilai  fitness
     * 
     * Perhitungan didasarkan pada jumlah deviasi rasional dari setiap fungsi objektif terhadap 
     * nilai referensi minimumnya. Metode ini membantu mentransformasikan dua objektif menjadi 
     * satu nilai fitness tunggal.
     *
     * @param makespan         nilai makespan 
     * @param minMakespan      nilai makespan minimum
     * @param totalFlowTime    nilai total flow time
     * @param minTotalFlowTime nilai total flow time minimum
     * 
     * @return nilai kebugaran skalar hasil perhitungan.
     */
    public double calculateFitness(int makespan, int minMakespan, int totalFlowTime, int minTotalFlowTime) {
        return (double) (makespan - minMakespan) / minMakespan
                + (double) (totalFlowTime - minTotalFlowTime) / minTotalFlowTime;
    }

    /**
     * Setter dan getter atribut yang dibutuhkan pada program
     * 
     */
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