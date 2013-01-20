(ns twitter-stats.core
  (:use 
    [twitter-stats.creds]
    [twitter-stats.theories]
    [twitter-stats.redis]
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.restful])
  (:require clojure.pprint))

;Use case:
;(def x (show-user 'seymores'))
;(def body (x :body))

;API list - https://github.com/adamwynne/twitter-api/blob/master/src/twitter/api/restful.clj
;Twitter API - https://dev.twitter.com/docs/api/1.1
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn friends
  "Returns the user's friend ids"
  [username]
  (:body (friends-ids :oauth-creds *creds* :params {:screen-name username})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn followers
  ([username]
   (let [b (:body (followers-ids :oauth-creds *creds* :params {:screen-name username }))
         ids (:ids b)
         cursor (:next_cursor b) ]))
  ([username cursor]
   (:body (followers-ids :oauth-creds *creds* :params {:screen-name username :cursor cursor}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn followers-all
  "Get followers for the given twitter account as vector"
  [username cursor]
  (let [v {:screen-name username}
        p (merge v (when (pos? cursor) {:cursor cursor})) 
        f (:body (followers-ids :oauth-creds *creds* :params p))
        c (:next_cursor f)]
    (into (if (pos? c) (followers-all username c) []) (:ids f))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn friends-all
  "Get friends for the given twitter account as vector"
  [username cursor]
  (let [v   {:screen-name username}
        par (merge v (when (pos? cursor) {:cursor cursor})) 
        fo  (:body (friends-ids :oauth-creds *creds* :params par))
        cur (:next_cursor fo)]
    (into (if (pos? cur) (friends-all username cur) []) (:ids fo))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn show
  "Show the user with the given twitter username"
  [username]
  (:body (users-show :oauth-creds *creds* :params {:screen-name username})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn mentions
  "Get the mentions for the given user"
  [username]
  (:body (statuses-mentions-timeline :oauth-creds *creds* :params {:screen-name username})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn verify-credentials [username]
  "Verify the credential of the oauth account"
  (account-verify-credentials :oauth-creds *creds* :params {:screen-name username}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn created-at->date 
  "Convert twitter created_at string date time to actual date object"
  [dateval]
  (.parse (java.text.SimpleDateFormat. "EEE MMM dd HH:mm:ss Z yyyy") dateval))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dump
  "Save a data dump of a given twitter user"
  [username]
  (let [o (users-show :oauth-creds *creds* :params {:screen-name username})
        d (assoc o :saved-at (java.util.Date.))
        r (assoc d :friends (friends-all username 0))
        l (assoc r :followers (followers-all username 0))]
    (save->db (.toLowerCase username) l)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dump-by-id
  "Save a data dump of a given twitter user by ID"
  [user-id]
  (let [username (:screen_name (:body (users-show :oauth-creds *creds* :params {:user-id user-id})))]
    (dump username)))

