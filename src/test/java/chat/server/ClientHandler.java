package chat.server;

import chat.db.UsersRepo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientHandler {
    private String name;
    private String login;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Socket socket;
    private final Chat chat;
    private final int timeout = 120 * 1000;

    public ClientHandler(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat = chat;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }

        listen();
    }

    public String getName() {
        return name;
    }

    private void listen() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::process);
        executorService.shutdown();

    }

    private void process() {
        try {
            doAuth();
            receiveMessage();
        } catch (IOException e) {
            sendMessage("Socket timed out and closed.");
            try {
                socket.close();
            } catch (IOException ioException) {
                throw new RuntimeException("SWW", e);
            }
        }
    }

    private void doAuth() throws IOException {
        sendMessage("Awaiting credentials (120 secs). Sample [-auth login password]");
        socket.setSoTimeout(timeout);
            while (true) {
                String mayBeCredentials = in.readUTF();
                if (mayBeCredentials.startsWith("-auth")) {
                    String[] credentials = mayBeCredentials.split("\\s");
                    login = credentials[1];
                    String mayBeNickname = chat.getAuthenticationService()
                            .findNicknameByLoginAndPassword(credentials[1], credentials[2]);
                    if (mayBeNickname != null) {
                        if (!chat.isNicknameOccupied(mayBeNickname)) {
                            sendMessage("[INFO] Auth OK");
                            name = mayBeNickname;
                            chat.broadcastMessage(String.format("[%s] logged in", name));
                            chat.subscribe(this);
                            return;
                        } else {
                            sendMessage("[INFO] Current user is already logged in.");
                        }
                    } else {
                        sendMessage("[INFO] Wrong login or password.");
                    }
                }
            }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public void receiveMessage() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("-exit")) {
                    chat.unsubscribe(this);
                    chat.broadcastMessage(String.format("[%s] logged out", name));
                    break;
                }
                else if (message.startsWith("-rename")) {
                    String[] new_name = message.split("\\s");
                    int row = UsersRepo.changeNickname(login, new_name[1]);
                    if (row == 1) {
                        chat.broadcastMessage(String.format("User %s renamed to %s", name, new_name[1]));
                        name = new_name[1];
                    }
                }
                else {
                    chat.broadcastMessage(String.format("[%s]: %s", name, message));
                }
            } catch (IOException e) {
                throw new RuntimeException("SWW", e);
            }
        }
    }
}
