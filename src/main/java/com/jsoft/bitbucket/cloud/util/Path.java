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
package com.jsoft.bitbucket.cloud.util;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * To build REST request path. 
 * @author Jason Wong
 */
public final class Path {

    /**
     * The base path.
     */
    private final transient String base;

    /**
     * The request parts.
     */
    private final transient List<String> parts;

    /**
     * Ctor.
     * @param base The base path
     * @param parts The path parts.
     */
    public Path(final String base, final String... parts) {
        this.base = base;
        this.parts = Arrays.asList(parts);
    }

    @Override
    public String toString() {
        final Queue<String> queue = new PriorityQueue<String>(this.parts);
        final String result;
        if (queue.isEmpty()) {
            result = this.base;
        } else {
            result = Joiner.on("/").join(this.base, queue.poll(), queue.toArray(new Object[0]));
        }
        return result;
    }
}
