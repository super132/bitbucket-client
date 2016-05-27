/**
 * 
 */
package com.jsoft.bitbucket;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Pull Requests in BitBucket.
 * @author Jason Wong
 *
 */
public interface PullRequests {

    /**
     * Retrieves all OPEN pull requests in the repo.
     * @return All OPEN pull requests.
     */
    Iterable<PullRequest> list();

    /**
     * Retrieves all pull requests in the repo according to the state.
     * @param state The state of pull requests to query.
     * @return All OPEN pull requests.
     */
    Iterable<PullRequest> list(final State state);

    /**
     * Get a specific pull request in the repo by ID.
     * @param id Pull request ID
     * @return The pull request.
     */
    PullRequest get(final String id);

    /**
     * Create a pull request with details.
     * @param details Create pull request details
     * @return Newly created pull request
     */
    PullRequest create(final RequestDetails details);

    /**
     * The pull request creation details.
     * @author Jason Wong
     *
     */
    final class RequestDetails {
        /**
         * The title of the pull request
         */
        private final transient String title;
        /**
         * The description of the pull request
         */
        private final transient String desc;
        /**
         * The source branch name.
         */
        private final transient String from;
        /**
         * The destination branch name.
         */
        private final transient String dest;
        /**
         * Indicate if the source branch should be closed after merging.
         */
        private final transient boolean close;
        /**
         * A list of reviwers.
         */
        private final transient List<String> reviewers;

        /**
         * Minimal request details with all mandatory parameters.
         * @param title The title of the pull request
         * @param source The source branch name
         * @param target The destination branch name
         */
        public RequestDetails(final String title, final String source,
            final String target) {
            this(
                title,
                "",
                source,
                target,
                false,
                Collections.<String>emptyList()
            );
        }

        /**
         * Request details with all parameters.
         * @param title The title of the pull request
         * @param desc The description of the pull request
         * @param source The source branch name
         * @param target The destination branch name
         * @param close Indicate if the source branch should be closed after
         *  merging.
         * @param reviewers A list of reviewers
         */
        public RequestDetails(final String title, final String desc,
            final String source, final String target, final boolean close,
            final List<String> reviewers) {
            this.title = title;
            this.desc = desc;
            this.from = source;
            this.dest = target;
            this.close = close;
            this.reviewers = new LinkedList<String>(reviewers);
        }

        /**
         * Obtain the title.
         * @return Title
         */
        public String title() {
            return this.title;
        }

        /**
         * Obtain the description.
         * @return Description
         */
        public String description() {
            return this.desc;
        }

        /**
         * Obtain the source branch name.
         * @return Source branch name
         */
        public String source() {
            return this.from;
        }

        /**
         * Obtain the destination branch name.
         * @return Destination branch name
         */
        public String destination() {
            return this.dest;
        }

        /**
         * Indicate if the source branch should be closed after merging.
         * @return True / false
         */
        public boolean shouldCloseSource() {
            return this.close;
        }

        /**
         * Obtain the reviewers.
         * @return Reviewers
         */
        public List<String> reviewers() {
            return Collections.unmodifiableList(this.reviewers);
        }
    }
    
    /**
     * The status of a pull request.
     * @author Jason Wong.
     *
     */
    enum State {
        /**
         * The pull request is declined.
         */
        DECLINED,
        /**
         * The pull request is opened.
         */
        OPEN,
        /**
         * The pull request is merged.
         */
        MERGED;
    }
}
