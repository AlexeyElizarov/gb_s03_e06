package chat.application;

public class ClientAppThree {
    public static void main(String[] args) {
        new chat.client.ClientChatAdapter("localhost", 8888);
    }
}
