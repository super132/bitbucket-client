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

/**
 * BitBucket pull request.
 * @author Jason Wong
 *
 */
public interface PullRequest extends Resource {

    /**
     * Update the current pull request with details.
     * @param details The details to update
     * @return The updated pull request
     */
    PullRequest update(final PullRequests.RequestDetails details);

    /**
     * List all commits associated in this pull request.
     * @return The list of commits.
     */
    Iterable<Commit> commits();

    /**
     * Approve this pull request for the current authenticated user.
     */
    void approve();

    /**
     * Revoke approval of this pull request for the current authenticated user.
     */
    void revoke();

    /**
     * Accept and merge a pull request without merge commit message.
     * @return The pull request that merged.
     */
    PullRequest accept();

    /**
     * Accept and merge a pull request with merge commit message.
     * @param message Merge commit message
     * @param close To close the source branch after this pull request is merged.
     * @return The pull request that merged.
     */
    PullRequest accept(final String message, final boolean close);

    /**
     * Declining a pull request.
     * @return Declined pull request
     */
    PullRequest decline();

    /**
     * Declining a pull request with reason specified.
     * @param reason Reason of decline.
     * @return Declined pull request
     */
    PullRequest decline(final String reason);

    /**
     * Retrieve the comments made in the pull request.
     * @return The comments.
     */
    Iterable<Comment> listComments();

    /**
     * Retrieve the specific comment made in the pull request.
     * @param id The comment id.
     * @return The comment.
     */
    Comment comment(final String id);

    /**
     * Post a new comment to the pull request.
     * @param content The comment to post.
     * @return Newly created comment.
     */
    Comment post(final String content);

    /**
     * Update a comment specified.
     * @param id The comment id.
     * @param content The content to update.
     * @return Updated comment.
     */
    Comment updateComment(final String id, final String content);

    /**
     * Delete a comment specified in the pull request.
     * @param id The comment id.
     * @return The deleted comment.
     */
    Comment deleteComment(final String id);
}
