(ns couchgames.layout
  (:import [java.awt LayoutManager Component Container Dimension Rectangle]
           [javax.swing JPanel JSplitPane])
  (:gen-class
   :name couchgames.layout.TwoColumns
   :methods [^:static [create [java.awt.Container] java.awt.LayoutManager]]))

(def left-head-key "left-head")
(def left-body-key "left-body")
(def right-head-key "right-head")
(def right-body-key "right-body")

(defn- layout-container
  [^Container parent components]
  (println "Laying out…" components)  
  (let [by-name (into {} (map vec (map reverse components)))
        parent-width (.getWidth parent)
        parent-height (.getHeight parent)]
    (doto (get by-name left-head-key)
      (.setBounds (Rectangle. 0 0
                              (/ parent-width 2) (/ parent-height 2))))
    (doto (get by-name left-body-key)
      (.setBounds (Rectangle. 0 (/ parent-height 2)
                              (/ parent-width 2) (/ parent-height 2))))
    (doto (get by-name right-head-key)
      (.setBounds (Rectangle. (/ parent-width 2) 0
                              (/ parent-width 2) (/ parent-height 2))))
    (doto (get by-name right-body-key)
      (.setBounds (Rectangle. (/ parent-width 2) (/ parent-height 2)
                              (/ parent-width 2) (/ parent-height 2))))    ))

(defn- preferred-layout-size [components]
  (Dimension. 50 50))

(defn- minimum-layout-size [components]
  (Dimension. 0 0)) ;;; arbitrary, for now

(defn- create-split-pane [left right]
  (doto (JSplitPane. JSplitPane/VERTICAL_SPLIT left right)
    
    ))

(defn- add-layout-component
  [components-atom name component]
  (println "add layout component not implemented yet"))

(defn- create-transparent-panel []
  (doto (JPanel.)
    (.setOpaque false)))

;;; Pass all compontents immediately, for now, then You'll have to re-create the splitpane
(defn create [^Container parent tmp-left-head tmp-left-body tmp-right-head tmp-right-body]
  (let [components (hash-map tmp-left-head left-head-key
                             tmp-left-body left-body-key
                             tmp-right-head right-head-key
                             tmp-right-body right-body-key)]
    (.add parent tmp-left-head)
    (.add parent tmp-left-body)
    (.add parent tmp-right-head)
    (.add parent tmp-right-body)
    (reify LayoutManager
      (addLayoutComponent [this name component]
        (add-layout-component components name component))
      (minimumLayoutSize [this parent]
        (minimum-layout-size components))
      (preferredLayoutSize [this parent]
        (preferred-layout-size components))
      (removeLayoutComponent [this comp]
        (swap! components #(dissoc % comp)))
      (layoutContainer [this parent]
        (layout-container parent components)))))

(defn -create [^Component parent]
  (create parent nil nil nil nil))
