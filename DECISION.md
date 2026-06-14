05.06.2026/remove excessive dependency 'Jackson 2'/ because SB4 give me 'Jackson 3' from starter and two version make a conflict

06.06.2026 Guest bookings are tied to a transaction, not to an email.
Context - guests are allowed to book without creating an account.
Decision - the booking is stored without being linked to a User;
the email is contact information only, not an identity.
Why - if someone later registers with the same email, they will not gain access to someone else’s guest bookings;
identity must not be based on a mutable contact detail.


