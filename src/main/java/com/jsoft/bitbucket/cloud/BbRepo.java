/**
 * 
 */
package com.jsoft.bitbucket.cloud;

import com.jcabi.http.Request;
import com.jsoft.bitbucket.Branches;
import com.jsoft.bitbucket.Commits;
import com.jsoft.bitbucket.PullRequests;
import com.jsoft.bitbucket.Repo;
import com.jsoft.bitbucket.User;
import com.jsoft.bitbucket.Repo.ForkPolicy;
import java.io.IOException;
import java.util.List;
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
     * The HTTP request object.
     */
    private final transient Request req;

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
            request, "", "", new Repo.Settings(true, Repo.ForkPolicy.NO_FORKS)
        );
    }

    /**
     * Create repository.
     * @param request The HTTP request
     * @param id The ID.
     * @param creation The creation date string.
     * @param info The details of the repo.
     */
    public BbRepo(final Request request, final String id,
        final String creation, final Repo.Settings info) {
        super(id, creation);
        this.req = request;
        this.details = info;
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
    public List<User> watchers() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Repo> forks() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.Repo#info()
     */
    @Override
    public Settings info() {
        return this.details;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.Repo#requests()
     */
    @Override
    public PullRequests requests() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.jsoft.bitbucket.Repo#branches()
     */
    @Override
    public Branches branches() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Commits commits() {
        // TODO Auto-generated method stub
        return null;
    }

}
