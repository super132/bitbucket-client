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
package com.jsoft.bitbucket.cloud;

import com.jcabi.http.Request;
import com.jcabi.http.response.BitBucketResponse;
import com.jcabi.http.response.JsonResponse;
import com.jsoft.bitbucket.PullRequest;
import com.jsoft.bitbucket.PullRequests;
import com.jsoft.bitbucket.Repo;
import com.jsoft.bitbucket.cloud.util.ItPaginated;
import com.jsoft.bitbucket.cloud.util.Path;
import java.io.IOException;
import javax.json.JsonObject;

/**
 * BitBucket Cloud Pull Requests.
 * @author Jason Wong
 *
 */
public final class BbPullRequests implements PullRequests {

    /**
     * The base REST API URI.
     */
    private static final String API_BASE = "/2.0/respositories";

    /**
     * HTTP request to talk to BitBucket cloud.
     */
    private final transient Request req;

    /**
     * The repository owner username.
     */
    private final transient String owner;

    /**
     * The repository slug.
     */
    private final transient String slug;

    /**
     * Ctor.
     * @param request The HTTP request.
     * @param owner The owner of the repository.
     * @param slug The repo slug.
     */
    public BbPullRequests(final Request request, final String owner,
        final String slug) {
        this.req = request;
        this.owner = owner;
        this.slug = slug;
    }

    @Override
    public Iterable<PullRequest> list() throws IOException {
        final JsonObject resp = this.req.uri().path(
            new Path(
                BbPullRequests.API_BASE, this.owner, this.slug, "pullrequests"
            ).toString()
        ).queryParam("state", PullRequests.State.OPEN)
        .back()
        .method(Request.GET)
        .fetch()
        .as(BitBucketResponse.class)
        .assertStatusOK()
        .as(JsonResponse.class)
        .json().readObject();
        return null;
    }

    @Override
    public Iterable<PullRequest> list(State state) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.PullRequests#get(java.lang.String)
     */
    @Override
    public PullRequest get(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.PullRequests#create(com.jsoft.bitbucket.PullRequests.RequestDetails)
     */
    @Override
    public PullRequest create(RequestDetails details) {
        // TODO Auto-generated method stub
        return null;
    }

}
