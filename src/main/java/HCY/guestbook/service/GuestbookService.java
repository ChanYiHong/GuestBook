package HCY.guestbook.service;

import HCY.guestbook.dto.GuestbookDTO;
import HCY.guestbook.dto.PageRequestDTO;
import HCY.guestbook.dto.PageResponseDTO;
import HCY.guestbook.entity.Guestbook;

public interface GuestbookService {

    Long register(GuestbookDTO dto);

    PageResponseDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDto);

    GuestbookDTO read(Long id);

    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
        return dto;
    }

}
