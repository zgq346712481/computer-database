package com.excilys.cdb.validator;

import com.excilys.cdb.dto.CreateComputerDTO;
import com.excilys.cdb.service.CompanyService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateComputerValidator extends Validator<CreateComputerDTO> {

    private static CreateComputerValidator instance;

    private CreateComputerValidator() {
    }

    public static CreateComputerValidator getInstance() {
        if (Objects.isNull(instance)) {
            instance = new CreateComputerValidator();
        }
        return instance;
    }

    @Override
    protected Map<String, String> validation(CreateComputerDTO toValidate) {
        HashMap<String, String> errors = new HashMap<>();
        if (Objects.isNull(toValidate.getName()) || toValidate.getName().trim().isEmpty()) {
            errors.put("name", "Le nom ne peux pas être nul ou vide.");
        }

        if (Objects.nonNull(toValidate.getIntroduced()) && Objects.nonNull(toValidate.getDiscontinued())
                && toValidate.getIntroduced().isAfter(toValidate.getDiscontinued())) {
            errors.put("discontinued", "Discontinued ne peux pas être avant Introduced.");
        }

        final Long mannufacturerId = toValidate.getMannufacturerId();
        if (Objects.nonNull(mannufacturerId) && !CompanyService.getInstance().exist(mannufacturerId)) {
            errors.put("mannufacturerId", "Le fabriaquant avec l'id " + mannufacturerId + " n'existe pas");
        }
        return errors;
    }

}