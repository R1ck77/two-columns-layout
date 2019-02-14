(ns couchgames.launcher
  (:require [couchgames.layout :as layout])
  (:import [couchgames.layout TwoColumns]
           [java.awt Dimension Color]
           [javax.swing JFrame JPanel JTextArea]
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

(defn new-multi-line-label [text color]
  (doto (JTextArea. text)
    (.setBackground color)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [content (JPanel.)
        layout (layout/create content
                              (new-multi-line-label "This is a text" (Color. 180 255 255)) (new-panel (Color. 200 255 255))
                              (new-multi-line-label "This is a\nmulti line\ntext" (Color. 255 180 255)) (new-panel (Color. 255 200 255)))]
    (.setLayout content layout)
;;;    (.add content (new-panel (Color. 180 255 255)) "head")
;;;    (.add content (new-panel (Color. 200 255 255)) "body")
    (show-in-window content)))
