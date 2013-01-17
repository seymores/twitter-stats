(ns twitter-stats.core
  (:use 
    [twitter-stats.creds]
    [twitter-stats.redis]
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

(defn friends
  "Returns the user's friend ids"
  [username]
  (:body (friends-ids :oauth-creds *creds* :params {:screen-name username})))

(defn followers
  [username]
  (:body (followers-ids :oauth-creds *creds* :params {:screen-name username})))

(defn show
  [username]
  (:body (users-show :oauth-creds *creds* :params {:screen-name username})))

;(defn friendships
;  [username friend]
;  (:body (friendship-show :oauth-creds *creds* 
;                          :params {:source-screen-name username :target-screen-name friend})))

(defn _mentions
  [username]
  (:body (statuses-mentions-timeline :oauth-creds *creds* :params {:screen-name username})))

(defn _test [username]
  (account-verify-credentials :oauth-creds *creds* :params {:screen-name username})
  )
;(println "hello world"))

(defn created-at-to-date 
  "Convert twitter created_at string date time to actual date object"
  [dateval]
  (. (java.text.SimpleDateFormat. "EEE MMM dd HH:mm:ss Z yyyy") parse dateval))

;(defn listfriends
;  [username]
;  (:body (list-followers :oauth-creds *creds* :params {:screen-name username :cursor -1})))

(defn dump
  "Save a data dump of a given twitter user"
  [username]
  (let [o (users-show :oauth-creds *creds* :params {:screen-name username})
        d (assoc o :saved-at (java.util.Date.))
        f (assoc d :friends (friends username))
        l (assoc f :followers (followers username))]
    (save->db username l)))

(defn dump-by-id
  "Save a data dump of a given twitter user by ID"
  [user-id]
  (let [username (:screen_name (:body (users-show :oauth-creds *creds* :params {:user-id user-id})))]
    (dump username)))

