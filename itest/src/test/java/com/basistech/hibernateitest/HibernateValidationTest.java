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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import java.io.File;
import java.util.Set;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

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
        MavenArtifactUrlReference karafUrl
                = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version("4.0.2")
                .type("tar.gz");
        return new Option[] {
                karafDistributionConfiguration()
                        .frameworkUrl(karafUrl)
                        .karafVersion("4.0.2")
                        .name("Apache Karaf")
                        .useDeployFolder(false)
                        //.runEmbedded(true)
                        .unpackDirectory(new File("target/pax/")),
                //DO NOT COMMIT WITH THIS LINE ENABLED!!!
                //KarafDistributionOption.keepRuntimeFolder(),
                //debugConfiguration(), // nor this
                systemProperty("java.awt.headless").value("true"),
                features(maven().groupId("com.basistech").artifactId("hibernate-validation")
                        .classifier("features")
                        .type("xml")
                        .version("0.0.1-SNAPSHOT"),
                        "hibernate-validation"),
                logLevel(LogLevelOption.LogLevel.INFO),
                // Do we need the junit stuff?
        };
    }

    @Test
    public void defaultProvider() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ValidateMe>> violations = validator.validate(new ValidateMe(0));
        Assert.assertEquals(0, violations.size());
    }
}
