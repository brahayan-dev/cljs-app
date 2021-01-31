(ns app.views.index
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [reagent.core :as r]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]
   [app.components.input :as input]
   [app.components.button :as button]))

(def message (r/atom nil))
(def loading? (r/atom false))
(def scrambled (r/atom ""))
(def original (r/atom ""))

(defn trait-response [body status]
  (let [error? (= status 400)
        match? (:has-match? (js->clj body))
        clear! (fn []
                 (reset! original "")
                 (reset! scrambled ""))]
    (cond
      error? (do (clear!) (reset! message "Only lower case letters!"))
      match? (reset! message "Ok, match between words!")
      :else  (do (clear!) (reset! message "Please try again!"))))
  (swap! loading? not))

(defn make-request []
  (go (let
       [response (<! (http/get "http://localhost:3000/word"
                               {:headers {"Access-Control-Allow-Origin" "http://localhost:8080"
                                          "Access-Control-Allow-Headers" "Content-Type, Origin, Accept, Authorization, Content-Length, X-Requested-With"
                                          "Access-Control-Allow-Methods" "GET,POST,OPTIONS"
                                          "Content-Type" "application/json"
                                          "Accept" "*/*"}
                                :with-credentials? false
                                :query-params {"original" @original
                                               "scrambled" @scrambled}}))]
        (trait-response (:body response) (:status response))))
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
        (if (nil? @message) "Give me a couple of words" @message)]
       [input/to-html
        (-> (input/create-component)
            ((input/set-value @original))
            ((input/set-placeholder "Original word"))
            ((input/on-change (update-word original))))]
       [input/to-html
        (-> (input/create-component)
            ((input/set-value @scrambled))
            ((input/set-addon "mt-4 mb-4"))
            ((input/set-placeholder "Scrambled word"))
            ((input/on-change (update-word scrambled))))]
       [button/to-html
        (-> (button/create-component)
            ((button/set-label "Analyze words"))
            ((button/set-loading? @loading?))
            ((button/on-click make-request)))]]]]]])
