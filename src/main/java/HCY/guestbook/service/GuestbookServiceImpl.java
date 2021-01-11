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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
// Transactional이 있어야 변경 감지 (Dirty Checking)가 가능!!!
@Transactional(readOnly = true)
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository guestbookRepository;

    @Transactional
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
//        Page<Guestbook> result = guestbookRepository.findAll(pageable);

        Page<Guestbook> result = guestbookRepository.findBySearchCond(requestDto, pageable);

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        return new PageResponseDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long id) {
        Optional<Guestbook> result = guestbookRepository.findById(id);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Transactional
    @Override
    public void remove(Long id) {
        guestbookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = guestbookRepository.findById(dto.getId());

        if(result.isPresent()){
            Guestbook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
        }
    }
}
