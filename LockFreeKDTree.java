import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class LockFreeKDTree {
    final int k; // Number of dimensions
    final ConcurrentHashMap<Long, Node> shardRoots = new ConcurrentHashMap<>();
    public final AtomicInteger physicalDeletions = new AtomicInteger(0);

    public LockFreeKDTree(int dimensions) {
        this.k = dimensions;
    }

    public static class Point {
        final int[] coords;

        public Point(int... coords) {
            this.coords = coords;
        }

        public int get(int index) {
            return coords[index];
        }

        public double distanceTo(Point other) {
            double dist = 0;
            for (int i = 0; i < coords.length; i++) {
                int d = coords[i] - other.coords[i];
                dist += d * d;
            }
            return Math.sqrt(dist);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Point && Arrays.equals(coords, ((Point) o).coords);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(coords);
        }
    }

    static class Node {
        final Point point;
        final int depth;
        final AtomicReference<Node> left = new AtomicReference<>(null);
        final AtomicReference<Node> right = new AtomicReference<>(null);
        final AtomicBoolean isDeleted = new AtomicBoolean(false);

        Node(Point point, int depth) {
            this.point = point;
            this.depth = depth;
        }
    }

    private long shardKey(Point p) {
        int a = (k > 0) ? (p.get(0) >>> 8) : 0;
        int b = (k > 1) ? (p.get(1) >>> 8) : 0;
        int c = (k > 2) ? (p.get(2) >>> 8) : 0;
        return ((long) a << 16) ^ ((long) b << 8) ^ c;
    }

    public void insert(Point point) {
        if (point.coords.length != k)
            throw new IllegalArgumentException("Point dimensionality mismatch");

        long key = shardKey(point);
        Node current = shardRoots.get(key);

        if (current == null) {
            Node newNode = new Node(point, 0);
            if (shardRoots.putIfAbsent(key, newNode) == null) return;
            current = shardRoots.get(key);
        }

        insertIterative(current, point);
    }

    private void insertIterative(Node current, Point point) {
        while (true) {
            int cd = current.depth % k;

            if (current.point.equals(point)) {
                current.isDeleted.set(false); // revive if previously deleted
                return;
            }

            AtomicReference<Node> nextRef = point.get(cd) < current.point.get(cd)
                    ? current.left : current.right;
            Node next = nextRef.get();
            if (next == null) {
                Node newNode = new Node(point, current.depth + 1);
                if (nextRef.compareAndSet(null, newNode)) return;
            } else {
                current = next;
            }
        }
    }

    public boolean contains(Point point) {
        if (point.coords.length != k)
            throw new IllegalArgumentException("Point dimensionality mismatch");

        Node current = shardRoots.get(shardKey(point));
        return current != null && containsRecursive(current, point);
    }

    private boolean containsRecursive(Node node, Point point) {
        while (node != null) {
            if (!node.isDeleted.get() && node.point.equals(point)) return true;
            int cd = node.depth % k;
            node = point.get(cd) < node.point.get(cd) ? node.left.get() : node.right.get();
        }
        return false;
    }

    public void delete(Point point) {
        if (point.coords.length != k)
            throw new IllegalArgumentException("Point dimensionality mismatch");

        Node current = shardRoots.get(shardKey(point));
        if (current != null) deleteRecursive(current, point);
    }

    private void deleteRecursive(Node node, Point point) {
        while (node != null) {
            if (node.point.equals(point)) {
                node.isDeleted.set(true);
                return;
            }
            int cd = node.depth % k;
            node = point.get(cd) < node.point.get(cd) ? node.left.get() : node.right.get();
        }
    }

    public void cleanup() {
        for (Map.Entry<Long, Node> entry : shardRoots.entrySet()) {
            AtomicReference<Node> ref = new AtomicReference<>(entry.getValue());
            Node cleaned = cleanRecursive(null, ref, entry.getValue());
            if (cleaned != null) {
                shardRoots.put(entry.getKey(), cleaned);
            }
        }
    }

    private Node cleanRecursive(Node parent, AtomicReference<Node> parentRef, Node node) {
        if (node == null) return null;

        AtomicReference<Node> leftRef = node.left;
        AtomicReference<Node> rightRef = node.right;

        Node leftClean = cleanRecursive(node, leftRef, leftRef.get());
        Node rightClean = cleanRecursive(node, rightRef, rightRef.get());

        node.left.set(leftClean);
        node.right.set(rightClean);

        if (node.isDeleted.get()) {
            Node replacement = (leftClean != null && rightClean == null) ? leftClean
                                : (rightClean != null && leftClean == null) ? rightClean
                                : null;

            if (replacement != null || (leftClean == null && rightClean == null)) {
                if (parentRef != null && parentRef.compareAndSet(node, replacement)) {
                    physicalDeletions.incrementAndGet();
                    return replacement;
                }
            }
        }

        return node;
    }

    public Point nearestNeighbor(Point target) {
        if (target.coords.length != k)
            throw new IllegalArgumentException("Target dimensionality mismatch");

        AtomicReference<Point> best = new AtomicReference<>(null);
        AtomicReference<Double> bestDist = new AtomicReference<>(Double.MAX_VALUE);

        for (Node root : shardRoots.values()) {
            nearestRecursive(root, target, best, bestDist);
        }
        return best.get();
    }

    private void nearestRecursive(Node node, Point target,
                                  AtomicReference<Point> best, AtomicReference<Double> bestDist) {
        if (node == null) return;

        if (!node.isDeleted.get()) {
            double d = node.point.distanceTo(target);
            if (d < bestDist.get()) {
                best.set(node.point);
                bestDist.set(d);
            }
        }

        int cd = node.depth % k;
        Node near = target.get(cd) < node.point.get(cd) ? node.left.get() : node.right.get();
        Node far = target.get(cd) < node.point.get(cd) ? node.right.get() : node.left.get();

        nearestRecursive(near, target, best, bestDist);
        if (Math.abs(target.get(cd) - node.point.get(cd)) < bestDist.get()) {
            nearestRecursive(far, target, best, bestDist);
        }
    }

    public List<Point> rangeQuery(Point min, Point max) {
        List<Point> result = new ArrayList<>();
        for (Node root : shardRoots.values()) {
            rangeRecursive(root, min, max, result);
        }
        return result;
    }

    private void rangeRecursive(Node node, Point min, Point max, List<Point> out) {
        if (node == null) return;

        boolean in = true;
        for (int i = 0; i < k; i++) {
            if (node.point.get(i) < min.get(i) || node.point.get(i) > max.get(i)) {
                in = false;
                break;
            }
        }

        if (in && !node.isDeleted.get()) {
            out.add(node.point);
        }

        rangeRecursive(node.left.get(), min, max, out);
        rangeRecursive(node.right.get(), min, max, out);
    }

    public void insert(int[] coords) {
        insert(new Point(coords));
    }

    public boolean contains(int[] coords) {
        return contains(new Point(coords));
    }

    public Point generateRandomPoint(int range) {
        Random rand = ThreadLocalRandom.current();
        int[] coords = new int[k];
        for (int i = 0; i < k; i++) coords[i] = rand.nextInt(range);
        return new Point(coords);
    }

    public int getPhysicalDeletionCount() {
        return physicalDeletions.get();
    }
}
