package HCY.guestbook.service;

import HCY.guestbook.dto.GuestbookDTO;
import HCY.guestbook.dto.PageRequestDTO;
import HCY.guestbook.dto.PageResponseDTO;
import HCY.guestbook.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GuestbookServiceTest {

    @Autowired
    GuestbookService guestbookService;

    @Test
    public void testRegister() throws Exception {

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer("user0")
                .build();

        System.out.println(guestbookService.register(guestbookDTO));

    }

    @Test
    public void testList() throws Exception {
        //given
        PageRequestDTO requestDto = PageRequestDTO.builder().page(1).size(10).build();

        //when
        PageResponseDTO<GuestbookDTO, Guestbook> result = guestbookService.getList(requestDto);

        //then

        System.out.println("PREV : " + result.isPrev());
        System.out.println("NEXT : " + result.isNext());
        System.out.println("TOTAL : " + result.getTotalPage());

        System.out.println("==============================");

        for(GuestbookDTO dto : result.getDtoList()){
            System.out.println(dto);
        }

        System.out.println("==============================");

        result.getPageList().forEach(System.out::println);
    }
}