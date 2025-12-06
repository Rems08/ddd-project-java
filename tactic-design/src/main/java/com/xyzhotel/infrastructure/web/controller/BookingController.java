package com.xyzhotel.infrastructure.web.controller;

import com.xyzhotel.application.booking.CancelBookingUseCase;
import com.xyzhotel.application.booking.ConfirmBookingUseCase;
import com.xyzhotel.application.booking.CreateBookingUseCase;
import com.xyzhotel.application.booking.GetBookingUseCase;
import com.xyzhotel.domain.account.AccountException;
import com.xyzhotel.domain.booking.BookingException;
import com.xyzhotel.domain.room.RoomException;
import com.xyzhotel.domain.wallet.WalletException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des réservations
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    
    private final CreateBookingUseCase createBookingUseCase;
    private final ConfirmBookingUseCase confirmBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final GetBookingUseCase getBookingUseCase;
    
    public BookingController(CreateBookingUseCase createBookingUseCase,
                            ConfirmBookingUseCase confirmBookingUseCase,
                            CancelBookingUseCase cancelBookingUseCase,
                            GetBookingUseCase getBookingUseCase) {
        this.createBookingUseCase = createBookingUseCase;
        this.confirmBookingUseCase = confirmBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
        this.getBookingUseCase = getBookingUseCase;
    }
    
    /**
     * Effectuer une réservation
     * POST /api/bookings
     */
    @PostMapping
    public ResponseEntity<CreateBookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) 
            throws AccountException, RoomException, BookingException, WalletException {
        
        CreateBookingUseCase.CreateBookingCommand command = new CreateBookingUseCase.CreateBookingCommand(
            request.accountId(),
            request.checkInDate(),
            request.numberOfNights(),
            request.rooms()
        );
        
        CreateBookingUseCase.CreateBookingResult result = createBookingUseCase.execute(command);
        
        CreateBookingResponse response = new CreateBookingResponse(
            result.bookingId(),
            result.accountId(),
            result.checkInDate(),
            result.numberOfNights(),
            result.totalAmount(),
            result.paidAmount(),
            result.status(),
            "Réservation créée avec succès. 50% du montant total a été débité."
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Confirmer une réservation
     * POST /api/bookings/{bookingId}/confirm
     */
    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<ConfirmBookingResponse> confirmBooking(@PathVariable String bookingId) 
            throws BookingException, AccountException, WalletException {
        
        ConfirmBookingUseCase.ConfirmBookingResult result = confirmBookingUseCase.execute(bookingId);
        
        ConfirmBookingResponse response = new ConfirmBookingResponse(
            result.bookingId(),
            result.status(),
            result.totalAmount(),
            result.paidAmount(),
            "Réservation confirmée avec succès. Le montant restant a été débité."
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Annuler une réservation
     * POST /api/bookings/{bookingId}/cancel
     */
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<CancelBookingResponse> cancelBooking(@PathVariable String bookingId) 
            throws BookingException, RoomException {
        
        CancelBookingUseCase.CancelBookingResult result = cancelBookingUseCase.execute(bookingId);
        
        CancelBookingResponse response = new CancelBookingResponse(
            result.bookingId(),
            result.status(),
            result.message()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Récupérer les détails d'une réservation
     * GET /api/bookings/{bookingId}
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDetailsResponse> getBooking(@PathVariable String bookingId) 
            throws BookingException {
        
        GetBookingUseCase.BookingDetailsResult result = getBookingUseCase.execute(bookingId);
        
        List<RoomItemResponse> rooms = result.rooms().stream()
            .map(item -> new RoomItemResponse(
                item.roomId(),
                item.roomType(),
                item.pricePerNight(),
                item.numberOfNights()
            ))
            .toList();
        
        BookingDetailsResponse response = new BookingDetailsResponse(
            result.bookingId(),
            result.accountId(),
            result.checkInDate(),
            result.numberOfNights(),
            result.totalAmount(),
            result.paidAmount(),
            result.remainingAmount(),
            result.status(),
            result.createdAt(),
            rooms
        );
        
        return ResponseEntity.ok(response);
    }
    
    public record CreateBookingRequest(
        @NotBlank(message = "L'identifiant du compte est obligatoire")
        String accountId,
        
        @NotNull(message = "La date de check-in est obligatoire")
        LocalDate checkInDate,
        
        @NotNull(message = "Le nombre de nuits est obligatoire")
        @Min(value = 1, message = "Le nombre de nuits doit être au moins 1")
        Integer numberOfNights,
        
        @NotEmpty(message = "Au moins une chambre doit être réservée")
        Map<String, Integer> rooms  // RoomType -> quantity
    ) {}
    
    public record CreateBookingResponse(
        String bookingId,
        String accountId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        String status,
        String message
    ) {}
    
    public record ConfirmBookingResponse(
        String bookingId,
        String status,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        String message
    ) {}
    
    public record CancelBookingResponse(
        String bookingId,
        String status,
        String message
    ) {}
    
    public record BookingDetailsResponse(
        String bookingId,
        String accountId,
        LocalDate checkInDate,
        int numberOfNights,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal remainingAmount,
        String status,
        LocalDateTime createdAt,
        List<RoomItemResponse> rooms
    ) {}
    
    public record RoomItemResponse(
        String roomId,
        String roomType,
        BigDecimal pricePerNight,
        int numberOfNights
    ) {}
}
