package com.albeartwo.mtgdeckmaker.other

import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.generated.Data

class UtilityClass {

    companion object {

        fun convertDataToCard(cardData :Data) : Card {

            val newCard = Card()

            newCard.oracleId = cardData.oracle_id
            newCard.cardName = cardData.name.toString()
            newCard.oracleText = cardData.oracle_text.toString()
            newCard.power = cardData.power.toString()
            newCard.toughness = cardData.toughness.toString()
            newCard.typeLine = cardData.type_line.toString()
            newCard.thumbnailUrl = cardData.image_uris?.art_crop.toString()
            newCard.producedMana = cardData.produced_mana.toString()

            return newCard
        }
    }

}