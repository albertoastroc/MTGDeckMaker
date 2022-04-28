package com.albeartwo.mtgdeckmaker.other

import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.generated.Data

class UtilityClass {

    companion object {

        fun convertDataToCard(cardData :Data) : Card {

            val newCard = Card().apply {

                this.oracleId = cardData.oracle_id
                this.cardName = cardData.name.toString()
                this.oracleText = cardData.oracle_text.toString()
                this.power = cardData.power.toString()
                this.toughness = cardData.toughness.toString()
                this.typeLine = cardData.type_line.toString()
                this.thumbnailUrl = cardData.image_uris?.art_crop.toString()
                this.producedMana = cardData.produced_mana.toString()
            }

            return newCard
        }
    }

}