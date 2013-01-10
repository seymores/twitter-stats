(ns twitter-stats.core
  (:use 
    [twitter-stats.creds]
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.restful])
  (:import (twitter.callbacks.protocols SyncSingleCallback)))

;Use case:
;(def x (show-user 'seymores'))
;(def body (x :body))

;API list - https://github.com/adamwynne/twitter-api/blob/master/src/twitter/api/restful.clj
;Twitter API - https://dev.twitter.com/docs/api/1.1

(defn show
  [username]
  (:body (show-user :oauth-creds *creds* :params {:screen-name username})))

(defn friends
  [username]
  (:body (show-friends :oauth-creds *creds* :params {:screen-name username})))

(defn followers
  [username]
  (:body (show-followers :oauth-creds *creds* :params {:screen-name username})))

(defn friendships
  [username friend]
  (:body (show-friendship :oauth-creds *creds* 
                          :params {:source-screen-name username :target-screen-name friend})))

(defn _mentions
  [username]
  (:body (mentions :oauth-creds *creds* :params {:screen-name username})))

(defn _test []
  (println "hello world"))

(defn listfriends
  [username]
  (:body (list-followers :oauth-creds *creds* :params {:screen-name username :cursor -1})))

