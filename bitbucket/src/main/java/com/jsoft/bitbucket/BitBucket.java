/**
 * 
 */
package com.jsoft.bitbucket;

/**
 * BitBucket client.
 *
 * @author Jason Wong
 */
public interface BitBucket {

    /**
     * Repositories in Bitbucket.
     * @return Repositories object.
     */
    Repos repositories();

    /**
     * Teams in Bitbucket.
     * @return Teams object.
     */
    Teams teams();

    /**
     * Users in Bitbucket.
     * @return Users object.
     */
    Users users();

    /**
     * Snippets in Bitbucket.
     * @return Snippets object.
     */
    Snippets snippets();
}
