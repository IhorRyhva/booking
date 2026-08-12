package com.petProject.booking.web;

import com.petProject.booking.common.exception.IncorrectBookTimeException;
import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomRepository;
import com.petProject.booking.room.dto.BookedData;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AiController {
    private final RoomRepository roomRepository;
    private final EmbeddingModel embeddingModel;

    @PostMapping("/ai")
    public String findRoom(@RequestParam String query, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("query", query);
        return "redirect:/ai/result";
    }

    @GetMapping("/ai/result")
    public String getResult (@RequestParam String query, Model model) {
        List<Room> rooms = this.roomRepository
                .searchNotRemovedAndByEmbeddingWithLimit(embeddingModel.embed(query));
        model.addAttribute("rooms", rooms);
        model.addAttribute("query", query);
        return "ai";
    }

    @PostMapping("/ai/result")
    public String newResult (@ModelAttribute SearchDTO searchDTO, Model model) throws IncorrectMaxMinPriceException, IncorrectBookTimeException {
        float[] embedding = embeddingModel.embed(searchDTO.query());
        if (searchDTO.min() > searchDTO.max()) {
            throw new IncorrectMaxMinPriceException("You enter incorrect values");
        }
        BookedData bookedData = new BookedData(searchDTO.start(), searchDTO.end());
        List<Room> rooms = this.roomRepository.searchRooms(searchDTO.country(), searchDTO.city(), searchDTO.start(), searchDTO.end(),
                searchDTO.min(), searchDTO.max(), searchDTO.roomCategory(), searchDTO.star(), searchDTO.bedNumber(), embedding);
        model.addAttribute("rooms", rooms);
        model.addAttribute("query", searchDTO.query());
        model.addAttribute("country", searchDTO.country());
        model.addAttribute("city", searchDTO.city());
        model.addAttribute("star", searchDTO.star());
        model.addAttribute("end", bookedData.getEndDate());
        model.addAttribute("start", bookedData.getStartDate());
        model.addAttribute("min", searchDTO.min());
        model.addAttribute("max", searchDTO.max());
        model.addAttribute("roomCategory", searchDTO.roomCategory());
        model.addAttribute("query", searchDTO.query());
        model.addAttribute("bedNumber", searchDTO.bedNumber());
        return "ai";
    }
}