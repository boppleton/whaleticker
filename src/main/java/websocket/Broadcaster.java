package websocket;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broadcaster implements Serializable {

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static LinkedList<BroadcastListener> listeners = new LinkedList<>();

    public interface BroadcastListener {
        void receiveBroadcast(String message) throws InterruptedException, IOException;
    }

    public static synchronized void broadcast(final String message) {
        for (final BroadcastListener listener : listeners)
            executorService.execute(() -> {
                try { listener.receiveBroadcast(message); } catch (Exception e) { e.printStackTrace(); }
            });
    }

    public static synchronized void register(BroadcastListener listener) {
        listeners.add(listener);
    }
    public static synchronized void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }


}