import java.util.ArrayList;

/**
 * Merepresentasikan model untuk masalah Flow Shop Scheduling (FSS).
 * 
 * Kelas ini bertanggung jawab untuk mensimulasikan jadwal dan menghitung 
 * matriks waktu penyelesaian (completion time). 
 * 
 * Kelas ini juga menyediakan metode untuk mengevaluasi 
 * fungsi objektif berupa makespan dan total flow time dari sebuah kandidat solusi.
 * 
 * Sumber: Membuat sendiri
 * @author Axel, Alex, Vandyka, Keane
 * 
 */
public class FSS {
    private final int[][] processingTime; //durasi waktu proses pekerjaan per mesin
    private final int jobCount; //total jumlah pekerjaan
    private final int machineCount; //total jumlah mesin yang tersedia pada sistem
    private int[][] completionTime; //matriks waktu penyelesaian

    /**
     * constructor
     *
     * @param processingTime matriks 2D yang memuat durasi pemrosesan. Indeks baris 
     * merepresentasikan ID pekerjaan asli, dan indeks kolom merepresentasikan ID mesin.
     * 
     * @param jobCount       total jumlah pekerjaan yang akan dievaluasi
     * @param machineCount   total jumlah mesin yang tersedia pada sistem
     */
    public FSS(int[][] processingTime, int jobCount, int machineCount) {
        this.processingTime = processingTime;
        this.jobCount = jobCount;
        this.machineCount = machineCount;
    }

    /**
     * Menghitung matriks waktu penyelesaian secara berurutan berdasarkan jadwal yang diberikan.
     *
     * @param jobSchedule representasi urutan kandidat solusi berupa daftar ID pekerjaan.
     */
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
                completionTime[i][j] = Math.max(completionTime[i - 1][j], completionTime[i][j - 1])
                        + processingTime[curJob][j];
            }
        }
    }

    /**
     * Menghitung nilai makespan dari jadwal yang dievaluasi.
     *
     * @return nilai makespan (waktu selesai pekerjaan terakhir pada mesin terakhir).
     */
    public int calculateMakespan() {
        return this.completionTime[jobCount - 1][machineCount - 1];
    }

    /**
     * Menghitung nilai total flow time dari jadwal yang dievaluasi.
     *
     * @return nilai akumulasi waktu penyelesaian seluruh pekerjaan pada mesin terakhir.
     */
    public int calculateTotalFlowTime() {
        int totalFlowTime = 0;
        for (int i = 0; i < jobCount; i++) {
            totalFlowTime += this.completionTime[i][machineCount - 1];
        }
        return totalFlowTime;
    }
}