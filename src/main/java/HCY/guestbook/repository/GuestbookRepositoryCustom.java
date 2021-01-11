package HCY.guestbook.repository;

import HCY.guestbook.dto.PageRequestDTO;
import HCY.guestbook.entity.Guestbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GuestbookRepositoryCustom {
    Page<Guestbook> findBySearchCond(PageRequestDTO pageRequestDTO, Pageable pageable);
}
