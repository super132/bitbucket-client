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
import com.jsoft.bitbucket.Repo;
import com.jsoft.bitbucket.Repo.Settings;
import com.jsoft.bitbucket.Repos;
import com.jsoft.bitbucket.cloud.util.ItPaginated;
import com.jsoft.bitbucket.cloud.util.Path;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.json.JsonObject;

/**
 * Bitbucket Cloud Repositories API implementation.
 * @author Jason Wong
 *
 */
public final class BbRepos implements Repos {

    /**
     * The base REST API URI.
     */
    private static final String API_BASE = "/2.0/repositories";

    /**
     * HTTP request to talk to BitBucket cloud.
     */
    private final transient Request req;

    /**
     * Ctor.
     * @param request HTTP request.
     */
    public BbRepos(final Request request) {
        this.req = request;
    }

    @Override
    public Repo get(String owner, String slug) throws IOException {
        final JsonObject resp = this.req.uri().path(
            new Path(BbRepos.API_BASE, owner, slug).toString()
        ).back().method(Request.GET)
        .fetch()
        .as(BitBucketResponse.class)
        .assertStatusOK()
        .as(JsonResponse.class)
        .json().readObject();
        return new BbRepo(this.req, resp);
    }

    @Override
    public Iterable<Repo> list(final String owner) throws IOException {
        return new ItPaginated<Repo>(
            this.req, new Path(BbRepos.API_BASE, owner), BbRepo.class
        );
    }

    @Override
    public Iterable<Repo> list() throws IOException {
        return new ItPaginated<Repo>(
            this.req, new Path(BbRepos.API_BASE), BbRepo.class
        );
    }

    @Override
    public Iterable<Repo> list(final Role role) throws IOException {
        return new ItPaginated<Repo>(
            this.req.uri().queryParam("role", role.value()).back(),
            new Path(BbRepos.API_BASE),
            BbRepo.class
        );
    }

    @Override
    public Repo create(final String owner, final String slug,
        final Settings settings) throws IOException {
        return new BbRepo(
            this.req,
            this.req.uri().path(
                new Path(BbRepos.API_BASE, owner, slug).toString()
            )
            .back()
            .method(Request.POST)
            .body().set(settings.toJson())
            .back()
            .fetch()
            .as(BitBucketResponse.class)
            .assertStatus(HttpURLConnection.HTTP_OK)
            .as(JsonResponse.class)
            .json().readObject()
        );
    }

    @Override
    public void delete(final String owner, final String slug)
        throws IOException {
        this.req.uri().path(
            new Path(BbRepos.API_BASE, owner, slug).toString()
        )
        .back()
        .method(Request.DELETE)
        .fetch()
        .as(BitBucketResponse.class)
        .assertStatus(HttpURLConnection.HTTP_NO_CONTENT);
    }

}
