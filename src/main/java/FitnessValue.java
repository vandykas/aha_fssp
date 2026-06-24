/**
 * Merepresentasikan nilai fitness atau fungsi objektif dari sebuah solusi
 * dalam Multi-Objective Flow Shop Scheduling Problem (MO-FSSP).
 * 
 * Record ini bertugas untuk membungkus dua metrik utama 
 * yang akan dievaluasi dan akan diminimalkan.
 *  
 * Karena diimplementasikan sebagai record, objek ini bersifat immutable. 
 * sehingga aman dan efisien saat digunakan dalam proses iterasi populasi algoritma.
 *
 * @param makespan      Waktu penyelesaian total dari seluruh pekerjaan pada mesin terakhir.
 * @param totalFlowTime Jumlah kumulatif waktu penyelesaian dari semua pekerjaan. 
 * 
 * Sumber: Membuat sendiri
 * @author Vandyka
 */
public record FitnessValue(int makespan, int totalFlowTime) {}