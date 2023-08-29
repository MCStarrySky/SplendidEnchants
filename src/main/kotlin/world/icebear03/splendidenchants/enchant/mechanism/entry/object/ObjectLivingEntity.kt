package world.icebear03.splendidenchants.enchant.mechanism.entry.`object`

import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.function.submit
import taboolib.platform.util.groundBlock
import world.icebear03.splendidenchants.api.*
import world.icebear03.splendidenchants.enchant.mechanism.entry.internal.*
import java.util.*
import kotlin.math.pow

object ObjectLivingEntity : ObjectEntry<LivingEntity>() {

    override fun modify(
        obj: LivingEntity,
        cmd: String,
        params: List<String>
    ): Boolean {
        objEntity.modify(obj, cmd, params)
        println(cmd)
        when (cmd) {
            "施加药水效果" -> obj.effect(PotionEffectType.getByName(params[0])!!, params[1].calcToInt(), params[2].calcToInt())
            "霹雷" -> {
                obj.world.strikeLightningEffect(obj.location)
                obj.realDamage((params[0, "4.0"]).calcToDouble())
            }

            "伤害" -> {
                println(params[1])
                obj.damage(params[0].calcToDouble(), objPlayer.disholderize(params[1]))
            }

            "弹飞" -> {
                val height = params[0].calcToDouble()
                val y = 0.1804 * height - 0.0044 * height.pow(2) + 0.00004 * height.pow(3)
                val vector = obj.velocity.also { it.y = 0.0; it.add(vector(0, y, 0)) }
                submit {
                    obj.velocity = vector
                }
            }
        }
        return true
    }

    override fun get(from: LivingEntity, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "血量" -> objString.h(from.health)
            "最大血量" -> objString.h(from.maxHealth)
            "脚下方块" -> objBlock.holderize(from.blockBelow ?: from.groundBlock)
            else -> objEntity[from, objName]
        }
    }

    override fun holderize(obj: LivingEntity) = this to "${obj.uniqueId}"

    override fun disholderize(holder: String) = Bukkit.getEntity(UUID.fromString(holder)) as? LivingEntity
}