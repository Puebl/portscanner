import java.net.*;
import java.util.concurrent.*;

public class PortScanner {
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: java PortScanner <host> <startPort> <endPort> <concurrency>");
            return;
        }
        String host = args[0];
        int start = Integer.parseInt(args[1]);
        int end = Integer.parseInt(args[2]);
        int conc = Integer.parseInt(args[3]);
        ExecutorService pool = Executors.newFixedThreadPool(conc);
        CountDownLatch latch = new CountDownLatch(end - start + 1);
        for (int p = start; p <= end; p++) {
            final int port = p;
            pool.submit(() -> {
                try (Socket s = new Socket()) {
                    s.connect(new InetSocketAddress(host, port), 200);
                    System.out.println("OPEN " + port);
                } catch (Exception ignored) {}
                latch.countDown();
            });
        }
        latch.await();
        pool.shutdown();
    }
}
