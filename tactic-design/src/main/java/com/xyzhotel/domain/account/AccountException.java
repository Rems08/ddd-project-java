package com.xyzhotel.domain.account;

import com.xyzhotel.domain.shared.DomainException;

/**
 * Exception levée lors d'erreurs liées aux comptes clients
 */
public class AccountException extends DomainException {
    public AccountException(String message) {
        super(message);
    }
}
