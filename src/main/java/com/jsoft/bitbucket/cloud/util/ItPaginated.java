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
import com.jcabi.http.response.BitBucketResponse;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
     * The total number of results expected.
     */
    private final transient int total;

    /**
     * The page size.
     */
    private final transient int size;

    /**
     * The endpoint to call for next page records.
     */
    private transient String next;

    /**
     * HTTP request.
     */
    private final transient Request req;

    /**
     * Target class instance.
     */
    private final transient Class<? extends T> clazz;

    /**
     * Internal storage.
     */
    private final transient List<T> storage;

    /**
     * Ctor.
     * @param data The JSON data of the paging response.
     * @param clazz The target class of the iterable.
     */
    public ItPaginated(final Request request, final JsonObject data,
        final Class<? extends T> clazz) {
        this.total = data.getInt("size", 0);
        this.size = data.getInt("pagelen", 0);
        this.next = data.getString("next", "");
        this.req = request;
        this.clazz = clazz;
        this.storage = new LinkedList<T>(
            this.getValues(data.getJsonArray("values"))
        );
    }

    @Override
    public Iterator<T> iterator() {
        return new PaginatedIterator(
            this.req, this.next, this.storage, "".equals(this.next)
        );
    }
 
    /**
     * Obtain the values from the array.
     * @param values The array of the current page.
     * @return The list
     */
    private List<T> getValues(final JsonArray values) {
        final int length = Math.min(this.size, values.size());
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
         */
        private transient int current;

        /**
         * The next REST end point.
         */
        private transient String next;

        /**
         * Internal storage of this iterator.
         */
        private final transient List<T> storage;

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
         * @param init
         * @param one
         */
        PaginatedIterator(final Request request, final String next,
            final List<T> init, final boolean one) {
            this.req = request;
            this.current = 0;
            this.next = next;
            this.storage = new LinkedList<T>(init);
            this.last = one;
        }

        @Override
        public boolean hasNext() {
            boolean result;
            if (this.current < this.storage.size()) {
                result = true;
            } else {
                if (!this.last) {
                    try {
                        final JsonObject resp = this.req.uri()
                            .set(URI.create(this.next))
                            .back()
                            .method(Request.GET)
                            .fetch()
                            .as(BitBucketResponse.class)
                            .assertStatus(HttpURLConnection.HTTP_OK)
                            .as(JsonResponse.class)
                            .json().readObject();
                        this.last = resp.getInt("page") ==
                            ItPaginated.this.total;
                        this.storage.clear();
                        this.storage.addAll(
                            ItPaginated.this.getValues(
                                resp.getJsonArray("values")
                            )
                        );
                        this.current = 0;
                        this.next = resp.getString("next");
                        result = true;
                    } catch (final IOException ex) {
                        result = false;
                    }
                } else {
                    result = false;
                }
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
            result = this.storage.get(this.current);
            ++this.current;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                "Removal is not supported."
            );
        }

    }
}
