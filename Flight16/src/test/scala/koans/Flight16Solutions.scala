
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans
import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.BlankValues._
import scala.collection.JavaConverters._
import support.KoanSuite
import koans.{ObtuseJavaLibSolution, ObtuseJavaLib}

class Flight16Solutions extends KoanSuite with Matchers with SeveredStackTraces {

  // using the techniques covered in the module slides, make the following tests pass (or uncomment them
  // and get them to compile and pass)
  test ("Convert null from Java to option") {
    val obtuse = new ObtuseJavaLib
    val c1 = Option(obtuse.getComplement("Fish"))
    val c2 = Option(obtuse getComplement "Horse")   // note use of inline style
    val c3 = Option(obtuse.getComplement("Chuck Norris"))

    c1 should be (Some("Chips"))
    c2 should be (Some("Cart"))
    c3 should be (None)
  }

  test ("Test if complements are 'null' in Java") {
    val obtuse = new ObtuseJavaLib
    val c1 = Option(obtuse.getComplement("Fish"))
    val c2 = Option(obtuse.getComplement("Horse"))
    val c3 = Option(obtuse.getComplement("Chuck Norris"))

    obtuse.isNull(c1.orNull) should be (false)
    obtuse.isNull(c2.orNull) should be (false)
    obtuse isNull c3.orNull should be (true)
  }

  test ("Apply function to list in Java") {
    val obtuse = new ObtuseJavaLib

    val l1 = obtuse.oneToTen

    // using the above list, and the obtuse doMathFuncOnList method, create a list that is the
    // squares of the one to ten list from java.

    val l2 = obtuse.doMathFuncOnList(l1, new ObtuseJavaLib.MathFunc {
      def apply(i: Int) = i * i
    })

    l2.asScala.toList should be (List(1, 4, 9, 16, 25, 36, 49, 64, 81, 100))
  }

  // complete the helper method applyFunc to use the ObtuseJavaLib.doMathFuncOnList so that you can uncomment the following
  // code which uses function literals rather than overriding the MathFunc interface, but don't cheat, really
  // use the doMathFuncOnList as dumb as that may seem.

  test ("Apply closures directly using the doMathFuncOnList method") {
    val obtuse = new ObtuseJavaLib

    // fix this method with a suitable implementation
    //def applyFunc(s: Seq[Int], fn: AnyRef): List[Int] = Nil

    def applyFunc(s: Seq[Int], fn: (Int) => Int) = {
      val al = s.toList.map(new java.lang.Integer(_))
      val mathFunc = new ObtuseJavaLib.MathFunc {
        def apply(i: Int) = fn(i)
      }
      obtuse.doMathFuncOnList(al.asJava, mathFunc).asScala.toList
    }

    applyFunc(List(1, 3, 5), (i: Int) => i + 10) should be (List(11, 13, 15))
    applyFunc(Array(6, 7, 8), (i: Int) => i * 2) should be (List(12, 14, 16))
  }

  test ("Use Option type from Java method") {
    val obtuse = new ObtuseJavaLibSolution

    // in the ObtuseJavaLib class, change the isNotDefined method so that the following tests pass
    // this will require you to provide a Some(string) for a non empty string, and a None for an empty string, in Java
    obtuse.isNotDefined("Fred") should be (false);
    obtuse.isNotDefined("") should be (true);
  }

  test ("Use a Scala trait from Java as a concrete instantiated interface") {
    // in the ObtuseJavaLib object, write a method makeMathObj that provides
    // a concrete implementation of the trait MathNum below so that these tests pass
    val obtuse = new ObtuseJavaLibSolution
    val mathObj = obtuse.makeMathObj(5);

    mathObj.add(5) should be (10)
    mathObj.sub(2) should be (3)
    mathObj.mul(3) should be (15)
    mathObj.div(2) should be (2)
  }
}

/* already defined in the exercises class
class TryOption {

  def notDefined(o1: Option[String]) =
    !o1.isDefined
}


trait MathNum {
  def add(a: Int): Int
  def sub(a: Int): Int
  def mul(a: Int): Int
  def div(a: Int): Int
}*/
