package com.xyzhotel.domain.account;

import com.xyzhotel.domain.shared.ValueObject;
import java.util.Objects;

/**
 * Value Object représentant le nom complet d'une personne
 */
public record FullName(String value) implements ValueObject {
    
    public FullName {
        Objects.requireNonNull(value, "Le nom complet ne peut pas être null");
        String trimmed = value.trim();
        if (trimmed.isBlank()) {
            throw new IllegalArgumentException("Le nom complet ne peut pas être vide");
        }
        if (trimmed.length() < 2) {
            throw new IllegalArgumentException("Le nom complet doit contenir au moins 2 caractères");
        }
    }
    
    public static FullName of(String value) {
        return new FullName(value.trim());
    }
}
