(ns twitter-stats.redis
  (:require [taoensso.carmine :as car]))

(def pool         (car/make-conn-pool)) ; See docstring for additional options
(def spec-server1 (car/make-conn-spec)) ; 
(defmacro wcar [& body] `(car/with-conn pool spec-server1 ~@body))

(defn ping 
  []
  (wcar (car/ping)))

(defn save->db
  "Save to db"
  [k v]
  (wcar (car/set k v)))

(defn get<-db
  "Get value from db store"
  [k]
  (wcar (car/get k)))
