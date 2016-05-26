/**
 * 
 */
package com.jsoft.bitbucket;

import java.util.List;

/**
 * A repository in BitBucket.
 * @author hcsrxo6
 *
 */
public interface Repo {

    /**
     * Obtain the watchers of this repository.
     * @return The list of watchers.
     */
    List<User> watchers();

    /**
     * Obtain the forks of this repository.
     * @return The list of forks.
     */
    List<Repo> forks();

    /**
     * The repository information.
     * @return The info.
     */
    Settings info();

    /**
     * Pull requests object of this repo.
     * @return The Pull requests object that manage pull requests.
     */
    PullRequests requests();

    /**
     * Branches object of this repo.
     * @return The branches object that manage branches.
     */
    Branches branches();

    /**
     * Repository settings used when creating new repo.
     * @author Jason Wong
     *
     */
    final class Settings {
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
            this("", "", open, "", policy, "", false, false);
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
            this("", "", open, desc, policy, "", false, false);
        }

        /**
         * Settings for creating a repo without issue tracker and wiki.
         * @param type The SCM type, either "hg" or "git".
         * @param open Indicate if the repo is public
         * @param policy The forking policy
         */
        public Settings(final String type, final boolean open, ForkPolicy policy) {
            this("", "", open, "", policy, "", false, false);
        }

        /**
         * Settings for creating a repo. Defaulted to git repo.
         * @param open Indicate if the repo is public
         * @param policy The forking policy
         */
        public Settings(final boolean open, ForkPolicy policy,
            final boolean tracker, final boolean wiki) {
            this("", "", open, "", policy, "", tracker, wiki);
        }

        /**
         * Create repo settings
         * @param scm The SCM type, either "hg" or "git".
         * @param name The name of the repo.
         * @param open Is it public
         * @param desc The description
         * @param policy The forking policy
         * @param language The programming language of this repo.
         * @param tracker With issue tracker?
         * @param wiki With wiki?
         */
        private Settings(
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
        NO_FORKS("no_forks");
        
        /**
         * The value that submit to BitBucket API.
         */
        private String value;

        private ForkPolicy(final String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
