package com.xyzhotel.domain.wallet;

import com.xyzhotel.domain.shared.DomainException;

/**
 * Exception levée lors d'erreurs liées au portefeuille
 */
public class WalletException extends DomainException {
    public WalletException(String message) {
        super(message);
    }
}
