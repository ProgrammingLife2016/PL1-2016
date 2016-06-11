package io.github.programminglife2016.pl1_2016.collapser;

/**
 * Created by ravishivam on 11-6-16.
 */
public class SimplePair<T, E> {

    private T t;
    private E e;


    public SimplePair(T t, E e) {
        this.t = t;
        this.e = e;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }
}
