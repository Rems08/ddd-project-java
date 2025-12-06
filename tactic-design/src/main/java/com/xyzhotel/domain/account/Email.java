package com.xyzhotel.domain.account;

import com.xyzhotel.domain.shared.ValueObject;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object représentant une adresse email
 */
public record Email(String value) implements ValueObject {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    public Email {
        Objects.requireNonNull(value, "L'email ne peut pas être null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Format d'email invalide: " + value);
        }
    }
    
    public static Email of(String value) {
        return new Email(value);
    }
}
