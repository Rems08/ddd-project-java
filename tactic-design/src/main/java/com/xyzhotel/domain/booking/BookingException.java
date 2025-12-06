package com.xyzhotel.domain.booking;

import com.xyzhotel.domain.shared.DomainException;

/**
 * Exception levée lors d'erreurs liées aux réservations
 */
public class BookingException extends DomainException {
    public BookingException(String message) {
        super(message);
    }
}
