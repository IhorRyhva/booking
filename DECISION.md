05.06.2026/remove excessive dependency 'Jackson 2'/ because SB4 give me 'Jackson 3' from starter and two version make a conflict

06.06.2026 Guest bookings are tied to a transaction, not to an email.
Context - guests are allowed to book without creating an account.
Decision - the booking is stored without being linked to a User;
the email is contact information only, not an identity.
Why - if someone later registers with the same email, they will not gain access to someone else’s guest bookings;
identity must not be based on a mutable contact detail.


18.06.2026 Why I don't catch JwtException? The reason is that I decided to use fail-closed principial, and if my server gets incorrect JWT that
authorization for this user must be interrupted
18.06.2026 Why extractRole is package-private? The reason is I want test methods extractRole(String token), though public methods loadUser(OidcUserRequest userRequest),
but for testing this method it is dependent on Keycloack, because loadUser(OidcUserRequest userRequest) call super.loadUser(), which works through Keycloak,
but I want test extractRole in isolate, so for this I made extractRole package-private

20.06.2026 How did I enforce admin access only for /admin/**. I did it in SecurityConfig with hasAuthority("admin"). I checked it works with negative test
nonAdminCannotDelete(). UI hiding is only UX - server side is the real boundary

20.06.2026 Why do I have both UI-level authorize (sec:authorize) and server-side enforcement? The reason is, that first
I use for better UX, but to prevent bypass with Dev-tools or Postman I must cover it with server-side enforcement.

24.06.2026 Why @DataJpaTest not a @SpringBootTest:
@SpringBootTest load the whole context and due to this my test fails on Security level, because it depends on KeyCloack,
so to avoid this problem I decided made my test isolated on DB layer and for this task I chose to use DataJpaTest, 
which doesn't load the whole program context and is created for this kind of test.
For using my @Container postgresql db, I must turn off default H2 db through AutoConfigureTestDatabase.

24.06.2026 Why this static block static { TimeZone.setDefault(TimeZone.getTimeZone("UTC")); }?
I must add it, because PG tzdata doesn't know about TZ "Europe/Kiev", because it was renamed into "Europe/Kyiv", but default
JVM TZ is "Europe/Kiev", so it make conflict. For avoiding this I wrote  static { TimeZone.setDefault(TimeZone.getTimeZone("UTC")); }

26.06.2026 Why a RoomInfo snapshot if Book already has FK to Room?
RoomInfo was created for user's booking-history. How it works? When a user makes a booking my server creates an object RoomInfo, which store 
price, hotel name and a room number at the moment of booking. And main advantage of this object is that it doesn't depend on Room's FK and even if this
room is later deleted from DB, our user still has this book in his history.

Why is the immutability guarantee in the Book constructor, not in the service?
Because only the Book constructor can guarantee that RoomInfo is always built in one place (Book's constructor), by one rule and will not be changed.
And I removed a Setter, Builder, AllArgsConstructor, in reason to remove possibility of changing any Book's field
and make NoArgsConstructor protected access for Hibernate. Next Book's constructor create RoomInfo inside from Room so it is impossible inject fake RoomInfo

Why is RoomInfo a record (embeddable)?
To guarantee immutability I would need make all fields private final and settable only through
the constructor. And here appear reason why I choose record, it does by language definition: all fields are private final and settable only though a constructor.

06.07.2026 I added CascadeType.MERGE in a Room.java for books because when our user made a book, I store this book on the room side
and user side, and is logical when I add new book for room's list and store it, I also want to store new book

16.07.2026 Why did I remove RoomCategory.ANY and Star.ANY? 
I decided to remove these enum values because a hotel can't have "any" stars and room can't have "any" category,
because "any" is value for searching, not for specified star or room category
Why did I add "hotel_star_check" and "room_category_check"?
The reason was to prevent that somebody would add to database an invalid value for these enum's.
This decision also has a downside, if developer decide to add new star or category value he also must 
add this change to new migration sql file, but if he forgets to do it, my tests will remind it for him
I decided to migrate my enums value in db from ordinal to varchar, because
Imagine a situation some developer put new enum value in the middle of class what does it mean? It means that old value 1 now is connected to another enum value, but with varchar it doesn't matter
where developer put new enum value.

22.07.2026 Why "room_id" is first here and is order here important CREATE INDEX idx_room_id_date ON book (room_id, start_date, end_date);
Here I will give an answer on both question, order is important because Bi-tree save rows as ordered list by column.
When room_id "Equality" is in first place leafs is ordered by room_id and inside narrowed by data.
If first argument is range "data", leaf will be firstly ordered by data and then by room_id, what make looking for available rooms harder.

22.07.2026 Why I have strict "<" and ">" here: criteriaBuilder.lessThan(bookRoot.get("bookedData").get("startDate"), end),
                                               criteriaBuilder.greaterThan(bookRoot.get("bookedData").get("endDate"), start)
The reason is that person can check in at the same date where another has check out in the same room

22.07.2026 Why is Specification better than JPQL in my case?
Specification is more flexibility than JPQL, and because I have 8 filter values and some of them is optional so flexibility in my case is important and Specification match here great.
When JPQL with new argument become more and more harder to maintain and understand

22.07.2026 criteriaBuilder.conjunction() vs null
.conjunction() is used for avoiding NPE and if you didn't give an argument sql give 1=1, what means that this filter isn't used