/**
 * 
 */
package com.jsoft.bitbucket.cloud;

import com.jcabi.http.Request;
import com.jcabi.http.request.ApacheRequest;
import com.jsoft.bitbucket.BitBucket;
import com.jsoft.bitbucket.Repos;
import com.jsoft.bitbucket.Snippets;
import com.jsoft.bitbucket.Teams;
import com.jsoft.bitbucket.Users;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * The Bitbucket Cloud API 2 client.
 * @author Jason Wong
 *
 */
public final class Cloud implements BitBucket {

    /**
     * User agent.
     */
    private static final String USER_AGENT = "bitbucket-client";

    /**
     * Default request to start with.
     */
    private static final Request REQUEST =
        new ApacheRequest("https://api.bitbucket.org")
            .header(HttpHeaders.USER_AGENT, Cloud.USER_AGENT)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

    /**
     * Http request object.
     */
    private final transient Request req;

    /**
     * Default client creation.
     */
    public Cloud() {
        this(Cloud.REQUEST);
    }

    /**
     * 
     * @param token
     */
    public Cloud(final String token) {
        this(
            Cloud.REQUEST.header(
                HttpHeaders.AUTHORIZATION,
                String.format("token %s", token)
            )
        );
    }

    /**
     * Create client with {@link Request} specified.
     */
    public Cloud(final Request request) {
        this.req = request;
    }

    @Override
    public Repos repositories() {
        return new BbRepos(this.req);
    }

    @Override
    public Teams teams() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Users users() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Snippets snippets() {
        // TODO Auto-generated method stub
        return null;
    }

}
