// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;


class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";


    public String getMethod() {
        return METHOD_NAME;
    }


    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }


    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }


    public HttpDeleteWithBody() {
        super();
    }
}