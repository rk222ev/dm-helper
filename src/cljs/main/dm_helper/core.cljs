(ns dm-helper.core
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [compassus.core :as c]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [dm-helper.adventure-dataset :as adventures]
            [dm-helper.views :as views]
            [dm-helper.parser :as parser]))

(declare history)
(declare update-route!)

(defonce app-state
  (atom {:app/title "DM-helper"
         :search/phrase ""
         :search/sort-column ::adventures/title
         :search/filter-column ::adventures/title
         :form/adventure nil
         :adventures adventures/data}))

(defonce bidi-routes
  ["/" {"" :index
        "about" :about
        "adventures" :adventures
        "contribute" :contribute}])

(declare app)

(defn update-route!
  [{:keys [handler] :as route}]
  (let [current-route (c/current-route app)]
    (when (not= handler current-route)
      (c/set-route! app handler))))

(defonce history
  (pushy/pushy update-route! (partial bidi/match-route bidi-routes)))

(defonce app
  (c/application {:routes {:index views/Adventures
                           :about views/About
                           :contribute views/Contribute}
                  :index-route :about
                  :reconciler (om/reconciler
                               {:state app-state
                                :parser (c/parser {:read parser/read :mutate parser/mutate})})
                  :mixins [(c/did-mount (fn [_] (pushy/start! history)))
                           (c/will-unmount (fn [_] (pushy/stop! history)))]}))

(defonce mounted? (atom false))

(defn init! []
  (enable-console-print!)
  (if-not @mounted?
    (do
      (c/mount! app (js/document.getElementById "app"))
      (swap! mounted? not))
    (let [route->component (-> app :config :route->component)
          c (om/class->any (c/get-reconciler app) (get route->component (c/current-route app)))]
      (.forceUpdate c))))
