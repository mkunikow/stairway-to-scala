
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans
import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.BlankValues._
import support.KoanSuite
import scala.collection._

class Flight07 extends KoanSuite with Matchers with SeveredStackTraces {

  // Using the object model on the last slide of Flight 7, and the instructions below, create
  // a class heirarchy that satisfies the tests below when they are uncommented

  // abstract class RollingStock should have an abstract field, name, of type String
  abstract class RollingStock {
    val name: String
  }

  // abstract class Car should subclass RollingStock and add a field carries of type String
  // and a method (with implementation) called pulled that returns a string formed of
  // name + " carrying " + carries
  abstract class Car extends RollingStock {
    val carries: String
    def pulled: String = name + " carrying " + carries
    override def toString = pulled
  }

  // concrete class PassengerCar should subclass Car, set the name field to be "Passenger car" and
  // the carries field to "people"
  class PassengerCar extends Car {
    override val carries: String = "people"
    override val name: String = "Passenger car"
  }

  // concrete class CargoCar should subclass Car, set the name field "Cargo car" and
  // the carries field to "cargo"
  class CargoCar extends Car {
    override val carries: String = "cargo"
    override val name: String = "Cargo car"
  }

  // now create an abstract Engine class that subclasses RollingStock and has the following:
  // a cars field with a mutable.ListBuffer of Cars
  // a method pull that returns a string consisting of name + " pulls " + the list of cars in the
  // ListBuffer using the pulled method to get the descriptive string of each car. The cars details should
  // be joined with " and ", and there should be no trailing " and " after the last car
  // an abstract maxCars field of type Int (that specifies the maximum number of cars that may
  // be added to the engine), and finally
  // an add method that takes a Car, and adds it to the ListBuffer *unless* that would exceed the
  // maximum number of Cars allowed in the maxCars field. If there are too many cars, throw an
  // IllegalStateException with a suitable message
  //
  // Note that mkString is an easy way to join a list of things, but by default it performs
  // a toString on each object, which doesn't return the string you want, either you will
  // need to define toString for the cars with some meaningful implementation (maybe forward 
  // to pulled) or else you will need to convert them all to the strings you want first.
  abstract class Engine extends RollingStock {
    val cars: mutable.ListBuffer[Car] = new mutable.ListBuffer()
    val pullActivity = "pulls"
    def pull:String = name + " " + pullActivity +" " + cars.mkString(" and ")
    val maxCars: Int
    def add(car: Car) = if (cars.size < maxCars) {cars += car; this} else throw new IllegalStateException()
  }

  // Create a concrete class SteamEngine that subclasses Engine, sets the name field to "Steam engine"
  // and the maxCars field to 3
  class SteamEngine extends Engine {
    override val maxCars: Int = 3
    override val name: String = "Steam engine"
  }

  // Create a concrete class DieselEngine that subclasses Engine, sets the name field to "Diesel engine"
  // and the maxCars field to 6
  class DieselEngine extends Engine {
    override val maxCars: Int = 6
    override val name: String = "Diesel engine"
  }

  // now uncomment the following tests (first comment block only) and run them to make sure they pass
   test ("2 passenger car steam train") {
    val steamEngine = new SteamEngine
    steamEngine.add(new PassengerCar)
    steamEngine.add(new PassengerCar)

    steamEngine.pull should be ("Steam engine pulls Passenger car carrying people and Passenger car carrying people")
  }

  test ("4 car steam train should throw exception") {
    val steamEngine = new SteamEngine
    steamEngine.add(new PassengerCar)
    steamEngine.add(new CargoCar)
    steamEngine.add(new PassengerCar)

    intercept[IllegalStateException] {
      steamEngine.add(new CargoCar)
    }

    steamEngine.pull should be ("Steam engine pulls Passenger car carrying people and " +
                                "Cargo car carrying cargo and Passenger car carrying people")
  }

