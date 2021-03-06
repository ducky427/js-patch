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
    (is (false? (.every [12 5 8 130 44] (fn [x] (>= x 10)))))
    (is (true? (.every [12 54 18 130 44] (fn [x] (>= x 10)))))))

(deftest test-filter
  (testing "Testing filter"
    (is (= [12 130 44] (.filter [12 5 8 130 44] (fn [x] (>= x 10)))))
    (is (= [12 54 18 130 44] (.filter [12 54 18 130 44] (fn [x] (>= x 10)))))))

(deftest test-forEach
  (testing "Testing forEach"
    (let [c     (volatile! 0)
          res   (.forEach [1 2 3] (fn [x] (vswap! c (fn [n] (+ n x)))))]
      (is (nil? res))
      (is (= @c 6)))))

(deftest test-indexOf
  (testing "Testing indexOf"
    (is (= 0 (.indexOf [2 5 9] 2)))
    (is (= -1 (.indexOf [2 5 9] 3)))
    (is (= -1 (.indexOf [2 5 9] 7)))
    (is (= 2 (.indexOf [2 5 9] 9 2)))
    (is (= -1 (.indexOf [2 5 9] 2 -1)))
    (is (= 0 (.indexOf [2 5 9] 2 -3)))
    (is (= 0 (.indexOf [2 5 9] 2 -4)))))

(deftest test-join
  (testing "Testing join"
    (let [a  ["Wind" "Rain" "Fire"]]
      (is (= "Wind,Rain,Fire" (.join a)))
      (is (= "Wind, Rain, Fire" (.join a ", ")))
      (is (= "Wind + Rain + Fire" (.join a " + ")))
      (is (= "WindRainFire" (.join a ""))))))

(deftest test-lastIndexOf
  (testing "Testing lastIndexOf"
    (is (= 3 (.lastIndexOf [2 5 9 2] 2)))
    (is (= -1 (.lastIndexOf [2 5 9 2] 7)))
    (is (= 3 (.lastIndexOf [2 5 9 2] 2 3)))
    (is (= 0 (.lastIndexOf [2 5 9 2] 2 2)))
    (is (= 0 (.lastIndexOf [2 5 9 2] 2 -2)))
    (is (= 3 (.lastIndexOf [2 5 9 2] 2 -1)))))

(deftest test-reduce
  (testing "Testing reduce"
    (is (= 10 (.reduce [0 1 2 3 4] (fn [x y] (+ x y)))))
    (is (= [0 1 2 3 4] (.reduce [0 1 2 3 4] (fn [x y] (conj x y)) [])))
    (is (= 20 (.reduce [0 1 2 3 4] (fn [x y z] (+ x y z)))))
    (is (= 20 (.reduce [0 1 2 3 4] (fn [x y] (+ x y)) 10)))))

(deftest test-reduceRight
  (testing "Testing reduceRight"
    (is (= 10 (.reduceRight [0 1 2 3 4] (fn [x y] (+ x y)))))
    (is (= [4 3 2 1 0] (.reduceRight [0 1 2 3 4] (fn [x y] (conj x y)) [])))
    (is (= 16 (.reduceRight [0 1 2 3 4] (fn [x y z] (+ x y z)))))
    (is (= 20 (.reduceRight [0 1 2 3 4] (fn [x y] (+ x y)) 10)))))

(deftest test-reverse
  (testing "Testing reverse"
    (is (= [0 1 2] (.reverse [2 1 0])))))

(deftest test-some
  (testing "Testing some"
    (is (false? (.some [2 5 8 1 4] (fn [x] (>= x 10)))))
    (is (true? (.some [12 5 8 1 4] (fn [x] (>= x 10)))))))

(deftest test-missing
  (testing "Testing missing functions call"
    (is (thrown-with-msg? js/Error #"sort is not implemented" (.sort [1 2 3])))
    (is (thrown-with-msg? js/Error #"pop is not implemented" (.pop [1 2 3])))
    (is (thrown-with-msg? js/Error #"push is not implemented" (.push [1 2 3] 4)))))
