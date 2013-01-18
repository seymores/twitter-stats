(ns twitter-stats.core
  (:use 
    [twitter-stats.creds]
    [twitter-stats.redis]
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.restful])
  (:require clojure.pprint)
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
  ([username]
   (let [b (:body (followers-ids :oauth-creds *creds* :params {:screen-name username }))
         ids (:ids b)
         cursor (:next_cursor b)
         ]))
  ([username cursor]
   (:body (followers-ids :oauth-creds *creds* :params {:screen-name username :cursor cursor}))))

(defn followers-all
  "Get followers for the given twitter account"
  [username cursor]
  (let [v {:screen-name username}
        param (merge v (when (> cursor 0) {:cursor cursor})) 
        fo (:body (followers-ids :oauth-creds *creds* :params param))
        cur (:next_cursor fo)]
    (print str (:ids fo) "\n\n ---- \n")
    (conj (when (> cur 0 ) (followers-all username cur)) (:ids fo))))

(defn friends-all
  "Get friends for the given twitter account"
  [username cursor]
  (let [v {:screen-name username}
        param (merge v (when (> cursor 0) {:cursor cursor})) 
        fo (:body (friends-ids oauth-creds *creds* :params param))
        cur (:next_cursor fo)]
    (print str (:ids fo) "\n\n ---- \n")
    (conj (when (> cur 0 ) (friends-all username cur)) (:ids fo))))

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

