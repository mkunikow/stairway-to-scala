
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans

import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.BlankValues._
import support.KoanSuite

class Flight08Solutions extends KoanSuite with Matchers with SeveredStackTraces {

  abstract class Candy
  class Fudge extends Candy
  class ChocolateFudge extends Fudge

  koan("Fun with top types of Strings") {
    // fill in the blanks on the following tests with the expected results. Make sure to
    // understand why these are the case
    val mAndMs = "M&Ms"

    mAndMs.isInstanceOf[Any] should be (true)
    mAndMs.isInstanceOf[AnyRef] should be (true)

    mAndMs.isInstanceOf[String] should be (true)

    val mAndMsRef: AnyRef = mAndMs

    mAndMsRef.isInstanceOf[String] should be (true)     // why? Because this is a runtime test!
    mAndMsRef.isInstanceOf[AnyRef] should be (true)
    mAndMsRef.isInstanceOf[Any] should be (true)
  }

  koan("Fun with top types of values") {
    val lots: Any = 1000
    val less: Any = 100.0

    lots.isInstanceOf[Any] should be (true)
    lots.isInstanceOf[AnyRef] should be (true) // huh? Why? - because it is autoboxed in order to give it instanceOf implicitly :-)
    lots.isInstanceOf[Int] should be (true)

    less.isInstanceOf[Any] should be (true)
    less.isInstanceOf[AnyRef] should be (true)
    less.isInstanceOf[Double] should be (true)
    less.isInstanceOf[Int] should be (false)

    val lotsVal: AnyVal = 1000
    val lessVal: Any = less

    lotsVal.isInstanceOf[Int] should be (true)
    lessVal.isInstanceOf[Double] should be (true)
  }

  koan("Fun with top types of fudges") {
    val chocolateFudge = new ChocolateFudge
    val fudge = new Fudge

    fudge.isInstanceOf[Any] should be (true)
    fudge.isInstanceOf[AnyRef] should be (true)
    fudge.isInstanceOf[Candy] should be (true)
    fudge.isInstanceOf[Fudge] should be (true)
    fudge.isInstanceOf[ChocolateFudge] should be (false)

    chocolateFudge.isInstanceOf[Any] should be (true)
    chocolateFudge.isInstanceOf[AnyRef] should be (true)
    chocolateFudge.isInstanceOf[Candy] should be (true)
    chocolateFudge.isInstanceOf[Fudge] should be (true)
    chocolateFudge.isInstanceOf[ChocolateFudge] should be (true)
  }

  koan("Fun with bottom types") {
    val null1: Null = null

    null1.isInstanceOf[String] should be (false)
    null1.isInstanceOf[Candy] should be (false)
    null1.isInstanceOf[Fudge] should be (false)
    null1.isInstanceOf[ChocolateFudge] should be (false)

    // Why won't either of the the following lines compile
    // null1.isInstanceOf[Null] should be (true)  // like above - isInstanceOf is not defined on null, so it can't match
    // null1.isInstanceOf[Nothing] should be (false)  // and again, but this time for Nothing

    val null2: String = null
    val null3: Candy = null
    val null4: Fudge = null
    val null5: ChocolateFudge = null

    null2.isInstanceOf[String] should be (false)
    null3.isInstanceOf[Candy] should be (false)
    null4.isInstanceOf[Fudge] should be (false)
    null5.isInstanceOf[ChocolateFudge] should be (false)
  }

  koan("More fun with bottom types") {
    // this will compile (why), but fail with (not surprisingly) an exception when run
    // can you find a way to fix it without removing the code? (Think football!)
    def getMeNothing() = throw new IllegalStateException
    intercept[IllegalStateException] {
      val nothing: Nothing = getMeNothing
      // can you do anything useful with Nothing?
      // not really, other than compare it with nothing
    }
  }

  koan("==, !=, eq and ne") {
    val v1 = 100
    val v2 = 100
    val v3 = 101

    (v1 == v2) should be (true)
    (v1 != v3) should be (true)

    val f1 = new Fudge
    val f2 = new Fudge
    val f3 = new ChocolateFudge

    (f1 == f2) should be (false)
    (f1 eq f2) should be (false)
    (f1 != f3) should be (true)
    (f2 ne f3) should be (true)
  
    val l1 = List(1,2,3)
    val l2 = List(1,2,3)
    val l3 = List(2,3,4)

    (l1 == l2) should be (true)
    (l1 eq l2) should be (false)
    (l1 != l3) should be (true)
    (l1 ne l3) should be (true)
  }

}
