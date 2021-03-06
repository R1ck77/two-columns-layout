(ns couchgames.launcher
  (:require [couchgames.layout :as layout])
  (:import [couchgames.layout TwoColumns]
           [java.awt Dimension Color]
           [javax.swing JFrame JPanel JTextArea JSplitPane])
  (:gen-class))

(def window-width 800)
(def window-height 600)

(defn show-in-window [component]
  (doto (JFrame.)
    (.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE)
    (.setSize (Dimension. window-width window-height))
    (.setContentPane component)
    (.setMinimumSize (.getMinimumSize component))
    (.pack)
    (.setVisible true)))

(defn new-panel [color]
  (doto (JPanel.)
    (.setBackground color)))

(defn new-multi-line-label [text color]
  (doto (JTextArea. text)
    (.setBackground color)))

(defn new-dimension [width height]
  (Dimension. width height))

(defn create-top-left-panel []
  (doto (new-multi-line-label "This is a text" (Color. 180 255 255))
    (.setMinimumSize (new-dimension 100 100))
    (.setPreferredSize (new-dimension 200 200))))

(defn create-bottom-left-panel []
  (new-panel (Color. 200 255 255)))

(defn create-top-right-panel []
  (doto (new-multi-line-label "This is a\nmulti line\ntext" (Color. 255 180 255))
    (.setMinimumSize (new-dimension 200  200))
    (.setPreferredSize (new-dimension 300 300))))

(defn create-bottom-right-panel []
  (new-panel (Color. 255 200 255)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (do (let [content (JPanel.)
         layout (layout/create content
                               (create-top-left-panel) (create-bottom-left-panel)
                               (create-top-right-panel) (create-bottom-right-panel))]
     (.setLayout content layout)
;;;    (.add content (new-panel (Color. 180 255 255)) "head")
;;;    (.add content (new-panel (Color. 200 255 255)) "body")
     (show-in-window content)))
  (comment (show-in-window (doto (JSplitPane. JSplitPane/HORIZONTAL_SPLIT
                                      (create-top-left-panel)
                                      (create-top-right-panel))
                     (.setResizeWeight 0.5)))))
