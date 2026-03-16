package com.sne.dto.mapper;

import com.sne.dto.BeneficiaryDto;
import com.sne.entity.Beneficiary;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public class BeneficiaryMapper extends BaseMapper<Beneficiary, BeneficiaryDto> {

    @Override
    public Beneficiary convertToEntity(BeneficiaryDto dto, Object... args) {
        Beneficiary beneficiary = new Beneficiary();
        if (!Objects.isNull(dto)) {
            BeanUtils.copyProperties(dto, beneficiary);
        }
        return beneficiary;
    }

    @Override
    public BeneficiaryDto convertToDto(Beneficiary entity, Object... args) {
        BeneficiaryDto dto = new BeneficiaryDto();
        if (!Objects.isNull(entity)) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}