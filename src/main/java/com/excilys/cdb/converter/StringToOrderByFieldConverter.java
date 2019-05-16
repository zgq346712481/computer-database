package com.excilys.cdb.converter;

import com.excilys.cdb.persistence.page.OrderBy;
import org.springframework.core.convert.converter.Converter;

public class StringToOrderByFieldConverter implements Converter<String, OrderBy.Field> {
    @Override
    public OrderBy.Field convert(String source) {
        return OrderBy.Field.byIdentifier(source).orElseThrow(() -> new IllegalArgumentException(source));
    }
}
