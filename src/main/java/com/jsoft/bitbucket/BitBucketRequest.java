/**
 * Copyright (c) 2016 JSoft.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this
 * permission notice shall be included in all copies or substantial
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT
 * WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jsoft.bitbucket;

import com.jcabi.http.Request;
import com.jcabi.http.request.ApacheRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * The utility object for creating HTTP request.
 * @author Jason Wong
 *
 */
public final class BitBucketRequest {

    /**
     * User agent.
     */
    private static final String USER_AGENT = "bitbucket-client";

    /**
     * The BitBucket REST URL.
     */
    private final transient String url;

    /**
     * Ctor.
     * @param endpoint The BitBucket REST API endpoint
     */
    public BitBucketRequest(final String endpoint) {
        this.url = endpoint;
    }

    /**
     * Obtain the HTTP request.
     * @return The request to the endpoint.
     */
    public Request request() {
        return new ApacheRequest(this.url)
            .header(HttpHeaders.USER_AGENT, BitBucketRequest.USER_AGENT)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }
}
