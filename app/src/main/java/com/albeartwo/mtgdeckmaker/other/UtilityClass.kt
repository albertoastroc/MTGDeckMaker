package com.albeartwo.mtgdeckmaker.other

import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.generated.Data

class UtilityClass() {

    companion object {

        fun convertDataToCard(data : Data) : Card {

            val oracleId = data.oracle_id

            val newCard = Card(0 , oracleId , 1)
            newCard.cardName = data.name.toString()
            newCard.oracleText = data.oracle_text.toString()
            newCard.power = data.power.toString()
            newCard.toughness = data.toughness.toString()
            newCard.typeLine = data.type_line.toString()
            newCard.thumbnailUrl = data.image_uris?.art_crop.toString()

            return newCard
        }
    }

}