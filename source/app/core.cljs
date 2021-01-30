(ns app.core
  (:require [reagent.dom :as dom]
            [app.views.index :as index]))

(defn ^:export ^:dev/after-load main []
  (dom/render [index/view] (js/document.getElementById "app")))