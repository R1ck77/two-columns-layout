(ns couchgames.layout
  (:import [java.awt LayoutManager Component])
  (:gen-class
   :name couchgames.layout.TwoColumns
   :methods [^:static [create [] java.awt.LayoutManager]]))

(defn -create [^Component parent]
  (let [components (atom [])]
    (reify LayoutManager
      (addLayoutComponent [this name comp]
        (swap! components #(conj % (list name comp))))
      (layoutContainer [this parent]
        )
      (minimumLayoutSize [this parent]
        )
      (preferredLayoutSize [this parent]
        )
      (removeLayoutComponent [this comp]
        ))))
