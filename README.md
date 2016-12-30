# RACE-Client-Example

This project is an example of how to use [RACE](https://github.com/NASARace/race.git) as an external library, i.e.
how to add new actors without having to clone and modify the RACE repository itself.

The concrete example provides a `TLEActor` that reads a Two Line Element (TLE) satellite orbit specification from its
configuration data, uses the 3rd party [predict4java](https://github.com/badgersoftdotcom/predict4java) library to compute
(lat,lon,elevation) positions based on simulation time, and displays the result within a WorldWind viewer using the
generic RACE `FlightPosLayer`.

This is not intended to be the basis for a `race-space` module, which would use a more space-specific WorldWind layer
and a more efficient implementation for trajectory computation.

Apart from the `RaceActor` example in `src/main/scala/TLEActor`, the relevant source is the `build.sbt` example
that shows how to import the RACE modules used by this project. Note that while it is usually sufficient to 
add just the most specific RACE module (e.g. `race-ww-air`) as a `libraryDependency`, it is good style and more robust
to add all explicitly used RACE modules (starting with `race-core`).

To build, execute `sbt stage` from within the projects top directory

To run, execute `./race-client-example config/ISS.conf`