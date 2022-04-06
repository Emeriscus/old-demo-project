package catalog.repository;

import catalog.classes.LibraryItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

public class LibraryItemRepository {

    private JdbcTemplate jdbcTemplate;

    public LibraryItemRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long saveLibraryItem(LibraryItem libraryItem) {
        try {
            //language=sql
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                        PreparedStatement ps = conn.prepareStatement
                                ("insert into library_items(title,item_type,available_quantity) values (?,?,?)",
                                        Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, libraryItem.getTitle());
                        ps.setString(2, libraryItem.getClass().toString());
                        ps.setInt(3, libraryItem.getQuantity());
                        return ps;
                    }, keyHolder
            );
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (NullPointerException npe) {
            throw new IllegalStateException("Book or Audio cannot be null!", npe);
        }
    }

    public long getLibraryItemIdByTitle(String title) throws EmptyResultDataAccessException {
        //language=sql
        return jdbcTemplate.queryForObject("select id from library_items where title = ?",
                (rs, rowNum) -> rs.getLong("id"), title);
    }

    public String getLibraryItemTypeById(long libraryItemId) {
        //language=sql
        return jdbcTemplate.queryForObject("select item_type from library_items where id = ?",
                (rs, rowNum) -> rs.getString("item_type"), libraryItemId);
    }

    public void deleteLibraryItemById(long libraryItemId) {
        //language=sql
        jdbcTemplate.update("delete from library_items where id=?", libraryItemId);
    }

    public void borrowLibraryItemById(long libraryItemId) {
        //language=sql
        jdbcTemplate.update
                ("update library_items set available_quantity = available_quantity-1 where id = ?", libraryItemId);
    }

    public boolean hasAvailableLibraryItemQuantitybyId(long libraryItemId) {
        //language=sql
        return jdbcTemplate.queryForObject("select * from library_items where id=?",
                (rs, rowNum) -> rs.getInt("available_quantity"), libraryItemId) > 0;
    }
}
