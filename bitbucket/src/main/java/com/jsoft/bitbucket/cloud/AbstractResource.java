/**
 * 
 */
package com.jsoft.bitbucket.cloud;

import com.jcabi.log.Logger;
import com.jsoft.bitbucket.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The abstract resource.
 * @author Jason Wong
 *
 */
public abstract class AbstractResource implements Resource {

    /**
     * Date format string.
     */
    private static final String DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";

    /**
     * The unique ID of the resource.
     */
    private final transient String uid;

    /**
     * The date when the resource is created.
     */
    private final transient Date created;

    /**
     * Ctor.
     * @param id The ID of the resource
     * @param date The creation date string of the resource in
     *  yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX format.
     */
    public AbstractResource(final String id, final String date) {
        this.uid = id;
        this.created = AbstractResource.parseDate(date);
    }

    @Override
    public String id() {
        return this.uid;
    }

    @Override
    public Date createdOn() {
        return this.created;
    }

    /**
     * Parse the date from the given date string.
     * @param date The date string.
     * @return The corresponding date object.
     */
    private static Date parseDate(final String date) {
        Date result = new Date(0L);
        try {
            final DateFormat format =
                new SimpleDateFormat(AbstractResource.DATE_FMT);
            result = format.parse(date);
        } catch (final ParseException ex) {
            Logger.error(
                AbstractResource.class, "Unable to parse date: %exception", ex
            );
        }
        return result;
    }
}
