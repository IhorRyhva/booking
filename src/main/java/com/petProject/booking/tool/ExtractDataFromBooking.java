package com.petProject.booking.tool;

//import com.petProject.booking.hotel.Hotel;
//import com.petProject.booking.hotel.HotelRepository;
//import com.petProject.booking.hotel.Location;
//import com.petProject.booking.hotel.Star;
//import com.petProject.booking.room.Room;
//import com.petProject.booking.room.RoomCategory;
//import lombok.RequiredArgsConstructor;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//import tools.jackson.core.type.TypeReference;
//import tools.jackson.databind.ObjectMapper;
//
//import java.io.*;
//import java.util.*;
//import java.util.concurrent.ThreadLocalRandom;
//
//import static com.petProject.booking.hotel.Star.*;
//import static com.petProject.booking.room.RoomCategory.*;

//@Service
//@RequiredArgsConstructor
public class ExtractDataFromBooking {
//    private final HotelRepository hotelRepository;
//    public void putData() throws IOException {
//        if (hotelRepository.count() < 100) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            ArrayList<Hotel> hotels = objectMapper.readValue(new ClassPathResource("data.json").getInputStream(), new TypeReference<ArrayList<Hotel>>() { });
//            for (Hotel hotel: hotels) {
//                for (Room room: hotel.getRooms()) {
//                    room.setHotel(hotel);
//                }
//            }
//            this.hotelRepository.saveAll(hotels);
//        }
//    }
//
//
//    public ArrayList<Hotel> generateData() throws IOException {
//        HashMap<String, List<String>> places = getData();
//        ArrayList<Hotel> hotels = new ArrayList<>();
//        for (String country: places.keySet()) {
//            for (String city: places.get(country)) {
//                hotels.addAll(generateHotels(country, city));
//            }
//        }
//        for (Hotel hotel: hotels) {
//            hotel.setRooms(getRooms(hotel));
//        }
//        return hotels;
//    }
//
//    private List<Room> getRooms(Hotel hotel) {
//        ArrayList<Room> rooms = new ArrayList<>();
//        RoomCategory[] categories = {LUX, FAMILY, ECONOMY, BASIC, DELUXE,};
//        int number = 1;
//        for (RoomCategory roomCategory: categories) {
//            for (int i = 0; i < 2; i++) {
//                rooms.add(Room.builder()
//                                .number(number)
//                                .category(roomCategory)
//                                .hotel(hotel)
//                                .price(getPriceByCategory(roomCategory))
//                                .build());
//                number++;
//            }
//        }
//        return rooms;
//    }
//
//    private int getPriceByCategory(RoomCategory roomCategory) {
//        return switch (roomCategory) {
//            case LUX -> ThreadLocalRandom.current().nextInt(150, 1000);
//            case DELUXE -> ThreadLocalRandom.current().nextInt(70, 150);
//            case FAMILY -> ThreadLocalRandom.current().nextInt(30, 100);
//            case BASIC -> ThreadLocalRandom.current().nextInt(15, 30);
//            case ECONOMY -> ThreadLocalRandom.current().nextInt(5, 15);
//            default -> 50;
//        };
//    }
//
//
//    private ArrayList<Hotel> generateHotels(String country, String city) {
//        Star[] stars = {ONE, TWO, THREE, FOUR, FIVE};
//        ArrayList<Hotel> hotels = new ArrayList<>();
//        for (Star star: stars) {
//            for (String nameOfHotel: getNameByStar(star, city)) {
//                hotels.add(Hotel.builder()
//                                .location(new Location(country, city))
//                                .nameOfHotel(nameOfHotel)
//                                .star(star)
//                                .build());
//            }
//        }
//        return hotels;
//    }
//
//    public List<String> getNameByStar(Star star, String city) {
//        return switch (star) {
//            case ONE -> getNameOneStar(city);
//            case TWO -> getNameTwoStar(city);
//            case THREE -> getNameThreeStar(city);
//            case FOUR -> getNameFourStar(city);
//            case FIVE -> getNameFiveStar(city);
//            default -> new ArrayList<>();
//        };
//    }
//
//    private List<String> getNameFiveStar(String city) {
//        return List.of(city + " Royal Crown Palace");
//    }
//
//    private List<String> getNameFourStar(String city) {
//        return Arrays.asList(
//                city + " Royal Garden Hotel",
//                city + " Elite Residence"
//        );
//
//    }
//
//    private List<String> getNameThreeStar(String city) {
//        return Arrays.asList(
//                city + " Central Plaza Hotel",
//                city + " Riverside Hotel"
//        );
//    }
//
//    private List<String> getNameTwoStar(String city) {
//            return Arrays.asList(
//                    city + " Comfort Inn",
//                    city + " City Comfort Hotel"
//            );
//    }
//
//    private List<String> getNameOneStar(String city) {
//        return Arrays.asList(
//                city + " City Hostel",
//                city + " Budget Inn"
//        );
//    }
//
//    public HashMap<String, List<String>> getData() throws IOException {
//        Document document = getDocument("https://www.booking.com/country.en.html");
//        Elements countries = document.getElementsByClass("block_third block_third--flag-module");
//
//        List<String> countryName = getCountryName(document);
//        ArrayList<String> countryLink = getCountriesLink(countries);
//
//        return getPlacesForHotels(countryLink, new HashMap<>(), countryName);
//    }
//
//    private HashMap<String, List<String>> getPlacesForHotels(ArrayList<String> countryLink, HashMap<String, List<String>> place, List<String> countryName) throws IOException {
//        int index = 0;
//        for (String cityLink: countryLink) {
//            Document document = getDocument(cityLink);
//            if (document != null) {
//                List<String> cities = document.getElementsByClass("de576f5064").stream().map(element -> element.getElementsByTag("a").text()).toList();
//                List<String> value = new ArrayList<>();
//                boolean canAdd = false;
//                for (String city : cities) {
//                    if (canAdd && city.isBlank()) {
//                        break;
//                    }
//                    if (canAdd) {
//                        value.add(city);
//                    } else if (city.equals("All holiday rentals")) {
//                        canAdd = true;
//                    }
//                }
//
//                if (!value.contains("Countries") && !value.contains("See all")) {
//                    System.out.println(countryName.get(index));
//                    place.put(countryName.get(index), value);
//                }
//            }
//            index++;
//        }
//        return place;
//    }
//
//    private List<String> getCountryName(Document document) {
//        return document.getElementsByClass("block_header").stream().map(e -> e.getElementsByTag("a").text()).toList();
//    }
//
//    private ArrayList<String> getCountriesLink(Elements countries) {
//        ArrayList<String> countryLink = new ArrayList<>();
//        String startUrl = "https://www.booking.com/booking-home";
//        for (Element hotel: countries) {
//            String country = hotel.getElementsByTag("a").getFirst().attr("href").split("\\?")[0];
//            StringBuilder url = new StringBuilder();
//            url.append(startUrl);
//            url.append(country);
//            countryLink.add(url.toString());
//        }
//        return countryLink;
//    }
//
//    private Document getDocument(String url)  {
//        try {
//            return Jsoup.connect(url)
//                    .userAgent("Chrome/51.0.2704.103 Safari/537.36")
//                    .referrer("https://google.com")
//                    .get();
//        } catch (IOException e) {
//            System.out.println("error");
//        }
//        return null;
//    }
}
