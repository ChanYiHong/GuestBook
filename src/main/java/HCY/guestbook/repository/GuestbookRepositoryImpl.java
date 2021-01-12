package HCY.guestbook.repository;

import HCY.guestbook.dto.PageRequestDTO;
import HCY.guestbook.entity.Guestbook;
import HCY.guestbook.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static HCY.guestbook.entity.QGuestbook.guestbook;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class GuestbookRepositoryImpl implements GuestbookRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Guestbook> findBySearchCond(PageRequestDTO pageRequestDTO, Pageable pageable) {

        List<OrderSpecifier> orders = getAllOrderSpecifiers(pageable);

        QueryResults<Guestbook> results = queryFactory
                .selectFrom(guestbook)
                .where(
                        titleEq(pageRequestDTO.getType(), pageRequestDTO.getKeyword()),
                        contentEq(pageRequestDTO.getType(), pageRequestDTO.getKeyword()),
                        writerEq(pageRequestDTO.getType(), pageRequestDTO.getKeyword())
                )
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Guestbook> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression titleEq(String type, String keyword) {
        if(type != null) {
            return type.contains("t") ? guestbook.title.containsIgnoreCase(keyword) : null;
        }else{
            return null;
        }
    }

    private BooleanExpression contentEq(String type, String keyword) {
        if(type != null) {
            return type.contains("c") ? guestbook.content.containsIgnoreCase(keyword) : null;
        }else{
            return null;
        }
    }

    private BooleanExpression writerEq(String type, String keyword) {
        if(type != null) {
            return type.contains("w") ? guestbook.writer.containsIgnoreCase(keyword) : null;
        }else{
            return null;
        }
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if(!isEmpty(pageable.getSort())){
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch(order.getProperty()){
                    case "id":
                        OrderSpecifier<?> orderId = QueryDslUtils.getSortedColumn(direction, guestbook, "id");
                        orders.add(orderId);
                        break;
                    case "title":
                        OrderSpecifier<?> orderTitle = QueryDslUtils.getSortedColumn(direction, guestbook, "title");
                        orders.add(orderTitle);
                    case "writer":
                        OrderSpecifier<?> orderWriter = QueryDslUtils.getSortedColumn(direction, guestbook, "writer");
                        orders.add(orderWriter);
                    default:
                        break;
                }
            }
        }

        return orders;
    }

}
