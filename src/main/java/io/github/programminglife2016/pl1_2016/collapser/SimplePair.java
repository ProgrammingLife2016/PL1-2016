package io.github.programminglife2016.pl1_2016.collapser;

/**
 * Class to store tuples.
 * @param <T> first element of the pair.
 * @param <E> second element of the pair.
 * @author Ravi Autar
 */
public class SimplePair<T, E> {

    private T t;
    private E e;

    /**
     * Creates new pair.
     * @param t first element of the pair.
     * @param e second element of the pair.
     */
    public SimplePair(T t, E e) {
        this.t = t;
        this.e = e;
    }
}
