package catalog.repository;

import catalog.classes.Audio;
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

public class AudioRepository {

    private JdbcTemplate jdbcTemplate;

    public AudioRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public long saveAudioAndGetId(long libraryItemId, Audio audio) throws NullPointerException {
        //language=sql
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement
                            ("insert into audios(library_items_id,title,performers,composers, year_of_publication) values (?,?,?,?,?)",
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, libraryItemId);
                    ps.setString(2, audio.getTitle());
                    ps.setString(3, StringListConverters.listToString(audio.getPerformers()));
                    ps.setString(4, StringListConverters.listToString(audio.getComposers()));
                    ps.setInt(5, audio.getYearOfPublication());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Audio getAudioByLibraryItemsId(long libraryItemId) {
        //language=sql
        return jdbcTemplate.queryForObject
                ("select * from library_items join audios on library_items.id=audios.library_items_id where library_items_id = ?",
                        (rs, rowNum) -> getAudioFromResultSet(rs), libraryItemId);
    }

    public List<Audio> getAllAudioItem() {
        //language=sql
        return jdbcTemplate.query("select * from audios join library_items on audios.library_items_id=library_items.id",
                (rs, rowNum) -> getAudioFromResultSet(rs));
    }

    public List<Audio> getAllAudioItemByTitleFragment(String fragment) {
        //language=sql
        return jdbcTemplate.query("select * from audios JOIN library_items ON library_items.id = audios.library_items_id and library_items.title like ?",
                (rs, rowNum) -> getAudioFromResultSet(rs), "%" + fragment + "%");
    }

    public void deleteAudioByLibraryItemsId(long libraryItemsId) {
        //language=sql
        jdbcTemplate.update("delete from audios where library_items_id = ?", libraryItemsId);
    }

    private Audio getAudioFromResultSet(ResultSet rs) throws SQLException {
        return new Audio(rs.getString("title"),
                StringListConverters.stringToList(rs.getString("performers")),
                StringListConverters.stringToList(rs.getString("composers")),
                rs.getInt("year_of_publication"),
                rs.getInt("available_quantity"));
    }
}
