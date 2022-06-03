package com.albeartwo.mtgdeckmaker.other

import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.generated.Data
import timber.log.Timber

class UtilityClass {

    companion object {

        fun convertDataToCard(cardData : Data) : Card {

            val newCard = Card().apply {

                this.oracleId = cardData.oracle_id
                this.cardName = cardData.name.toString()
                this.oracleText = cardData.oracle_text.toString()
                this.power = cardData.power.toString()
                this.toughness = cardData.toughness.toString()
                this.typeLine = cardData.type_line.toString()
                this.thumbnailUrl = cardData.image_uris?.art_crop.toString()
                this.producedMana = cardData.produced_mana.toString()
                this.loyalty = cardData.loyalty.toString()
            }

            return newCard
        }

        //creating two lists so that symbols go in the right order
        fun getCmcArray(manaString : String) : List<String> {

            Timber.d("1.1 $manaString")

            var string = manaString

            val list = mutableListOf<String>()

            val splitSymbolList = mutableListOf<String>()

            if (string.contains("/")) {

                val slashIndex = string.indexOf("/")

                if ((string[slashIndex + 1]) == '/' || (string[slashIndex - 1]) == '/') {
                    return list
                    //handle split cards
                } else {

                    while (string.contains("/")) {

                        val splitSymbol = extractSplitMana(string)
                        splitSymbolList.add(splitSymbol)
                        string = string.replaceFirst(splitSymbol , "")

                    }

                    Timber.d("1.2 $list")
                }
            }

            list.addAll(extractSimpleMana(string))
            list.addAll(splitSymbolList)

            return list
        }

        fun extractSplitMana(sample : String) : String {

            Timber.d("2.1 $sample")

            val regex = Regex("[^0-9a-zA-Z/]+")

            val symbols = regex.replace(sample , "")

            var splitSymbol = ""

            if (symbols.contains("/")) {

                val slashIndex = symbols.indexOf("/")

                if ((symbols[slashIndex + 1]) == '/' || (symbols[slashIndex - 1]) == '/') {
                    //handle split cards
                    return splitSymbol
                } else {

                    splitSymbol += symbols[slashIndex - 1]
                    splitSymbol += "/"
                    splitSymbol += symbols[slashIndex + 1]
                }
            }

            Timber.d("2.2 $splitSymbol")

            return splitSymbol
        }

        fun extractSimpleMana(sample : String) : List<String> {

            Timber.d("3.1 $sample")

            val regex = Regex("[^0-9a-zA-Z]+")

            val symbols = regex.replace(sample , "")

            val simpleList = mutableListOf<String>()

            for (i in symbols) {

                simpleList.add(i.toString())
            }

            Timber.d("3.2 $simpleList")

            return simpleList
        }
    }
}