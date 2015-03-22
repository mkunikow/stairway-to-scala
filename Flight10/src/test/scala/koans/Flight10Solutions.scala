
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans

import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.KoanSuite

class Flight10Solutions extends KoanSuite with Matchers with SeveredStackTraces {

  test ("Just in time imports") {

    // uncomment each of the tests below and add the necessary imports before them
    // to make them compile and pass. Above each is a hint in the form of the full package and class
    // it uses, but don't change the test, import the right package or class(es) instead
    // you should be able to fix each with just one import source line

    // java.sql.Date
    import java.sql
    (new sql.Date(110, 7, 12)).toString should be ("2010-08-12")

    // java.io.File and java.io.FileReader (don't forget the exception as well - in the same package)
    import java.io.{FileNotFoundException, FileReader, File}
    intercept[FileNotFoundException] {
      (new FileReader(new File("Hello.txt")))
    }

    // Figure this one out for yourself
    List(1,2,3) should be (List(1,2,3))

    // java.lang.Math to M
    import java.lang.{Math => M}
    M.PI should be (3.14159 +- 0.00001)

    // scala.collection.immutable.ListSet
    import scala.collection._
    (new immutable.ListSet[String]).isEmpty should be (true)

    // scala.collection.mutable.HashMap
    (new mutable.HashMap[String, Int]).put("One", 1) should be (None) // put returns the old value, if any

    // the previous two tests can actually be satisfied with just one import in a couple of different ways
    // so remove the import from the last test, and try some options with the previous import to make it
    // satisfy both tests
  }

  test ("Private scoping") {
    // alter the internals of the class after uncommenting the line in the equals method to make
    // this compile and the tests pass, you should not have to change the contents of the
    // equals or hashcode methods.

    class Complex(r: Int, i: Int) {
      private val rl = r
      private val im = i

      override def toString = "" + rl + " + " + im + "i"

      // uncomment out the line in the equals, make it work without changing the line, or the test
      override def equals(other: Any) = {
        other match {
          case c: Complex => (rl == c.rl) && (im == c.im)
          case _ => false
        }
      }

      override def hashCode() = this.rl * this.im
    }

    val c1 = new Complex(1, 2)
    val c2 = new Complex(1, 2)

    c1 should be (c2)
  }

  test ("Hamster in a wheel") {
    // add a hamster class inside of this class that can turn the wheel,
    // then make an instance of it called hammy that is accessible from outside
    // so that the tests below pass when you uncomment them,

    class ExerciseWheel {
      private def turn() = "Squeak, squeak, squeak"

      class Hamster {
        def run() = turn()
      }

      val hammy = new Hamster
    }

    val wheel = new ExerciseWheel
    wheel.hammy.run should be ("Squeak, squeak, squeak")
  }

  test("Shoot a LASER beam") {
    // in order to really demonstrate your understanding of packages and scoping, uncomment the
    // following tests, and then fill in the file Laser (look in the project panel on the left)
    // with the right package and class structure to make it
    // compile and run. Note that while it is a good idea to keep conventions going, you can put the file
    // in the same directory as this file, and still have the necessary package structure, from the top, to
    // make it all work. Note the use of _root_ in the imports to make sure we are going all the way to
    // the top when putting your file together.
    //
    // The Gun should take a single Int parameter "wattage" for the power, and when the shoot method
    // is called, should return a Gun.Beam with a value "lumens" that is calculated as 10 times the
    // wattage.

    import _root_.labsoln.awesome.laser

    val highPoweredLaser = new laser.Gun(100)
    val lowPoweredLaser = new laser.Gun(10)

    val highBeam: highPoweredLaser.Beam = highPoweredLaser.shoot()
    highBeam.lumens should be (1000)

    val lowBeam1: lowPoweredLaser.Beam = lowPoweredLaser.shoot()
    lowBeam1.lumens should be (100)

    val lowBeam2: lowPoweredLaser.Beam = lowPoweredLaser.shoot()
    lowBeam2.lumens should be (100) // should have same power
    lowBeam2 should not be (lowBeam1) // but should be separate beams
  }
}
