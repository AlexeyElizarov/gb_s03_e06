package chat.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRepo {

    public static CredentialsEntry findUser(String username, String password) {
        Connection connection = ConnectionService.connect();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new CredentialsEntry(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nickname"));
            }
            throw new RuntimeException("No such user registered.");
        } catch (SQLException e) {
            throw new RuntimeException("SWW", e);
        } finally {
            ConnectionService.close(connection);
        }
    }

    public static int changeNickname(String username, String nickname) {
        Connection connection = ConnectionService.connect();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users " +
                            "SET nickname = ? " +
                            "WHERE username = ?"
            );
            statement.setString(1, nickname);
            statement.setString(2, username);
            int row = statement.executeUpdate();
            connection.commit();
            return row;
        } catch (SQLException e) {
            ConnectionService.rollback(connection);
            throw new RuntimeException("SWW", e);
        } finally {
            ConnectionService.close(connection);
        }
    }
}
