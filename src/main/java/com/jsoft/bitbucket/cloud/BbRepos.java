/**
 * 
 */
package com.jsoft.bitbucket.cloud;

import com.jcabi.http.Request;
import com.jcabi.http.response.BitBucketResponse;
import com.jcabi.http.response.JsonResponse;
import com.jsoft.bitbucket.Repo;
import com.jsoft.bitbucket.Repo.ForkPolicy;
import com.jsoft.bitbucket.Repo.Settings;
import com.jsoft.bitbucket.Repos;
import com.jsoft.bitbucket.cloud.util.ItPaginated;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.NoSuchElementException;
import javax.json.JsonObject;

/**
 * Bitbucket Cloud Repositories API implementation.
 * @author Jason Wong
 *
 */
public final class BbRepos implements Repos {

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
        try {
            final JsonObject resp = this.req.uri().path(
                String.format("/2.0/repositories/%s/%s", owner, slug)
            ).back().method(Request.GET)
            .fetch()
            .as(BitBucketResponse.class)
            .assertStatus(HttpURLConnection.HTTP_OK)
            .as(JsonResponse.class)
            .json().readObject();
            return new BbRepo(
                this.req,
                resp.getString("uuid"),
                resp.getString("created_on"),
                new Repo.Settings(
                    resp.getString("scm"),
                    resp.getString("name"),
                    !resp.getBoolean("is_private"),
                    resp.getString("description"),
                    ForkPolicy.fromValue(resp.getString("fork_policy")),
                    resp.getString("language"),
                    resp.getBoolean("has_issues"),
                    resp.getBoolean("has_wiki")
                )
            );
        } catch (final AssertionError ex) {
            throw new NoSuchElementException(ex.getMessage());
        }
    }

    @Override
    public Iterable<Repo> list(final String owner) throws IOException {
        final JsonObject resp = this.req.uri().path(
            String.format("/2.0/repositories/%s", owner)
        ).back().method(Request.GET)
        .fetch()
        .as(BitBucketResponse.class)
        .assertStatus(HttpURLConnection.HTTP_OK)
        .as(JsonResponse.class)
        .json().readObject();
        return new ItPaginated<Repo>(this.req, resp, BbRepo.class);
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.Repos#list()
     */
    @Override
    public List<Repo> list() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.Repos#list(com.jsoft.bitbucket.Repos.Role)
     */
    @Override
    public List<Repo> list(Role role) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.Repos#create(java.lang.String, java.lang.String, com.jsoft.bitbucket.Repo.Settings)
     */
    @Override
    public Repo create(String owner, String slug, Settings settings) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.Repos#delete(java.lang.String, java.lang.String)
     */
    @Override
    public void delete(String owner, String slug) {
        // TODO Auto-generated method stub

    }

}
