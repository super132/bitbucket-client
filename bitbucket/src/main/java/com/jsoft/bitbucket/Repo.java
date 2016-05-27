/**
 * 
 */
package com.jsoft.bitbucket;

import java.io.IOException;
import java.util.List;

/**
 * A repository in BitBucket.
 * @author hcsrxo6
 *
 */
public interface Repo extends Resource {

    /**
     * Obtain the watchers of this repository.
     * @return The list of watchers.
     * @throws IOException If errors occur
     */
    List<User> watchers() throws IOException;

    /**
     * Obtain the forks of this repository.
     * @return The list of forks.
     * @throws IOException If errors occur
     */
    List<Repo> forks() throws IOException;

    /**
     * The repository information.
     * @return The info.
     */
    Settings info();

    /**
     * Pull requests object of this repo.
     * @return The Pull requests object that manage pull requests.
     * @throws IOException If errors occur
     */
    PullRequests requests() throws IOException;

    /**
     * Branches object of this repo.
     * @return The branches object that manage branches.
     * @throws IOException If errors occur
     */
    Branches branches() throws IOException;

    /**
     * Commits object to query repository commits. 
     * @return Commits
     */
    Commits commits();

    /**
     * Repository settings used when creating new repo.
     * @author Jason Wong
     *
     */
    final class Settings {
        /**
         * Default SCM type.
         */
        private static final String GIT = "git";
        /**
         * SCM type.
         */
        private final transient String scm;
        /**
         * Name of the repository (Optional).
         */
        private final transient String name;
        /**
         * Indicate if this repository is public.
         */
        private final transient boolean pub;
        /**
         * Description of the repo.
         */
        private final transient String desc;
        /**
         * Fork Policy.
         */
        private final transient ForkPolicy fork;
        /**
         * The programming language of the repo (Optional).
         */
        private final transient String lang;
        /**
         * Indicate if issue tracker is needed.
         */
        private final transient boolean tracker;
        /**
         * Indicate if wiki is needed.
         */
        private final transient boolean wiki;

        /**
         * Settings for creating a repo without issue tracker and wiki.
         * Defaulted to git repo.
         * @param open Indicate if the repo is public
         * @param policy The forking policy
         */
        public Settings(final boolean open, ForkPolicy policy) {
            this(Settings.GIT, "", open, "", policy, "", false, false);
        }

        /**
         * Settings for creating a repo without issue tracker and wiki.
         * Defaulted to git repo.
         * @param open Indicate if the repo is public
         * @param policy The forking policy
         * @param desc The description of the repo
         */
        public Settings(final boolean open, ForkPolicy policy,
            final String desc) {
            this(Settings.GIT, "", open, desc, policy, "", false, false);
        }

        /**
         * Settings for creating a repo without issue tracker and wiki.
         * @param type The SCM type, either "hg" or "git".
         * @param open Indicate if the repo is public
         * @param policy The forking policy
         */
        public Settings(final String type, final boolean open,
            ForkPolicy policy) {
            this(Settings.GIT, "", open, "", policy, "", false, false);
        }

        /**
         * Settings for creating a repo. Defaulted to git repo.
         * @param open Indicate if the repo is public
         * @param policy The forking policy
         */
        public Settings(final boolean open, ForkPolicy policy,
            final boolean tracker, final boolean wiki) {
            this(Settings.GIT, "", open, "", policy, "", tracker, wiki);
        }

        /**
         * Create repo full settings.
         * @param scm The SCM type, either "hg" or "git".
         * @param name The name of the repo.
         * @param open Is it public
         * @param desc The description
         * @param policy The forking policy
         * @param language The programming language of this repo.
         * @param tracker With issue tracker?
         * @param wiki With wiki?
         */
        public Settings(
            final String scm,
            final String name,
            final boolean open,
            final String desc,
            final ForkPolicy policy,
            final String language,
            final boolean tracker,
            final boolean wiki
        ) {
            this.scm = scm;
            this.name = name;
            this.pub = open;
            this.desc = desc;
            this.fork = policy;
            this.lang = language;
            this.tracker = tracker;
            this.wiki = wiki;
        }

        /**
         * The SCM type.
         * @return SCM type
         */
        public String type() {
            return this.scm;
        }

        /**
         * The repo name.
         * @return
         */
        public String name() {
            return this.name;
        }

        /**
         * Indicate if this repo is a public repo.
         * @return
         */
        public boolean isPublic() {
            return this.pub;
        }

        /**
         * The description of the repo.
         * @return Description
         */
        public String description() {
            return this.desc;
        }

        /**
         * The fork policy of the repo.
         * @return Forking policy
         */
        public ForkPolicy policy() {
            return this.fork;
        }

        /**
         * The programming language used in this repo.
         * @return Language.
         */
        public String language() {
            return this.lang;
        }

        /**
         * Is this repo with an issue tracker?
         * @return True or false
         */
        public boolean withTracker() {
            return this.tracker;
        }

        /**
         * Is this repo with a wiki?
         * @return True or false
         */
        public boolean withWiki() {
            return this.wiki;
        }
    }
    

    /**
     * Fork Policy of a repo.
     * @author Jason Wong
     */
    enum ForkPolicy {
        /**
         * Unrestricted forking.
         */
        ALLOW_FORKS("allow_forks"),
        /**
         * Restrict forking to private forks (forks cannot be made public
         * later).
         */
        NO_PUBLIC_FORKS("no_public_forks"),
        /**
         * Deny all forking.
         */
        NO_FORKS("no_forks"),
        /**
         * Unknown fork policy.
         */
        UNKNOWN("");
        /**
         * The value that submit to BitBucket API.
         */
        private String value;

        /**
         * Ctor.
         * @param value The value.
         */
        private ForkPolicy(final String value) {
            this.value = value;
        }

        /**
         * Obtain the value.
         * @return The value.
         */
        public String value() {
            return this.value;
        }

        /**
         * Look up the ForkPolicy from Bitbucket API value.
         * @param value The value from REST API
         * @return ForkPolicy
         */
        public static ForkPolicy fromValue(final String value) {
            ForkPolicy result = ForkPolicy.UNKNOWN;
            for (final ForkPolicy policy : ForkPolicy.values()) {
                if (policy.value().equals(value)) {
                    result = policy;
                    break;
                }
            }
            return result;
        }
    }
}
