(ns js-patch.core
  (:require-macros [js-patch.core :refer [js-array-map js-array-concat js-array-every
                                          js-array-filter js-array-forEach
                                          js-array-indexOf js-array-join]]))

(defn initialize!
  []
  (js-array-map PersistentVector)
  (js-array-concat PersistentVector)
  (js-array-every PersistentVector)
  (js-array-filter PersistentVector)
  (js-array-forEach PersistentVector)
  (js-array-indexOf PersistentVector)
  (js-array-join PersistentVector))
