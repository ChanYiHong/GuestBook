package HCY.guestbook.repository;

import HCY.guestbook.entity.Guestbook;
import HCY.guestbook.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class GuestbookRepositoryTest {

    @Autowired
    GuestbookRepository guestBookRepository;

    @Test
    public void insertDummies() throws Exception {

        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title : " + i)
                    .content("Content : " + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestBookRepository.save(guestbook));
        });

    }

    @Test
    public void updateTest() throws Exception {

        Optional<Guestbook> result = guestBookRepository.findById(300L);

        if(result.isPresent()){

            Guestbook guestBook = result.get();

            guestBook.changeTitle("Changed Title ... ");
            guestBook.changeContent("Changed Content ... ");

            guestBookRepository.save(guestBook);

        }

    }

    @Test
    public void testQuery1() throws Exception {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        QGuestbook qGuestBook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = qGuestBook.title.contains(keyword);

        builder.and(expression);

        Page<Guestbook> result = guestBookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    @Test
    public void testQuery2() throws Exception {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        QGuestbook qGuestBook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestBook.title.contains(keyword);
        BooleanExpression exContent = qGuestBook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);
        builder.and(qGuestBook.id.gt(0L));

        Page<Guestbook> result = guestBookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

}