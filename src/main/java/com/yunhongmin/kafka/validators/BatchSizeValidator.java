package com.yunhongmin.kafka.validators;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

public class BatchSizeValidator implements ConfigDef.Validator {
    @Override
    public void ensureValid(String name, Object value) {
        Integer batchSize = (Integer) value;
        if (!(batchSize >=1 && batchSize <= 100)) {
           throw new ConfigException(
                   name, value,
                   "batch.size must be integer between 1 and 100"
           );
        }

    }
}
