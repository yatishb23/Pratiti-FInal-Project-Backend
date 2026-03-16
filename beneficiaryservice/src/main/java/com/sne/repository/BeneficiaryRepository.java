package com.sne.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sne.entity.Beneficiary;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, String> {
//    List<Beneficiary> findByCustomerId1(Long customerId);

	Optional<Beneficiary> findByBeneficiaryAccountNo(Long beneficiaryAccountNo);

	List<Beneficiary> findByUserId(Long userId);

//	boolean existsByCustomerIdAndBene/ficiaryAccountNo(Long customerId, Long beneficiaryAccountNo);

	boolean existsByUserIdAndBeneficiaryAccountNo(Long userId, String string);
}
