(ns couchgames.launcher
  (:require [couchgames.layout])
  (:import [couchgames.layout TwoColumns])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (TwoColumns/create))
)
