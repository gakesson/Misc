package gakesson.util.misc;

import java.util.EmptyStackException;

/**
 * This interface represents a last-in-first-out (LIFO) stack of objects.
 * 
 * @author Gustav Akesson - gustav.r.akesson@gmail.com
 * 
 * @param <E>
 */
public interface Stack<E>
{
    /**
     * Pushes the provided element to the top of the stack.
     * 
     * @param e
     *            The element to push.
     */
    public void push(E e);

    /**
     * Returns and removes the top of the stack, or throws
     * {@link EmptyStackException} in case the stack is empty.
     * 
     * @return The top of the stack.
     */
    public E pop();

    /**
     * Returns, but does not remove, the top of the stack, or throws
     * {@link EmptyStackException} in case the stack is empty.
     * 
     * @return The top of the stack.
     */
    public E peek();

    /**
     * Verifies whether or not this stack is empty.
     * 
     * @return {@code true} if this stack is empty, otherwise {@code false}.
     */
    public boolean isEmpty();

    /**
     * Returns the size of this stack.
     * 
     * @return The size of this stack.
     */
    public int size();
}
