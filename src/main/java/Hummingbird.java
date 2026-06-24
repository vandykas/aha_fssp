/**
 * Merepresentasikan individu pada Artificial Hummingbird Algorithm (AHA).
 * 
 * Kelas ini memiliki FoodSource, karena diasumsikan bahwa 
 * setiap kolibri berada dalam suatu foodsource, 
 * bertindak sebagai representasi dari kandidat solusi pada ruang pencarian.
 * 
 * Sumber: Membuat sendiri
 * @author Axel, Vandyka
 */
public class Hummingbird {
    
    /**
     * foodsource yang dimiliki / sedang didatangi oleh hummingbird
     */
    private FoodSource foodSource;

    /**
     * Konstruktor untuk menginisialisasi objek berdasarkan sumber makanan tertentu.
     *
     * @param foodSource sumber makanan yang merupakan suatu solusi
     */
    public Hummingbird(FoodSource foodSource) {
        this.foodSource = foodSource;
    }

    /**
     * Konstruktor untuk menduplikasi objek Hummingbird beserta propertinya.
     *
     * @param other objek Hummingbird referensi yang akan diduplikasi.
     */
    public Hummingbird(Hummingbird other) {
        this.foodSource = new FoodSource(other.foodSource);
    }

    /**
     * Mengembalikan objek sumber makanan yang memuat kandidat solusi saat ini.
     *
     * @return foodsource yang sedang didatangi oleh hummingbird ini.
     */
    public FoodSource getFoodSource() {
        return foodSource;
    }
}