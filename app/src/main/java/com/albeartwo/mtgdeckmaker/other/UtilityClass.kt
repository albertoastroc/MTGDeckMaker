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

        fun getCmcArray(manaString : String) : List<String> {

            var string = manaString

            val list = mutableListOf<String>()

            while (string.contains("/")) {

                val splitSymbol = extractSplitSymbols(string)
                list.add(splitSymbol)
                string = string.replace(splitSymbol, "")

            }

            list.addAll(extractSimpleMana(string))
            return list
        }

        fun extractSplitSymbols(sample : String) : String{

            val regex = Regex("[^0-9a-zA-Z/]+")

            val symbols = regex.replace(sample, "")

            var splitSymbol = ""

            if (symbols.contains("/")) {

                val slashIndex = symbols.indexOf("/")

                splitSymbol += symbols[slashIndex-1]
                splitSymbol += "/"
                splitSymbol += symbols[slashIndex+1]
            }

            return splitSymbol
        }

        fun extractSimpleMana(sample : String) : List<String> {

            val regex = Regex("[^0-9a-zA-Z]+")

            val symbols = regex.replace(sample, "")

            val simpleList = mutableListOf<String>()

            for (i in symbols) {

                simpleList.add(i.toString())
            }

            return simpleList
        }
    }
}