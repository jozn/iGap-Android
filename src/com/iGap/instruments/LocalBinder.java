// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.lang.ref.WeakReference;
import android.os.Binder;


public class LocalBinder<S> extends Binder {

    private final WeakReference<S> mService;


    public LocalBinder(final S service) {
        mService = new WeakReference<S>(service);
    }


    public S getService() {
        return mService.get();
    }

}
