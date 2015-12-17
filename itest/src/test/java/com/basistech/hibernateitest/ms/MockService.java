/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.hibernateitest.ms;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import javax.validation.spi.ValidationProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class MockService implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MockService.class);
    public void validateSomething() {
        HibernateValidatorConfiguration configuration =
                Validation.byProvider(HibernateValidator.class)
                        .providerResolver(new ValidationProviderResolver() {
                            @Override
                            public List<ValidationProvider<?>> getValidationProviders() {
                                ValidationProvider<HibernateValidatorConfiguration> prov = new HibernateValidator();
                                List<ValidationProvider<?>> provs = new ArrayList<>();
                                provs.add(prov);
                                return provs;
                            }
                        })
                        .configure();
        //Comment the following in to make it work
        //Thread.currentThread().setContextClassLoader(MockService.class.getClassLoader());
        Validator validator = configuration.buildValidatorFactory().getValidator();
        Set<ConstraintViolation<ValidateMe>> violations = validator.validate(new ValidateMe(0));
        if (violations.size() == 0) {
            LOG.error("Validation ran but didn't complain");
        }
    }

    @Override
    public void run() {
        validateSomething();
    }
}
