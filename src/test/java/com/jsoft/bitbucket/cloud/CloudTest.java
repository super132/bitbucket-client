/**
 * 
 */
package com.jsoft.bitbucket.cloud;

import com.jsoft.bitbucket.OAuth;
import org.junit.Test;

/**
 * Tests for {@link Cloud}.
 * @author Jason Wong
 *
 */
public final class CloudTest {

    @Test
    public void connectsToBitBucket() throws Exception {
        new Cloud(new OAuth("", "").accessToken())
            .repositories().get("aia_qms", "aia-qms");
        
    }
}
