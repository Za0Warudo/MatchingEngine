import java.util.*;
import java.lang.Math;

/**
 * Representation of a trade book for a single asset, with a worst-case complexity of O(n).
 * @author Zawarudo (Vinicius)
 * @version 2.0
 * @since 2025-02-20
 */
public class Book {

    private final TreeMap<String, Node<Order>> id = new TreeMap<>(); // id (String) -> Node (Order)
    private final TreeMap<Double, LinkedList<Order>> sell = new TreeMap<>(); // price (Double) -> List<Order>
    private final TreeMap<Double, LinkedList<Order>> buy = new TreeMap<>(); // price (Double) -> List<Order>

    /**
     * Print all orders in the book.
     * @see Order
     */
    public void show () {
        System.out.println("Book:");
        System.out.println("Sell Orders:");
        for (Map.Entry<Double, LinkedList<Order>> entry : sell.entrySet())
            for (Order o : entry.getValue()) System.out.println(o.getQuantity() + " @ " + o.getPrice());

        System.out.println("**************");

        System.out.println("Buy Orders:");
        for (Map.Entry<Double, LinkedList<Order>> entry : buy.entrySet())
            for (Order o : entry.getValue()) System.out.println(o.getQuantity() + " @ " + o.getPrice());
    }

    /**
     * Decode the String {@code str} to an Order.
     * @param str string encoding an Order.
     * @throws IllegalArgumentException if the String is not in the specified format.
     * @return {@code Order} the decoded Order.
     */
     private Order decode (String str) {
         try {
             String[] parts = str.split(" ");

             // info
             Order.Type type;
             Order.Side side;
             double price;
             int quantity;

             type = Order.typeMap.get(parts[0]); // the first argument is always the order type

             if (type == Order.Type.LIMIT || type == Order.Type.PEG) {
                     if (Order.sideMap.containsKey(parts[1])) { // is 'buy' or 'sell'
                         side = Order.sideMap.get(parts[1]);
                         price = Double.parseDouble(parts[2]);
                         quantity = Integer.parseInt(parts[3]);
                     }
                     else if (parts[1].equals("bid") || parts[1].equals("offer")) {
                         if (!Order.sideMap.containsKey(parts[2])) throw new IllegalArgumentException(); // do not contain the order side
                         side = Order.sideMap.get(parts[2]);

                         if (parts[1].equals("bid") && buy.isEmpty()) throw new IllegalArgumentException(); // no buy order in the book
                         if (parts[1].equals("offer") && sell.isEmpty()) throw new IllegalArgumentException(); // no sell order in the book

                         price = parts[1].equals("bid") ? buy.lastKey() : sell.firstKey(); // current bid or offer

                         if (parts.length == 4) quantity = Integer.parseInt(parts[3]); // quantity
                         else { // give an alternative value to compare with bid or offer (look at the given example)
                             price = parts[1].equals("bid") ? Math.max(price, Double.parseDouble(parts[3])) : Math.min(price, Double.parseDouble(parts[3]));
                             quantity = Integer.parseInt(parts[4]);
                         }
                     }
                     else throw new IllegalArgumentException();
                     return new Order(type, side, price, quantity);
             }
             else if (type == Order.Type.MARKET) {
                     side = Order.sideMap.get(parts[1]);
                     if (side == null) throw new IllegalArgumentException();
                     quantity = Integer.parseInt(parts[2]);
                     price = side == Order.Side.BUY ? sell.lastKey() : buy.firstKey(); // always can trade with the market, could raise null error
                     return new Order(type, side, price, quantity);
             } else throw new IllegalArgumentException();
         }
         catch (Exception e) { throw new IllegalArgumentException("Wrong input format");}  // invalid expression
     }

    /**
     * Add a new encoded order to the book.
     * @param str An Order encoded as a String.
     * @throws IllegalArgumentException if the String given is not in the specified format.
     * @see Order
     */
    public void add (String str) {
        try {
            Order order = decode(str);
            add(order);
        }
        catch (Exception e) { throw new IllegalArgumentException(e);}
    }

    /**
     * Add an order to the book and trade it.
     * @param order an Order.
     * @see Order
     */
    private void add(Order order) {
        trade(order); // try to trade Order in the market
        if ((order.getType() == Order.Type.LIMIT || order.getType() == Order.Type.PEG) && order.getQuantity() > 0) { // if false, the order has been already traded.
            put(order);
            System.out.println("Order created: " + order.getSide() + " " +order.getQuantity() + " @ " + order.getPrice() + " " + order);
        }
    }

