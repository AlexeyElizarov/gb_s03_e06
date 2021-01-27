package chat.server;
import chat.db.CredentialsEntry;
import chat.db.UsersRepo;


public class AuthenticationService {

    public AuthenticationService() {}

    public static String findNicknameByLoginAndPassword(String login, String password) {
        try {
            CredentialsEntry entry = UsersRepo.findUser(login, password);
            return entry.getNickname();
        }
        catch (RuntimeException e) {
            return null;
        }
    }

}
