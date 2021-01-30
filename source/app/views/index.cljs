(ns app.views.index
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [reagent.core :as r]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]
   [app.components.input :as input]
   [app.components.button :as button]))

(def loading? (r/atom false))
(def original (r/atom ""))
(def scrambled (r/atom ""))

(defn make-request []
  (go (let
       [response (<! (http/get "http://localhost:3000/words"
                               {:headers {"Access-Control-Allow-Origin" "http://localhost:8080"
                                          "Access-Control-Allow-Headers" "Content-Type, Origin, Accept, Authorization, Content-Length, X-Requested-With"
                                          "Access-Control-Allow-Methods" "GET,POST,OPTIONS"
                                          "Content-Type" "application/json"
                                          "Accept" "*/*"}
                                :with-credentials? false
                                :query-params {"original" @original
                                               "scrambled" @scrambled}}))]
        (prn  (:body response))))
  (swap! loading? not))

(defn update-word [a]
  (fn [evt] (reset! a (-> evt .-target .-value))))

(defn view []
  [:section.hero.is-fullheight
   [:div.hero-body
    [:div.container.is-max-desktop.is-flex.is-justify-content-center.is-align-content-center
     [:div.columns
      [:div.box.column.is-half.is-offset-one-quarter
       [:h1.title.is-4.has-text-grey.has-text-centered
        "Give me a couple of words"]
       [input/to-html
        (-> (input/create-component)
            ((input/set-placeholder "Original word"))
            ((input/on-change (update-word original))))]
       [input/to-html
        (-> (input/create-component)
            ((input/set-addon "mt-4 mb-4"))
            ((input/set-placeholder "Scrambled word"))
            ((input/on-change (update-word scrambled))))]
       [button/to-html
        (-> (button/create-component)
            ((button/set-label "Analyze words"))
            ((button/set-loading? @loading?))
            ((button/on-click make-request)))]]]]]])
