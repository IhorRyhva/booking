06.06.2026 Moved the booking key "email" from the session to the authenticated principal
OidcUser, closing the hole where email come from user session data
06.06.2026 Fixed a bug in bookMain.html. Bug was that my post form didn't work, because
I forgot write "th:action", and due to this mistake Thymeleaf couldn't inject the CSRF token and I catch 403 forbidden error
08.06.2026 Today I added new authority "admin" and used in the filter chain on /admin/**. A non-admin user receive 403. I wrote 3 tests:
a negative test, a test where I checked corrects of working CSRF and test where I verifying that admin with CSRF can access to /admin/hotel/{id}/remove endpoint
14.06.2026 Today I added test which prove correct work of ny book methods, it checks that user can't make book without email
addingAttributeForPage "BookController" create new BookDTO, but for auth branch when we return to same page we must give all models attributes, so firstly we use 
addingAttributeForPage after add current BookDTO from current page, this is weak site of POST mapping /bookRoom
20.06.2026 logout-url move to the @Value + .yaml. Admin hotel/delete enforced server-side. I covered it with test
and verified correct works of these tests by breaking enforcement and they went red
20.06.2026 Added admin.html and for UX, I made that only user with admin authorized can see button "Admin panel", with sec:authority="hasAuthorize('admin')"
06.07.2026 Change bookDTO.getEmail() to oidcUser.getEmail() in order to oidcUser is trusted source of information, because
it was checked by KeyCloack.
All secrets were migrated to .env

30.07.2026 I added two new migration, set up soft delete logic for Room and Hotel, create admin specification and cover it with tests
05.08.2026 I added two new migration, hotels and rooms are now soft deleted, also I added batch for quicker data inserts.

08.08.2026 I added embedding for every room and fix some bug with booking, the most hard to find was
mistake where I wrote ":removed", not "remove", so my WHERE filter worked fully incorrect

10.08.2026 I added tests for my method "searchNotRemovedAndByEmbeddingWithLimit", and made sure that it works correctly.
My method give me good result on testing data 10/10 for literal "quiet".
I removed this distance upper border, because it is impossible to set normal border:
for quiet room: strange room is distance 0.438, for chromodynamics room is distance 0.605, but both fully aren't match for "quiet room"

