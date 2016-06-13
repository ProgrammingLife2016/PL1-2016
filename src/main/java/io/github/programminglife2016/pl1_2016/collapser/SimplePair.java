package io.github.programminglife2016.pl1_2016.collapser;

/**
 * Class to store tuples.
 * @param <T> first element of the pair.
 * @param <E> second element of the pair.
 * @author Ravi Autar
 */
public class SimplePair<T, E> {

    private static final int HASH_FACTOR = 31;
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

    public E getE() {
        return e;
    }

    public T getT() {
        return t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePair<?, ?> that = (SimplePair<?, ?>) o;

        if (!t.equals(that.t)) return false;
        return e.equals(that.e);
    }

    @Override
    public int hashCode() {
        int result = t.hashCode();
        result = HASH_FACTOR * result + e.hashCode();
        return result;
    }
}
