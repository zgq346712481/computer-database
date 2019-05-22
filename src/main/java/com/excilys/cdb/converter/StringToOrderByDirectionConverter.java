package com.excilys.cdb.converter;

import com.excilys.cdb.persistence.page.OrderBy;
import org.springframework.core.convert.converter.Converter;

public class StringToOrderByDirectionConverter implements Converter<String, OrderBy.Direction> {
    @Override
    public OrderBy.Direction convert(String source) {
        return OrderBy.Direction.byIdentifier(source).orElseThrow(() -> new IllegalArgumentException(source));
    }
}
