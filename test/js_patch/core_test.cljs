(ns js-patch.core-test
  (:require [cljs.test :refer-macros [is are deftest testing]]
            [js-patch.core :as jc]))

(jc/initialize!)

(deftest test-map
  (testing "Testing Map"
    (is (= [2 3 4] (.map [1 2 3] inc)))
    (is (= [] (.map [] inc)))
    (is (= [0 1 2] (.map [1 2 3] (fn [x i] i))))))

(deftest test-concat
  (testing "Testing concat"
    (is (= ["a" "b" "c" 1 2 3] (.concat ["a" "b" "c"] [1 2 3])))
    (is (= [1 2 3 4 5 6 7 8 9] (.concat [1 2 3] [4 5 6] [7 8 9])))
    (is (= ["a" "b" "c" 1 2 3] (.concat ["a" "b" "c"] 1 [2 3])))))

(deftest test-every
  (testing "Testing every"
    (is (= false (.every [12 5 8 130 44] (fn [x] (>= x 10)))))
    (is (= true (.every [12 54 18 130 44] (fn [x] (>= x 10)))))))
