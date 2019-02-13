(ns couchgames.launcher
  (:require [couchgames.layout])
  (:import [couchgames.layout TwoColumns]
           [java.awt Dimension Color]
           [javax.swing JFrame JPanel]
           )
  (:gen-class))

(def window-width 800)
(def window-height 600)

(defn show-in-window [component]
  (doto (JFrame.)
    (.setSize (Dimension. window-width window-height))
    (.setContentPane component)
    (.setVisible true)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (show-in-window (doto (JPanel.)
                    (.setBackground Color/RED)))
)
