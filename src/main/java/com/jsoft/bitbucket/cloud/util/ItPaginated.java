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

import com.google.common.collect.EvictingQueue;
import com.jcabi.http.Request;
import com.jcabi.http.response.BitBucketResponse;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Paginated Iterable that represents the paginated response from BitBucket
 * APIs.
 * @author Jason Wong
 * @see <a href="https://confluence.atlassian.com/bitbucket/version-2-423626329.html#Version2-Pagingthroughobjectcollections">Paging response</a>
 */
public final class ItPaginated<T> implements Iterable<T> {

    /**
     * HTTP request.
     */
    private final transient Request req;

    /**
     * Target class instance.
     */
    private final transient Class<? extends T> clazz;

    /**
     * The URI path to the paginated request.
     */
    private final transient Path path;

    /**
     * Ctor.
     * @param requset The HTTP request object.
     * @param path The URI path of the paging response.
     * @param clazz The target class of the iterable.
     */
    public ItPaginated(final Request request, final Path path,
        final Class<? extends T> clazz) {
        this.req = request;
        this.clazz = clazz;
        this.path = path;
    }

    @Override
    public Iterator<T> iterator() {
        return new PaginatedIterator(this.req, this.path);
    }
 
    /**
     * Obtain the values from the array.
     * @param values The array of the current page.
     * @return The list
     */
    private List<T> getValues(final JsonArray values) {
        final int length = values.size();
        List<T> result = new ArrayList<T>(length);
        try {
            for (int ind = 0; ind < length; ++ind) {
                result.add(
                    this.clazz.getConstructor(Request.class, JsonObject.class).newInstance(
                        this.req, values.getJsonObject(ind)
                    )
                );
            }
            return result;
        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * The current index.
     * Paginated Iterator.
     * @author Jason Wong
     */
    private final class PaginatedIterator implements Iterator<T> {

        /**
         * The buffer size.
         */
        private static final int BUFFER = 100;

        /**
         * The next REST end point.
         */
        private transient String path;

        /**
         * Internal storage of this iterator.
         */
        private transient EvictingQueue<T> storage;

        /**
         * The request to retrieve more results.
         */
        private final transient Request req;

        /**
         * Indicate if the last page is reached.
         */
        private boolean last;

        /**
         * Ctor.
         * @param request
         * @param next
         */
        PaginatedIterator(final Request request, final Path uri) {
            this.req = request;
            this.path = uri.toString();
            this.last = false;
            this.storage = EvictingQueue.create(PaginatedIterator.BUFFER);
        }

        @Override
        public boolean hasNext() {
            boolean result;
            if (!this.last) {
                if (this.storage.isEmpty()) {
                    try {
                        final JsonObject resp = this.get();
                        this.path = resp.getString("next", "");
                        this.last = "".equals(this.path);
                        this.storage.addAll(
                            ItPaginated.this.getValues(
                                resp.getJsonArray("values")
                            )
                        );
                        result = !this.storage.isEmpty();
                    } catch (final IOException ex) {
                        result = false;
                    }
                } else {
                    result = true;
                }
            } else if (!this.storage.isEmpty()) {
                result = true;
            } else {
                result = false;
            }
            return result;
        }

        @Override
        public T next() {
            final T result;
            if (!this.hasNext()) {
                throw new NoSuchElementException(
                    "No more elements available."
                );
            }
            result = this.storage.poll();
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                "Removal is not supported."
            );
        }

        /**
         * Obtain the response from the path.
         * @return The JSON response.
         * @throws IOException If error occurs.
         */
        private JsonObject get() throws IOException {
            return this.req.uri()
            .path(this.path)
            .back()
            .method(Request.GET)
            .fetch()
            .as(BitBucketResponse.class)
            .assertStatusOK()
            .as(JsonResponse.class)
            .json().readObject();
        }
    }
}
