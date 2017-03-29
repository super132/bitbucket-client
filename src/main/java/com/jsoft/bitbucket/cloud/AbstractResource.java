/**
 * Copyright (c) 2016 JSoft.com
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The above copyright notice and this
 * permission notice shall be included in all copies or substantial
 * portions of the Software.
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

import com.jcabi.log.Logger;
import com.jsoft.bitbucket.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The abstract resource.
 * @author Jason Wong (super132j@yahoo.com)
 * @version $Id$
 * @since 0.1
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
    public final String id() {
        return this.uid;
    }

    @Override
    public final Date createdOn() {
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
