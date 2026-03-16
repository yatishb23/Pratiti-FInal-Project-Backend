package com.sne.service;
import com.sne.client.AccountClient;
import com.sne.dto.BeneficiaryDto;
import com.sne.dto.mapper.BeneficiaryMapper;
import com.sne.entity.Beneficiary;
import com.sne.exception.ResourceConflict;
import com.sne.exception.ResourceNotFound;
import com.sne.external.AccountDto;
import com.sne.dto.response.Response;
import com.sne.repository.BeneficiaryRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository repository;
    private final BeneficiaryMapper mapper = new BeneficiaryMapper();

    @Value("${spring.application.ok}")
    private String success;
    
   
      @Autowired
        private AccountClient accountClient; // Feign Client to Account Microservice

//      public Response addBeneficiary(BeneficiaryDto beneficiaryDto) {
//          String accNo = beneficiaryDto.getBeneficiaryAccountNo();
//          boolean needsCreation = false;
//
//          // 1. Check if Account exists in Account Microservice
//          try {
//              // This matches your AccountController returning ResponseEntity<AccountDto>
//              accountClient.getAccountByNumber(accNo);
//              log.info("Account {} already exists in Account Service. Proceeding to link.", accNo);
//          } catch (FeignException e) {
//        	    // We check the HTTP status OR the JSON message body for "404"
//        	    String errorBody = e.contentUTF8();
//        	    
//        	    if (e.status() == 404 || (errorBody != null && errorBody.contains("\"message\":\"404\""))) {
//        	        log.info("Account not found (Handled {}). Proceeding to create.", e.status());
//        	        needsCreation = true;
//        	    } else {
//        	        log.error("Actual Account Service Error: {}", errorBody);
//        	        return Response.builder().responseCode("500").message("Account Service Error").build();
//        	    }
//        	}
//
//          // 2. Create Placeholder Account if missing
//          if (needsCreation) {
//              AccountDto newAcc = AccountDto.builder()
//                      .accountNumber(accNo)
//                      .userId(beneficiaryDto.getUserId())
//                      .accountType("BENEFICIARY")
//                      .accountStatus("ACTIVE") 
//                      .availableBalance(BigDecimal.ZERO)
//                      .branchName(beneficiaryDto.getBranchName())
//                      .build();
//
//              Response createRes = accountClient.createAccount(newAcc);
//              
//              // Safety check: Ensure the response from Account Service is successful
//              // Matches the 'success' code variable you set in AccountServiceImpl
//              if (createRes == null || (!"201".equals(createRes.getResponseCode()) && !"200".equals(createRes.getResponseCode()))) {
//                   log.error("Account creation failed for No: {}. Response: {}", accNo, createRes);
//                   return Response.builder()
//                           .responseCode("500")
//                           .message("Failed to initialize account in Account Service")
//                           .build();
//              }
//          }
//
//          // 3. Prevent Duplicate Links for the same user in Beneficiary DB
//          if (repository.existsByUserIdAndBeneficiaryAccountNo(beneficiaryDto.getUserId(), beneficiaryDto.getBeneficiaryAccountNo())) {
//              log.warn("User {} tried to add duplicate beneficiary {}", beneficiaryDto.getUserId(), accNo);
//              return Response.builder()
//                      .responseCode("409")
//                      .message("Beneficiary already exists in your list")
//                      .build();
//          }
//          
//          System.out.println(beneficiaryDto.getNickName());
//
//
//          
//          Beneficiary beneficiary = new Beneficiary();
//          
//          // 5. Ensure internal fields are set (since they aren't in the input JSON)
//          beneficiary.setStatus("ACTIVE");
//          beneficiary.setAddedTimestamp(LocalDateTime.now());
//          beneficiary.setAccountType("BENEFICIARY");
//          beneficiary.setBranchName(beneficiaryDto.getBranchName());
//          beneficiary.setUserId(beneficiaryDto.getUserId());
//          beneficiary.setBeneficiaryAccountNo(accNo);
//          beneficiary.setBeneficiaryName(beneficiaryDto.getBeneficiaryName());
//          beneficiary.setNickName(beneficiaryDto.getNickName());
//          beneficiary.setEmailId(beneficiaryDto.getEmailId());
//          beneficiary.setIfscCode(beneficiaryDto.getIfscCode());
//
//          repository.save(beneficiary);
//          log.info("Beneficiary {} successfully added for user {}", accNo, beneficiaryDto.getUserId());
//
//          return Response.builder()
//                  .responseCode("201")
//                  .message("Beneficiary added and synchronized successfully")
//                  .build();
//      }
   // Define the mapping (Ideally, move this to a shared Utility class or Config)
      private static final Map<String, String> IFSC_TO_BRANCH_MAP = Map.of(
    		    "BANK0001234", "AUNDH",
    		    "BANK0005678", "HINJEWADI",
    		    "BANK0009012", "SHIVAJINAGAR",
    		    "BANK0005682", "BANER"
    		);
      public Response addBeneficiary(BeneficiaryDto beneficiaryDto) {
    	    String accNo = beneficiaryDto.getBeneficiaryAccountNo();
    	    // 1. Get IFSC from user input and normalize it
    	    String ifscInput = beneficiaryDto.getIfscCode() != null ? beneficiaryDto.getIfscCode().toUpperCase().trim() : "";
    	    
    	    // 2. Validate IFSC and Resolve Branch automatically
    	    String resolvedBranch = IFSC_TO_BRANCH_MAP.get(ifscInput);
    	    
    	    if (resolvedBranch == null) {
    	        log.warn("Invalid IFSC code received: {}", ifscInput);
    	        return Response.builder()
    	                .responseCode("400")
    	                .message("Invalid IFSC Code. Please provide a valid bank IFSC.")
    	                .build();
    	    }

    	    boolean needsCreation = false;

    	    // 3. Check if Account exists in Account Microservice
    	    try {
    	        accountClient.getAccountByNumber(accNo);
    	    } catch (FeignException e) {
    	        String errorBody = e.contentUTF8();
    	        if (e.status() == 404 || (errorBody != null && errorBody.contains("\"message\":\"404\""))) {
    	            needsCreation = true;
    	        } else {
    	            return Response.builder().responseCode("500").message("Account Service Error").build();
    	        }
    	    }

    	    // 4. Create Placeholder Account if missing
    	    if (needsCreation) {
    	        AccountDto newAcc = AccountDto.builder()
    	                .accountNumber(accNo)
    	                .userId(beneficiaryDto.getUserId())
    	                .accountType("BENEFICIARY")
    	                .accountStatus("ACTIVE") 
    	                .availableBalance(BigDecimal.ZERO)
    	                // Use the Branch we found via the IFSC
    	                .branchName(resolvedBranch) 
    	                .build();

    	        Response createRes = accountClient.createAccount(newAcc);
    	        
    	        if (createRes == null || (!"201".equals(createRes.getResponseCode()) && !"200".equals(createRes.getResponseCode()))) {
    	             return Response.builder().responseCode("500").message("Account Sync Failed").build();
    	        }
    	    }

    	    // 5. Duplicate Check
    	    if (repository.existsByUserIdAndBeneficiaryAccountNo(beneficiaryDto.getUserId(), accNo)) {
    	        return Response.builder().responseCode("409").message("Beneficiary already exists").build();
    	    }

    	    // 6. Save Beneficiary
    	    Beneficiary beneficiary = new Beneficiary();
    	    beneficiary.setStatus("ACTIVE");
    	    beneficiary.setAddedTimestamp(LocalDateTime.now());
    	    beneficiary.setAccountType("BENEFICIARY");
    	    beneficiary.setUserId(beneficiaryDto.getUserId());
    	    beneficiary.setBeneficiaryAccountNo(accNo);
    	    beneficiary.setBeneficiaryName(beneficiaryDto.getBeneficiaryName());
    	    beneficiary.setNickName(beneficiaryDto.getNickName());
    	    beneficiary.setEmailId(beneficiaryDto.getEmailId());
    	    
    	    // Set both based on the resolved logic
    	    beneficiary.setIfscCode(ifscInput);
    	    beneficiary.setBranchName(resolvedBranch);

    	    repository.save(beneficiary);

    	    return Response.builder()
    	            .responseCode("201")
    	            .message("Beneficiary added to " + resolvedBranch + " branch successfully")
    	            .build();
    	}
      
    @Override
    public BeneficiaryDto getBeneficiaryById(Long id) {
        return repository.findByBeneficiaryAccountNo(id)
                .map(mapper::convertToDto)
                .orElseThrow(() -> new ResourceNotFound("Beneficiary not found"));
    }

