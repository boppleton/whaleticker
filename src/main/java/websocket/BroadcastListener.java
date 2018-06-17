package websocket;

import java.io.IOException;

public class BroadcastListener implements Broadcaster.BroadcastListener {

    public BroadcastListener() {

        Broadcaster.register(this);
    }

    @Override
    public void receiveBroadcast(String message) throws InterruptedException, IOException {



    }
}
