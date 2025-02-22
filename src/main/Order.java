package main;

import java.util.Map;

/**
 * Represents an asset order in the trade market.
 * @author Zawarudo (Vinicius)
 * @version 1.0
 * @since 2025-02-20
 */
public class Order {

    /**
     * Encode order possible types. Includes {@code LIMIT}, {@code MARKET} and {@code PEG}.
     */
    public enum Type {LIMIT, MARKET, PEG}

    /**
     * Encode order possible sides. Includes {@code BUY} and {@code SELL}.
     */
    public enum Side {BUY, SELL}

    /**
     * A Map from a String representation of a type to an encoded type.
     * @see Type
     */
    public static final Map<String, Type> typeMap = Map.of("limit", Type.LIMIT, "market", Type.MARKET, "peg", Type.PEG);

    /**
     * A Map from a String representation of a side to an encoded side.
     * @see Side
     */
    public static final Map<String, Side> sideMap = Map.of("buy", Side.BUY, "sell", Side.SELL);

    private final Type type;
    private final Side side;
    private double price;
    private int quantity;

    /**
     * Creates a new Order.
     * @param type A Type.
     * @param side A Side.
     * @param price A positive real number.
     * @param quantity A positive integer number.
     * @throws IllegalArgumentException if price or quantity is not a positive integer.
     * @see Type
     * @see Side
     */
    public Order(Type type, Side side, double price, int quantity) {
        if (price <= 0) throw new IllegalArgumentException("Price must be positive"); // usually, do not make sense
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.type = type;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Return the order type.
     * @return {@code Type}
     * @see Type
     */
    public Type getType() {return type;}

    /**
     * Return the order side.
     * @return {@code Side}
     * @see Side
     */
    public Side getSide() {return side;}

    /**
     * Return the order price.
     * @return {@code Price}
     */
    public double getPrice() {return price;}

    /**
     * Return the order quantity.
     * @return {@code Quantity}
     */
    public int getQuantity() {return quantity;}

    /**
     * Change the order price.
     * @throws IllegalArgumentException if {@code price} is not positive.
     * @param price A positive real number.
     */
    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Price must be positive");
        this.price = price;
    }

    /**
     * Change the order quantity.
     * @throws IllegalArgumentException if {@code quantity} is not positive or equal to zero.
     * @param quantity A positive integer number.
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity must be positive or zero"); // notice == 0
        this.quantity = quantity;
    }
}