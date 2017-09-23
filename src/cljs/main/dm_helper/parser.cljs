(ns dm-helper.parser
  (:require
   [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state query]} _ _]
  {:value (select-keys @state query)})

(defmulti mutate om/dispatch)


(defmethod mutate 'search/update!
  [{:keys [state _]} _ {:keys [key val]}]
  {:action #(swap! state assoc key val)})


(defmethod mutate 'form/update!
  [{:keys [state _]} _ {:keys [key val]}]
  {:action #(swap! state assoc :form/adventure val)})

(defmethod mutate 'form/update-val!
  [{:keys [state _]} _ {:keys [key val]}]
  {:action #(swap! state assoc-in [:form/adventure key] val)})
