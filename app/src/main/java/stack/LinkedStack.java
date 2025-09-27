package stack;

public class LinkedStack<E> implements Stack<E> {
    // ---------- Inner Node class ----------
    private static class Node<E> {
        private E element;       // reference to the stored element
        private Node<E> next;    // reference to the next node

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        public E getElement() { return element; }
        public Node<E> getNext() { return next; }
        public void setNext(Node<E> next) { this.next = next; }
    }

    // ---------- Instance variables ----------
    private Node<E> head = null;   // top of the stack
    private int size = 0;          // number of elements in stack

    // ---------- Constructor ----------
    public LinkedStack() { }

    // ---------- Methods ----------
    /** Returns the number of elements in the stack. */
    @Override
    public int size() {
        return size;
    }

    /** Tests whether the stack is empty. */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Adds an element at the top of the stack. */
    @Override
    public void push(E e) {
        head = new Node<>(e, head);   // new node points to old head
        size++;
    }

    /** Returns, but does not remove, the element at the top of the stack. */
    @Override
    public E top() {
        if (isEmpty()) return null;
        return head.getElement();
    }

    /** Removes and returns the top element from the stack. */
    @Override
    public E pop() {
        if (isEmpty()) return null;
        E answer = head.getElement();
        head = head.getNext();   // drop the head node
        size--;
        return answer;
    }
}