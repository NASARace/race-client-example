/*  Copyright (C) 2016 Peter C. Mehlitz
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package race.example.tle

import akka.actor.ActorRef
import com.typesafe.config.Config
import gov.nasa.race._
import gov.nasa.race.air.FlightPos
import gov.nasa.race.config.ConfigUtils._
import gov.nasa.race.core.Messages.RaceTick
import gov.nasa.race.core.{ContinuousTimeRaceActor, PeriodicRaceActor, PublishingRaceActor}
import gov.nasa.race.geo.LatLonPos
import gov.nasa.race.uom.Angle._
import gov.nasa.race.uom.Length._
import gov.nasa.race.uom.Speed._
import uk.me.g4dpz.satellite.{SatelliteFactory, TLE}

/**
  * example actor that reads in orbit specs in Two Line Element (TLE) format, and publishes
  * FlightPos objects with positions computed from the TLE
  */
class TLEActor (val config: Config) extends PublishingRaceActor with ContinuousTimeRaceActor with PeriodicRaceActor {

  val tle = new TLE(config.getStringArray("tle"))
  val satellite = SatelliteFactory.createSatellite(tle)

  override def onStartRaceActor(originator: ActorRef) = {
    ifTrue(super.onStartRaceActor(originator)){
      startScheduler
    }
  }

  override def handleMessage = {
    case RaceTick => publishPosition
  }

  def publishPosition = {
    val tNow = updatedSimTime

    satellite.calculateSatelliteVectors(tNow.toDate)
    val satPos = satellite.calculateSatelliteGroundTrack

    val fpos = FlightPos(tle.getCatnum.toString, tle.getName,
      LatLonPos(Radians(satPos.getLatitude), Radians(satPos.getLongitude)), Kilometers(satPos.getAltitude),
      MetersPerSecond(0), Degrees(0), // speed and heading don't really make sense, we would need a SatellitePos for that
      tNow
    )

    publish(fpos)
  }
}
