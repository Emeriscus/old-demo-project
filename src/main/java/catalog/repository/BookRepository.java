package catalog.repository;

import catalog.classes.Book;
import catalog.utils.StringListConverters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class BookRepository {

    private JdbcTemplate jdbcTemplate;

    public BookRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long saveBookAndGetId(long libraryItemId, Book book) throws NullPointerException {
        //language=sql
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
                    PreparedStatement ps =
                            conn.prepareStatement("insert into books(library_items_id,title,authors,year_of_publication) values(?,?,?,?)",
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, libraryItemId);
                    ps.setString(2, book.getTitle());
                    ps.setString(3, StringListConverters.listToString(book.getContributors()));
                    ps.setInt(4, book.getYearOfPublication());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Book getBookByLibraryItemsId(long libraryItemId) {
        //language=sql
        return jdbcTemplate.queryForObject
                ("select * from library_items join books ON library_items.id = books.library_items_id WHERE library_items_id=?",
                        (rs, rowNum) -> getBookFromResultSet(rs), libraryItemId);
    }

    public List<Book> getAllBookItem() {
        //language=sql
        return jdbcTemplate.query("select * from books join library_items on books.library_items_id=library_items.id",
                (rs, rowNum) -> getBookFromResultSet(rs));
    }

    public List<Book> getAllBookItemByTitleFragment(String fragment) {
        //language=sql
        return jdbcTemplate.query("select * from books JOIN library_items ON library_items.id = books.library_items_id and library_items.title like ?",
                (rs, rowNum) -> getBookFromResultSet(rs), "%" + fragment + "%");
    }

    public void deleteBookByLibraryItemsId(long libraryItemId) {
        //language=sql
        jdbcTemplate.update("delete from books where library_items_id = ?", libraryItemId);
    }

    private Book getBookFromResultSet(ResultSet rs) throws SQLException {
        return new Book(rs.getString("title"),
                StringListConverters.stringToList(rs.getString("authors")),
                rs.getInt("year_of_publication"), rs.getInt("available_quantity"));
    }
}
