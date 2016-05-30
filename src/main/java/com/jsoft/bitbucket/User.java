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

/**
 * Bitbucket user.
 * @author hcsrxo6
 *
 */
public interface User extends Resource {

    /**
     * The display name of the user.
     * @return The display name.
     */
    String displayName();

    /**
     * Query the list of followers of the user.
     * @return The followers
     * @throws IOException When I/O errors occur.
     */
    Iterable<User> followers() throws IOException;

    /**
     * Query the list of repositories of the user. To show private repositories
     * of the user, the authenticated OAuth token of the user must be used.
     * @return The repositories of the user.
     * @throws IOException When I/O errors occur.
     */
    Iterable<Repo> repositories() throws IOException;
}
