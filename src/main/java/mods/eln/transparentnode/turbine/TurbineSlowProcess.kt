package mods.eln.transparentnode.turbine

import mods.eln.sim.IProcess
import mods.eln.sim.PhysicalConstant
import mods.eln.sim.mna.component.PowerSource
import mods.eln.sound.SoundCommand
import mods.eln.sound.SoundLooper


class TurbineSlowProcess(internal val turbine: TurbineElement) : IProcess {
    internal var soundLooper = object : SoundLooper(turbine) {
        override fun mustStart(): SoundCommand? {
            val deltaT = turbine.warmLoad.Tc - turbine.coolLoad.Tc
            if (deltaT < 40) return null
            val factor = (deltaT / turbine.descriptor.nominalDeltaT).toFloat()
            val track = turbine.descriptor.sound.copy().mulVolume(1 * (0.1f * factor), 0.9f + 0.2f * factor)
            return track
        }
    }

    override fun process(time: Double) {
        soundLooper.process(time)
    }
}
