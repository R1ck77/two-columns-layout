(ns couchgames.layout
  (:import [java.awt LayoutManager])
  (:gen-class
   :name couchgames.layout.TwoColumns
   :methods [^:static [create [] java.awt.LayoutManager]]))

(defn -create []
  (reify LayoutManager
    (addLayoutComponent [this name comp]
      )
    (layoutContainer [this parent]
      )
    (minimumLayoutSize [this parent]
      )
    (preferredLayoutSize [this parent]
      )
    (removeLayoutComponent [this comp]
      )))
