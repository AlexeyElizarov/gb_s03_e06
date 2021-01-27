package chat.client;

import chat.gui.ChatFrame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ClientChatAdapter {
    private ChatFrame chatFrame;
    private Client client;

    public ClientChatAdapter(String host, int port) {
        client = new Client(host, port);
        chatFrame = new ChatFrame(new Consumer<String>() {
            @Override
            public void accept(String messageFromFormSubmitListener) {
                client.sendMessage(messageFromFormSubmitListener);
            }
        });
        read();
    }

    private void read() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::receive);
        executorService.shutdown();
    }

    private void receive() {
        try {
            while (true) {
                chatFrame.append(
                        client.receiveMessage()
                );
            }
        } catch (ClientConnectionException e) {
            // AE: if there are problems with connection, close in and output streams.
            client.close();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

}