//    
//    public Response updateBeneficiary(Long id, BeneficiaryDto dto) {
//        return repository.findByBeneficiaryAccountNo(id)
//                .map(b -> {
//                    BeanUtils.copyProperties(dto, b, "beneficiaryId", "addedTimestamp");
//                    repository.save(b);
//                    return Response.builder()
//                            .responseCode(success)
//                            .message("Beneficiary updated successfully")
//                            .build();
//                }).orElseThrow(() -> new ResourceNotFound("Beneficiary not found"));
//    }
    @Override
    public Response updateBeneficiary(Long accountNo, BeneficiaryDto dto) {
        return repository.findByBeneficiaryAccountNo(accountNo)
                .map(b -> {
                    // Copy only safe fields
                    BeanUtils.copyProperties(dto, b, "beneficiaryId", "beneficiaryAccountNo", "addedTimestamp", "customerId");
                    repository.save(b);
                    return Response.builder()
                            .responseCode(success)
                            .message("Beneficiary updated successfully")
                            .build();
                }).orElseThrow(() -> new ResourceNotFound("Beneficiary not found"));
    }
  
    public Response deleteBeneficiary(Long id) {
        return repository.findByBeneficiaryAccountNo(id)
                .map(b -> {
                    repository.delete(b);
                    return Response.builder()
                            .responseCode(success)
                            .message("Beneficiary deleted successfully")
                            .build();
                }).orElseThrow(() -> new ResourceNotFound("Beneficiary not found"));
    }


    @Override
    public List<BeneficiaryDto> getBeneficiariesByCustomer(Long userId) {
        log.info("Fetching all beneficiaries for user: {}", userId);

        // 1. Fetch the list of entities from the database
        List<Beneficiary> beneficiaries = repository.findByUserId(userId);

        // 2. Map the Entities to DTOs and return
        return beneficiaries.stream()
                .map(entity -> BeneficiaryDto.builder()
                        .userId(entity.getUserId())
                        .beneficiaryAccountNo(entity.getBeneficiaryAccountNo())
                        .beneficiaryName(entity.getBeneficiaryName())
                        .ifscCode(entity.getIfscCode()).accountType("BENEFICIARY")
                        .emailId(entity.getEmailId())
                        .status(entity.getStatus())
                        .nickName(entity.getNickName())
                        .addedTimestamp(entity.getAddedTimestamp())
                        .build())
                .collect(Collectors.toList());
    }

}