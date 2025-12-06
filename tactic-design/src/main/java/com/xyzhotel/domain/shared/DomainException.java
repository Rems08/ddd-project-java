package com.xyzhotel.domain.shared;

/**
 * Exception de base pour tous les erreurs métier du domaine
 */
public class DomainException extends Exception {
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
