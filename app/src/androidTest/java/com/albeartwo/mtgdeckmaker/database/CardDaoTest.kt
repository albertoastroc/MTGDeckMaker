package com.albeartwo.mtgdeckmaker.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.albeartwo.mtgdeckmaker.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Rule

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CardDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database : CardDatabase
    private lateinit var daoTest : CardDao

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext() ,
            CardDatabase::class.java
        ).allowMainThreadQueries().build()
        daoTest = database.getCardDatabaseDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertDeck() = run {
        runTest {

            val deck = Deck("testDeck")
            daoTest.insertDeck(deck)

            val allDecks = daoTest.getDecksList().getOrAwaitValue()
            assertThat(allDecks).contains(deck)
        }
    }

    @Test
    fun deleteDeck() = run {
        runTest {

            val deck = Deck( "testDeck")
            val deckId = daoTest.insertDeck(deck)
            daoTest.deleteDeckFromDeckTable(deckId.toInt())

            val allDecks = daoTest.getDecksList().getOrAwaitValue()
            assertThat(allDecks).doesNotContain(deck)
        }
    }

    @Test
    fun insertCard() = run {
        runTest {

            val card = Card(1, "oracle_id", "TestCard", "Does something", "Creature", "3", "4", "img_url", "{B}")

            val cardId = daoTest.insertCard(card)

            val deck = Deck( "testDeck")
            val deckId = daoTest.insertDeck(deck)

            val deckCardRelation = DeckCardCrossRef(deckId.toInt() , cardId , card.oracleId)

            daoTest.insertDeckCardCrossRef(deckCardRelation)

            val deckWithCards = daoTest.getCardsOfDeck(deckId.toInt()).getOrAwaitValue()
            val cardList = deckWithCards.first().cards
            assertThat(cardList).contains(card)
        }
    }

    @Test
    fun deleteCard() = run {
        runTest {

            val card = Card(1, "oracle_id", "TestCard", "Does something", "Creature", "3", "4", "img_url", "{B}")

            val cardId = daoTest.insertCard(card)

            val deck = Deck( "testDeck")
            val deckId = daoTest.insertDeck(deck)

            val deckCardRelation = DeckCardCrossRef(deckId.toInt() , cardId , card.oracleId)

            daoTest.insertDeckCardCrossRef(deckCardRelation)
            daoTest.removeFromDatabase(cardId.toInt())
            daoTest.deleteFromCrossRef(card.oracleId, deckId.toInt())

            val deckWithCards = daoTest.getCardsOfDeck(deckId.toInt()).getOrAwaitValue()
            val cardList = deckWithCards.first().cards
            assertThat(cardList).doesNotContain(card)

        }
    }

    @Test
    fun observeTotalDeckCardCount() = run {
        runTest {

            val card1 = Card(1, "oracle_id", "TestCard", "Does something", "Creature", "3", "4", "img_url", "{B}")

            val card2 = Card(1, "oracle_id", "TestCard", "Does something", "Creature", "3", "4", "img_url", "{B}")

            val card3 = Card(1, "oracle_id", "TestCard", "Does something", "Creature", "3", "4", "img_url", "{B}")

            val cardId1 = daoTest.insertCard(card1)
            val cardId2 = daoTest.insertCard(card2)
            val cardId3 = daoTest.insertCard(card3)

            val deck = Deck( "testDeck")
            val deckId = daoTest.insertDeck(deck)

            val deckCardRelation1 = DeckCardCrossRef(deckId.toInt() , cardId1 , card1.oracleId)
            val deckCardRelation2 = DeckCardCrossRef(deckId.toInt() , cardId2 , card2.oracleId)
            val deckCardRelation3 = DeckCardCrossRef(deckId.toInt() , cardId3 , card3.oracleId)

            daoTest.insertDeckCardCrossRef(deckCardRelation1)
            daoTest.insertDeckCardCrossRef(deckCardRelation2)
            daoTest.insertDeckCardCrossRef(deckCardRelation3)

            val deckWithCards = daoTest.getCardsOfDeck(deckId.toInt()).getOrAwaitValue()
            val cardList = deckWithCards.first().cards.size
            assertThat(cardList).isEqualTo(3)
        }
    }
}