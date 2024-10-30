package stockmarket;

public class Stock {
    private final String symbol;

    public Stock(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Stock other = (Stock) obj;
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

}
