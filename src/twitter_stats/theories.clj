(ns twitter-stats.theories
  :use [twitter-stats.redis])

(defn followers-friends-ratio-theory
  "Balance ratio is the best - above threshold considerd fakey"
  [username]
  (let [user (:body (get<-db username))
        followers-count (:followers_count user)
        friends-count (:friends_count user)
        ratio (/ followers-count friends-count)]
    ;(if () true false)
    ))
