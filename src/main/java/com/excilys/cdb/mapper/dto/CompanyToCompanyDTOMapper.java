package com.excilys.cdb.mapper.dto;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.model.Company;

@Component
public class CompanyToCompanyDTOMapper implements Mapper<Company, CompanyDTO> {

    @Override
    public CompanyDTO map(Company company) {
	Objects.requireNonNull(company);
	return CompanyDTO.builder().id(company.getId()).name(company.getName()).build();
    }

}
