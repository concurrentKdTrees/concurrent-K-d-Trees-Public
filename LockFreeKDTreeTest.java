import java.util.concurrent.ThreadLocalRandom;

public class LockFreeKDTreeTest {
    private static int RANGE;
    private static int THREADS;
    private static int TIME;

    LockFreeKDTree instance;
    long[] opCount;
    long totalOps;
    Thread[] th;
    Thread cleanerThread;
    long start;
    int s_Limit, i_Limit;
    volatile boolean stopCleaning = false;

    public LockFreeKDTreeTest(int num_threads, int range, int time, int dims, int arg1, int arg2) {
        instance = new LockFreeKDTree(dims);
        THREADS = num_threads;
        RANGE = range;
        TIME = time;
        th = new Thread[num_threads];
        opCount = new long[num_threads];
        totalOps = 0;
        s_Limit = arg1;
        i_Limit = arg2;
    }

    public void prefill() throws Exception {
        Thread fillThread = new Fill();
        fillThread.start();
        fillThread.join();
    }

    class Fill extends Thread {
        int PER_THREAD_PREFILL = RANGE / 2;

        public void run() {
            for (int i = 0; i < PER_THREAD_PREFILL;) {
                int[] coords = ThreadLocalRandom.current().ints(instance.k, 0, RANGE).toArray();
                instance.insert(new LockFreeKDTree.Point(coords));
                i++;
            }
        }
    }

    public void testParallel() throws Exception {
        startCleanerThread();

        for (int i = 0; i < THREADS; i++) {
            th[i] = new AllMethods(i);
        }
        start = System.currentTimeMillis();
        for (int i = 0; i < THREADS; i++) th[i].start();
        for (int i = 0; i < THREADS; i++) th[i].join();

        stopCleaning = true;
        cleanerThread.join(); // wait for cleanup thread to stop
    }

    class AllMethods extends Thread {
        int tid;

        AllMethods(int id) {
            this.tid = id;
        }

        public void run() {
            long count = 0;
            long end = System.currentTimeMillis();
            int WARMUP_TIME = 5000;

            while ((end - start) <= WARMUP_TIME) {
                int[] coords = ThreadLocalRandom.current().ints(instance.k, 0, RANGE).toArray();
                instance.contains(new LockFreeKDTree.Point(coords));
                end = System.currentTimeMillis();
            }

            end = System.currentTimeMillis();
            while ((end - start) <= TIME + 5000) {
                int chVal = ThreadLocalRandom.current().nextInt(100);
                int ch = (chVal < s_Limit) ? 0 : ((chVal < i_Limit) ? 1 : 2);

                int[] coords = ThreadLocalRandom.current().ints(instance.k, 0, RANGE).toArray();
                LockFreeKDTree.Point point = new LockFreeKDTree.Point(coords);

                switch (ch) {
                    case 0 -> instance.contains(point);
                    case 1 -> instance.insert(point);
                    case 2 -> instance.delete(point);
                }
                count++;
                end = System.currentTimeMillis();
            }
            opCount[tid] = count;
        }
    }

    private void startCleanerThread() {
        cleanerThread = new Thread(() -> {
            while (!stopCleaning) {
                try {
                    Thread.sleep(5000); // every 5 seconds
                    instance.cleanup();
                } catch (InterruptedException ignored) {}
            }
        });
        cleanerThread.start();
    }

    public long totalOperations() {
        for (int i = 0; i < THREADS; i++) {
            totalOps += opCount[i];
        }
        return totalOps;
    }

    public static void main(String[] args) {
        int num_threads = Integer.parseInt(args[0]);
        int range = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);
        int dims = Integer.parseInt(args[3]);
        int s_Limit = Integer.parseInt(args[4]);
        int i_Limit = Integer.parseInt(args[5]);

        LockFreeKDTreeTest ob = new LockFreeKDTreeTest(num_threads, range, time, dims, s_Limit, i_Limit);

        try { ob.prefill(); } catch (Exception e) { System.out.println(e); }
        try { ob.testParallel(); } catch (Exception e) { System.out.println(e); }

        long total_Operations = ob.totalOperations();
        double throughput = (total_Operations / (1000000.0 * time)) * 1000;
        //int deleted = ob.instance.getPhysicalDeletionCount();

        System.out.printf("\t:num_threads:%d\t:range:%d\t:total_Operations:%d\t:throughput:%.4f\t:Dims:%d\n", num_threads, range, total_Operations, throughput, dims);
    }
}
