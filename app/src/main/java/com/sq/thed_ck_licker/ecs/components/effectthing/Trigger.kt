package com.sq.thed_ck_licker.ecs.components.effectthing

enum class Trigger {
    /**
     * This is to be used in cases where there is no trigger or the trigger is not relevant.
      */
    Blank,
    OnPlay, OnDraw, OnTurnStart, OnDeactivation, OnSpecial, OnCreation, OnDeath;
}