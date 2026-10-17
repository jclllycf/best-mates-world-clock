# Best Mates

**Langues :** [English](../README.md) · [中文](README_zh-CN.md) · [Français](README_fr.md) · [Deutsch](README_de.md) · [Русский](README_ru.md) · [العربية](README_ar.md) · [Türkçe](README_tr.md)

Best Mates est un éditeur de widgets d'horloge mondiale pour Android, conçu pour les personnes qui souhaitent des widgets d'écran d'accueil esthétiques, tactiles et personnalisés, sans avoir à maîtriser des créateurs de widgets complexes basés sur des formules.

> **Statut : jalon final v0.5.0 — Développement actif terminé.** Best Mates v0.5.0 représente la version consolidée et acceptée pour cette étape de développement. Le développement actif de fonctionnalités est désormais clos, et le projet passe en maintenance communautaire.

---

## Galerie visuelle

| Widgets sur écran d'accueil | Éditeur sur toile (4×2) |
| :---: | :---: |
| ![Widgets sur écran d'accueil](../docs/review/2026-10-15/after-home-widgets.jpg) | ![Éditeur sur toile](../docs/review/2026-10-15/after-editor-4x2-selected.jpg) |

| Sélecteur céleste | Bibliothèque de formes |
| :---: | :---: |
| ![Sélecteur céleste](../docs/review/2026-10-15/after-sky-picker.jpg) | ![Bibliothèque de formes](../docs/review/2026-10-15/after-shape-gallery.jpg) |

| Styles de thème (Daybook / Rosewater / Nocturne / Studio) | Localisation arabe avec disposition RTL |
| :---: | :---: |
| ![Styles de thème](../docs/review/2026-10-15/styles-workspace.jpg) | ![Disposition arabe RTL](../docs/review/2026-10-15/arabic-rtl.jpg) |

---

## Fonctionnalités disponibles

- **Gestion multi-projets :** Créez, dupliquez, renommez et organisez plusieurs configurations de widgets indépendantes dans la galerie Workspace.
- **Éditeur proportionnel à la grille :** Toile de conception dimensionnée précisément selon les proportions cibles du widget (1×1, 2×1, 2×2, 3×3, 4×1, 4×2, 4×3, 4×4).
- **Magnétisme intelligent & sélection par calque :** Guides magnétiques précis (bords, centre, tiers, quarts, calques frères) et sélection directe en un clic des villes ou textes superposés aux formes.
- **Ensemble céleste Daybook :** 29 icônes originales et géométriques de soleil et de lune, calculées dynamiquement selon les coordonnées géographiques réelles et le cycle jour/nuit local.
- **Bibliothèque de formes & finitions :** 25 formes géométriques réparties en quatre familles (dont 13 nouveaux ajouts en v0.5.0), avec finitions Mist (verre dépoli), Glass et Solid.
- **Typographie :** 21 polices open source incluses (Fraunces, Figtree, Bricolage Grotesque, JetBrains Mono, Baloo 2, etc.) ainsi que 14 styles de polices système en temps réel.
- **Quatre identités visuelles complètes :** Thèmes Daybook, Rosewater, Nocturne et Studio avec modes clair, sombre et système.
- **Recherche de villes hors ligne & en ligne :** Accès instantané hors ligne via la base de données intégrée GeoNames, complété par la recherche en ligne via Open-Meteo Geocoding.
- **Localisation en sept langues :** Interface complète en anglais, chinois simplifié, français, allemand, russe, arabe (avec disposition native de droite à gauche) et turc.
- **Précision sur l'écran d'accueil :** Rendu pleine surface sans bandes noires ni débordement d'ombre sur les lanceurs Android récents (y compris Samsung One UI).
- **Respect de la vie privée :** Données stockées exclusivement en local sur l'appareil. Aucun compte utilisateur, aucun suivi analytique, aucune publicité et aucune permission de localisation requise.

*Remarque sur les limites de fonctionnalités :* Cette version est strictement dédiée à l'horloge mondiale et à la typographie. Les prévisions météorologiques en direct, les aiguilles d'horloge analogique et la synchronisation cloud / import-export de fichiers sont des concepts exploratoires non inclus dans la v0.5.0.

---

## Téléchargement et avertissement important

Téléchargez **`BestMates-v0.5.0-debug.apk`** depuis la page [Releases](../../releases) du dépôt GitHub.

- **Nom du fichier :** `BestMates-v0.5.0-debug.apk`
- **Taille :** ~35,0 Mo (35 066 983 octets)
- **Empreinte SHA-256 :** `7BE893961D26D839D7CF7C2B0093825D0F008AE785462947BC7F595BBF2C4B03`

> [!WARNING]
> **Avertissement crucial sur la signature et la perte de données :**
> - Cette version est signée avec un certificat de test (Debug). Le système de sécurité d'Android bloque toute mise à jour directe si la clé de signature ne correspond pas exactement à celle de la version déjà installée (comme les préversions v0.1.0 ou v0.2.0).
> - **NE DÉSINSTALLEZ PAS Best Mates si vous devez conserver vos projets et widgets existants.**
> - Best Mates stocke l'intégralité des configurations localement dans l'espace privé de l'application, et ne dispose pas actuellement d'une fonctionnalité complète d'exportation/sauvegarde. Désinstaller l'application supprimera définitivement tous vos projets locaux.
> - Si vous utilisez déjà une ancienne version et tenez à vos configurations, conservez votre installation actuelle en attendant un outil de sauvegarde ou de migration validé.
> - Pour les nouveaux utilisateurs ou les nouvelles configurations sans données antérieures, l'APK v0.5.0 peut être installé directement.

---

## Politique de maintenance future

Le développement actif de Best Mates est officiellement clôturé avec le jalon v0.5.0 :
- Aucune nouvelle fonctionnalité n'est programmée.
- Aucune refonte visuelle n'est prévue.
- Aucune restructuration architecturale inutile ne sera effectuée.
- Les retours de la communauté restent ouverts via les [GitHub Issues](../../issues).
- Les mises à jour futures seront envisagées uniquement au cas par cas pour corriger d'éventuels bugs critiques, des failles de sécurité ou garantir la compatibilité avec les futures versions d'Android.

---

## Attribution et licences

- **Code source :** Sous licence [Apache 2.0](LICENSE).
- **Données géographiques :** Données dérivées de [GeoNames](https://www.geonames.org/) sous licence [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/).
- **Géocodage en ligne :** Fourni par [Open-Meteo Geocoding API](https://open-meteo.com/en/docs/geocoding-api).
- **Polices intégrées :** Sous licence [SIL Open Font License 1.1](http://scripts.sil.org/OFL) ou Apache 2.0.
- **Icônes tierces :** Artworks vectoriels open source provenant de Lucide, Tabler, Phosphor, Fluent UI, Iconoir, Material Design et Meteocons (voir [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md)).

Copyright 2026 Les contributeurs de Best Mates.
