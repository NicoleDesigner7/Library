package otherClass;
import contractsClasses.IDatabase;
import modelClasses.User;
import repositoryClasses.BookRepository;
import repositoryClasses.MySQL;
import repositoryClasses.UserRepository;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Db {

    public static String VIEW_FOLDER = "WEB-INF/view";
    public static String NOT_FOUND = "notfound.jsp";
    public static String DB_SOURCE = "jdbc:mysql://localhost:3306/diary?useSSL=false&serverTimezone=UTC&user=root&password=&charset=UTF-8";
    private static IDatabase DATABASE = null;

    public static void view(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String viewFile = getViewFile(request);
        request
                .getRequestDispatcher(Db.VIEW_FOLDER + File.separator + viewFile)
                .forward(request, response);
    }

    private static String getViewFile(HttpServletRequest request) {
        Object viewFileAttribute = request.getAttribute("viewFile");
        return (viewFileAttribute == null)
                ? Db.NOT_FOUND
                : viewFileAttribute.toString();
    }

    private static IDatabase getMySQLDatabase() {
        if (Db.DATABASE == null) {
            Db.DATABASE = new MySQL(Db.DB_SOURCE);
        }
        return Db.DATABASE;
    }

    public static UserRepository userRepository() {
        return new UserRepository(Db.getMySQLDatabase());
    }

    public static BookRepository bookRepository() {
        return new BookRepository(Db.getMySQLDatabase());
    }

    public static boolean checkParameters(String[] parameters, Map<String, String[]> parameterMap) {
        for (String parameter : parameters) {
            if (!parameterMap.containsKey(parameter)) {
                return false;
            }
        }
        return true;
    }

    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return text;
        }
    }

    public static User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userAttribute = session.getAttribute("user");
        return userAttribute == null ? null : (User) userAttribute;
    }
}

