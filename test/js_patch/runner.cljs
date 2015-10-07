(ns js-patch.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [js-patch.core-test]))

(doo-tests 'js-patch.core-test)