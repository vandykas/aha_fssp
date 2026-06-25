# Artificial Hummingbird Algorithm (AHA) for MO-FSSP
Proyek ini membahas penyelesaian Multi-Objective Flow Shop Scheduling 
Problem (MO-FSSP), yaitu permasalahan penjadwalan 
pekerjaan pada beberapa mesin dengan tujuan mengoptimalkan 
beberapa kriteria sekaligus, seperti meminimalkan makespan, 
keterlambatan, dan konsumsi energi. Karena MO-FSSP 
merupakan masalah NP-hard dengan ruang pencarian 
yang sangat besar, digunakan Artificial Hummingbird Algorithm (AHA), 
sebuah algoritma metaheuristik berbasis swarm 
intelligence yang terinspirasi dari perilaku pencarian makan 
burung kolibri. Melalui mekanisme eksplorasi dan 
eksploitasi yang adaptif, AHA digunakan untuk 
menemukan solusi Pareto yang berkualitas secara 
efisien pada permasalahan penjadwalan multiobjektif.

## Cara Menjalankan
Kode program sudah diubah menjadi app.jar sehingga untuk menjalankan
kode hanya perlu menjalankan file ini dengan
```
java -jar app.jar <lokasi-hyperparameter.txt> <lokasi-input.txt>
```

### Hyperparameter
Contoh isi file hyperparameter dapat dilihat pada
```
src/main/resources/hyperparameter.txt
```
Isi file hyperparameter
```
N max_iter
```
Penjelasan hyperparameter:
- N : Jumlah populasi hummingbird dan food source
- max_iter : Jumlah pergerakan (foraging) hummingbird

### Input
File input menggunakan struktur Taillard Benchmark dan 
sudah disediakan beberapa file pada folder berikut
```
src/main/resources/
```
Struktur file input seperti berikut ini. 
```
n m seed upper_bound lower_bound
P11 ... P1n
P21 ... P2n
 .  ...  .
 .  ...  .
Pm1 ... Pmn
```
Penjelasan variabel:
- n : Jumlah pekerjaan
- m : Jumlah mesin
- Pij : Waktu proses job ke j di mesin ke i 
