(ns app.components.button)

(def ^:private c-base
  {:label "Button"
   :loading? false
   :on-click #(println "Click event launched from button")})

;; --- Modifiers ---

(defn create-component []
  c-base)

(defn set-label [s]
  (fn [base] (assoc base :label s)))

(defn set-loading? [b]
  (fn [base] (if b
               (assoc base :loading? b)
               base)))

(defn on-click [f]
  (fn [base] (assoc base :on-click f)))

;; --- Elements ---

(defn to-html [base]
  (let [addon (when (:loading? base) "is-loading")]
    [:button.button.is-link.is-large.is-fullwidth.is-outlined
     {:class addon :on-click (:on-click base)}
     (:label base)]))
