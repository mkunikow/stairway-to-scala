
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans

import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import org.scalatest.FunSpec
import org.scalatest.prop.PropertyChecks

/*
  This flight contains 3 different test classes. Please work on them in this order:

  1. Flight11DesignByContract
  2. Flight11TestDrivenDevelopment
  3. Flight11BehaviorDrivenDevelopment
*/
class Flight11BehaviorDrivenDevelopment extends FunSpec with Matchers with PropertyChecks with SeveredStackTraces {

  class Fraction(n: Int, d: Int) {

    require(d != 0, "denominator cannot be zero")

    val numer = if (d < 0) -1 * n else n
    val denom = d.abs

    override def toString = numer + " / " + denom
  }

  // Please replace each pending test with a body that
  // actually verifies the Fraction class behaves as specified
  describe("A Fraction") {
    describe("when a zero denominator is passed") {
      it("should throw IllegalArgumentException")(pending)
    }
    describe("when a positive denominator is passed") {
      it("should leave the numerator at the same sign")(pending)
      it("should leave the denominator positive")(pending)
    }
    describe("a negative denominator is passed") {
      it("change the sign of the numerator")(pending)
      it("change the sign of the denominator")(pending)
    }
  }

  // Now write a property based test of the same Fraction class
  describe("That same Fraction") {
    it("the denominator should always be normalized to positive") {
      forAll { (n: Int, d: Int) =>
        whenever (d != 0) {
          val frac = new Fraction(n, d)
          // Replace the assertion below with an assertion
          // that states frac's denominator should be greater than 0
          true should be (false)
        }
      }
    }

  }
}
