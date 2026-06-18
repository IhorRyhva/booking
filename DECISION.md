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