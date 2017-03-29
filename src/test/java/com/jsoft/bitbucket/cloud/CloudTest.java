/**
 * 
 */
package com.jsoft.bitbucket.cloud;

import com.jsoft.bitbucket.OAuth;
import com.jsoft.bitbucket.Repo;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Tests for {@link Cloud}.
 * @author Jason Wong
 *
 */
public final class CloudTest {

    /**
     * {@link Cloud}
     * @throws Exception
     */
    @Test
    public void connectsToBitBucket() throws Exception {
        final String name = "testing-repo";
        final Repo repo = new Cloud(
            new OAuth(
                "cyDpS8pX5BV6HnU88X",
                "Q9v8t8JdEExgZjw6TEhvucU4zFmmNxmg"
            ).accessToken()
        ).repositories().get("jason-kc", name);
        MatcherAssert.assertThat(repo, Matchers.notNullValue());
        MatcherAssert.assertThat(
            repo.info().name(), Matchers.is(name)
        );
        MatcherAssert.assertThat(
            new Cloud(
                new OAuth(
                    "cyDpS8pX5BV6HnU88X",
                    "Q9v8t8JdEExgZjw6TEhvucU4zFmmNxmg"
                ).accessToken()
            ).repositories().list("jason-kc"),
            Matchers.<Repo>iterableWithSize(1)
        );
    }
}
