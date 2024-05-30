package dataaccess;
import org.mindrot.jbcrypt.BCrypt;
public class HashedPassword
{
    private static String normalPassword;

    public static String hashPassword(String normalPassword)
    {
        return BCrypt.hashpw(normalPassword, BCrypt.gensalt());
    }

    public static boolean checkPassWord(String normalPassword)
    {
        String hashedPassword = hashPassword(normalPassword);
        return BCrypt.checkpw(normalPassword, hashedPassword);
    }

}
