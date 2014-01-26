(ns arps.core
  (:use overtone.live)
  (:require [clojure.string :as str]))


(demo (sin-osc))

(sin-osc 440)

(def shitty-snare (sample (freesound-path 26903)))
(shitty-snare)

(def long-kick (sample (freesound-path 40616)))
(def phat-kick (sample (freesound-path 189174)))
(def open-hat (sample (freesound-path 26657)))

(phat-kick 1.25 :loop 5)
(long-kick)

(defsynth sin-square2 [freq 440 level 0.5]
  (out 0 (* [0.5 0.5] (+ (square (* level freq)) (sin-osc freq)))))

(defsynth simple-saw [freq 220 level 0.5]
  (let [v (* level (saw freq))]
    (out 0 v)
    (out 1 v)))

(stop)
(simple-saw)
(simple-saw 222)

(kill 1142 1143)

(sin-square2 :level 0.1 :freq (* 329.1  0.5 1.7))


(stop)


(demo (* 0.5 (lpf (sin-osc [220 221]) 280)))

(defsynth reverb-on-left [sample-buff 2]
  (let [dry (play-buf 1 sample-buff)
    wet (free-verb :in dry :mix 0.81 :damp 0.1 :room 1)]
    (out 0 [wet dry])))
(reverb-on-left shitty-snare)


(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))


(definst o-hat [amp 0.9 t 0.5]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))

(defn swinger [beat]
  (at (metro beat) (o-hat))
  (at (metro (inc beat)) (c-hat))
  (at (metro (+ 1.65 beat)) (c-hat))
  (apply-at (metro (+ 2 beat)) #'swinger (+ 2 beat) []))

; define a metronome at a given tempo, expressed in beats per minute.
(def metro (metronome 120))

(swinger (metro))
(stop)





