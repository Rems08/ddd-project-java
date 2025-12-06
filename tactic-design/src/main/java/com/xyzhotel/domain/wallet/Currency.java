package com.xyzhotel.domain.wallet;

import java.math.BigDecimal;

/**
 * Énumération des devises acceptées par l'hôtel
 * Avec leur taux de conversion vers l'Euro
 */
public enum Currency {
    EUR(BigDecimal.ONE),           // Euro (devise de référence)
    USD(BigDecimal.valueOf(0.92)), // Dollar américain
    GBP(BigDecimal.valueOf(1.17)), // Livre Sterling
    JPY(BigDecimal.valueOf(0.0062)), // Yen japonais
    CHF(BigDecimal.valueOf(1.05)); // Franc Suisse
    
    private final BigDecimal conversionRateToEuro;
    
    Currency(BigDecimal conversionRateToEuro) {
        this.conversionRateToEuro = conversionRateToEuro;
    }
    
    /**
     * Convertit un montant de cette devise vers l'Euro
     * @param amount Le montant à convertir
     * @return Le montant en Euro
     */
    public BigDecimal convertToEuro(BigDecimal amount) {
        return amount.multiply(conversionRateToEuro);
    }
}
