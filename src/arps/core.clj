(ns arps.core
  (:use overtone.live)
  (:require [clojure.string :as str]))



; Stop everything
(stop)

; Load some samples (free sound!!!!!!!!!)

(def shitty-snare (sample (freesound-path 26903)))
(def long-kick (sample (freesound-path 40616)))
(def phat-kick (sample (freesound-path 189174)))
(def open-hat (sample (freesound-path 26657)))

; Play with the sounds

;(phat-kick) ; phat ass kick
;(long-kick 5) ; long kick 5 times speed
;(open-hat)


(defsynth sin-square2 [freq 440 level 0.5]
  (out 0 (* [0.5 0.5] (+ (square (* level freq)) (sin-osc freq)))))

(defsynth simple-saw [freq 220 level 0.5 distance 2]
  (let []
    (out 0 (* level (saw freq)))
    (out 1 (* level (saw (+ freq distance))))))


; Play with the synths
;(simple-saw (* 110) :distance 5)
; (simple-saw 55)
;(sin-square2 :level 0.1 :freq (* 329.1  0.5 1.7))
;(sin-square2 :level 0.5 :freq 110)


; Some other shit
(demo (* 0.5 (lpf (sin-osc [220 221]) 280)))


;; synths over a sample buffer

(defsynth reverb-on-left [sample-buff 1 mix 0.81 damp 0.1 room 1]
  (let [dry (play-buf 1 sample-buff)
    wet (free-verb :in dry :mix mix :damp damp :room room)]
    (out 0 [wet dry])))

(reverb-on-left shitty-snare :mix 0.6)







