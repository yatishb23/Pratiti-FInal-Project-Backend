//package com.sne.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.sne.model.AccountType;
//import com.sne.model.entity.Account;
//
//import java.util.Optional;
//
//public interface AccountRepository extends JpaRepository<Account, String> {
//
//    
//
//    Optional<Account> findAccountByCustomerIdAndAccountType(Long userId, AccountType accountType);
//
// 
//    Optional<Account> findAccountByAccountNumber(Long accountNumber);
//
//  
////    Optional<Account> findAccountByustomerId(Long userId);
//
//
////	Optional<Account> findAccountByCustomerId(Long userId);
//
//
//	Optional<Account> findAccountByUserId(Long userId);
//}

package com.sne.repository;

import com.sne.model.AccountType;
import com.sne.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Finds an account by its unique 11-character string account number.
     * Spring Data JPA derives the query: SELECT * FROM accounts WHERE account_number = ?
     */
//    Optional<Account> findAccountByAccountNumber(String accountNumber);

    /**
     * Finds the account associated with a specific Customer/User ID.
     * Assumes the field in your Account entity is named 'userId' or 'customerId'.
     */
    Optional<Account> findAccountByUserId(Long userId);

    /**
     * Checks if an account exists by its string account number.
     * Useful for the Beneficiary Service internal validation.
     */
    boolean existsByAccountNumber(String accountNumber);

	Optional<Account> findAccountByAccountNumber(String accountNumber);

	Optional<List<Account>> findByUserIdAndAccountType(Long userId, AccountType self);
}
