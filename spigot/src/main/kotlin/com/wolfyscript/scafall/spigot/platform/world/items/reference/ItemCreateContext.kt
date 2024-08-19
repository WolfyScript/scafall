package com.wolfyscript.scafall.spigot.platform.world.items.reference

import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*
import kotlin.Int

interface ItemCreateContext {
    fun amount(): Int

    fun reference(): Optional<StackReference> {
        return Optional.empty()
    }

    fun player(): Optional<Player> {
        return Optional.empty()
    }

    fun world(): Optional<World> {
        return Optional.empty()
    }

    /**
     * Builder to construct an ItemCreateContext
     *
     */
    interface Builder {
        fun reference(reference: StackReference): Builder

        fun player(player: Player): Builder

        fun world(world: World): Builder

        fun build(): ItemCreateContext

        class BuilderImpl(private val amount: Int) : Builder {
            private var reference: StackReference? = null
            private var player: Player? = null
            private var world: World? = null

            override fun reference(reference: StackReference): Builder {
                this.reference = reference
                return this
            }

            override fun player(player: Player): Builder {
                this.player = player
                return this
            }

            override fun world(world: World): Builder {
                this.world = world
                return this
            }

            override fun build(): ItemCreateContext {
                return object : ItemCreateContext {
                    override fun amount(): Int {
                        return amount
                    }

                    override fun reference(): Optional<StackReference> {
                        return Optional.ofNullable(reference)
                    }

                    override fun player(): Optional<Player> {
                        return Optional.ofNullable(player)
                    }

                    override fun world(): Optional<World> {
                        return Optional.ofNullable(world)
                    }
                }
            }
        }
    }

    companion object {
        /**
         * An empty implementation only containing the required values.
         *
         * @return An empty context only containing the required values
         */
        @JvmStatic
        fun empty(amount: Int): ItemCreateContext {
            return object  : ItemCreateContext {
                override fun amount(): Int = amount
            }
        }

        fun of(amount: Int): Builder {
            return Builder.BuilderImpl(amount)
        }

        fun of(reference: StackReference): Builder {
            return of(reference.amount()).reference(reference)
        }
    }
}
