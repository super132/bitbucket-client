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
package com.jsoft.bitbucket;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Bitbucket repositories.
 *
 * @author Jason Wong
 */
public interface Repos {

    /**
     * Return a repository. It throws {@link NoSuchElementException} if no
     * repository is found.
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
    Iterable<Repo> list(final String owner) throws IOException;

    /**
     * Return a list of all public repositories.
     * @return All public repositories.
     * @throws IOException If errors occur
     */
    Iterable<Repo> list() throws IOException;

    /**
     * Return a list of all repositories based on the role.
     * @return All public repositories.
     * @throws IOException If errors occur
     */
    Iterable<Repo> list(final Repos.Role role) throws IOException;

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
