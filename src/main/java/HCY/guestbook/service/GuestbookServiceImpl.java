package HCY.guestbook.service;

import HCY.guestbook.dto.GuestbookDTO;
import HCY.guestbook.dto.PageRequestDTO;
import HCY.guestbook.dto.PageResponseDTO;
import HCY.guestbook.entity.Guestbook;
import HCY.guestbook.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository guestbookRepository;

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO ---------------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        guestbookRepository.save(entity);

        return entity.getId();
    }

    @Override
    public PageResponseDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDto) {
        Pageable pageable = requestDto.getPageable(Sort.by("id").descending());
        Page<Guestbook> result = guestbookRepository.findAll(pageable);
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        return new PageResponseDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long id) {
        Optional<Guestbook> result = guestbookRepository.findById(id);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }
}
