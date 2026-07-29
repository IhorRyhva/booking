package com.petProject.booking.room;

import com.petProject.booking.room.dto.FilterData;
import com.petProject.booking.specification.RoomSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public List<Room> getRooms(FilterData filterData) {
        return roomRepository.findAll(Specification.allOf(
                RoomSpecification.filterByCountry(filterData.country()),
                RoomSpecification.filterByTown(filterData.city()),
                RoomSpecification.filterByCategory(filterData.roomCategory()),
                RoomSpecification.filterByStar(filterData.star()),
                RoomSpecification.getNotRemovedRoom(),
                RoomSpecification.filterByPrice(filterData.min(), filterData.max()),
                RoomSpecification.filterByDate(filterData.start(), filterData.end())
        ));
    }
}
