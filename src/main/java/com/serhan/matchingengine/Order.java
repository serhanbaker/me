package main.java.com.serhan.matchingengine;

public class Order {
    private int id;
    private char side;
    private double price;
    private int volume;

    public Order(int id, char side, double price, int volume) {
        this.id = id;
        this.side = side;
        this.price = price;
        this.volume = volume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", side=" + side +
                ", price=" + price +
                ", volume=" + volume +
                '}';
    }
}
