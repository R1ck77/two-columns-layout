(ns couchgames.layout
  (:import [java.awt LayoutManager Component Container Dimension Rectangle]
           [javax.swing JPanel])
  (:gen-class
   :name couchgames.layout.TwoColumns
   :methods [^:static [create [java.awt.Container] java.awt.LayoutManager]]))

(def head-key "head")
(def body-key "body")

(defn- layout-container
  [^Container parent components]
  (println "Laying out…" components)  
  (let [by-name (into {} (map vec (map reverse components)))
        parent-width (.getWidth parent)
        parent-height (.getHeight parent)]
    (doto (get by-name head-key)
      (.setBounds (Rectangle. 0 0
                              parent-width (/ parent-height))))
    (doto (get by-name body-key)
      (.setBounds (Rectangle. 0 (/ parent-height 2)
                              parent-width (/ parent-height 2))))))

(defn- preferred-layout-size
  [components]
  (Dimension. 50 50))

(defn- minimum-layout-size
  [components]
  (Dimension. 0 0)) ;;; arbitrary, for now

(defn- add-layout-component
  [components-atom name component]
  (println "adding" component "with name" name)
  (swap! components-atom #(assoc % component name)))

(defn- create-transparent-panel []
  (doto (JPanel.)
    (.setOpaque false)))

(defn -create [^Component parent]
  (let [components (atom (hash-map (create-transparent-panel) head-key
                                   (create-transparent-panel) body-key))]
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
