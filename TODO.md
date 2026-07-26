05.06.2026 book must have unique number 
06.06.2026 add EmailServer
~~06.06.2026 Add DTO for request body of post /bookRoom with email, username field and booked data~~
~~11.06.2026 fix response with incorrect book data~~
~~11.06.2026 book logic for not-auth user~~
~~14.06.2026 change return result after correct book for not-auth user~~
18.06.2026 add admin window and use sec:authorize
20.06.2026 CRUD admin for hotel, ban user and CRUD for book at the admin side.
20.06.2026 soft delete for hotels
~~26.06.2026  registerBook: double call in guest branch → duplicate bookings~~
~~registerBook: userOptional.get() throws on unauthenticated user
guest-booking flow not implemented (separate block, after JPA)
HotelService  public ArrayList<Room> getRoomsByAnotherInput(int min, int max, Star star, RoomCategory roomCategory, ArrayList<Room> newResponses) {
return null;
} create this method on db side~~