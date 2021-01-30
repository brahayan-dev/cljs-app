(ns app.components.input)

(def ^:private c-base
  {:addon ""
   :placeholder "Input"
   :on-change #(println "Change event launched from input")})

;; --- Modifiers ---

(defn create-component []
  c-base)

(defn set-placeholder [s]
  (fn [base] (assoc base :placeholder s)))

(defn set-addon [s]
  (fn [base] (assoc base :addon s)))

(defn on-change [f]
  (fn [base] (assoc base :on-change f)))

;; --- Elements ---

(defn to-html [base]
  [:input.input.is-link.is-large.has-text-centered
   {:type "text"
    :class (:addon base)
    :placeholder (:placeholder base)
    :on-change (:on-change base)}])
