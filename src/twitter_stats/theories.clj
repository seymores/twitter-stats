(ns twitter-stats.theories
  (:use [twitter-stats.redis]))

(defn followers-friends-ratio-theory
  "Balance ratio is the best - above threshold considerd fakey"
  [username]
  (let [user      (:body (get<-db username))
        followers-count (:followers_count user)
        friends-count (:friends_count user)
        ratio     (/ followers-count friends-count)]
    (< 0.8 ratio 1.5)))

(defn customized-background?
  "Check if the user is using non-default background -- default = bad"
  [username]
  (let [user      (:body (get<-db username))
        bg-img    (:profile_background_image_url_https user)
        bg-df-count (count (.split bg-img))
        bg-tile   (:profile_background_tile user)
        df-pic    (:profile_image_url user)
        df-pr-pic (.contains df-pic "default_profile_0")]
    (or (> bg-df-count 1) bg-tile df-pr-pic)))

(defn default-profile-state?
  "Check this account profile is in default state"
  [username]
  (let [user (:body (get<-db username))]
    (and (:default_profile user) (:default_profile_image user))))

(defn is-location-set?
  "Check if the user set the location -- no location = bad"
  [username]
  (let [user      (:body (get<-db username))
        location  (:location user)]
    (nil? location)))

(defn verified?
  "Check if the user is verified"
  [username]
  (let [user (:body (get<-db username))]
        (:verified user)))

