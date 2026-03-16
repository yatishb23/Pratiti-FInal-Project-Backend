package com.sne.service;

import java.util.List;

import com.sne.dto.BeneficiaryDto;
import com.sne.dto.response.Response;

public interface BeneficiaryService {

	Response addBeneficiary(BeneficiaryDto beneficiaryDto);

//	List<BeneficiaryDto> getBeneficiariesByCustomer(int customerId);

	BeneficiaryDto getBeneficiaryById(Long id);

	Response updateBeneficiary(Long id, BeneficiaryDto beneficiaryDto);

	Response deleteBeneficiary(Long id);

	List<BeneficiaryDto> getBeneficiariesByCustomer(Long userId);

}