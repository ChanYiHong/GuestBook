package HCY.guestbook.repository;

import HCY.guestbook.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>
        , QuerydslPredicateExecutor<Guestbook>, GuestbookRepositoryCustom {
}
