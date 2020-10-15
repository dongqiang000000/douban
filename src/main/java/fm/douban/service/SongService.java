package fm.douban.service;

import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import org.springframework.data.domain.Page;

public interface SongService {
    Song add(Song song);
    Song get(String songId);
    Page<Song> list(SongQueryParam songQueryParam);
    boolean modify(Song song);
    boolean delete(String songId);
}
