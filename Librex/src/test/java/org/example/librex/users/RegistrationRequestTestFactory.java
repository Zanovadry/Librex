package org.example.librex.users;

import org.example.librex.users.dto.RegistrationRequest;

public class RegistrationRequestTestFactory {

    public RegistrationRequest validUser() {
        RegistrationRequest r = new RegistrationRequest();
        setField(r, "firstname", "Test");
        setField(r, "surname", "User");
        setField(r, "email", "test@example.com");
        setField(r, "username", "tester");
        setField(r, "password", "Strong123!");
        return r;
    }

    public RegistrationRequest updatedUser() {
        RegistrationRequest r = new RegistrationRequest();
        setField(r, "firstname", "UpdatedName");
        setField(r, "surname", "UpdatedSurname");
        setField(r, "email", "updated@example.com");
        setField(r, "username", "tester");
        setField(r, "password", "Strong123!");
        return r;
    }

    private void setField(RegistrationRequest request, String field, String value) {
        try {
            var f = request.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(request, value);
        } catch (Exception ignored) {}
    }
}
