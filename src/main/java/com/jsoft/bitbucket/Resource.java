/**
 * 
 */
package com.jsoft.bitbucket;

import java.util.Date;

/**
 * BitBucket Resource.
 * @author Jason Wong
 *
 */
public interface Resource {

    /**
     * Retrieve the unique identifier of the resource.
     * @return The ID.
     */
    String id();

    /**
     * The date that the object created on.
     * @return
     */
    Date createdOn();
}
