(ns js-patch.core
  (:require-macros [js-patch.core :refer [js-array-map js-array-concat js-array-every]]))

(defn initialize!
  []
  (js-array-map PersistentVector)
  (js-array-concat PersistentVector)
  (js-array-every PersistentVector))
