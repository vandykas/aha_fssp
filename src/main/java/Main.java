import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Kelas untuk mengeksekusi program optimasi Multi-Objective 
 * Flow Shop Scheduling Problem (MO-FSSP) menggunakan Artificial Hummingbird Algorithm
 * 
 * Kelas ini bertanggung jawab atas penanganan operasi I/O (Input/Output). 
 * 
 * Hal ini mencakup:
 * pembacaan parameter algoritma dan data matriks masalah (waktu pemrosesan) dari file teks eksternal, 
 * menginisialisasi FSS, 
 * menjalankan AHA, dan mencetak solusi terbaik ke output (konsol).
 * 
 * Sumber: Membuat sendiri dengan bantuan LLM
 * @author Axel, Alex, Vandyka, Keane
 */
public class Main {
    
    /**
     * Metode utama yang mengendalikan alur eksekusi program.
     * 
     * Metode ini memvalidasi argumen yang diberikan, mem-parsing isi dari file hyperparameter 
     * dan file input, serta menginstansiasi objek pendukung. Pengacakan (random generator) 
     * diinisialisasi dengan seed tetap (42) untuk keperluan pengujian dan reproduktibilitas hasil.
     * 
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar Main.jar <hyperparameter-file-location> <input-file-location>");
        }

        int jobCount = 0;
        int machineCount = 0;
        int[][] processingTime = null;

        try {
            File inputFile = new File(args[1]);
            try (BufferedReader bf = new BufferedReader(new FileReader(inputFile))) {
                String line = bf.readLine();
                String[] input = line.trim().split("\\s+");
                jobCount =  Integer.parseInt(input[0]);
                machineCount =  Integer.parseInt(input[1]);
                processingTime = new int[jobCount][machineCount];

                int i = 0;
                while ((line = bf.readLine()) != null) {
                    input = line.trim().split("\\s+");
                    for (int j = 0; j < jobCount; j++) {
                        processingTime[j][i] = Integer.parseInt(input[j]);
                    }
                    i++;
                }
            }
            catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }

        }
        catch (NullPointerException e) {
            System.out.println("File not found: " + args[1]);
        }

        int N = 0;
        int maxIter = 0;
        try {
            File paramFile = new File(args[0]);
            try (BufferedReader bf = new BufferedReader(new FileReader(paramFile))) {
                String line = bf.readLine();
                String[] input = line.trim().split("\\s+");
                N = Integer.parseInt(input[0]);
                maxIter = Integer.parseInt(input[1]);
            }
            catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }
        }
        catch (NullPointerException e) {
            System.out.println("File not found: " + args[0]);
        }

        FSS fss = new FSS(processingTime, jobCount, machineCount);

        // Menggunakan seed 42 untuk hasil pencarian acak konsisten setiap run
        Random rand = new Random(42); 
        AHA aha = new AHA(fss, rand, N, maxIter, jobCount);
        Hummingbird best = aha.run();

        System.out.println("=== Best Hummingbird ===");
        ArrayList<Integer> sol = best.getFoodSource().getJobSchedule();
        System.out.print("[" + (sol.getFirst() + 1));
        for (int i = 1; i < sol.size(); i++) {
            System.out.print(", " + (sol.get(i) + 1));
        }
        System.out.print("]\n");
        System.out.println("Makespan: " + best.getFoodSource().getMakespan());
        System.out.println("Tft: " + best.getFoodSource().getTotalFlowTime());
    }
}