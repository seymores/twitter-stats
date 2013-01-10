(ns twitter-stats.creds
  (:use [twitter.oauth]))

(def ^:dynamic *app-consumer-key* "mjmOvjPdWAIxGbWgjC529w")
(def ^:dynamic *app-consumer-secret* "OAJzd9eooTY4iQjUYhBXk8rRsImUZ2fpSriAJlyRdic")
(def ^:dynamic *user-access-token* "6741022-efZuh0cTZOPPzwm72CXYLTpWle2z483703ihCYsM")
(def ^:dynamic *user-access-token-secret* "k5ziQVlRuI8EQzmJI8DQJbzdQ94Pj4755A5Fle1s")
(def ^:dynamic *creds* (make-oauth-creds *app-consumer-key*
                                         *app-consumer-secret*
                                         *user-access-token*
                                         *user-access-token-secret*))

