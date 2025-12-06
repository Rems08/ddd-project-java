package com.xyzhotel.infrastructure.web.controller;

import com.xyzhotel.application.account.CreateAccountUseCase;
import com.xyzhotel.application.account.GetAccountUseCase;
import com.xyzhotel.application.account.GetAllAccountsUseCase;
import com.xyzhotel.application.booking.GetAccountBookingsUseCase;
import com.xyzhotel.domain.account.AccountException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des comptes clients
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final GetAllAccountsUseCase getAllAccountsUseCase;
    private final GetAccountBookingsUseCase getAccountBookingsUseCase;
    
    public AccountController(CreateAccountUseCase createAccountUseCase,
                            GetAccountUseCase getAccountUseCase,
                            GetAllAccountsUseCase getAllAccountsUseCase,
                            GetAccountBookingsUseCase getAccountBookingsUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.getAllAccountsUseCase = getAllAccountsUseCase;
        this.getAccountBookingsUseCase = getAccountBookingsUseCase;
    }
    
    /**
     * Créer un nouveau compte client
     * POST /api/accounts
     */
    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) 
            throws AccountException {
        
        CreateAccountUseCase.CreateAccountCommand command = new CreateAccountUseCase.CreateAccountCommand(
            request.fullName(),
            request.email(),
            request.phoneNumber()
        );
        
        CreateAccountUseCase.CreateAccountResult result = createAccountUseCase.execute(command);
        
        CreateAccountResponse response = new CreateAccountResponse(
            result.accountId(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            "Compte créé avec succès. Utilisez cet identifiant pour les services de l'hôtel."
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Récupérer tous les comptes
     * GET /api/accounts
     */
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<GetAllAccountsUseCase.AccountSummaryResult> results = getAllAccountsUseCase.execute();
        
        List<AccountResponse> response = results.stream()
            .map(result -> new AccountResponse(
                result.accountId(),
                result.fullName(),
                result.email(),
                result.phoneNumber(),
                result.createdAt()
            ))
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupérer un compte par son ID
     * GET /api/accounts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String id) throws AccountException {
        GetAccountUseCase.AccountDetailsResult result = getAccountUseCase.execute(id);
        
        AccountResponse response = new AccountResponse(
            result.accountId(),
            result.fullName(),
            result.email(),
            result.phoneNumber(),
            result.createdAt()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupérer toutes les réservations d'un compte
     * GET /api/accounts/{id}/bookings
     */
    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingSummaryResponse>> getAccountBookings(@PathVariable String id) 
            throws AccountException {
        
        List<GetAccountBookingsUseCase.BookingSummaryResult> results = getAccountBookingsUseCase.execute(id);
        
        List<BookingSummaryResponse> response = results.stream()
            .map(result -> new BookingSummaryResponse(
                result.bookingId(),
                result.checkInDate(),
                result.numberOfNights(),
                result.totalAmount(),
                result.paidAmount(),
                result.remainingAmount(),
                result.status(),
                result.createdAt()
            ))
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    public record CreateAccountRequest(
        @NotBlank(message = "Le nom complet est obligatoire")
        String fullName,
        
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,
        
        @NotBlank(message = "Le numéro de téléphone est obligatoire")
        String phoneNumber
    ) {}
    
    public record CreateAccountResponse(
        String accountId,
        String fullName,
        String email,
        String phoneNumber,
        String message
    ) {}
    
    public record AccountResponse(
        String accountId,
        String fullName,
        String email,
        String phoneNumber,
        LocalDateTime createdAt
    ) {}
    
    public record BookingSummaryResponse(
        String bookingId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal remainingAmount,
        String status,
        LocalDateTime createdAt
    ) {}
}

