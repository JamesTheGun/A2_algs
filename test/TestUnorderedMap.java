/**
 * Thorough test harness for UnorderedMap.
 * Designed for COMP3506/7505 Assignment 2.
 */

import uq.comp3506.a2.structures.UnorderedMap;

public class TestUnorderedMap {

    public static void main(String[] args) {
        System.out.println("=== Testing UnorderedMap ===\n");

        UnorderedMap<Integer, String> map = new UnorderedMap<>();

        // 1️⃣ Basic put and get
        System.out.println("1️⃣  Basic insertion and retrieval");
        map.put(1, "apple");
        map.put(2, "banana");
        map.put(3, "cherry");

        System.out.println("Size after 3 inserts: " + map.size());
        System.out.println("Get 1 → " + map.get(1)); // expect apple
        System.out.println("Get 2 → " + map.get(2)); // expect banana
        System.out.println("Get 3 → " + map.get(3)); // expect cherry
        System.out.println("Get 4 (missing) → " + map.get(4)); // expect null
        System.out.println("Is empty? " + map.isEmpty());
        System.out.println();

        // 2️⃣ Overwriting values
        System.out.println("2️⃣  Overwriting existing key");
        String oldVal = map.put(2, "blueberry");
        System.out.println("Old value returned for key 2: " + oldVal); // banana
        System.out.println("Get 2 → " + map.get(2)); // blueberry
        System.out.println("Size still: " + map.size());
        System.out.println();

        // 3️⃣ Removing values
        System.out.println("3️⃣  Removing items");
        String removed = map.remove(1);
        System.out.println("Removed key 1, value was: " + removed);
        System.out.println("Now get(1) → " + map.get(1)); // null
        System.out.println("Size after removal: " + map.size());
        System.out.println();

        // 4️⃣ Testing collisions (keys that hash to same bucket)
        System.out.println("4️⃣  Collision behavior test");
        UnorderedMap<String, Integer> collisions = new UnorderedMap<>();

        collisions.put("Aa", 111);  // Known hash collision pair with "BB" for DJB2
        collisions.put("BB", 222);

        System.out.println("Get 'Aa' → " + collisions.get("Aa")); // expect 111
        System.out.println("Get 'BB' → " + collisions.get("BB")); // expect 222
        System.out.println("Size: " + collisions.size());
        System.out.println();

        // 5️⃣ Testing clear()
        System.out.println("5️⃣  Clearing map");
        map.clear();
        System.out.println("Size after clear: " + map.size());
        System.out.println("Is empty? " + map.isEmpty());
        System.out.println("Get 2 → " + map.get(2));
        System.out.println();

        // 6️⃣ Re-inserting after clear
        System.out.println("6️⃣  Re-inserting after clear");
        map.put(10, "dog");
        map.put(20, "cat");
        map.put(30, "fish");
        System.out.println("Size now: " + map.size());
        System.out.println("Get 20 → " + map.get(20));
        System.out.println();

        // 7️⃣ Testing resizing (exceeding load factor)
        System.out.println("7️⃣  Triggering resize()");
        for (int i = 0; i < 100; i++) {
            map.put(i, "val" + i);
        }
        System.out.println("Inserted 100 items");
        System.out.println("Size now: " + map.size());
        System.out.println("Retrieve some random keys:");
        System.out.println("Get 0 → " + map.get(0));
        System.out.println("Get 50 → " + map.get(50));
        System.out.println("Get 99 → " + map.get(99));
        System.out.println("Get 101 (not present) → " + map.get(101));
        System.out.println();

        // 8️⃣ Removing multiple keys post-resize
        System.out.println("8️⃣  Removing keys after resize");
        map.remove(10);
        map.remove(20);
        map.remove(30);
        System.out.println("Size after removals: " + map.size());
        System.out.println("Check removed ones: get(10)=" + map.get(10)
                + ", get(20)=" + map.get(20)
                + ", get(30)=" + map.get(30));
        System.out.println();

        // 9️⃣ Random stress test of gets
        System.out.println("9️⃣  Stress test of random keys");
        int found = 0;
        int missing = 0;
        for (int i = 0; i < 200; i++) {
            String result = map.get(i);
            if (result != null) found++;
            else missing++;
        }
        System.out.println("Found: " + found + ", Missing: " + missing);
        System.out.println();

        // 🔟 Final summary
        System.out.println("🔟 Final size: " + map.size());
        System.out.println("Is empty? " + map.isEmpty());
        System.out.println("\n=== All tests complete ===");
    }
}
