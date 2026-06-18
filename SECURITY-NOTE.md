1- I decided to extract data from OidcUser instead of http session, because OidcUser is object from security context and contains only verified data, so are trustful
2- OidcUser is object from security context which is created after user authorized
3- I accept make /bookedRoom only for authorized user, because, only when authorized user made a book service can connect user with this book and be 100% confident that this user is real.
If I just connect email from form to book, after that person who authorized with this email can watch all books, it can be some attacer which stole this email
4- CSRF token exists for protect your data from attack like this: "bad site made your browser send request to your app with your session cookies and think that this is you", but csrf token 
is the tool which helps to server understand "Is this real you?". csrf token is generated in server and is injected as 'hidden' field
so 'bad site' can't read it because this action is forbidden by same-origin policy of your browser
5- The server enforce the check, it means that if a user tries to get access to forbidden for him url, server stops him, but if I would just hide it by UI, for user it would be very easy to get access to forbidden url
6- I also use hasAuthority, not hasRole, because, hasRole add prefix (_ROLE), which isn't in my role, so hasRole doen't match to my roles
7- I add separated CSRF test, because I have two test which must return 403 (Forbidden), so I must catch which is reason of tests result
8- For auth user I can extract email from security context, NOT FROM DTO, because user can't give fake email or another data to Security context, so it is safer to use data from 
security context
9- My method extractRole(String token) catch only NULL structure-element, if some element is NULL my method return empty Set, because FAKE JWT will be caught in decode(), 
and if inside JWT's structure is incorrect data, this error will be thrown on the end of the method