package project;

import java.util.AbstractList;

public class CustomList<E> extends AbstractList<E> {
    private static final int MIN_SIZE = 5;
    private Object[] arr;
    private int size;

    public CustomList(int initialSize) {
        arr = new Object[initialSize];
        size = 0;
    }

    public CustomList() {
        this(MIN_SIZE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        return (E) arr[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E obj) {
        addLast(obj);
    }

    public void addLast(E obj) {
        if (size + 1 > arr.length) {
            resize();
        }
        arr[size] = obj;
        size++;
    }

    public void addFirst(E obj) {
        if (size + 1 > arr.length) {
            resize();
        }
        shiftRight();
        arr[0] = obj;
        size++;
    }

    public E pop() {
        return getAndRemove(0);
    }

    public E popLast() {
        return getAndRemove(size - 1);
    }

    @SuppressWarnings("unchecked")
    public E getAndRemove(int index){
        Object old = arr[index];
        removeByIndex(index);
        return (E) old;
    }

    public boolean remove(Object obj){
        int index = 0;
        boolean found = false;

        for (int i = 0; i < arr.length && !found; i++) {
            if(obj == arr[i]){
                found = true;
                index = i;
            }
        }
        if(found) {
            removeByIndex(index);
        }
        return found;
    }

    public void removeByIndex(int index){
        size--;
        System.arraycopy(arr, index + 1, arr, index, size - index);
    }

    public void clear() {
        arr = new Object[MIN_SIZE];
        size = 0;
    }

    private void shiftRight() {
        Object[] shifted_arr = new Object[arr.length + 1];
        System.arraycopy(arr, 0, shifted_arr, 1, arr.length);
        this.arr = shifted_arr;
    }

    private void resize() {
        Object[] resized_arr = new Object[size + 1];
        System.arraycopy(arr, 0, resized_arr, 0, arr.length);
        this.arr = resized_arr;
    }

    public CustomList<E> clone(){
        CustomList<E> customList = new CustomList<>();
        for (int i=0; i<this.size; i++){
            customList.addLast(this.get(i));
        }
        return customList;
    }
}
