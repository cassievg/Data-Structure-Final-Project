public class Product {
    private int id;
    private String name;
    private int stock;
    private int minStock;

    public Product(int id, String name, int stock, int minStock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.minStock = minStock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", minStock=" + minStock +
                '}';
    }
}
