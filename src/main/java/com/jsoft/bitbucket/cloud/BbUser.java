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
import com.jsoft.bitbucket.Repo;
import com.jsoft.bitbucket.User;
import java.io.IOException;
import javax.json.JsonObject;

/**
 * BitBucket user.
 * @author hcsrxo6
 *
 */
public final class BbUser extends AbstractResource implements User {

    /**
     * The display name of the user.
     */
    private final transient String name;

    /**
     * Ctor.
     * @param req The HTTP request
     * @param The JSON object from REST API.
     */
    public BbUser(final Request req, final JsonObject value) {
        this(
            value.getString("uuid"),
            value.getString("created_on"),
            value.getString("display_name")
        );
    }

    /**
     * Ctor.
     * @param id The user ID.
     * @param creation The creation date string
     * @param name The user display name
     */
    public BbUser(final String id, final String creation, final String name) {
        super(id, creation);
        this.name = name;
    }

    @Override
    public String displayName() {
        return this.name;
    }

    @Override
    public Iterable<User> followers() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Repo> repositories() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    
}
