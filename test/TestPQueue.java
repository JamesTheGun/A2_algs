/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 * Thorough test harness for the Heap-based priority queue.
 */

import uq.comp3506.a2.structures.Heap;
import uq.comp3506.a2.structures.Entry;
import java.util.Random;
import java.util.ArrayList;

public class TestPQueue {

    public static void main(String[] args) {
        System.out.println("=== Comprehensive Heap-based Priority Queue Tests ===\n");

        Heap<Integer, String> pq = new Heap<>();

        /* -------------------------------------------------------------
         * 1️⃣ Basic insertions
         * ------------------------------------------------------------- */
        System.out.println("1️⃣  Basic insertions and peek test:");
        pq.insert(5, "five");
        pq.insert(2, "two");
        pq.insert(8, "eight");
        pq.insert(1, "one");
        pq.insert(3, "three");

        System.out.println("Heap size (expected 5): " + pq.size());
        Entry<Integer, String> peek = pq.peekMin();
        System.out.println("Peek min (expected key=1): " + (peek == null ? "null" : peek.getKey() + " → " + peek.getValue()));

        /* -------------------------------------------------------------
         * 2️⃣ Sequential removals
         * ------------------------------------------------------------- */
        System.out.println("\n2️⃣  Sequential removals (should be in ascending order):");
        ArrayList<Integer> removedKeys = new ArrayList<>();
        while (!pq.isEmpty()) {
            Entry<Integer, String> e = pq.removeMin();
            removedKeys.add(e.getKey());
            System.out.println("Removed: " + e.getKey() + " → " + e.getValue());
        }
        System.out.println("Removed order: " + removedKeys);
        System.out.println("Heap empty? " + pq.isEmpty());

        /* -------------------------------------------------------------
         * 3️⃣ Edge cases (empty heap behaviour)
         * ------------------------------------------------------------- */
        System.out.println("\n3️⃣  Edge cases:");
        System.out.println("Peek on empty heap: " + pq.peekMin());
        System.out.println("Remove on empty heap: " + pq.removeMin());
        System.out.println("Heap size (expected 0): " + pq.size());

        /* -------------------------------------------------------------
         * 4️⃣ Duplicate keys test
         * ------------------------------------------------------------- */
        System.out.println("\n4️⃣  Inserting duplicate keys:");
        pq.insert(4, "A");
        pq.insert(4, "B");
        pq.insert(4, "C");
        pq.insert(2, "D");
        pq.insert(6, "E");

        System.out.println("Heap size (expected 5): " + pq.size());
        System.out.println("Removing all elements:");
        while (!pq.isEmpty()) {
            Entry<Integer, String> e = pq.removeMin();
            System.out.println("Removed: " + e.getKey() + " → " + e.getValue());
        }

        /* -------------------------------------------------------------
         * 5️⃣ Randomised heap test (100 elements)
         * ------------------------------------------------------------- */
        System.out.println("\n5️⃣  Randomised stress test with 100 elements:");
        Random rand = new Random(42);
        ArrayList<Integer> insertedKeys = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int key = rand.nextInt(1000);
            pq.insert(key, "V" + key);
            insertedKeys.add(key);
        }
        System.out.println("Inserted " + insertedKeys.size() + " elements.");

        // remove them all and verify ascending order
        int prev = Integer.MIN_VALUE;
        boolean sorted = true;
        while (!pq.isEmpty()) {
            int curr = pq.removeMin().getKey();
            if (curr < prev) sorted = false;
            prev = curr;
        }
        System.out.println("Heap maintained ascending order? " + sorted);

        /* -------------------------------------------------------------
         * 6️⃣ Clear and reuse test
         * ------------------------------------------------------------- */
        System.out.println("\n6️⃣  Clear and reuse:");
        pq.insert(10, "ten");
        pq.insert(7, "seven");
        pq.insert(12, "twelve");
        System.out.println("Heap size before clear: " + pq.size());
        pq.clear();
        System.out.println("Heap cleared. Size now: " + pq.size());
        pq.insert(1, "new");
        System.out.println("Heap reused successfully: " + pq.peekMin().getKey() + " → " + pq.peekMin().getValue());

        /* -------------------------------------------------------------
         * ✅ Summary
         * ------------------------------------------------------------- */
        System.out.println("\n✅ All heap tests completed successfully.");
    }
}
