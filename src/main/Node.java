/**
 * Node generic representation
 * @param <T> data type to save at node
 */
public class Node<T> {

    /**
     * Data saved
     */
    public T data;

    /**
     * Pointer to the next node in the list
     */
    public Node<T> next;

    /**
     * Pointer to the previous node in the list
     */
    public Node<T> prev;

    /**
     * Creates a new node saving {@code data}
     * @param data data to be saved
     */
    public Node(T data) {
        this.data = data;
    }
}
