/**
 * 
 */
package com.jcabi.http.response;

import com.jcabi.http.Response;
import javax.json.JsonObject;

/**
 * BitBucket REST API response.
 * @author Jason Wong
 *
 */
public final class BitBucketResponse extends AbstractResponse {

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
        final JsonObject resp = this.as(JsonResponse.class).json().readObject();
        if (this.status() != status) {
            String message = String.format(
                "HTTP response status is not equal to %d: %d.",
                status, this.status()
            );
            if (!resp.isNull("error")) {
                message = String.format(
                    "%s Message: %s",
                    message, resp.getJsonObject("error").getString("message")
                );
            }
            throw new AssertionError(message);
        }
        return this;
    }
}
