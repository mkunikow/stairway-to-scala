
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
class Flight11BehaviorDrivenDevelopmentSolutions extends FunSpec with Matchers with PropertyChecks with SeveredStackTraces {

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
      it("should throw IllegalArgumentException") {
        evaluating {
          new Fraction(1, 0)
        } should produce[IllegalArgumentException]
      }
    }
    describe("when a positive denominator is passed") {
      it("should leave the numerator at the same sign") {
        (new Fraction(2, 5)).numer should be(2)
        (new Fraction(0, 5)).numer should be(0)
        (new Fraction(-2, 5)).numer should be(-2)
      }
      it("should leave the denominator positive") {
        (new Fraction(2, 5)).denom should be(5)
        (new Fraction(0, 5)).denom should be(5)
        (new Fraction(-2, 5)).denom should be(5)
      }
    }
    describe("a negative denominator is passed") {
      it("change the sign of the numerator") {
        (new Fraction(2, -5)).numer should be(-2)
        (new Fraction(0, -5)).numer should be(0)
        (new Fraction(-2, -5)).numer should be(2)
      }
      it("change the sign of the denominator") {
        (new Fraction(2, -5)).denom should be(5)
        (new Fraction(0, -5)).denom should be(5)
        (new Fraction(-2, -5)).denom should be(5)
      }
    }
  }

  // Now write a property based test of the same Fraction class
  // Replace (pending) with a call to check, passing in the
  // property function.

  describe("That same Fraction") {
    it("the denominator should always be normalized to positive") {
      class Fraction(n: Int, d: Int) {

        require(d != 0, "denominator cannot be zero")
        require(d != Integer.MIN_VALUE, "denominator cannot be Integer.MIN_VALUE")

        val numer = if (d < 0) -1 * n else n
        val denom = d.abs

        override def toString = numer + " / " + denom
      }

      forAll { (n: Int, d: Int) =>
        whenever (d != 0 && d != Integer.MIN_VALUE) {
          (new Fraction(n, d)).denom should be > 0
        }
      }
    }
  }
}
