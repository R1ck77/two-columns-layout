(ns couchgames.layout
  (:import [java.awt LayoutManager Component Container Dimension Rectangle]
           [javax.swing JPanel JSplitPane]
           [java.beans PropertyChangeEvent PropertyChangeListener])
  (:gen-class
   :name couchgames.layout.TwoColumns
   :methods [^:static [create [java.awt.Container] java.awt.LayoutManager]]))

(def left-head-key "left-head")
(def left-body-key "left-body")
(def right-head-key "right-head")
(def right-body-key "right-body")

(defn- layout-container
  [^Container parent inner-panel components left right]
  (println "Laying out…" components)  
  (let [by-name (into {} (map vec (map reverse components)))
        parent-width (.getWidth parent)
        parent-height (.getHeight parent)]
    (doto inner-panel
      (.setBounds (Rectangle. 0 0 parent-width parent-height)))
    (.setSize (get by-name left-head-key) (.getSize left))
    (.setSize (get by-name right-head-key) (.getSize right))))

(defn- preferred-layout-size [components]
  (Dimension. 50 50))

(defn- minimum-layout-size [components]
  (Dimension. 0 0)) ;;; arbitrary, for now

(defn- add-layout-component
  [components name component]
  (println "add layout component not implemented yet"))

(defn- create-transparent-panel []
  (doto (JPanel.)
    (.setOpaque false)))

(defn- create-split-pane [container left right components]
  (let [split-pane (JSplitPane. JSplitPane/HORIZONTAL_SPLIT left right)
        listener (reify PropertyChangeListener
                   (propertyChange [this event]
                     (layout-container container
                                       split-pane
                                       components left right)))]
    (.addPropertyChangeListener split-pane listener)
    split-pane))

;;; Pass all compontents immediately, for now, then You'll have to re-create the splitpane
(defn create [^Container parent tmp-left-head tmp-left-body tmp-right-head tmp-right-body]
  (let [components (hash-map tmp-left-head left-head-key
                             tmp-left-body left-body-key
                             tmp-right-head right-head-key
                             tmp-right-body right-body-key)
        left-panel (doto (JPanel. nil)
                    (.add tmp-left-head)
                    (.add tmp-left-body))
        right-panel (doto (JPanel. nil)
                    (.add tmp-right-head)
                    (.add tmp-right-body))
        inner-panel (create-split-pane parent left-panel right-panel components)]
    (.add parent inner-panel)
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
        (layout-container parent inner-panel components left-panel right-panel)))))

(defn -create [^Component parent]
  (create parent nil nil nil nil))
