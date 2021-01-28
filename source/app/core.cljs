(ns app.core
  (:require
   [reagent.dom :as rdom]))

(defn app []
  [:h1 "App is working!"])

(defn ^:export main []
  (rdom/render [app] (js/document.getElementById "app")))