/**
 * 
 */
package com.jsoft.bitbucket;

import java.io.IOException;
import java.util.List;

/**
 * Bitbucket repositories.
 *
 * @author Jason Wong
 */
public interface Repos {

    /**
     * Return a repository.
     * @param owner The owner username of the repository.
     * @param slug The repository slug.
     * @return The repository.
     * @throws IOException If errors occur
     */
    Repo get(final String owner, final String slug) throws IOException;

    /**
     * Return a list of repository of an account / team.
     * @param owner The owner account to search.
     * @return A list of repository under the owner.
     * @throws IOException If errors occur
     */
    List<Repo> list(final User owner) throws IOException;

    /**
     * Return a list of all public repositories.
     * @return All public repositories.
     * @throws IOException If errors occur
     */
    List<Repo> list() throws IOException;

    /**
     * Return a list of all repositories based on the role.
     * @return All public repositories.
     * @throws IOException If errors occur
     */
    List<Repo> list(final Repos.Role role) throws IOException;

    /**
     * Create a repository with specific settings
     * @param owner The owner username of the repository.
     * @param slug The repository slug.
     * @param settings The repository setting.
     * @return The newly created repository.
     * @throws IOException If errors occur
     */
    Repo create(final String owner, final String slug,
        final Repo.Settings settings) throws IOException;

    /**
     * Delete a repository
     * @param owner The owner username of the repository.
     * @param slug The repository slug.
     * @throws IOException If errors occur
     */
    void delete(final String owner, final String slug) throws IOException;

    /**
     * The role of listing repositories.
     * @author Jason Wong
     */
    enum Role {
        /**
         * Owner role.
         */
        OWNER("owner"),
        /**
         * Admin role. Returns repositories to which the user has explicit
         * administrator access.
         */
        ADMIN("admin"),
        /**
         * Contributor role. Returns repositories to which the user has
         * explicit write access.
         */
        CONTRIBUTOR("contributor"),
        /**
         * Member role. Returns repositories to which the user has explicit
         * read access.
         */
        MEMBER("member");

        private String value;

        private Role(final String value) {
            this.value = value;
        }

        /**
         * Obtain the value of the role.
         * @return The value.
         */
        public String value() {
            return this.value;
        }
    }
}
