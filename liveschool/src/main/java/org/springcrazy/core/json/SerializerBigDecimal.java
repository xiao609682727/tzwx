package org.springcrazy.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class SerializerBigDecimal extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(Objects.isNull(value)) {
            gen.writeNull();
        } else {
            // 这里取floor
            gen.writeString(value.setScale(2, RoundingMode.FLOOR).toString());
        }
    }
}
