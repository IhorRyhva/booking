06.06.2026 Moved the booking key "email" from the session to the authenticated principal
OidcUser, closing the hole where email come from user session data
06.06.2026 Fixed a bug in bookMain.html. Bug was that my post form didn't work, because
I forgot write "th:action", and due to this mistake Thymeleaf couldn't inject the CSRF token and I catch 403 forbidden error
08.06.2026 Today I added new authority "admin" and used in the filter chain on /admin/**. A non-admin user receive 403. I wrote 3 tests:
a negative test, a test where I checked corrects of working CSRF and test where I verifying that admin with CSRF can access to /admin/hotel/{id}/remove endpoint