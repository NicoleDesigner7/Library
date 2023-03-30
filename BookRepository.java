package repositoryClasses;
import contractsClasses.IDatabase;
import contractsClasses.IRepository;
import modelClasses.Book;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements IRepository<Book, Integer> {
    private final IDatabase database;
    public BookRepository(IDatabase database) {
        this.database = database;
    }

    @Override
    public Book get(Integer index) {
        Book book = null;
        String query = String
                .format("SELECT * FROM book WHERE book_id = %d", index);
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                book = new Book();
                book.setBook_id(resultSet.getInt("book_Id"));
                book.setAutor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));
                book.setPublication_date(resultSet.getDate("dateOfBorrowBook"));
                book.setVisibility(resultSet.getBoolean("visibility"));

            }
        } catch (Exception e) {
            return book;
        }
        return book;
    }

    @Override
    public List<Book> getAll() {
        List<Book> list = new ArrayList<>();
        String query = "SELECT * FROM book "
                + "WHERE visibility = 1 "
                + "ORDER BY book_id DESC";
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                Book book = new Book();
                book.setBook_id(resultSet.getInt("book_Id"));
                book.setAutor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));
                book.setPublication_date(resultSet.getDate("dateOfBorrowBook"));
                book.setVisibility(resultSet.getBoolean("visibility"));
                list.add(book);
            }
        } catch (Exception ex) {
            return list;
        }
        return list;
    }

    public List<Book> getAllbyUserId(Integer userIndex, boolean showAll) {
        List<Book> list = new ArrayList<>();

        String query = "SELECT * FROM book "
                + "WHERE " + (!showAll ? "visibility = 1 AND " : " ")
                + "user_id = " + userIndex;
        try {
            ResultSet resultSet = database.executeQuery(query);
            while (resultSet.next()) {
                Book book = new Book();
                book.setBook_id(resultSet.getInt("book_Id"));
                book.setAutor(resultSet.getString("author"));
                book.setTitle(resultSet.getString("title"));
                book.setPublication_date(resultSet.getDate("dateOfBorrowBook"));
                book.setVisibility(resultSet.getBoolean("visibility"));
                list.add(book);
            }
        } catch (Exception ex) {
            return list;
        }
        return list;
    }

    @Override
    public boolean add(Book book) {
        boolean result = false;
        String query = String.format("INSERT INTO book"
                        + " VALUES(NULL, '%s', '%s', '%s', %s)",
                book.getBook_id(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(book.getPublication_date()),
                book.getTitle(),
                book.getAutor(),
                book.isVisibility() ? 1 : 0);
        try {
            database.executeSQL(query);
            result = true;
        } catch (Exception ex) {
            return result;
        }
        return result;
    }

    @Override
    public Book update(Integer index, Book book) {
        Book updatedBook = get(index);
        String query = String.format("UPDATE book SET "
                        + "publication_date = '%s', "
                        + "visibility = %s "
                        + "WHERE book_id = %d",
                book.getPublication_date(),
                book.isVisibility() ? 1 : 0,
                index);

        try {
            boolean result = (boolean) database.executeSQL(query);
            updatedBook = result ? updatedBook : book;
        } catch (Exception ex) {
            return updatedBook;
        }
        return updatedBook;
    }

    @Override
    public Book remove(Integer index) {
        String query = String
                .format("DELETE FROM book WHERE book_id = %d", index);
        Book deletedBook = get(index);
        try {
            database.executeSQL(query);
        } catch (Exception ex) {
            return deletedBook;
        }
        return deletedBook;
    }

}
