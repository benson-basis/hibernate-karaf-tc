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

package com.basistech.hibernateitest;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.spi.ValidationProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

/**
 *
 */
@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class HibernateValidationTest {

    static class ValidateMe {
        @Min(1)
        private int number;

        public ValidateMe(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    @Configuration
    public static Option[] configuration() {
        return new Option[] {
                mavenBundle().groupId("javax.validation")
                        .artifactId("validation-api")
                        .versionAsInProject(),
                mavenBundle().groupId("org.jboss.logging")
                        .artifactId("jboss-logging")
                        .versionAsInProject(),
                mavenBundle().groupId("com.fasterxml")
                        .artifactId("classmate")
                        .versionAsInProject(),
                mavenBundle().groupId("org.apache.servicemix.bundles")
                .artifactId("org.apache.servicemix.bundles.hibernate-validator")
                .versionAsInProject(),
                mavenBundle().groupId("org.glassfish")
                        .artifactId("javax.el")
                        .versionAsInProject(),
                mavenBundle().groupId("javax.el")
                        .artifactId("javax.el-api")
                        .versionAsInProject(),
                mavenBundle().groupId("org.apache.servicemix.bundles")
                        .artifactId("org.apache.servicemix.bundles.joda-time")
                        .versionAsInProject(),
                mavenBundle().groupId("org.apache.servicemix.bundles")
                        .artifactId("org.apache.servicemix.bundles.jsoup")
                        .versionAsInProject(),
                mavenBundle().groupId("org.apache.servicemix.specs")
                        .artifactId("org.apache.servicemix.specs.stax-api-1.2")
                        .versionAsInProject(),
                mavenBundle().groupId("org.codehaus.woodstox")
                        .artifactId("woodstox-core-asl")
                        .versionAsInProject(),
                mavenBundle().groupId("org.codehaus.woodstox")
                        .artifactId("stax2-api")
                        .versionAsInProject(),
                //debugConfiguration(), // nor this
                systemProperty("java.awt.headless").value("true"),
                junitBundles(),
                systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
                systemProperty("org.ops4j.pax.exam.rbc.rmi.host").value("localhost")
        };
    }

    @Test
    @Ignore
    public void defaultProvider() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ValidateMe>> violations = validator.validate(new ValidateMe(0));
        Assert.assertEquals(1, violations.size());
    }

    @Test
    public void withConfiguration() {
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
        //configuration.externalClassLoader(Thread.currentThread().getContextClassLoader());
        Validator validator = configuration.buildValidatorFactory().getValidator();
        Thread.currentThread().setContextClassLoader(null);
        Set<ConstraintViolation<ValidateMe>> violations = validator.validate(new ValidateMe(0));
        Assert.assertEquals(1, violations.size());
    }
}
