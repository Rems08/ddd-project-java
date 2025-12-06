package com.xyzhotel.domain.room;

import com.xyzhotel.domain.shared.DomainException;

/**
 * Exception levée lors d'erreurs liées aux chambres
 */
public class RoomException extends DomainException {
    public RoomException(String message) {
        super(message);
    }
}
