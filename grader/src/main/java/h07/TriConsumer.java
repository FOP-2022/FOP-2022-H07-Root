package h07;

@FunctionalInterface
public interface TriConsumer<T, S, R> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param r the third input argument
     */
    void accept(T t, S u, R r);
}
