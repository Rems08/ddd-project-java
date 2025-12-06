package com.xyzhotel.domain.account;

import com.xyzhotel.domain.shared.ValueObject;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object représentant un numéro de téléphone
 */
public record PhoneNumber(String value) implements ValueObject {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+?[0-9]{10,15}$"
    );
    
    public PhoneNumber {
        Objects.requireNonNull(value, "Le numéro de téléphone ne peut pas être null");
        String normalized = value.replaceAll("[\\s-]", "");
        if (!PHONE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Format de numéro de téléphone invalide: " + value);
        }
    }
    
    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}
