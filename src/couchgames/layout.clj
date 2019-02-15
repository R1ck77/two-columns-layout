(ns couchgames.layout
  (:import [java.awt LayoutManager Component Container Dimension Rectangle]
           [javax.swing JPanel JSplitPane]
           [java.beans PropertyChangeEvent PropertyChangeListener])
  (:gen-class
   :name couchgames.layout.TwoColumns
   :methods [^:static [create [java.awt.Container] java.awt.LayoutManager]]))

(def split-pane-divider-size 3)

(def left-head-key "left-head")
(def left-body-key "left-body")
(def right-head-key "right-head")
(def right-body-key "right-body")

;;; TODO not optimal. Just pass the Size at this point :p
(defn size-to-vector [size]
  (vec ((juxt #(.getWidth %)
              #(.getHeight %)) size)))

(defn get-size [component]
  (size-to-vector (.getSize component)))

(defn get-preferred-size [component]
  (size-to-vector (.getPreferredSize component)))

(defn rectangle-to-vector [rectangle]
  (vec ((juxt #(.getX %) #(.getY %)
              #(.getWidth %) #(.getHeight %)) rectangle)))

(defn get-bounds [component]
  (rectangle-to-vector (.getBounds component)))

(defn- size-components [container]
  (let [[outer-width outer-height] (get-size (:panel container))
        top-height (min outer-height (.getHeight (.getPreferredSize (:head container))))]
    (.setBounds (:head container) 0 0 outer-width top-height)
    (.setBounds (:body container) 0 top-height outer-width (- outer-height top-height))))

(defn- layout-container
  [^Container parent inner-panel left right]

  (let [parent-width (.getWidth parent)
        parent-height (.getHeight parent)]
    (.setBounds inner-panel (Rectangle. 0 0 parent-width parent-height))
    (size-components left)
    (size-components right)))

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

(defn- create-split-pane [container left right]
  (let [split-pane (doto (JSplitPane. JSplitPane/HORIZONTAL_SPLIT (:panel left) (:panel right))
                     (.setDividerSize split-pane-divider-size)
                     (.setResizeWeight 0.5))
        listener (reify PropertyChangeListener
                   (propertyChange [this event]
                     (layout-container container
                                       split-pane
                                       left right)))]
    (.addPropertyChangeListener split-pane listener)
    split-pane))

(defn create-side-panel [head body other-head]
  {:head head
   :body body
   :other-head other-head
   :panel (doto (JPanel. nil)
            (.add head)
            (.add body))})

;;; Pass all compontents immediately, for now, then You'll have to re-create the splitpane
(defn create [^Container parent tmp-left-head tmp-left-body tmp-right-head tmp-right-body]
  (let [components (hash-map tmp-left-head left-head-key
                             tmp-left-body left-body-key
                             tmp-right-head right-head-key
                             tmp-right-body right-body-key)
        left-panel (create-side-panel tmp-left-head tmp-left-body tmp-right-head)
        right-panel (create-side-panel tmp-right-head tmp-right-body tmp-left-head)
        inner-panel (create-split-pane parent left-panel right-panel)]
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
        (layout-container parent inner-panel left-panel right-panel)))))

(defn -create [^Component parent]
  (create parent nil nil nil nil))
