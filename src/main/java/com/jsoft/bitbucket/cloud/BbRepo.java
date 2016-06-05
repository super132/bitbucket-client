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

import com.google.common.base.Optional;
import com.jcabi.http.Request;
import com.jcabi.http.response.BitBucketResponse;
import com.jcabi.http.response.JsonResponse;
import com.jsoft.bitbucket.Branches;
import com.jsoft.bitbucket.Commits;
import com.jsoft.bitbucket.PullRequests;
import com.jsoft.bitbucket.Repo;
import com.jsoft.bitbucket.User;
import com.jsoft.bitbucket.cloud.util.ItPaginated;
import com.jsoft.bitbucket.cloud.util.Path;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Bitbucket Cloud Repository implementation. The REST API used is Bitbucket
 * cloud v2.
 * @author hcsrxo6
 * @see <a href="https://confluence.atlassian.com/bitbucket/repository-resource-423626331.html">Repository API</a>
 *
 */
public final class BbRepo extends AbstractResource implements Repo {

    /**
     * The base REST API URI.
     */
    private static final String API_BASE = "/2.0/repositories";

    /**
     * The HTTP request object.
     */
    private final transient Request req;

    /**
     * The username of the owner.
     */
    private final transient String owner;

    /**
     * Repo details.
     */
    private final transient Repo.Settings details;

    /**
     * Create default repo with public access and no forks.
     * @param request The HTTP request
     */
    public BbRepo(final Request request) {
        this(
            request,
            "",
            "",
            "",
            new Repo.Settings(true, Repo.ForkPolicy.NO_FORKS)
        );
    }

    /**
     * Create repository.
     * @param request The HTTP request
     * @param id The ID.
     * @param creation The creation date string.
     * @param owner The username of the owner of this repo
     * @param info The details of the repo.
     */
    public BbRepo(final Request request, final String id,
        final String creation, final String owner, final Repo.Settings info) {
        super(id, creation);
        this.req = request;
        this.details = info;
        this.owner = owner;
    }

    /**
     * Ctor.
     * @param request HTTP request.
     * @param value The JSON object.
     */
    public BbRepo(final Request request, final JsonObject value) {
        this(
            request,
            value.getString("uuid"),
            value.getString("created_on"),
            Optional.fromNullable(value.getJsonObject("owner"))
                .or(Json.createObjectBuilder().build())
                .getString("username", ""),
            new Repo.Settings(
                value.getString("scm"),
                value.getString("name"),
                !value.getBoolean("is_private"),
                value.getString("description"),
                ForkPolicy.fromValue(value.getString("fork_policy")),
                value.getString("language"),
                value.getBoolean("has_issues"),
                value.getBoolean("has_wiki")
            )
        );
    }

    @Override
    public Iterable<User> watchers() throws IOException {
        final JsonObject resp = this.req.uri().path(
            new Path(
                BbRepo.API_BASE, this.owner, this.details.name(), "watchers"
            ).toString()
        ).back().method(Request.GET)
        .fetch()
        .as(BitBucketResponse.class)
        .assertStatusOK()
        .as(JsonResponse.class)
        .json().readObject();
        return new ItPaginated<User>(this.req, resp, BbUser.class);
    }

    @Override
    public Iterable<Repo> forks() throws IOException {
        final JsonObject resp = this.req.uri().path(
            new Path(
                BbRepo.API_BASE, this.owner, this.details.name(), "forks"
            ).toString()
        ).back().method(Request.GET)
        .fetch()
        .as(BitBucketResponse.class)
        .assertStatusOK()
        .as(JsonResponse.class)
        .json().readObject();
        return new ItPaginated<Repo>(this.req, resp, BbRepo.class);
    }

    @Override
    public Settings info() {
        return this.details;
    }

    @Override
    public PullRequests requests() throws IOException {
        return new BbPullRequests(this.req, this.owner, this.info().name());
    }

    @Override
    public Branches branches() throws IOException {
        return new BbBranches(this.req);
    }

    @Override
    public Commits commits() {
        return new BbCommits(this.req);
    }

}
