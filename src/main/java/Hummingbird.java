public class Hummingbird{
    private FoodSource foodSource;

    public Hummingbird(FoodSource foodSource) {
        this.foodSource = foodSource;
    }

    public Hummingbird(Hummingbird other) {
        this.foodSource = new FoodSource(other.foodSource);
    }

    public FoodSource getFoodSource() {
        return foodSource;
    }
}
