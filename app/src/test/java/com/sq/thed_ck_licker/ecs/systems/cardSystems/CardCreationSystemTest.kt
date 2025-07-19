package com.sq.thed_ck_licker.ecs.systems.cardSystems

import androidx.compose.runtime.mutableIntStateOf
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.OnDeactivation
import com.sq.thed_ck_licker.ecs.components.effectthing.OnPlay
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.FixedRandom
import com.sq.thed_ck_licker.helpers.MyRandom
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator_Factory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.properties.Delegates
import kotlin.test.assertEquals

class CardCreationSystemTest {
    var cardCreationSystem by Delegates.notNull<CardCreationSystem>()
    var owner by Delegates.notNull<EntityId>()
    @BeforeEach
    fun setUp() {
        owner = EntityManager.getPlayerID()
        cardCreationSystem = CardCreationSystem(
            gameNavigator = GameNavigator_Factory.newInstance()
        )
    }

    @Test
    fun addBreakingDefaultCards() {
        val breakingCard = cardCreationSystem.addBreakingDefaultCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(breakingCard))
        val scoreComponent = ScoreComponent(100)
        owner add scoreComponent

        val context = EffectContext(
            trigger = OnPlay,
            source = breakingCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(scoreComponent.getScore() == 200) { "Score should be 200, but was ${scoreComponent.getScore()}" }
        val realDesc = "OnPlay:\nGain (100.0) points"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun addTimeBoundTestCards() {
    }

    @Test
    fun addDamageCards() {
        val damageCard = cardCreationSystem.addDamageCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(damageCard))
        val healthComponent = HealthComponent(
            maxHealth = 1000f
        )
        owner add healthComponent

        val context = EffectContext(
            trigger = OnPlay,
            source = damageCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(healthComponent.getHealth() == 850f) { "Health should be 850f, but was ${healthComponent.getHealth()}" }
        val realDesc = "OnPlay:\nTake damage (150.0)"
        assert(desc == realDesc) { "Description should be\n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun `Activate trap card`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        owner add HealthComponent(10f)

        val context = EffectContext(
            trigger = OnPlay,
            source = trapCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert((owner get HealthComponent::class).getHealth() == 5f) { "Health should be 5, but was ${(owner get HealthComponent::class).getHealth()}" }
        val realDesc = "OnPlay:\n" +
                "Take (5.0) rising damage.\n" +
                "OnDeactivation:\n" +
                "Take (-30.0) rising score damage."
        assert(desc == realDesc) { "Description should be '$realDesc', but was '$desc'" }
    }

    @Test
    fun `Deactivate trap card`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        val  scoreComponent = ScoreComponent(100)
        owner add scoreComponent

        val context = EffectContext(
            trigger = OnDeactivation,
            source = trapCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(scoreComponent.getScore() == 70) { "Score should be 70, but was ${owner get ScoreComponent::class}" }
        val realDesc =
            "OnPlay:\n" + "Take (5.0) rising damage.\n" + "OnDeactivation:\n" + "Take (-30.0) rising score damage."
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }


    @Test
    fun `Activate and deactivate trap card multiple times`() {
        val trapCard = cardCreationSystem.addTrapTestCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        val hpComponent = HealthComponent(300f)
        owner add hpComponent
        val scoreComponent = ScoreComponent(2000)
        owner add scoreComponent

        val context = EffectContext(
            trigger = OnDeactivation,
            source = trapCard,
            target = owner,
        )
        val decontext = EffectContext(
            trigger = OnPlay,
            source = trapCard,
            target = owner,
        )

        repeat(10) {
            TriggerEffectHandler.handleTriggerEffect(context)
            TriggerEffectHandler.handleTriggerEffect(decontext)
        }

        assert(hpComponent.getHealth() == 25f) { "Health should be 25, but was ${hpComponent.getHealth()}" }
        assert(scoreComponent.getScore() == 350) { "Score should be 350, but was ${scoreComponent.getScore()}" }
    }

    @Test
    fun `Explode max hp trap once`() {
        val trapCard = cardCreationSystem.addMaxHpTrapCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        val hpComp = HealthComponent(100f)
        owner add hpComp

        MyRandom.random = FixedRandom(listOf(1))

        val context = EffectContext(
            trigger = OnPlay,
            source = trapCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        val maxi = hpComp.getMaxHealth()
        val hp = hpComp.getHealth()

        assert(hp == 50f) { "Health should be 50, but hp was $hp" }
        assert(maxi == 100f) { "Max health should be 100, but maxi was $maxi" }
        val realDesc = "OnPlay:\nGain (10.0) max health or might explode"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun `Activate max hp trap once`() {
        val trapCard = cardCreationSystem.addMaxHpTrapCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(trapCard))
        val hpComp = HealthComponent(100f)
        owner add hpComp

        MyRandom.random = FixedRandom(listOf(3))


        val context = EffectContext(
            trigger = OnPlay,
            source = trapCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        val maxi = hpComp.getMaxHealth()
        val hp = hpComp.getHealth()

        assert(hp == 100f) { "Health should be 100, but hp was $hp" }
        assert(maxi == 110f) { "Max health should be 110, but maxi was $maxi" }
        val realDesc = "OnPlay:\nGain (10.0) max health or might explode"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun addBeerGogglesTestCards() {
    }


    @Test
    fun addBasicScoreCards() {
        val basicScoreCard = cardCreationSystem.addBasicScoreCards(1).first()
        owner add LatestCardComponent(mutableIntStateOf(basicScoreCard))
        val scoreComponent = ScoreComponent(0)
        owner add scoreComponent

        val context = EffectContext(
            trigger = OnPlay,
            source = basicScoreCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(scoreComponent.getScore() == 10) { "Score should be 10, but was ${scoreComponent.getScore()}" }
        val realDesc = "OnPlay:\nGain (10.0) points"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }


    @Test
    fun addShuffleTestCards() {
    }

    @Test
    fun addShovelCards() {
    }

    @Test
    fun addHealingCards() {
        val healingCard = cardCreationSystem.addHealingCards(1).first()
        val healthComponent = HealthComponent(
            health = 100f,
            maxHealth = 1000f
        )
        owner add healthComponent

        val context = EffectContext(
            trigger = OnPlay,
            source = healingCard,
            target = owner,
        )
        val desc = TriggerEffectHandler.describe(context)
        TriggerEffectHandler.handleTriggerEffect(context)

        assert(healthComponent.getHealth() == 140f) { "Health should be 140f, but was ${healthComponent.getHealth()}" }
        val realDesc = "OnPlay:\nHeal (40.0)"
        assert(desc == realDesc) { "Description should be \n'$realDesc', but was \n'$desc'" }
    }

    @Test
    fun addScoreGainerTestCards() {
    }

    @Test
    fun `DeactivationTestCard OnPlay should not affect score or HP initially`() {
        val deactCard = cardCreationSystem.addDeactivationTestCards(1).first()
        owner add HealthComponent(1000f)
        owner add ScoreComponent(0)

        val context = EffectContext(
            trigger = OnPlay,
            source = deactCard,
            target = owner,
        )

        TriggerEffectHandler.handleTriggerEffect(context)

        assertEquals(0, (owner get ScoreComponent::class).getScore())
        assertEquals(1000f, (owner get HealthComponent::class).getHealth())
    }

    @Test
    fun `DeactivationTestCard OnDeactivation should reduce HP over time`() {
        val deactCard = cardCreationSystem.addDeactivationTestCards(1).first()
        owner add HealthComponent(1000f)
        owner add ScoreComponent(0)

        val context = EffectContext(
            trigger = OnDeactivation,
            source = deactCard,
            target = owner,
        )

        repeat(10) {
            TriggerEffectHandler.handleTriggerEffect(context)
        }

        assertEquals(945f, (owner get HealthComponent::class).getHealth())
        assertEquals(0, (owner get ScoreComponent::class).getScore())
    }


    @Test
    fun `DeactivationTestCard OnDeactivation, then OnActivation should give points`() {
        val deactCard = cardCreationSystem.addDeactivationTestCards(1).first()
        val hpComp = HealthComponent(1000f)
        owner add hpComp
        val scoreComp = ScoreComponent(0)
        owner add scoreComp

        val actContext = EffectContext(
            trigger = OnPlay,
            source = deactCard,
            target = owner,
        )

        val deActContext = EffectContext(
            trigger = OnDeactivation,
            source = deactCard,
            target = owner,
        )

        repeat(10) {
            TriggerEffectHandler.handleTriggerEffect(deActContext)
        }
        TriggerEffectHandler.handleTriggerEffect(actContext)

        assertEquals(1650, scoreComp.getScore())
        assertEquals(945f, hpComp.getHealth())
    }

    @Test
    fun `DeactivationTestCard OnDeactivation, OnActivation then OnActivation should not give points`() {
        val deactCard = cardCreationSystem.addDeactivationTestCards(1).first()
        val hpComp = HealthComponent(1000f)
        owner add hpComp
        val scoreComp = ScoreComponent(0)
        owner add scoreComp

        val actContext = EffectContext(
            trigger = OnPlay,
            source = deactCard,
            target = owner,
        )

        val deActContext = EffectContext(
            trigger = OnDeactivation,
            source = deactCard,
            target = owner,
        )


        repeat(10) {
            TriggerEffectHandler.handleTriggerEffect(deActContext)
        }
        TriggerEffectHandler.handleTriggerEffect(actContext)

        TriggerEffectHandler.handleTriggerEffect(actContext)

        assertEquals(1650, scoreComp.getScore())
        assertEquals(945f, hpComp.getHealth())
    }


    @Test
    fun addTempMultiplierTestCards() {
    }

    @Test
    fun addMerchantCards() {
    }

}