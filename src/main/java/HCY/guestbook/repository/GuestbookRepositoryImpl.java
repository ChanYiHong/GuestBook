package HCY.guestbook.repository;

import HCY.guestbook.dto.PageRequestDTO;
import HCY.guestbook.entity.Guestbook;
import HCY.guestbook.entity.QGuestbook;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static HCY.guestbook.entity.QGuestbook.guestbook;

@RequiredArgsConstructor
public class GuestbookRepositoryImpl implements GuestbookRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Guestbook> findBySearchCond(PageRequestDTO pageRequestDTO, Pageable pageable) {

        QueryResults<Guestbook> results = queryFactory
                .selectFrom(guestbook)
                .where(
                        titleEq(pageRequestDTO.getType(), pageRequestDTO.getKeyword()),
                        contentEq(pageRequestDTO.getType(), pageRequestDTO.getKeyword()),
                        writerEq(pageRequestDTO.getType(), pageRequestDTO.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Guestbook> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression titleEq(String type, String keyword) {
        return type.contains("t") ? guestbook.title.like(keyword) : null;
    }

    private BooleanExpression contentEq(String type, String keyword) {
        return type.contains("c") ? guestbook.content.like(keyword) : null;
    }

    private BooleanExpression writerEq(String type, String keyword) {
        return type.contains("w") ? guestbook.writer.like(keyword) : null;
    }
}
