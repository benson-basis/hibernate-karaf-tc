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

import com.basistech.hibernateitest.ms.MockService;
import com.basistech.hibernateitest.ms.MockServiceActivator;
import com.basistech.hibernateitest.ms.ValidateMe;
import com.basistech.hibernateitest.mw.MockWhiteboard;
import com.basistech.hibernateitest.mw.MockWhiteboardActivator;
import com.basistech.hibernateitest.mw.MockWhiteboardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.tinybundles.core.TinyBundles;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.io.InputStream;

import static org.ops4j.pax.exam.CoreOptions.bundleStartLevel;
import static org.ops4j.pax.exam.CoreOptions.frameworkStartLevel;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.streamBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

/**
 *
 */
@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class HibernateValidationTest {

    @Inject
    BundleContext bundleContext;

    @Configuration
    public Option[] configuration() {
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
//                mavenBundle().groupId("org.apache.servicemix.bundles")
//                .artifactId("org.apache.servicemix.bundles.hibernate-validator")
//                .versionAsInProject(),
                mavenBundle().groupId("org.hibernate")
                  .artifactId("hibernate-validator")
                  .versionAsInProject(),
                mavenBundle().groupId("org.jboss.logging")
                        .artifactId("jboss-logging")
                        .versionAsInProject(),
               /* mavenBundle().groupId("org.apache.logging.log4j")
                        .artifactId("log4j-api")
                        .versionAsInProject(),
                mavenBundle()
                        .artifactId("pax-logging-log4j2")
                        .groupId("org.ops4j.pax.logging")
                        .versionAsInProject(),
                mavenBundle()
                        .artifactId("pax-logging-api")
                        .groupId("org.ops4j.pax.logging")
                        .versionAsInProject(),*/
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
                streamBundle(mockWhiteboardBundle()).startLevel(10),
                streamBundle(mockServiceBundle()).startLevel(20),
                frameworkStartLevel(30),
                systemProperty("java.awt.headless").value("true"),
                junitBundles(),
                systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
                systemProperty("org.ops4j.pax.exam.rbc.rmi.host").value("localhost")
        };
    }

//    @ProbeBuilder
//    public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {
//        // makes sure the generated Test-Bundle contains this import!
//        probe.setHeader(Constants.BUNDLE_SYMBOLICNAME, "com.basistech.hibernate-validator-test");
//        probe.setHeader(Constants.DYNAMICIMPORT_PACKAGE, "com.basistech.hibernateitest.mw,*");
//        return probe;
//    }


    private InputStream mockWhiteboardBundle() {
        return TinyBundles.bundle()
                .add(MockWhiteboardActivator.class)
                .add(MockWhiteboard.class)
                .add(MockWhiteboardService.class)
                .set(Constants.IMPORT_PACKAGE, "org.osgi.framework,org.slf4j")
                .set(Constants.EXPORT_PACKAGE, "com.basistech.hibernateitest.mw")
                .set(Constants.BUNDLE_ACTIVATOR, MockWhiteboardActivator.class.getName())
                .build(TinyBundles.withClassicBuilder());
    }

    private InputStream mockServiceBundle() {
        return TinyBundles.bundle()
                .add(MockServiceActivator.class)
                .add(MockService.class)
                .add(ValidateMe.class)
                .set(Constants.IMPORT_PACKAGE, "com.basistech.hibernateitest.mw,org.osgi.framework,"
                        + "org.osgi.util.tracker,"
                        + "javax.validation.constraints,"
                        + "org.slf4j,javax.validation,org.hibernate.validator,"
                        + "javax.validation.bootstrap,"
                        + "javax.el,"
                        + "com.sun.el")
                .set(Constants.BUNDLE_ACTIVATOR, MockServiceActivator.class.getName())
                .build(TinyBundles.withClassicBuilder());
    }

    @Test
    public void withConfiguration() throws Exception {
        ServiceTracker<Runnable, Runnable> tracker
                = new ServiceTracker<>(bundleContext, Runnable.class.getCanonicalName(), null);
        tracker.open();
        tracker.waitForService(0);
        Runnable whiteboardRunnable = tracker.getService();
        whiteboardRunnable.run();
    }
}
