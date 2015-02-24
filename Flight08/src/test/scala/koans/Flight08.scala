
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans

import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.BlankValues._
import support.KoanSuite

class Flight08 extends KoanSuite with Matchers with SeveredStackTraces {

  abstract class Candy
  class Fudge extends Candy
  class ChocolateFudge extends Fudge

  koan("Fun with top types of Strings") {
    // fill in the blanks on the following tests with the expected results. Make sure to
    // understand why these are the case
    val mAndMs = "M&Ms"

    mAndMs.isInstanceOf[Any] should be (__)
    mAndMs.isInstanceOf[AnyRef] should be (__)

    mAndMs.isInstanceOf[String] should be (__)

    val mAndMsRef: AnyRef = mAndMs

    mAndMsRef.isInstanceOf[String] should be (__)     // why?
    mAndMsRef.isInstanceOf[AnyRef] should be (__)
    mAndMsRef.isInstanceOf[Any] should be (__)
  }

  koan("Fun with top types of values") {
    val lots: Any = 1000
    val less: Any = 100.0

    lots.isInstanceOf[Any] should be (__)
    lots.isInstanceOf[AnyRef] should be (__) // Huh? Why?
    lots.isInstanceOf[Int] should be (__)

    less.isInstanceOf[Any] should be (__)
    less.isInstanceOf[AnyRef] should be (__)
    less.isInstanceOf[Double] should be (__)
    less.isInstanceOf[Int] should be (__)

    val lotsVal: AnyVal = 1000
    val lessVal: Any = less

    lotsVal.isInstanceOf[Int] should be (__)
    lessVal.isInstanceOf[Double] should be (__)
  }

  koan("Fun with top types of fudges") {
    val chocolateFudge = new ChocolateFudge
    val fudge = new Fudge

    fudge.isInstanceOf[Any] should be (__)
    fudge.isInstanceOf[AnyRef] should be (__)
    fudge.isInstanceOf[Candy] should be (__)
    fudge.isInstanceOf[Fudge] should be (__)
    fudge.isInstanceOf[ChocolateFudge] should be (__)

    chocolateFudge.isInstanceOf[Any] should be (__)
    chocolateFudge.isInstanceOf[AnyRef] should be (__)
    chocolateFudge.isInstanceOf[Candy] should be (__)
    chocolateFudge.isInstanceOf[Fudge] should be (__)
    chocolateFudge.isInstanceOf[ChocolateFudge] should be (__)
  }

  koan("Fun with bottom types") {
    val null1: Null = null

    null1.isInstanceOf[String] should be (__)
    null1.isInstanceOf[Candy] should be (__)
    null1.isInstanceOf[Fudge] should be (__)
    null1.isInstanceOf[ChocolateFudge] should be (__)

    // Why won't either of the the following lines compile
    // null1.isInstanceOf[Null] should be (true)
    // null1.isInstanceOf[Nothing] should be (false)

    val null2: String = null
    val null3: Candy = null
    val null4: Fudge = null
    val null5: ChocolateFudge = null

    null2.isInstanceOf[String] should be (__)
    null3.isInstanceOf[Candy] should be (__)
    null4.isInstanceOf[Fudge] should be (__)
    null5.isInstanceOf[ChocolateFudge] should be (__)
  }

  koan("More fun with bottom types") {
    // this will compile (why), but fail with (not surprisingly) an exception when run
    // can you find a way to fix it without removing the code? (Think football!)
    def getMeNothing() = throw new IllegalStateException
    val nothing: Nothing = getMeNothing
    // can you do anything useful with Nothing?
  }

  koan("==, !=, eq and ne") {
    val v1 = 100
    val v2 = 100
    val v3 = 101

    (v1 == v2) should be (__)
    (v1 != v3) should be (__)

    val f1 = new Fudge
    val f2 = new Fudge
    val f3 = new ChocolateFudge

    (f1 == f2) should be (__)
    (f1 eq f2) should be (__)
    (f1 != f3) should be (__)
    (f2 ne f3) should be (__)
  
    val l1 = List(1,2,3)
    val l2 = List(1,2,3)
    val l3 = List(2,3,4)

    (l1 == l2) should be (__)
    (l1 eq l2) should be (__)
    (l1 != l3) should be (__)
    (l1 ne l3) should be (__)
  }

}