    /**
     * Save order in the book.
     * @param order new Order.
     * @see Order
     */
    private void put(Order order) {
        TreeMap<Double, LinkedList<Order>> side = order.getSide() == Order.Side.BUY? buy : sell;
        Node<Order> newnode; // save in id for fast remove
        if (side == buy && !side.isEmpty() && order.getPrice() > side.lastKey()) { // a new bid
            LinkedList<Order> list = side.get(side.lastKey()); // old bid list
            newnode = update(order, side, list);
        }
        else if (!side.isEmpty() && order.getPrice() < side.firstKey()) { // new offer
            LinkedList<Order> list = side.get(side.firstKey()); // old offer list
            newnode = update(order, side, list);
        }
        else newnode = side.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).push(order); // not in bid or offer

        id.put(order.toString(), newnode); // save node
    }

    /**
     * Update peg Orders in the book.
     * @param order New order that is a bid or an offer.
     * @param side Trade side.
     * @param list Old bid or offer Orders list.
     * @return {@code node} new node added to the book.
     */
    private Node<Order> update(Order order, TreeMap<Double, LinkedList<Order>> side, LinkedList<Order> list) {
        Node<Order> newnode;
        LinkedList<Order> rm = new LinkedList<>(); // all orders pegs to bid

        for (Order o : list) { if (o.getType() == Order.Type.PEG) rm.push(o); }

        side.computeIfAbsent(order.getPrice(), k -> new LinkedList<>());
        for (Order o : rm) {
            remove(o.toString()); // remove this peg order from old bid
            o.setPrice(order.getPrice()); // set new price
            add(o);
        }
        newnode = side.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).push(order); // add new order
        return newnode;
    }

    /**
     * Cancels an Order identify by the given identifier.
     * @param ident Order identifier.
     * @see Order
     */
    public void cancel (String ident) {
        if (!id.containsKey(ident)) throw new NoSuchElementException("There is no such identifier in the book"); // order is not in the book
        Node<Order> node = id.get(ident);
        System.out.println("Order cancelled");
        remove(node);
    }

    /**
     * Removes an Order identify by the given identifier.
     * @param ident Order identifier.
     * @see Order
     */
    private void remove (String ident) {
        if (!id.containsKey(ident)) return; // order is not in the book
        Node<Order> node = id.remove(ident);
        remove(node);
    }

    /**
     * Remove the order associate with the given node from the book.
     * @param node A Node with an Order.
     * @see Order
     */
    private void remove (Node<Order> node) {
        Order order = node.data; // the order associate with node
        TreeMap<Double, LinkedList<Order>> side = order.getSide() == Order.Side.BUY? buy : sell;
        side.get(order.getPrice()).remove(node); // remove order from List
        if (side.get(order.getPrice()).isEmpty()) side.remove(order.getPrice()); // empty List
    }

    /**
     * Try to trade the given Order with the Orders in the book.
     * @param order An Order.
     * @see Order
     */
    private void trade(Order order) {
        TreeMap<Double, LinkedList<Order>> otherside = order.getSide() == Order.Side.BUY? sell : buy;
        Map.Entry<Double, LinkedList<Order>> BidOrOffer = otherside == sell ? otherside.firstEntry() : otherside.lastEntry();
        while (BidOrOffer != null && order.getQuantity() > 0) { // try to trade
            Map.Entry<Integer, Double> pair = trade(order, BidOrOffer); // {quantity, price}
            if (pair == null) break; // cannot trade
            BidOrOffer = otherside == sell?  otherside.firstEntry() : otherside.lastEntry(); // new bid or offer
            System.out.println("Trade: price " + pair.getValue() + ", quantity: " + pair.getKey());
        }
    }

    /**
     * Compute if possible the trade between the given order and the bidOrOffer value given.
     * @param order An order.
     * @param BidOrOffer The bid Or the offer of the book.
     * @return A pair with the quantity sold and the sold price. If not possible to trade return {@code null}.
     * @see Order
     */
    private AbstractMap.SimpleEntry<Integer, Double> trade(Order order, Map.Entry<Double, LinkedList<Order>> BidOrOffer) {
        if (order.getSide() == Order.Side.BUY && BidOrOffer.getKey() > order.getPrice()) return null; // no valid trade
        if (order.getSide() == Order.Side.SELL && BidOrOffer.getKey() < order.getPrice()) return null; // no valid trade

        Node<Order> node = BidOrOffer.getValue().getHead(); // never an empty list
        Order o = node.data;

        if (o.getQuantity() > order.getQuantity()) { // can trade all
            int qty = order.getQuantity();
            o.setQuantity(o.getQuantity() - qty); // remainder
            order.setQuantity(0);
            return new AbstractMap.SimpleEntry<>(qty, o.getPrice());
        }
        else if (o.getQuantity() == order.getQuantity()) { // can trade all
            int qty = o.getQuantity();
            remove(o.toString());
            order.setQuantity(0);
            return new AbstractMap.SimpleEntry<>(qty, o.getPrice());
        }
        else { // partial trade
            int qty = o.getQuantity();
            remove(o.toString());
            order.setQuantity(order.getQuantity() - qty); // remainder
            return new AbstractMap.SimpleEntry<>(qty, o.getPrice());
        }
    }
}