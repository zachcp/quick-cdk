(ns leiningen.new.quick-cdk
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "quick-cdk"))

(defn quick-cdk
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' quick-cdk project.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             [".gitignore" (render "gitignore" data)])))
