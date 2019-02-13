(ns couchgames.layout
  (:import [java.awt LayoutManager Component Container Dimension])
  (:gen-class
   :name couchgames.layout.TwoColumns
   :methods [^:static [create [java.awt.Container] java.awt.LayoutManager]]))

(defn- layout-container
  [^Container parent components]

  (println "Laying out…"))

(defn- preferred-layout-size
  [components]
  (Dimension. 50 50))

(defn- minimum-layout-size
  [components]
  (Dimension. 0 0))

(defn- add-layout-component
  [components-atom name component]
  (swap! components-atom #(assoc % component name)))

(defn -create [^Component parent]
  (let [components (atom {})]
    (reify LayoutManager
      (addLayoutComponent [this name component]
        (add-layout-component components name component))
      (minimumLayoutSize [this parent]
        (minimum-layout-size @components))
      (preferredLayoutSize [this parent]
        (preferred-layout-size @components))
      (removeLayoutComponent [this comp]
        (swap! components #(dissoc % comp)))
      (layoutContainer [this parent]
        (layout-container parent @components)))))
