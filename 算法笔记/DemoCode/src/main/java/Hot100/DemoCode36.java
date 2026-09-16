package Hot100;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DemoCode36 {
    Map<Integer, Integer> hashMap = new HashMap<>();
    List<Integer> list = new LinkedList<>();
    Integer capacity = 0;

    public DemoCode36(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        if (hashMap.containsKey(key)) {
            list.remove(Integer.valueOf(key));
            list.addFirst(key);
            return hashMap.get(key);
        } else return -1;
    }

    public void put(int key, int value) {
        if (hashMap.containsKey(key)){
            list.remove(Integer.valueOf(key));
            hashMap.put(key, value);
            list.addFirst(key);
            return;
        }

        if (list.size() >= capacity) {
            int lastKey = list.removeLast();
            hashMap.remove(lastKey);
        }
        hashMap.put(key, value);
        list.addFirst(key);
    }

    public static void main(String[] args) {
        DemoCode36 cache = new DemoCode36(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        cache.put(3, 3);
        System.out.println(cache.get(2));
        cache.put(4, 4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
    }
}
