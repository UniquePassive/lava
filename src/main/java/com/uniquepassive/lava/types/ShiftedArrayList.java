package com.uniquepassive.lava.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * This collection extends {@link ArrayList} with the ability to set the collection's starting index.
 *
 * Compensation for the index shifting is applied to all possible methods.
 */
public class ShiftedArrayList<T> extends ArrayList<T> {

    private int startingIndex = 0;

    public ShiftedArrayList(int startingIndex, int size) {
        super(size);

        this.startingIndex = startingIndex;
    }

    @Override
    public int indexOf(Object o) {
        int index = super.indexOf(o);
        if (index >= 0) {
            index += startingIndex;
        }
        return index;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = super.lastIndexOf(o);
        if (index >= 0) {
            index += startingIndex;
        }
        return index;
    }

    @Override
    public T get(int index) {
        ensureNotPhantom(index);
        index -= startingIndex;
        return super.get(index);
    }

    @Override
    public T set(int index, T element) {
        ensureNotPhantom(index);
        index -= startingIndex;
        return super.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        ensureNotPhantom(index);
        index -= startingIndex;
        super.add(index, element);
    }

    @Override
    public T remove(int index) {
        ensureNotPhantom(index);
        index -= startingIndex;
        return super.remove(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        ensureNotPhantom(index);
        index -= startingIndex;
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        ensureNotPhantom(fromIndex);
        ensureNotPhantom(toIndex);
        fromIndex -= startingIndex;
        toIndex -= startingIndex;
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        ensureNotPhantom(index);
        index -= startingIndex;
        return super.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        ensureNotPhantom(fromIndex);
        ensureNotPhantom(toIndex);
        fromIndex -= startingIndex;
        toIndex -= startingIndex;
        return super.subList(fromIndex, toIndex);
    }

    private void ensureNotPhantom(int index) {
        if (index < startingIndex) {
            throw new RuntimeException("This collection starts at index " + startingIndex + ". Cannot access index " + index);
        }
    }
}
