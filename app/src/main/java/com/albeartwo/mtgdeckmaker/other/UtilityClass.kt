package com.albeartwo.mtgdeckmaker.other

import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.generated.Data

class UtilityClass() {

    companion object {

        fun convertDataToCard(cardData : Resource<Data>) : Card {

            val cardProperties = cardData.data

            val newCard = Card()

            if (cardProperties != null) {

                newCard.oracleId = cardProperties.oracle_id
                newCard.cardName = cardProperties.name.toString()
                newCard.oracleText = cardProperties.oracle_text.toString()
                newCard.power = cardProperties.power.toString()
                newCard.toughness = cardProperties.toughness.toString()
                newCard.typeLine = cardProperties.type_line.toString()
                newCard.thumbnailUrl = cardProperties.image_uris?.art_crop.toString()
                newCard.producedMana = cardProperties.produced_mana.toString()

            }

            return newCard
        }
    }

}