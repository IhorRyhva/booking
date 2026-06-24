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