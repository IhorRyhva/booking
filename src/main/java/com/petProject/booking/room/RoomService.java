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
                RoomSpecification.filterByDate(filterData.bookedData().getStartDate(), filterData.bookedData().getEndDate()),
                RoomSpecification.filterByCategory(filterData.roomCategory()),
                RoomSpecification.filterByTown(filterData.city()),
                RoomSpecification.filterByCountry(filterData.country()),
                RoomSpecification.filterByPrice(filterData.min(), filterData.max()),
                RoomSpecification.filterByStar(filterData.star())
        ));
    }
}
