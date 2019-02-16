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
(defn dimension-to-vector [dimension]
  (vec ((juxt (memfn getWidth)
              (memfn getHeight)) dimension)))

(defn get-size [component]
  (dimension-to-vector (.getSize component)))

(defn get-minimum-size [component]
  (dimension-to-vector (.getMinimumSize component)))

(defn get-preferred-size [component]
  (dimension-to-vector (.getPreferredSize component)))

(defn rectangle-to-vector [rectangle]
  (vec ((juxt (memfn getX) (memfn getY)
              (memfn getWidth) (memfn getHeight)) rectangle)))

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
  (Dimension. 300 300))

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

(defn compute-combined-size [[head-width head-height] [body-width body-height]]
  (vector (max head-width body-width)
          (+ head-height body-height)))

(defn set-minimum-size [comp [width height]]
  (.setMinimumSize comp (Dimension. width height)))

(defn set-preferred-size [comp [width height]]
  (.setPreferredSize comp (Dimension. width height)))

(defn create-side-panel [head body other-head]
  (let [head-size  nil
        body-size nil]
    {:head head
     :body body
     :other-head other-head
     :panel (doto (JPanel. nil)
              (.add head)
              (.add body)
              (set-minimum-size (compute-combined-size (get-minimum-size head) (get-minimum-size body)))
              (set-preferred-size (compute-combined-size (get-preferred-size head) (get-preferred-size body))))}))

;;; Pass all compontents immediately, for now, then You'll have to re-create the splitpane
(defn create [^Container parent tmp-left-head tmp-left-body tmp-right-head tmp-right-body]
  (let [components (hash-map tmp-left-head left-head-key
                             tmp-left-body left-body-key
                             tmp-right-head right-head-key
                             tmp-right-body right-body-key)
        left-panel (create-side-panel tmp-left-head tmp-left-body tmp-right-head)
        right-panel (create-side-panel tmp-right-head tmp-right-body tmp-left-head)
        inner-panel (create-split-pane parent left-panel right-panel)]
    (println (dimension-to-vector (.getMinimumSize tmp-left-head)))
    (.add parent inner-panel)
    (reify LayoutManager
      (addLayoutComponent [this name component]
        (add-layout-component components name component))
      (minimumLayoutSize [this parent]
        (println (dimension-to-vector (.getMinimumSize inner-panel)))
        (.getMinimumSize inner-panel))
      (preferredLayoutSize [this parent]
        (println (dimension-to-vector (.getPreferredSize inner-panel)))
        (.getPreferredSize inner-panel))
      (removeLayoutComponent [this comp]
        (swap! components #(dissoc % comp)))
      (layoutContainer [this parent]
        (layout-container parent inner-panel left-panel right-panel)))))

(defn -create [^Component parent]
  (create parent nil nil nil nil))
