/*
 * Solusi.java
 * Representasi satu solusi (posisi hummingbird / food source) untuk
 * Multi-Objective Flow Shop Scheduling Problem (MO-FSSP).
 *
 * Dua fungsi objektif yang diminimalkan secara bersamaan:
 *   1. MAKESPAN   : waktu selesai job terakhir di mesin terakhir
 *   2. TOTAL FLOW TIME (TFT) : jumlah waktu selesai seluruh job di mesin terakhir
 *
 * Representasi solusi: permutasi urutan job (Permutation Encoding)
 *   Contoh: [J3, J1, J4, J2] → job ke-3 dikerjakan pertama, dst.
 */
import java.util.Random;

/**
 * @author AHA Conversion
 */
public class Solusi {

    private int[] urutanPekerjaan;  // permutasi job = posisi hummingbird
    private int[] urutanMesin;      // selalu [1,2,...,m] untuk flow shop
    private int   makespan;         // objektif 1: C_max
    private int   totalFlowTime;    // objektif 2: TFT = Σ C_i
    private boolean isDominan;      // flag untuk Pareto dominance check

    /**
     * Konstruktor: bangkitkan permutasi job secara acak.
     */
    public Solusi(int banyakPekerjaan, int banyakMesin) {
        Random rand = new Random();
        this.urutanPekerjaan = new int[banyakPekerjaan];
        this.urutanMesin     = new int[banyakMesin];
        this.makespan        = Integer.MAX_VALUE;
        this.totalFlowTime   = Integer.MAX_VALUE;
        this.isDominan       = false;

        // Bangkitkan permutasi acak tanpa duplikat
        for (int i = 0; i < urutanPekerjaan.length; i++) {
            int hslRandom = rand.nextInt(banyakPekerjaan) + 1;
            if (i == 0) {
                this.urutanPekerjaan[i] = hslRandom;
            } else {
                while (!this.cekUnik(i, hslRandom)) {
                    hslRandom = rand.nextInt(banyakPekerjaan) + 1;
                }
                this.urutanPekerjaan[i] = hslRandom;
            }
        }
        for (int i = 0; i < urutanMesin.length; i++) {
            urutanMesin[i] = i + 1;
        }
    }

    /**
     * Konstruktor dengan urutan pekerjaan yang sudah ditentukan.
     * Dipakai setelah operasi gerak AHA (guided/territorial/migration).
     */
    public Solusi(int[] urutanJob, int banyakMesin) {
        this.urutanPekerjaan = urutanJob.clone();
        this.urutanMesin     = new int[banyakMesin];
        this.makespan        = Integer.MAX_VALUE;
        this.totalFlowTime   = Integer.MAX_VALUE;
        this.isDominan       = false;
        for (int i = 0; i < urutanMesin.length; i++) {
            urutanMesin[i] = i + 1;
        }
    }

    private boolean cekUnik(int penunjuk, int hslRandom) {
        for (int i = 0; i < penunjuk; i++) {
            if (this.urutanPekerjaan[i] == hslRandom) return false;
        }
        return true;
    }

    /**
     * Cek apakah solusi ini mendominasi solusi lain.
     * Solusi A mendominasi solusi B jika:
     *   A lebih baik atau sama di SEMUA objektif, DAN
     *   A lebih baik di MINIMAL SATU objektif.
     */
    public boolean mendominasi(Solusi lain) {
        boolean lebihBaikSatu = false;
        if (this.makespan > lain.makespan || this.totalFlowTime > lain.totalFlowTime)
            return false;
        if (this.makespan < lain.makespan || this.totalFlowTime < lain.totalFlowTime)
            lebihBaikSatu = true;
        return lebihBaikSatu;
    }

    /**
     * Fitness gabungan untuk pemilihan target pada guided foraging AHA.
     * Menggunakan normalisasi sederhana:
     *   fitness = 1 / (1 + makespan + totalFlowTime)
     * Semakin kecil kedua objektif, semakin besar fitness (nektar lebih banyak).
     */
    public double getFitness() {
        return 1.0 / (1.0 + makespan + totalFlowTime);
    }

    public int[]   getUrutanPekerjaan()              { return urutanPekerjaan; }
    public int[]   getUrutanMesin()                  { return urutanMesin; }
    public int     getMakespan()                     { return makespan; }
    public int     getTotalFlowTime()                { return totalFlowTime; }
    public boolean isDominan()                       { return isDominan; }
    public void    setMakespan(int makespan)         { this.makespan = makespan; }
    public void    setTotalFlowTime(int tft)         { this.totalFlowTime = tft; }
    public void    setDominan(boolean d)             { this.isDominan = d; }
}
