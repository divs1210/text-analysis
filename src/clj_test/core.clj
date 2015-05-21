(ns clj-test.core
  (:require [clojure.string :as string]))

(def big-text
  (str (slurp "resources/a_tale_of_two_cities.txt") " "
       (slurp "resources/aesop.txt") " "
       (slurp "resources/sherlock.txt")))

(defn words-from-text [txt]
  (-> txt
      (string/replace #"[^\w\s]|-" " ")
      (string/replace #"[0-9_]+" " ")
      (string/replace #"\s+" " ")
      string/trim
      string/lower-case
      (string/split #" ")))

(defn word-probabilities [words]
  (let [word-frequencies (frequencies words)
        word-count       (count words)]
    (into {} (map (fn [[k v]]
                    [k (/ v word-count)])
                  word-frequencies))))

(defn analyze-text [txt]
  (let [known-word-probabilities (word-probabilities (words-from-text big-text))
        test-word-probabilities  (word-probabilities (words-from-text txt))]
    (filter (fn [[k v]]
              (> (* v 0.0005)
                 (get known-word-probabilities k 0)))
            test-word-probabilities)))

(analyze-text
 "Dongyue Temple dates back to 1319, and it’s one of the largest Taoist temples in Beijing.
 It’s also one of the creepiest. It’s home to the Beijing Folklore Museum,
 which was opened in 1999 and commemorates a number of traditions throughout Chinese culture.
 The festivals and demonstrations that go on almost year-round mean that there’s a lot of
 blessing going on at the temple and at the museum. You can walk the blessing road or
 participate in the many ceremonies held there.All that blessing is a good thing,
 because the temple also shows you exactly what’s going to happen to you if you don’t behave.
 The main yard, which is accessible via the Happiness Road, has two huge pavilions that were
 built as memorials to two of the Qing Dynasty emperors. There are also 72 small rooms,
 each one representing one of the departments of hell. The namesake of the temple, Dong Yue,
 is, after all, the one responsible for overseeing all 18 layers of hell and the 76
 departments within them. And it’s all very bureaucratic. You can take a peek at the
 Department of Pity and Sympathy, which is filled with little clay statues of beggars waiting
 for their appeal. The Department for Accumulating Wealth is packed and, of course,
 there’s also a Department for Implementing 15 Kinds of Violent Death. There are donation
 and collection boxes outside each one of the cubicles if the garishly painted, absolutely
 terrifying statues are able to convince you that it won’t hurt to leave a little something
 behind for some goodwill from the overseers.")
