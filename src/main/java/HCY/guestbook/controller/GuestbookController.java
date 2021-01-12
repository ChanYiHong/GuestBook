package HCY.guestbook.controller;

import HCY.guestbook.dto.GuestbookDTO;
import HCY.guestbook.dto.PageRequestDTO;
import HCY.guestbook.dto.PageResponseDTO;
import HCY.guestbook.entity.Guestbook;
import HCY.guestbook.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService guestbookService;

    @GetMapping("/")
    public String index() {
        log.info("home... redirect to list...");
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO requestDTO, Model model) {
        log.info("list............... " + requestDTO);
        PageResponseDTO<GuestbookDTO, Guestbook> result = guestbookService.getList(requestDTO);
        model.addAttribute("result", result);
    }

    @GetMapping("/register")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
        Long id = guestbookService.register(dto);
        redirectAttributes.addFlashAttribute("msg", id);
        return "redirect:/guestbook/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(long id, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info("id: " + id);
        GuestbookDTO dto = guestbookService.read(id);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/remove")
    public String remove(long id, RedirectAttributes redirectAttributes){

        log.info("delete...... " + id);
        guestbookService.remove(id);
        redirectAttributes.addFlashAttribute("msg", id);
        return "redirect:/guestbook/list";

    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        log.info("post modify..................");
        log.info("dto :" + dto);

        guestbookService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("id", dto.getId());

        return "redirect:/guestbook/read";
    }

}
