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

package com.basistech.hibernateitest.mw;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

/**
 *
 */
public class MockWhiteboardActivator implements BundleActivator {
    private static final Logger LOG = LoggerFactory.getLogger(MockWhiteboardActivator.class);
    @Override
    public void start(BundleContext context) throws Exception {
        LOG.info("whiteboard activator");
        Hashtable<String, Object> props = new Hashtable<>();
        props.put("description", "Mock Whiteboard");
        MockWhiteboardService service = new MockWhiteboard();
        context.registerService(MockWhiteboardService.class.getName(), service, props);
        context.registerService(Runnable.class.getName(), service, props);
        LOG.info("Whiteboard registered.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        LOG.info("whiteboard stop.");
    }
}
