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

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.File;
import java.io.IOException;

/**
 * OAuth testing client. It always connects to Bitbucket OAuth server.
 * @author Jason Wong
 *
 */
public final class OAuth {

    /**
     * OAuth key.
     */
    private final transient String key;

    /**
     * OAuth secret.
     */
    private final transient String secret;

    /**
     * File data store.
     */
    private final transient FileDataStoreFactory store;

    /**
     * Ctor.
     * @param key The client key.
     * @param secret The client secret.
     * @throws IOException If file data store cannot be created.
     */
    public OAuth(final String key, final String secret) throws IOException {
        this.key = key;
        this.secret = secret;
        this.store = new FileDataStoreFactory(
            new File(System.getProperty("java.io.tmpdir"))
        );
    }

    /**
     * Obtain the oauth access token from bitbucket server.
     * @return The oAuth token.
     * @throws IOException If there is connection problem.
     */
    public String accessToken() throws IOException {
        final AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
            BearerToken.authorizationHeaderAccessMethod(),
            new NetHttpTransport(),
            new JacksonFactory(),
            new GenericUrl("https://bitbucket.org/site/oauth2/access_token"),
            new ClientParametersAuthentication(this.key, this.secret),
            this.key,
            "https://bitbucket.org/site/oauth2/authorize"
        ).setDataStoreFactory(this.store).build();
        final LocalServerReceiver receiver = new LocalServerReceiver.Builder()
            .setHost("localhost").setPort(9090).build();
        return new AuthorizationCodeInstalledApp(flow, receiver)
            .authorize("bitbucket-client-test").getAccessToken();
    }
}
