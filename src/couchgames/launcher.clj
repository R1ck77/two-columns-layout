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

(defn new-panel [color]
  (doto (JPanel.)
    (.setBackground color)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [content (JPanel.)
        layout (TwoColumns/create content)]
    (.setLayout content layout)
    (.add content (new-panel (Color. 180 255 255)) "head")
    (.add content (new-panel (Color. 200 255 255)) "body")
    (show-in-window content)))