  test ("5 car diesel train") {
    val dieselEngine = new DieselEngine
    dieselEngine.add(new CargoCar)
    dieselEngine.add(new PassengerCar)
    dieselEngine.add(new CargoCar)
    dieselEngine.add(new PassengerCar)
    dieselEngine.add(new CargoCar)

    dieselEngine.pull should be ("Diesel engine pulls Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo")
  }

  // now create a new class - ShuntEngine that overrides the pull method to print the string:
  // name + " doesn't pull, it pushes " and then the rest of the train description as before. It should
  // also provide the name as "Shunt engine" and the maxCars as 10 (shunt engines are strong :-) )
  // then uncomment the test below and make sure it passes
  class ShuntEngine extends Engine{
    override val maxCars: Int = 10
    override val pullActivity = "doesn't pull, it pushes"
    override val name: String = "Shunt engine"
  }

   test ("Shunt engine with 8 cars") {
    val shuntEngine = new ShuntEngine
    shuntEngine.add(new CargoCar)
    shuntEngine.add(new PassengerCar)
    shuntEngine.add(new CargoCar)
    shuntEngine.add(new PassengerCar)
    shuntEngine.add(new CargoCar)
    shuntEngine.add(new PassengerCar)
    shuntEngine.add(new CargoCar)
    shuntEngine.add(new PassengerCar)

    shuntEngine.pull should be ("Shunt engine doesn't pull, it pushes Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people")  
  }

  // now, let's make the train creation a little more readable with some factory methods. Create
  // an object called Engine with factory methods to create the different engine types (call the methods
  // diesel, steam and shunt) and a Car object with passenger and cargo methods, then uncomment
  // the test below and make sure it passes.

  object Engine {
    def diesel = new DieselEngine()
    def steam = new SteamEngine()
    def shunt = new ShuntEngine()

  }
  
  object Car {
    def passenger = new PassengerCar()
    def cargo = new CargoCar()
  }

   test ("Train factory") {
    val steamEngine = Engine.steam
    steamEngine add Car.passenger    // crafty use of infix operator syntax to make it read nicely
    steamEngine add Car.passenger

    steamEngine.pull should be ("Steam engine pulls Passenger car carrying people and Passenger car carrying people")

    val dieselEngine = Engine.diesel
    dieselEngine add Car.cargo
    dieselEngine add Car.passenger
    dieselEngine add Car.cargo
    dieselEngine add Car.passenger
    dieselEngine add Car.cargo

    dieselEngine.pull should be ("Diesel engine pulls Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo")

    val shuntEngine = Engine.shunt
    shuntEngine add Car.cargo
    shuntEngine add Car.passenger
    shuntEngine add Car.cargo
    shuntEngine add Car.passenger
    shuntEngine add Car.cargo
    shuntEngine add Car.passenger
    shuntEngine add Car.cargo
    shuntEngine add Car.passenger
    
    shuntEngine.pull should be ("Shunt engine doesn't pull, it pushes Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people and " +
                                 "Cargo car carrying cargo and " +
                                 "Passenger car carrying people")
  }

  // extra credit - alter the add method in the Engine abstract class to return the engine instance
  // at the end of the method, so that the cars can be added in a chain like this:
  //
  val steamTrain = Engine.steam add Car.cargo add Car.passenger add Car.cargo
  //
  // and write a test to make sure that it works, as expected, for all trains and carriages
  //
  // Why didn't that change break your existing tests?
  // You have effectively created a simple DSL for creating trains, how do you like that?

   test ("Chained train creation") {
    val steamEngine = Engine.steam add Car.cargo add Car.passenger add Car.passenger

    steamEngine.pull should be ("Steam engine pulls Cargo car carrying cargo and Passenger car carrying people and Passenger car carrying people")
    
  }

  // extra extra credit: there may be some repetition in your ShuntEngine implementation where it lists
  // out the cars in the overridden pull method. Refactor out the car string resolution into a separate
  // method and use that in both the Engine pull method and the overridden ShuntEngine pull method to
  // reduce the duplication. Make sure all of the tests still pass.
  //
  // If you already did this without being prompted, you get a gold star for being a smart-ass :-)
}
