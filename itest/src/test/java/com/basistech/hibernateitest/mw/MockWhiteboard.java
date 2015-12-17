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

/**
 *
 */
public class MockWhiteboard implements MockWhiteboardService {

    Runnable thing;

    @Override
    public void registerRunnable(Runnable runnable) {
        thing = runnable;
    }

    @Override
    public void run() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                thing.run();
            }
        };
        thread.setContextClassLoader(getClass().getClassLoader());
        thread.run();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
