package com.xyzhotel.infrastructure.config;

import com.xyzhotel.application.account.CreateAccountUseCase;
import com.xyzhotel.application.account.GetAccountUseCase;
import com.xyzhotel.application.account.GetAllAccountsUseCase;
import com.xyzhotel.application.admin.GetRoomBookingHistoryUseCase;
import com.xyzhotel.application.admin.GetRoomStatisticsUseCase;
import com.xyzhotel.application.booking.*;
import com.xyzhotel.application.room.GetRoomInfoUseCase;
import com.xyzhotel.application.wallet.CreditWalletUseCase;
import com.xyzhotel.application.wallet.GetWalletBalanceUseCase;
import com.xyzhotel.domain.account.AccountRepository;
import com.xyzhotel.domain.booking.BookingRepository;
import com.xyzhotel.domain.room.RoomRepository;
import com.xyzhotel.domain.wallet.WalletRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration des use cases de l'application
 */
@Configuration
public class UseCaseConfig {
    
    // Account Use Cases
    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepository accountRepository,
                                                      WalletRepository walletRepository) {
        return new CreateAccountUseCase(accountRepository, walletRepository);
    }
    
    @Bean
    public GetAccountUseCase getAccountUseCase(AccountRepository accountRepository) {
        return new GetAccountUseCase(accountRepository);
    }
    
    @Bean
    public GetAllAccountsUseCase getAllAccountsUseCase(AccountRepository accountRepository) {
        return new GetAllAccountsUseCase(accountRepository);
    }
    
    // Wallet Use Cases
    @Bean
    public CreditWalletUseCase creditWalletUseCase(AccountRepository accountRepository,
                                                    WalletRepository walletRepository) {
        return new CreditWalletUseCase(accountRepository, walletRepository);
    }
    
    @Bean
    public GetWalletBalanceUseCase getWalletBalanceUseCase(AccountRepository accountRepository,
                                                            WalletRepository walletRepository) {
        return new GetWalletBalanceUseCase(accountRepository, walletRepository);
    }
    
    // Room Use Cases
    @Bean
    public GetRoomInfoUseCase getRoomInfoUseCase(RoomRepository roomRepository) {
        return new GetRoomInfoUseCase(roomRepository);
    }
    
    // Booking Use Cases
    @Bean
    public CreateBookingUseCase createBookingUseCase(AccountRepository accountRepository,
                                                      RoomRepository roomRepository,
                                                      BookingRepository bookingRepository,
                                                      WalletRepository walletRepository) {
        return new CreateBookingUseCase(accountRepository, roomRepository, bookingRepository, walletRepository);
    }
    
    @Bean
    public ConfirmBookingUseCase confirmBookingUseCase(BookingRepository bookingRepository,
                                                        AccountRepository accountRepository,
                                                        WalletRepository walletRepository) {
        return new ConfirmBookingUseCase(bookingRepository, accountRepository, walletRepository);
    }
    
    @Bean
    public CancelBookingUseCase cancelBookingUseCase(BookingRepository bookingRepository,
                                                      RoomRepository roomRepository) {
        return new CancelBookingUseCase(bookingRepository, roomRepository);
    }
    
    @Bean
    public GetBookingUseCase getBookingUseCase(BookingRepository bookingRepository) {
        return new GetBookingUseCase(bookingRepository);
    }
    
    @Bean
    public GetAccountBookingsUseCase getAccountBookingsUseCase(BookingRepository bookingRepository,
                                                                AccountRepository accountRepository) {
        return new GetAccountBookingsUseCase(bookingRepository, accountRepository);
    }
    
    // Admin Use Cases
    @Bean
    public GetRoomStatisticsUseCase getRoomStatisticsUseCase(RoomRepository roomRepository) {
        return new GetRoomStatisticsUseCase(roomRepository);
    }
    
    @Bean
    public GetRoomBookingHistoryUseCase getRoomBookingHistoryUseCase(BookingRepository bookingRepository) {
        return new GetRoomBookingHistoryUseCase(bookingRepository);
    }
}
