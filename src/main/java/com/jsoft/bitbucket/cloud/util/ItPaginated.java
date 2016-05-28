/**
 * 
 */
package com.jsoft.bitbucket.cloud.util;

import com.jcabi.http.Request;
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.http.response.BitBucketResponse;
import com.jcabi.http.response.JsonResponse;
import com.jsoft.bitbucket.cloud.Cloud;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

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
        this.total = data.getInt("size");
        this.size = data.getInt("pagelen");
        this.next = data.getString("next", "");
        this.req = request;
        this.clazz = clazz;
        this.storage = new LinkedList<T>(
            this.getValues(data.getJsonArray("values"))
        );
    }

    @Override
    public Iterator<T> iterator() {
        return new PaginatedIterator(this.next, this.storage);
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
        } catch (final SecurityException see) {
            throw new IllegalStateException(see);
        } catch (final ReflectiveOperationException roe) {
            throw new IllegalStateException(roe);
        }
    }

    /**
     * Paginated Iterator.
     * @author Jason Wong
     */
    private class PaginatedIterator implements Iterator<T> {

        /**
         * The current index.
         */
        private transient int current = 0;

        /**
         * The next REST end point.
         */
        private transient String next;

        /**
         * Internal storage of this iterator.
         */
        private final transient List<T> storage;

        /**
         * Ctor.
         * @param next
         * @param init
         */
        public PaginatedIterator(final String next, final List<T> init) {
            this.next = next;
            this.storage = new LinkedList<T>(init);
        }

        @Override
        public boolean hasNext() {
            final boolean result;
            if (this.current < this.storage.size()) {
                result = true;
            } else {
                result = false;
            }
            return result;
        }
        @Override
        public T next() {
            final T result;
            if (this.hasNext()) {
                result = ItPaginated.this.storage.get(this.current);
                ++this.current;
            } else {
                if (this.current >= ItPaginated.this.total) {
                    throw new NoSuchElementException(
                        "No more elements available."
                    );
                }
                try {
                    final JsonObject resp = new ApacheRequest(this.next)
                        .header(HttpHeaders.USER_AGENT, Cloud.USER_AGENT)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .method(Request.GET)
                        .fetch()
                        .as(BitBucketResponse.class)
                        .assertStatus(HttpURLConnection.HTTP_OK)
                        .as(JsonResponse.class)
                        .json().readObject();
                    this.storage.addAll(
                        ItPaginated.this.getValues(
                            resp.getJsonArray("values")
                        )
                    );
                    result = this.storage.get(this.current);
                    ++this.current;
                } catch (final IOException ex) {
                    throw new NoSuchElementException(ex.getMessage());
                }
            }
            return result;
        }
        
    }
}
