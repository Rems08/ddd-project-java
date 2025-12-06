package com.xyzhotel.domain.shared;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object représentant un identifiant unique
 */
public record EntityId(String value) implements ValueObject {
    
    public EntityId {
        Objects.requireNonNull(value, "L'identifiant ne peut pas être null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être vide");
        }
    }
    
    public static EntityId generate() {
        return new EntityId(UUID.randomUUID().toString());
    }
    
    public static EntityId of(String value) {
        return new EntityId(value);
    }
}
