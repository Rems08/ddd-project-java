package com.xyzhotel.infrastructure.web.controller;

import com.xyzhotel.application.wallet.CreditWalletUseCase;
import com.xyzhotel.application.wallet.GetWalletBalanceUseCase;
import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.wallet.WalletException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * Contrôleur REST pour la gestion des portefeuilles
 */
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    
    private final CreditWalletUseCase creditWalletUseCase;
    private final GetWalletBalanceUseCase getWalletBalanceUseCase;
    
    public WalletController(CreditWalletUseCase creditWalletUseCase,
                           GetWalletBalanceUseCase getWalletBalanceUseCase) {
        this.creditWalletUseCase = creditWalletUseCase;
        this.getWalletBalanceUseCase = getWalletBalanceUseCase;
    }
    
    /**
     * Alimenter le portefeuille
     * POST /api/wallets/{accountId}/credit
     */
    @PostMapping("/{accountId}/credit")
    public ResponseEntity<CreditWalletResponse> creditWallet(
            @PathVariable String accountId,
            @Valid @RequestBody CreditWalletRequest request) 
            throws AccountException, WalletException {
        
        CreditWalletUseCase.CreditWalletCommand command = new CreditWalletUseCase.CreditWalletCommand(
            accountId,
            request.amount(),
            request.currency()
        );
        
        CreditWalletUseCase.CreditWalletResult result = creditWalletUseCase.execute(command);
        
        CreditWalletResponse response = new CreditWalletResponse(
            result.accountId(),
            result.newBalance(),
            result.creditedAmountInEuros(),
            "EUR",
            "Portefeuille crédité avec succès"
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Consulter le solde du portefeuille
     * GET /api/wallets/{accountId}/balance
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<WalletBalanceResponse> getBalance(@PathVariable String accountId) 
            throws AccountException {
        
        GetWalletBalanceUseCase.WalletBalanceResult result = getWalletBalanceUseCase.execute(accountId);
        
        WalletBalanceResponse response = new WalletBalanceResponse(
            result.accountId(),
            result.balance(),
            "EUR"
        );
        
        return ResponseEntity.ok(response);
    }
    
    public record CreditWalletRequest(
        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
        BigDecimal amount,
        
        @NotBlank(message = "La devise est obligatoire")
        String currency
    ) {}
    
    public record CreditWalletResponse(
        String accountId,
        BigDecimal newBalance,
        BigDecimal creditedAmountInEuros,
        String currency,
        String message
    ) {}
    
    public record WalletBalanceResponse(
        String accountId,
        BigDecimal balance,
        String currency
    ) {}
}
