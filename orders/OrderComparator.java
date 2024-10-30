package orders;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    private static OrderComparator instance;

    public static OrderComparator getInstance() {
        if (instance == null) {
            instance = new OrderComparator();
        }
        return instance;
    }

    @Override
    public int compare(Order o1, Order o2) {
        if (o1.getType().equals(OrderType.BUY)) {
            if (o1.getPriceLimit() != o2.getPriceLimit()) {
                return Double.compare(o2.getPriceLimit(), o1.getPriceLimit());
            } else {
                return Integer.compare(o1.getTurn(), o2.getTurn());
            }
        } else {
            if (o1.getPriceLimit() != o2.getPriceLimit()) {
                return Double.compare(o1.getPriceLimit(), o2.getPriceLimit());
            } else {
                return Integer.compare(o1.getTurn(), o2.getTurn());
            }
        }
    }

}
