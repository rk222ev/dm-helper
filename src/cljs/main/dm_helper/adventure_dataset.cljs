(ns dm-helper.adventure-dataset
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]
   [phrase.alpha :refer-macros [defphraser]]))

(def editions ["1" "AD&D" "3" "3.5" "4" "5"])

(def ^:private not-blank? (complement str/blank?))
(defn- valid-edition? [s] (boolean ((into #{} editions) s)))

(s/def ::title (s/and string? not-blank?))
(s/def ::summary (s/and string? not-blank?))
(s/def ::authors (s/coll-of string? :kind vector? :distinct true))
(s/def ::edition valid-edition?)
(s/def ::enemies (s/coll-of string? :kind vector? :distinct true))
(s/def ::environment #())
(s/def ::found-in #())
(s/def ::min-level #())
(s/def ::max-level #())
(s/def ::min-characters #())
(s/def ::max-characters #())
(s/def ::format #())
(s/def ::pages #())
(s/def ::publisher #())
(s/def ::setting #())
(s/def ::storyline #())

(s/explain-data ::title "")

(defphraser not-blank?
  [_ _ _]
  "required")

(s/def :dm-helper/adventure
  (s/keys :req-un [::title]
          :opt-un [::authors ::edition ::enemies]))

(def empty-adventure
  (into (sorted-map)
        {
         ::authors []
         ::edition ""
         ::enemies []
         ::environment ""
         ::format ""
         ::found-in ""
         ::handouts false
         ::items []
         ::max-characters nil
         ::max-level nil
         ::min-characters nil
         ::min-level nil
         ::pages nil
         ::published ""
         ::publisher ""
         ::setting ""
         ::storyline ""
         ::summary ""
         ::tactical-maps false
         ::title ""
         ::villains []
         }))

(def ^:private sort-by-keys (partial into (sorted-map)))
(def ^:private add-all-keys (partial merge empty-adventure))

(def
  data
  (map (comp sort-by-keys add-all-keys)
       [
        {::storyline "Starter Set"
         ::edition "5"
         ::enemies ["goblin" "drow" "bugbear" "hobgoblin" "dragon"]
         ::max-level 5
         ::min-level 1
         ::pages 64
         ::publisher "Wizard of the Coast"
         ::setting "Forgotten Realms"
         ::title "Lost Mines of Phandelver"
         ::published "2014"}

        {::authors ["Wolfgang Baurand" "Steve Winter"]
         ::storyline "Tyranny of Dragons"
         ::title "Hoard of the Dragon Queen"
         ::edition "5"
         ::min-level 1
         ::max-level 7
         ::min-characters 4
         ::setting "Forgotten Realms"
         ::summary "In an audacious bid for power, the Cult of the Dragon, along with its dragon allies and the Red Wizards of Thay, seek to bring Tiamat from her prison in the Nine Hells to Faerûn. To this end, they are sweeping from town to town, laying waste to all those who oppose them and gathering a hoard of riches for their dread queen. The threat of annihilation has become so dire that groups as disparate as the Harpers and Zhentarim are banding together in the fight against the cult. Never before has the need for heroes been so desperate."
         ::publisher "Wizard of the Coast"
         ::published "2014"}

        {::authors ["Wizards RPG Team"]
         ::storyline "Tyranny of Dragons"
         ::title "The Rise of Tiamat"
         ::edition "5"
         ::min-level 8
         ::max-level 15
         ::setting "Forgotten Realms"
         ::summary "The Cult of the Dragon leads the charge in an unholy crusade to bring Tiamat back to the Realms, and the situation grows more perilous for good people with each passing moment. The battle becomes increasingly political as opportunities to gather allies and gain advantage present themselves. From Waterdeep to the Sea of Moving Ice to Thay, it is a race against Evil. Succeed or succumb to the oppression of draconic tyranny. Win or lose, things will never be the same again."
         ::publisher "Wizard of the Coast"
         ::published "2014"}

        {::authors ["Wizards RPG Team"]
         ::storyline "Elemental Evil"
         ::title "Princes of the Apocalypse"
         ::edition "5"
         ::min-level 1
         ::max-level 15
         ::setting "Forgotten Realms"
         ::summary "Called by the Elder Elemental Eye to serve, four corrupt prophets have risen from the depths of anonymity to claim mighty weapons with direct links to the power of the elemental princes. Each of these prophets has assembled a cadre of cultists and creatures to serve them in the construction of four elemental temples of lethal design. It is up to adventurers from heroic factions such as the Emerald Enclave and the Order of the Gauntlet to discover where the true power of each prophet lay, and dismantle it before it comes boiling up to obliterate the Realms."
         ::publisher "Wizard of the Coast"
         ::published "2015"}

        {::authors ["Wizards RPG Team"]
         ::storyline "Rage of Demons"
         ::title "Out of the Abyss"
         ::edition "5"
         ::min-level 1
         ::max-level 15
         ::setting "Forgotten Realms"
         ::summary "The Underdark is a subterranean wonderland, a vast and twisted labyrinth where fear reigns. It is the home of horrific monsters that have never seen the light of day. It is here that the dark elf Gromph Baenre, Archmage of Menzoberranzan, casts a foul spell meant to ignite a magical energy that suffuses the Underdark and tears open portals to the demonic Abyss. What steps through surprises even him, and from that moment on, the insanity that pervades the Underdark escalates and threatens to shake the Forgotten Realms to its foundations. Stop the madness before it consumes you! "
         ::publisher "Wizard of the Coast"
         ::published "2015"}

        {::authors ["Chris Perkins"]
         ::storyline "Curse of Strahd"
         ::title "Curse of Strahd"
         ::edition "5"
         ::min-level 1
         ::max-level 10
         ::setting "Forgotten Realms"
         ::summary "Under raging storm clouds, the vampire Count Strahd von Zarovich stands silhouetted against the ancient walls of Castle Ravenloft. Rumbling thunder pounds the castle spires. The wind’s howling increases as he turns his gaze down toward the village of Barovia. A lightning flash rips through the darkness, but Strahd is gone. Only the howling of the wind fills the midnight air. The master of Castle Ravenloft is having guests for dinner—and you are invited. "
         ::publisher "Wizard of the Coast"
         ::published "2016"}

        {::storyline "Storm King's Thunder"
         ::title "Storm King's Thunder"
         ::min-level 1
         ::edition "5"
         ::max-level 11
         ::setting "Forgotten Realms"
         ::publisher "Wizard of the Coast"
         ::published "2016"}

        {::title "Tales from the Yawning Portal"
         ::setting "Forgotten Realms"
         ::summary "When the shadows grow long in Waterdeep and the fireplace in the taproom of the Yawning Portal dims to a deep crimson glow, adventurers from across the Forgotten Realms, and even from other worlds, spin tales and spread rumors of dark dungeons and lost treasures. Some of the yarns overheard by Durnan, the barkeep of the Yawning Portal, are inspired by places and events in far-flung lands from across the D&D multiverse, and these tales have been collected into a single volume.

Within this tome are seven of the most compelling dungeons from the 40+ year history of Dungeons & Dragons. Some are classics that have hosted an untold number of adventurers, while others are some of the most popular adventures ever printed.

The seeds of these stories now rest in your hands. D&D’s most storied dungeons are now part of your modern repertoire of adventures. Enjoy, and remember to keep a few spare character sheets handy."
         ::edition "5"
         ::publisher "Wizard of the Coast"
         ::published "2017"}

        {::authors ["Douglas Niles"]
         ::edition "3.5"
         ::enemies ["trogdolyte"]
         ::environment ""
         ::handouts false
         ::max-level 3
         ::min-level 1
         ::min-characters 4
         ::max-characters 7
         ::pages 32
         ::publisher "TSR"
         ::published "1995"
         ::setting "Greyhawk"
         ::summary "Terror by night! The village of Orlane Is dying. Once a small and thriving community. Orlane has become a maze of locked doors and frightened faces. Strangers are shunned, trade has withered. Rumors flourish, growing wilder with each retelling. Terrified peasants flee their homes, abandoning their farms with no explanation. Others simply disappear... No one seems to know the cause of the decay —why are there no clues? Who skulks through the twisted shadows of the night? Who or what Is behind the doom that has overtaken the village? It will take a brave and skillful band of adventurers to solve the dark riddle of Orlane! This module is designed for 4-7 characters of first through third levels. It Includes a map of the village and a description of its buildings and occupants, an overland journey to a challenging underground adventure for especially brave (or foolhardy...) characters, and a list of pre-rolled first level characters."
         ::tactical-maps false
         ::title "Against the Cult of the Reptile God"}

        {::authors ["Ed Greenwood" "Matt Sernett" "Steve Winter"]
         ::storyline "The Sundering"
         ::setting "Forgotten Realms"
         ::summary "Wealth flows into the city of Baldur’s Gate like water. As the rich luxuriate in their mansions atop the bluff and artisans ply their trades on the steep streets, masses of poor laborers swell the slums. Money and power beget political scandal, religious fervor, crime . . . and murder. No one feels safe on the rain-darkened streets. Strange, foreign gods are beseeched in secret shrines. The city is rife with corruption. And through it all, the body count keeps rising."
         ::title "Murder In Baldur's Gate"}

        {::authors ["R. A. Salvatore" "James Wyatt" "Jeffrey Ludwig"]
         ::edition "3.5"
         ::setting "Forgotten Realms"
         ::storyline "The Sundering"
         ::summary "The people of Icewind Dale have long stood against the perils of the North. For most of these folk, the events that shook the region a hundred years ago are now distant memories. But what was defeated was not destroyed, and the sinister influence of the Crystal Shard, Crenshinibon, has now wormed its way into the very land of Icewind Dale. As evil forces converge on Ten Towns, the people of the North face their greatest trial yet. Fortunately, they won’t have to face it alone.

Legacy of the Crystal Shard allows characters to continue to participate in important events connected to the Sundering and glimpse the future of the Forgotten Realms."
         ::title "Legacy of the Crystal Shard"
         ::villains []}

        {::summary "Ghosts of Dragonspear Castle is a D&D Next preview and mini-campaign comprised of four thrilling adventures designed to advance characters from 1st level to 10th level. The book also contains everything a Dungeon Master needs to run the adventures, including D&D Next game rules developed during the massive public playtest, monster statistics, spell descriptions, magic item descriptions, and background information on the coastal town of Daggerford, where the campaign is based.

Against the backdrop of the Sundering, brave adventurers must protect the town of Daggerford against an insidious foreign threat while forging alliances, exploring dungeons, and battling monsters. The action moves from the Lizard Marsh to the orc-infested hills, finally culminating in a deadly altercation amid the crumbling ruins of the legendary Dragonspear Castle."
         ::max-level 10
         ::min-level 1
         ::tactical-maps false
         ::title "Ghosts of Dragonspear Castle"}

        {::title "Blood of Gruumsh"
         ::authors ["Steve Winter"]
         ::edition "4"
         ::max-level 6
         ::min-level 4
         ::pages 17
         ::publisher "Dungeon #210"
         ::summary "An elf cleric hires the party to investigate a long-lost religious colony in the woods. Exploring the colony the party discovers that it belonged to a peaceful sect of elves and orcs worshipping a mythical orc-elf demigod. Its inhabitants were massacred in a raid by elf zealots from the cleric's order. The party is surprised by an orc scouting band during their exploration."
         }

        {::title "The Crucible of Freya"
         ::authors ["Clark Peterson" "Bill Webb"]
         ::edition "3"
         ::min-level 1
         ::max-level 2
         ::publisher "Necromancer Games"
         ::summary "The first in a new series of D20 adventures by Necromancer Games The Crucible of Freya is a challenging introductory adventure for any fantasy campaign. Detailing the village of Fairhill the surrounding wilderness and a nearby ruined keep as well as the dungeon levels beneath this adventure module provides all the material a DM needs to start his new 3rd-Edition campaign. Will your characters learn what dark forces are behind the orc raids?"}

        {::title "Dead by Dawn"
         ::authors ["Aeryn Rudel"]
         ::edition "4"
         ::min-level 2
         ::pages 12
         ::publisher "Dungeon #176"
         ::summary "As the party discovers an abandoned temple just beyond the King's Wall a shard of a great evil meteor causes the dead to rise from their graves. The party has to clear the temple first then defend it against two successive waves of undead besiegers over the course of a night. If they survive they have to decide what to do with the evil shard: destroy it with a god's help or keep it despite the consequences."}

        {::title "Fall of the Gray Veil"
         ::authors ["Chris Perkins"]
         ::edition "4"
         ::min-level 2
         ::max-level 4
         ::pages 37
         ::publisher "Dungeon #221"
         ::summary "The restless spirit of a blind boy has drained the area around Baron's Hill of all color. The party finds out that a necromancer is turning the villagers' remains into powder and brewing potions of reanimation. They have to defeat him then chase down the goblin who has the boy's essence flowing through his veins."}

        {::title "Heathen"
         ::authors ["Scott Fitzgerald Grey"]
         ::edition "4"
         ::min-level 5
         ::pages "30"
         ::publisher "Dungeon #155"
         ::summary "The party investigates the fate of the paladin Jaryn and his team once sent to destroy the Hand of Naarash death cult. The cult has been terrorizing the frontier lands left defenceless after the recent fall of the empire. The party investigates rumours repels multiple cult attacks and assassins and takes a river journey by boat to a mountainous temple. There they face off against the corrupted Jaryn and his master the elemental demon Naarash."}

        {::title "Keep On the Borderlands"
         ::authors ["Gary Gygax"]
         ::edition "1"
         ::max-level 3
         ::min-level 1
         ::publisher "TSR"
         ::summary "Player characters begin by arriving at the eponymous keep and can base themselves there before investigating the series of caverns in the nearby hills teeming with monsters. These Caves of Chaos house multiple species of vicious humanoids. Plot twists include a treacherous priest within the keep hungry lizardmen in a nearby swamp and a mad hermit in the wilderness. It typifies the dungeon crawls associated with beginning D&D players while permitting some limited outdoor adventures."
         }


        {::title "Keep on the Shadowfell"
         ::authors ["Bruce Cordell" "Mike Mearls"]
         ::edition "4"
         ::min-level 1
         ::max-level 3
         ::publisher "Wizards of the Coast"
         ::summary "Kobold raiders are menacing the Nentir Vale village of Winterhaven. They attack the player characters who are also traveling to Winterhaven. When the player characters arrive at Winterhaven they are asked to clean out the kobold nest. The player characters soon discover that the kobolds are pawns of Kalarel a priest of Orcus Demon Prince of Undeath. Kalarel has a lair at a nearby ruined keep that contains the Shadow Rift once a gateway to the Shadowfell and is no longer in use. Kalarel plans to reopen the Shadow Rift to connect the material world to Orcus's temple in the Shadowfell. This unleashes an army of undead upon the unsuspecting region. The player characters journey to the keep and descend through its crypts resulting in a final climactic confrontation with Kalarel."}

        {::title "Return to the Temple of Elemental Evil"
         ::authors ["Bruce Cordell" "Mike Mearls"]
         ::min-level 1
         ::max-level 3
         ::publisher "Wizards of the Coast"
         ::summary "Years ago brave heroes put the denizens of the Temple of Elemental Evil to the sword. Now dark forces whisper again in the shadows of the once-deserted temple - forces far more insidious and dangerous than any sane person could dream. Evil has risen again to threaten the village of Hommlet. Characters battle the power of darkness in Hommlet and beyond forging their way through hundreds of dire encounters before reaching the fiery finale. Designed as the backbone of a full campaign Return to the Temple of Elemental Evil takes characters from 4th to as high as 14th level. This deluxe adventure builds on the groundwork of the original Temple of Elemental Evil (1985) as well as other classic adventures. However none of those products are necessary to enjoy this one."}

        {::title "The Village of Hommlet"
         ::authors ["Gary Gygax"]
         ::edition "1"
         ::min-level 1
         ::publisher "Necromancer Games"
         ::summary "The Village of Hommlet has grown up around a crossroads in a woodland. Once far from any important activity it became embroiled in the struggle between gods and demons when the Temple of Elemental Evil arose but a few leagues away. Luckily of its inhabitants the Temple and its evil hordes were destroyed a decade ago but Hommlet still suffers from incursions of bandits and strange monsters."
         }


        ]))
