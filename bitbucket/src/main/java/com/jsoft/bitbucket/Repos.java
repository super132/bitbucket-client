/**
 * 
 */
package com.jsoft.bitbucket;

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
     */
    Repo get(final String owner, final String slug);

    /**
     * Return a list of repository of an account / team.
     * @param owner The owner account to search.
     * @return A list of repository under the owner.
     */
    List<Repo> list(final User owner);

    /**
     * Return a list of all public repositories.
     * @return All public repositories.
     */
    List<Repo> list();

    /**
     * Return a list of all repositories based on the role.
     * @return All public repositories.
     */
    List<Repo> list(final Repos.Role role);

    /**
     * Create a repository with specific settings
     * @param owner The owner username of the repository.
     * @param slug The repository slug.
     * @param settings The repository setting.
     * @return The newly created repository.
     */
    Repo create(final String owner, final String slug,
        final Repo.Settings settings);

    /**
     * Delete a repository
     * @param owner The owner username of the repository.
     * @param slug The repository slug.
     */
    void delete(final String owner, final String slug);

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
