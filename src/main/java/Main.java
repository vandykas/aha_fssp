import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        File dataFile = new File("src/main/resources/ins_500_20_00.txt");

        int jobCount = 0; 
        int machineCount = 0;
        int[][] processingTime = null;

        if (dataFile.exists()) {
            try (BufferedReader bf = new BufferedReader(new FileReader(dataFile))) {
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
        FSS fss = new FSS(processingTime, jobCount, machineCount);
        Random rand = new Random(42);
        File outputFile = new File("src/main/resources/result.csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write("population_size,makespan,total_flow_time\n");
            for (int i = 5; i <= 100; i+=5) {
                AHA aha = new AHA(fss, rand, i, 1000, jobCount);
                Hummingbird best = aha.run();

                ArrayList<Integer> sol = best.getFoodSource().getJobSchedule();
                bw.write(i + "," + best.getFoodSource().getMakespan() + "," + best.getFoodSource().getTotalFlowTime() + "\n");
                System.out.println(i);

                // System.out.print("[" + (sol.get(0) + 1));
                // for (int i = 1; i < sol.size(); i++) {
                //     System.out.print(", " + (sol.get(i) + 1));
                // }
                // System.out.print("]\n");
                // System.out.println("Makespan: " + best.getFoodSource().getMakespan());
                // System.out.println("Tft: " + best.getFoodSource().getTotalFlowTime());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
