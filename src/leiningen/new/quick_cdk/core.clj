(ns {{name}}.core
  (:require [org.openscience.cdk/cdk-smiles SmilesParser]))

(enable-console-print!)

(ns testmaven.core
  (:require [clojure.string :as string])
  (:import [org.openscience.cdk.smiles SmilesParser]
           [org.openscience.cdk DefaultChemObjectBuilder]
           [org.openscience.cdk.aromaticity CDKHueckelAromaticityDetector]
           [org.openscience.cdk.tools.manipulator AtomContainerManipulator]
           [org.openscience.cdk CDKConstants]
           [org.openscience.cdk.fingerprint MACCSFingerprinter]))

(def small-smiles "CC1=C(C=C(C=C1)C(C#N)C2=CC=CC=C2)C
CC(=O)OC(C1=CC=CC=C1)C(C=C)(F)F
C1=CC=C(C=C1)C(=O)NC(=O)C2=CN=CC=C2
CC1=C(C(=O)CC1)C=CCCC2=CC=CC=C2
CC(=CCCOC(=O)CCC1=CC=CC=C1)C
COC(=O)C1CCN(CC1)CC2=CC=CC=C2
COC(=O)CCC=CCOCC1=CC=CC=C1
CCOC1(CC1CC2=CC=CC=C2)OC(=O)C
CC(CC(CC=C)O)COCC1=CC=CC=C1
C1=CC=C(C=C1)CC(=O)NCCCCCCN
CC(C)(C=C)NC(=S)NCC1=CC=CC=C1
C1=CC(=C2C(=C1)C(=O)C3=CC=CN32)C4=CC=CO4
CC1=C(C(=O)N(C1Cl)CC2=CC=CC=C2)C
C=CC(CCCC=NCC1=CC=CC=C1)Cl
CN1C2=C(C=C(N(C2=O)O)C3=CC=CC=C3)C=N1
CSCCNC(=O)CSC1=CC=CC=C1
CC1=NNC(=S)N2C1=NC(=N2)C3=CC=CC=C3
C=C(CNC1=CC=CC=C1)C2=CC=C(C=C2)Cl
CC(=O)NNC=C1C(=O)OC(=N1)C2=CC=CC=C2
C=C1CCCN(C1=O)C(=O)OCC2=CC=CC=C2
C1=CC=C(C=C1)COC2=CC(=NC(=C2)CO)CO
CC1CC(=O)NC1C(=O)OC(=O)C2=CC=CC=C2")

;; IChemObjectBuilder     builder = SilentChemObjectBuilder.getInstance();
;; SmilesParser           sp      = new SmilesParser(builder);
;; SmilesGenerator        sg      = new SmilesGenerator();
;; InChIGeneratorFactory  igf     = InChIGeneratorFactory.getInstance();

;; IAtomContainer m = sp.parseSmiles("[O]");
;; System.out.println(sg.create(m));                         // [O]
;; System.out.println(igf.getInChIGenerator(m).getInchi());  // InChI=1S/O

;; // configure atom types
;; AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);
;; CDKHydrogenAdder.getInstance(builder).addImplicitHydrogens(m);

;; System.out.println(sg.create(m));                         // O ([OH2])
;; System.out.println(igf.getInChIGenerator(m).getInchi());  // InChI=1S/H2O/h1H2






(def ready-smiles (string/split-lines small-smiles))
(def dcob (. DefaultChemObjectBuilder (getInstance)))
(def sp (new SmilesParser dcob))

;; read in our SMILES
(println (concat (list "Got" (count ready-smiles) "smiles")))

;; parse them
(def mols (map (fn [x] (. sp (parseSmiles x))) ready-smiles))
(println (concat (list "Got " (count mols)
		       "molecules")))

;; lets do some stress testing
;; make a replicate method
(defn rep [x n]
  (if (= 1 n) (list x)
      (concat (list x) (rep x (- n 1)))))

;; replicate the SMILES list
;; (def smiles (reduce concat (for [x smiles] (rep x 100))))
;; (println (count smiles))

;; now parse this list
(def mols (for [x ready-smiles] (. sp (parseSmiles x))))

;; how long does this take?
(time (def mols (for [x smiles] (. sp (parseSmiles x)))))

(defn isaromatic? [mol] (some (fn [x]
				(. x (getFlag (. org.openscience.cdk.CDKConstants ISAROMATIC))))
			      (. mol atoms)))

(for [x mols]
  (println "hi"))

(def a (first ready-smiles))

(def aa (. sp (parseSmiles a)))
(map #(. sp (parseSmiles %)) ready-smiles)
(map string/upper-case ready-smiles)
(defn parse [smi] (. sp (parseSmiles smi)))
(map parse ready-smiles)

aa


(for [x mols]
  (do
    (println x)
    (println (isaromatic? x))))

(def pol (new org.openscience.cdk.charges.Polarizability))

(def fprinter (new MACCSFingerprinter))

(time (def junk (map (fn [x] (. fprinter (getFingerprint x))) mols)))
(time (def junk (pmap (fn [x] (. fprinter (getFingerprint x))) mols)))


;; (time (def junk (map (fn [x] (. CDKHueckelAromaticityDetector (detectAromaticity x))) mols)))
;; (time (def junk (pmap (fn [x] (. CDKHueckelAromaticityDetector (detectAromaticity x))) mols)))

;; (time (def junk (map (fn [x] (. pol (calculateKJMeanMolecularPolarizability x))) mols)))
;; (time (def junk (pmap (fn [x] (. pol (calculateKJMeanMolecularPolarizability x))) mols)))

;; (time (def junk (map (fn [x] (isaromatic? x)) mols)))
;; (time (def junk (pmap (fn [x] (isaromatic? x)) mols)))

(shutdown-agents)
;; see which mol was aromatic
;; (def aromatics (for [x mols :when (isaromatic? x)] (x)))



