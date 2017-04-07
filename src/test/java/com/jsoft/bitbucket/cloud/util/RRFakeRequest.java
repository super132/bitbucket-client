/**
 * 
 */
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
package com.jsoft.bitbucket.cloud.util;

import com.jcabi.http.Request;
import com.jcabi.http.RequestBody;
import com.jcabi.http.RequestURI;
import com.jcabi.http.Response;
import com.jcabi.http.Wire;
import com.jcabi.http.request.FakeRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Round-Robin fake request for testing purpose.
 * @author Jason Wong
 *
 */
public final class RRFakeRequest implements Request {

    /**
     * The origin fake requests
     */
    private final transient List<FakeRequest> origins;

    /**
     * The current number to use.
     */
    private transient int current;

    /**
     * The request to use.
     */
    private Request req;

    /**
     * Create round robin requests.
     * @param requests The fake requests to use
     */
    public RRFakeRequest(FakeRequest... requests) {
        this.origins = Arrays.asList(requests);
        this.current = 0;
        this.req = this.pickRequest();
    }

    @Override
    public RequestURI uri() {
        return new RequestURI() {
            private final RequestURI origin = RRFakeRequest.this.req.uri();
            @Override
            public Request back() {
                return RRFakeRequest.this;
            }
            @Override
            public URI get() {
                return this.origin.get();
            }
            @Override
            public RequestURI set(URI uri) {
                return this;
            }
            @Override
            public RequestURI queryParam(String name, Object value) {
                return this;
            }
            @Override
            public RequestURI queryParams(Map<String, String> map) {
                return this;
            }
            @Override
            public RequestURI path(String segment) {
                return this;
            }
            @Override
            public RequestURI userInfo(String info) {
                return this;
            }
            @Override
            public RequestURI port(int num) {
                return this;
            }
        };
    }

    @Override
    public RequestBody body() {
        return this.req.body();
    }

    @Override
    public Request header(String name, Object value) {
        return this;
    }

    @Override
    public Request reset(String name) {
        return this;
    }

    @Override
    public Request method(String method) {
        return this;
    }

    @Override
    public Request timeout(int connect, int read) {
        return this;
    }

    @Override
    public Response fetch() throws IOException {
        final Response resp = this.req.fetch();
        this.req = this.pickRequest();
        return resp;
    }

    @Override
    public Response fetch(InputStream stream) throws IOException {
        final Response resp = this.req.fetch(stream);
        this.req = this.pickRequest();
        return resp;
    }

    @Override
    public <T extends Wire> Request through(Class<T> type, Object... args) {
        return this;
    }

    /**
     * Pick the request according to the 
     * @return The fake request to use
     */
    private Request pickRequest() {
        final int ind = this.current % this.origins.size();
        final Request req = this.origins.get(ind);
        this.current = ind + 1;
        return req;
    }
}
