package com.petProject.booking.room;

import com.petProject.booking.room.dto.FilterData;
import com.petProject.booking.specification.RoomSpecification;
import com.petProject.booking.web.SearchDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final static int PAGE_SIZE = 50;
    private final static int LIMIT = 50;

    public Page<Room> getRooms(FilterData filterData, int page) {
        Pageable pageable = PageRequest.of(page, RoomService.PAGE_SIZE, Sort.by("id").ascending());
        return roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCountry(filterData.country()),
                RoomSpecification.filterByTown(filterData.city()),
                RoomSpecification.filterByCategory(filterData.roomCategory()),
                RoomSpecification.filterByStar(filterData.star()),
                RoomSpecification.getNotRemovedRoom(),
                RoomSpecification.filterByPrice(filterData.min(), filterData.max()),
                RoomSpecification.filterByDate(filterData.start(), filterData.end()),
                RoomSpecification.getRoomByBedNumber(filterData.bedNumber())
        ), pageable);
    }

    public List<Room> searchNotRemovedAndByEmbeddingWithLimit(float[] embed) {
        return this.roomRepository.searchNotRemovedAndByEmbeddingWithLimit(Arrays.toString(embed));
    }

    public List<Room> findRoom(SearchDTO searchDTO, float[] embedding) {
        return this.roomRepository.searchRooms(searchDTO.country(), searchDTO.city(), searchDTO.start(), searchDTO.end(),
                searchDTO.min(), searchDTO.max(), searchDTO.roomCategory(), searchDTO.star(), searchDTO.bedNumber(), Arrays.toString(embedding), RoomService.LIMIT);
    }
}
