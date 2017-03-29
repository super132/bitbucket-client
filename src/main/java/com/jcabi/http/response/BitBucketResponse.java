/**
 * Copyright (c) 2016 JSoft.com
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The above copyright notice and this
 * permission notice shall be included in all copies or substantial
 * portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT
 * WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jcabi.http.response;

import com.jcabi.http.Response;
import java.net.HttpURLConnection;
import java.util.NoSuchElementException;
import javax.json.JsonObject;

/**
 * BitBucket REST API response.
 * @author Jason Wong (super132j@yahoo.com)
 * @version $Id$
 * @since 0.1
 */
public final class BitBucketResponse extends AbstractResponse {

    /**
     * Error message pattern.
     */
    private static final String ERR_MSG = "%s Message: %s";

    /**
     * Ctor.
     * @param resp The response.
     */
    public BitBucketResponse(final Response resp) {
        super(resp);
    }

    /**
     * Assert the HTTP response status is correct and get the error message
     * if the response is unexpected.
     * @param status The expected response.
     * @return The same object
     */
    public BitBucketResponse assertStatus(final int status) {
        if (this.status() != status) {
            final JsonObject resp = this.as(JsonResponse.class).json()
                .readObject();
            final String message = BitBucketResponse.obtainErrorMsg(
                resp,
                String.format(
                    "HTTP response status is not equal to %d: %d.",
                    status, this.status()
                )
            );
            throw new AssertionError(message);
        }
        return this;
    }

    /**
     * Assert the HTTP response status is always 200 and if the status is 404,
     * {@link NoSuchElementException} will be thrown instead.
     * @return The same object
     */
    public BitBucketResponse assertStatusOK() {
        if (this.status() != HttpURLConnection.HTTP_OK) {
            final JsonObject resp = this.as(JsonResponse.class).json()
                .readObject();
            final String message = BitBucketResponse.obtainErrorMsg(
                resp,
                String.format(
                    "HTTP response status is not 200: %d.",
                    this.status()
                )
            );
            if (this.status() == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new NoSuchElementException(message);
            } else {
                throw new AssertionError(message);
            }
        }
        return this;
    }

    /**
     * Obtains the error message from the response.
     * @param message The default message.
     * @param resp The JSON response.
     * @return The error message from the JSON response.
     */
    private static String obtainErrorMsg(final JsonObject resp,
        final String message) {
        String result = message;
        final String error = "error";
        if (!resp.isNull(error)) {
            result = String.format(
                BitBucketResponse.ERR_MSG,
                message, resp.getJsonObject(error).getString("message")
            );
        }
        return result;
    }
}
