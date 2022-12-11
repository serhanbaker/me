package main.java.com.serhan.matchingengine;

import java.util.*;

public class MatchingEngine extends TreeMap<Double, LinkedList<Order>> {

    public void processOrder(String orderLine) {
        String[] orderParts = orderLine.split(",");
        int orderId = Integer.parseInt(orderParts[0]);
        char side = orderParts[1].charAt(0);
        double price = Double.parseDouble(orderParts[2]);
        int volume = Integer.parseInt(orderParts[3]);

        Order order = new Order(orderId, side, price, volume);
        executeTrades(order);

        if (order.getVolume() > 0) {
            LinkedList<Order> ordersAtPrice = this.get(order.getPrice());
            if (ordersAtPrice == null) {
                ordersAtPrice = new LinkedList<>();
            }
            ordersAtPrice.add(order);
            this.put(order.getPrice(), ordersAtPrice);
        }
    }

    private void executeTrades(Order aggressiveOrder) {
        if (aggressiveOrder.getSide() == 'B') {
            Map.Entry<Double, LinkedList<Order>> entry = this.firstEntry();
            while (entry != null) {
                LinkedList<Order> restingOrders = entry.getValue();
                LinkedList<Order> toRemove = new LinkedList<>();
                for (Order restingOrder : restingOrders) {
                    if (restingOrder.getSide() == 'S' && restingOrder.getPrice() <= aggressiveOrder.getPrice()) {
                        matchOrders(aggressiveOrder, restingOrder);
                        if (restingOrder.getVolume() == 0) {
                            toRemove.add(restingOrder);
                        }
                    }
                }
                removeOrders(toRemove);
                entry = this.higherEntry(entry.getKey());
            }
        } else {
            Map.Entry<Double, LinkedList<Order>> entry = this.lastEntry();
            while (entry != null) {
                LinkedList<Order> restingOrders = entry.getValue();
                LinkedList<Order> toRemove = new LinkedList<>();
                for (Order restingOrder : restingOrders) {
                    if (restingOrder.getSide() == 'B' && restingOrder.getPrice() >= aggressiveOrder.getPrice()) {
                        matchOrders(aggressiveOrder, restingOrder);
                        if (restingOrder.getVolume() == 0) {
                            toRemove.add(restingOrder);
                        }
                    }
                }
                removeOrders(toRemove);
                entry = this.lowerEntry(entry.getKey());
            }
        }
    }

    private void matchOrders(Order aggressiveOrder, Order restingOrder) {
        int tradeVolume = Math.min(aggressiveOrder.getVolume(), restingOrder.getVolume());
        aggressiveOrder.setVolume(aggressiveOrder.getVolume() - tradeVolume);
        restingOrder.setVolume(restingOrder.getVolume() - tradeVolume);
//        System.out.println("Trade: " + aggressiveOrder.getOrderId() + " (aggressor) <-> " + restingOrder.getOrderId() + " (resting) @ " + restingOrder.getPrice() + " for " + tradeVolume + " shares");
        System.out.println("trade " + aggressiveOrder.getId() + "," + restingOrder.getId() + "," + restingOrder.getPrice() + "," + tradeVolume);
    }

    private void removeOrders(LinkedList<Order> toRemove) {
        for (Order order : toRemove) {
            LinkedList<Order> orders = this.get(order.getPrice());
            if (orders != null) {
                orders.remove(order);

                if (orders.isEmpty())
                    this.remove(order.getPrice());
            }
        }
    }

    public void printOrderBook() {
        TreeMap<Double, int[]> volumes = new TreeMap<>();

        for (Map.Entry<Double, LinkedList<Order>> entry : this.entrySet()) {
            double price = entry.getKey();
            LinkedList<Order> orders = entry.getValue();

            int bidVolume = 0;
            int askVolume = 0;

            for (Order order : orders) {
                if (order.getSide() == 'B') {
                    bidVolume += order.getVolume();
                } else {
                    askVolume += order.getVolume();
                }
            }

            volumes.put(price, new int[] {bidVolume, askVolume});
        }

        System.out.println("\nBid #\t\t|\t\tPrice\t\t|\tAsk #");

        for (Map.Entry<Double, int[]> entry : volumes.descendingMap().entrySet()) {
            double price = entry.getKey();
            int[] volume = entry.getValue();
            int bidVolume = volume[0];
            int askVolume = volume[1];

            String tabBid;
            if (bidVolume <= 999)
                tabBid = "\t\t\t";
            else if (bidVolume <= 999999)
                tabBid = "\t\t";
            else
                tabBid = "\t";

            String tabPrice;
            if (price <= 999)
                tabPrice = "\t\t\t";
            else if (bidVolume <= 999999)
                tabPrice = "\t\t";
            else
                tabPrice = "\t";

            System.out.println(bidVolume + tabBid + "|\t" + price + tabPrice + "|\t" + askVolume);
        }
    }


    public static void main(String[] args) {
        MatchingEngine me = new MatchingEngine();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String orderLine = scanner.nextLine();
            me.processOrder(orderLine);
        }

        me.printOrderBook();
    }
}