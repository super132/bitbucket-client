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
import com.jcabi.http.request.FakeRequest;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for {@link ItPaginated}.
 * @author Jason Wong
 *
 */
public final class ItPaginatedTest {

    /**
     * {@link ItPaginated} can iterate multiple page records.
     */
    @Test
    public void iteratesMultiplePageRecords() {
        final Iterator<SimpleObject> itr = new ItPaginated<SimpleObject>(
            new FakeRequest(
                HttpURLConnection.HTTP_OK,
                "OK",
                Collections.<Map.Entry<String, String>>emptyList(),
                // @checkstyle LineLength (1 line)
                "{\"size\":2,\"page\":2,\"pagelen\":1,\"next\":\"http://abc.com/more\",\"values\":[{\"string\":\"Testing 2\"}]}".getBytes()
            ),
            ItPaginatedTest.jsonFromString(
                // @checkstyle LineLength (1 line)
                "{\"size\":2,\"page\":1,\"pagelen\":1,\"next\":\"http://abc.com/more\",\"values\":[{\"string\":\"Testing\"}]}"
            ),
            SimpleObject.class
        ).iterator();
        MatcherAssert.assertThat(itr.hasNext(), Matchers.is(true));
        MatcherAssert.assertThat(itr.next().value(), Matchers.is("Testing"));
        MatcherAssert.assertThat(itr.hasNext(), Matchers.is(true));
        MatcherAssert.assertThat(itr.next().value(), Matchers.is("Testing 2"));
        MatcherAssert.assertThat(itr.hasNext(), Matchers.is(false));
    }

    /**
     * {@link ItPaginated} can iterate single page record.
     */
    @Test
    public void iteratesSinglePageRecord() {
        final Iterator<SimpleObject> itr = new ItPaginated<SimpleObject>(
            new FakeRequest(
                HttpURLConnection.HTTP_OK,
                "OK",
                Collections.<Map.Entry<String, String>>emptyList(),
                // @checkstyle LineLength (1 line)
                "{\"size\":2,\"page\":2,\"pagelen\":1,\"next\":\"\",\"values\":[{\"string\":\"Testing 2\"}]}".getBytes()
            ),
            ItPaginatedTest.jsonFromString(
                // @checkstyle LineLength (1 line)
                "{\"size\":1,\"page\":1,\"pagelen\":1,\"next\":\"\",\"values\":[{\"string\":\"Testing\"}]}"
            ),
            SimpleObject.class
        ).iterator();
        MatcherAssert.assertThat(itr.hasNext(), Matchers.is(true));
        MatcherAssert.assertThat(itr.next().value(), Matchers.is("Testing"));
        MatcherAssert.assertThat(itr.hasNext(), Matchers.is(false));
    }

    /**
     * Create JSON object from JSON string.
     * @param json
     * @return
     */
    private static JsonObject jsonFromString(final String json) {
        return Json.createReader(new StringReader(json)).readObject();
    }

    /**
     * Simple object for testing.
     * @author Jason Wong
     */
    private static final class SimpleObject {

        /**
         * The actual value. 
         */
        private final transient String value;

        /**
         * Ctor.
         * @param req The HTTP request
         * @param json The JSON object
         */
        public SimpleObject(final Request req, final JsonObject json) {
            this.value = json.getString("string");
        }

        /**
         * Obtain the value.
         * @return The value.
         */
        public String value() {
            return this.value;
        }
    }
}
