# Échos de l'Antiquité

[🇬🇧 Read the README in English](./README.md)

---

**Auteur :** FROSTYTRIX  
**Plateforme :** Fabric 1.21

---

## 🛠 Matériaux & Ingrédients

### 🌑 Lingot d'Acier de l'End 🟣
* Un matériau de base pour l'artisanat avancé.
* **Recette :** Fabrication sans forme avec 1 Lingot de fer, 1 Fruit de chorus éclaté et 1 Perle de l'Ender.

### 🖤 Cuir Traité par le Néant 🟣
* Du cuir imprégné des énergies de l'End.
* **Recette :** 1 Cuir et 4 Fruits de chorus disposés en forme de croix.

### 📜 Écrit Ancien ⚪
* Un matériau ancien utilisé dans l'artisanat.

---

## ⚔️ Armement

### 🏹 Arc de Dragon 🟣
* Une arme puissante à longue portée.
* Tire avec **deux fois la vélocité** de l'arc classique et est **plus précis**.

### 🗡 Lame de Relique 🟣
* Une lame tranchante forgée à partir d'Acier de l'End pur.
* **Recette :** 3 Lingots d'Acier de l'End disposés en colonne verticale.

### 🗡️ Espadon de Relique 🟣
* Une épée massive avec des animations personnalisées pour la tenue et l'attaque.
* **Recette :** Placez une **Lame de Relique** (en haut), un **Bâton** (au milieu) et du **Cuir Traité par le Néant** (en bas) dans une colonne verticale.
* Possède **8,5** de Dégâts d'attaque, mais avec une Vitesse d'attaque de **0,6** (plus bas = plus lent).

### 🗡️ Siphon d'Âmes 🔵
* A les **mêmes** Dégâts d'attaque qu'une **Épée en pierre**, mais avec une Vitesse d'attaque de **2,6**.
* Les entités tuées par le **Siphon d'Âmes** lâcheront un **Fragment d'Âme**.

---

## ⬛ Blocs

### 👣 Bottes de l'Ender 🟣
* Confère de la furtivité en arrêtant les signaux de vibration (les Capteurs sculk ne vous détecteront pas).
* **Recette :** Combinez des Bottes en cuir avec du Cuir Traité par le Néant dans une Table de forgeron.

### 🔮 Piédestal du Néant 🟣
* Permet au joueur d'exposer un objet.
* Empêche la téléportation dans un **rayon de 20 blocs** lorsqu'il est actif.
* **Activation :** Placez un **Œil de l'Ender** à l'intérieur.
* **Visuels :** L'Œil tourne et suit les joueurs à proximité.
* **Effet :** Applique le tag `void_pedestal_suppressed` aux entités à portée.

### ⚓ Ancre du Vide 🟣
* Utilisée comme une "ancre" pour la **Perle Statique**.
* Faites un clic droit sur l'Ancre du Vide avec une **teinture magenta** pour changer sa texture. Vous pouvez **laver** la teinture avec un seau d'eau.

### 🪚 Décrafteur ⚪
* Prend des objets et blocs en entrée, et donne un ingrédient **aléatoire** issu de ses recettes de fabrication **et** de cuisson.
* Ne produira **pas** de blocs/objets permettant une trop grande **duplication** (ex: pas de bloc de fer à partir d'un lingot).

### 🪨 Pierre de Téléportation (Waystone) ⚪
* Un point de repère pour la téléportation.
* **Activation :** Faites un clic droit avec **10 Fragments d'Âme** pour l'activer.
* Enregistre le joueur qui l'a placée/activée et le téléportera a la position de la Pierre si sa vie tombe sous 2,5 cœurs.

### 🧲 Ancre de Gravité ⚪
* **Activation :** Alimentée par la Redstone.
* **Effet :** Lorsqu'elle est active, projette un champ d'énergie dans un **rayon de 10 blocs** qui fige les blocs soumis à la gravité (comme le Sable ou le Gravier) en plein vol.

---

## Objets

### 🔘 Perle Statique 🟣
* **Mécaniques :** Utilisez-la une fois sur une Ancre du Vide pour la lier à **cette** Perle Statique, utilisez-la à nouveau pour vous y téléporter instantanément.
* **Shift + Clic droit** sur une nouvelle Ancre du Vide pour modifier celle à laquelle la Perle est liée.
* Peut être utilisée **20 fois**.

### 🧿 Fragments d'Âme 🔵
* Utilisés pour activer les **Pierres de Téléportation** (Coût : 10 fragments).

### 🕷️ Patte d'Araignée Grimpante ⚪
* Permet à tout joueur tenant cet objet (main principale ou secondaire) de grimper aux murs comme une araignée.
* A 0,5 % de chances d'être lâchée par les araignées venimeuses.

### 📏 Mètre Ruban ⚪
* Faites un clic droit sur un bloc pour définir sa position comme point de départ de la mesure.
* Faites **Shift + Clic droit** sur un bloc pour définir sa position comme point d'arrivée de la mesure.
* Faites **Shift + Clic droit** en l'air pour changer la méthode de calcul de la distance.
* Possède 2 méthodes de calcul de distance :
  * Distance vectorielle : Calcule la norme du vecteur reliant les deux blocs.
  * Distance de Manhattan : Calcule la distance entre les deux blocs tout en restant sur la grille des blocs.

### 🪣 Seau d'Eau Infini ⚪
* Place de l'eau comme un seau normal.
* **Mécaniques :** Faites **Shift + Clic droit** directement sur un bloc de fluide pour le drainer/absorber.

### 📐 Niveau (Niveau d'Architecte) ⚪
* **Mécaniques :** Faites un clic droit sur un bloc pour aplanir instantanément une **zone de 3x3** autour de lui.
* Il rasera automatiquement jusqu'à 2 blocs au-dessus du niveau ciblé.
* Il comblera automatiquement les trous en utilisant la **Terre** (Dirt) de votre inventaire.

### 🥽 Lunettes d'Obsidienne 🟣
* Fabriquées avec de l'Obsidienne, des Vitres et une Perle Statique.
* Permet au joueur **de regarder les Enderman** sans les énerver. Contrairement à la **Citrouille sculptée**, cela **ne couvrira pas** partiellement votre vue.

---

## ✨ Potions & Effets 🥛

### 👻 Déphasage 🥛
* **Effet :** Permet de traverser des murs d'une **épaisseur maximale de 1 bloc**.
* **Alambic :** Fruit de chorus éclaté dans une Potion étrange.
* **Note :** Cet effet est désactivé près d'une Ancre du Vide active.

### 📏 Bras Longs 🥛
* **Effet :** Augmente la portée d'interaction de **+2,0 blocs**.
* **Alambic :** Fruit de chorus éclaté dans une Potion de Déphasage.

---

## 📦 Onglet Créatif

* Retrouvez tous les objets et blocs annotés avec "🟣" dans l'onglet **"Les Humains Déchus"**.
* Retrouvez tous les objets et blocs annotés avec "🔵" dans l'onglet **"Les Clones Ratés"**.
* Retrouvez tous les objets et blocs annotés avec "⚪" dans l'onglet **"Les Outils de l'Architecte"**.
* Retrouvez tous les objets annotés avec "🥛" dans l'onglet classique **"Food & Drinks"** (Nourriture et Boissons).