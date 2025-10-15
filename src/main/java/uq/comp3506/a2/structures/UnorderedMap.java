// @edu:student-assignment

package uq.comp3506.a2.structures;

import java.util.LinkedList;
import java.util.ArrayList;
/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 * <p>
 * NOTE: You should go and carefully read the documentation provided in the
 * MapInterface.java file - this explains some of the required functionality.
 */
public class UnorderedMap<K, V> implements MapInterface<K, V> {


    /**
     * you will need to put some member variables here to track your
     * data, size, capacity, etc...
     */
    private ArrayList[] data;
    private int size = 0;

    private static int capacity = 8;
    private static final double acceptable_alpha = 0.8;


    /**
     * Item class for hash map
     */
    private static class Item {
        Object key;
        Object value;
        Item(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
    /**
     * Make hash
     */
    private int HashDjb2(Object key) {
        String stringed_key = key.toString();
        long hash = 5381;
        for (int i = 0; i < stringed_key.length(); i++) {
            char c = stringed_key.charAt(i);
            hash = ((hash << 5) + hash) + c;
        }
        return Math.abs((int) (hash % capacity));
    }

    /**
     * Constructs an empty UnorderedMap
     */
    public UnorderedMap() {
        data = new ArrayList[capacity];
        for (int i = 0; i < data.length; i++) {
            data[i] = new ArrayList();
        }
        size = 0;
    }

    /**
     * returns the size of the structure in terms of pairs
     * @return the number of kv pairs stored
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * helper to indicate if the structure is empty or not
     * @return true if the map contains no key-value pairs, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the map. That means, after calling clear(),
     * the return of size() should be 0, and the data structure should appear
     * to be "empty".
     */
    @Override
    public void clear() {
        for (Object obj : data) {
            ArrayList bucket = (ArrayList) obj;
            bucket.clear();
        }
        size = 0;
    }
    /**
     * get an item
     */
    private Item getItem(K key) {
        int idx = HashDjb2(key);
        if (data[idx].isEmpty()){
            return null;
        }
        for (Object obj : data[idx]) {
            Item item = (Item) obj;
            if (item.key.equals(key)) {
                return item;
            }
        }
        return null;
    }
    /**
     * get an load factor
     */
    private float getLoadFactor() {
        return (float) size / (float) capacity;
    }
    /**
     * resize
     */
    private void resize() {
        capacity *= 2;
        ArrayList[] newData = new ArrayList[capacity];
        for (int i = 0; i < capacity; i++) {
            newData[i] = new ArrayList();
        }
        for (ArrayList bucket : data) {
            for (Object obj : bucket) {
                Item item = (Item) obj;
                int newIndex = HashDjb2(item.key);
                newData[newIndex].add(item);
            }
        }
        data = newData;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value
     * is replaced by the specified value.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the payload data value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no such key
     */

    @Override
    public V put(K key, V value) {
        Item existingItem = getItem(key);
        if (existingItem != null) {
            V existingValue = (V) existingItem.value;
            existingItem.value = value;
            return existingValue;
        }

        int idx = HashDjb2(key);
        Item thisItem = new Item(key, value);
        data[idx].add(thisItem);
        size++;

        if (getLoadFactor() > acceptable_alpha) {
            resize();
        }

        return null;
    }
    /**
     * Looks up the specified key in this map, returning its associated value
     * if such key exists.
     *
     * @param key the key with which the specified value is to be associated
     * @return the value associated with key, or null if there was no such key
     */
    @Override
    public V get(K key) {
        Item item =  getItem((K) key);
        if (item == null) {
            return null;
        }
        return (V) item.value;
    }

    /**
     * Looks up the specified key in this map, and removes the key-value pair
     * if the key exists.
     *
     * @param key the key with which the specified value is to be associated
     * @return the value associated with key, or null if there was no such key
     */
    @Override
    public V remove(K key) {
        Item item = getItem(key);
        if (item == null) {
            return null;
        }
        int idx = HashDjb2(key);
        data[idx].remove(item);
        size = size - 1;
        return (V) item.value;
    } 

}
