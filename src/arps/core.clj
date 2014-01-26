(ns arps.core
  (:use overtone.live)
  (:require [clojure.string :as str]))



; Stop everything
(stop)

; Load some samples (free sound!!!!!!!!!)

(defn freesound [path] (sample (freesound-path path)))


(def shitty-snare (freesound 26903))
(def long-kick (freesound 40616))
(def phat-kick (freesound 189174))
(def open-hat (freesound 26657))
(def closed-hat (freesound 101442))
(def alienwhisper (freesound 9665))
(def grenade (freesound 33245))
(def that-was-close (freesound 203336))
(def giggle (freesound 204496))

; Play with the sounds
(phat-kick) ; phat ass kick
(long-kick 3) ; long kick 5 times speed
(open-hat 0.125)
(shitty-snare 0.012)
(closed-hat 0.01)
(alienwhisper 0.5)
(grenade 0.25)
(that-was-close)
(giggle 0.8)

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
; (demo (* 0.5 (lpf (sin-osc [220 221]) 280)))


;; synths over a sample buffer

(defsynth reverb-on-left [sample-buff 1 mix 0.81 damp 0.1 room 1]
  (let [dry (play-buf 1 sample-buff)
    wet (free-verb :in dry :mix mix :damp damp :room room)]
    (out 0 [wet dry])))

(reverb-on-left giggle)
(reverb-on-left shitty-snare :mix 0.6)

(def metro (metronome 128))


;; After starting the player below change instruments etc on the fly
(defn player [beat]
  (at (metro beat) (long-kick 25) (closed-hat 13))
  (at (metro (+ 0.5 beat)) (closed-hat 1.4) (open-hat 10))
  (at (metro (+ 0.25 beat)) (open-hat 2.5))
  (at (metro (+ 0.5 beat)) (closed-hat 1.7))
  (at (metro (+ 0.75 beat)) (closed-hat 1.4))
  (apply-at (metro (inc beat)) #'player (inc beat) []))

; Start the player with the metronome
(player (metro))
(stop)
; change up the tempo
;(metro-bpm metro (* 128 2))
