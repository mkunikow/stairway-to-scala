
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.BlankValues._

/*
  This flight contains 3 different test classes. Please work on them in this order:

  1. Flight11DesignByContract
  2. Flight11TestDrivenDevelopment
  3. Flight11BehaviorDrivenDevelopment
*/

// The class defined below is a bit contrived but it serves to illustrate design by contract.
// It is created with a color using String lookup from the color map, right now if the color name is
// not in the map, you get a NoSuchElementException. You can also set a highlight color on the instance
// which again is looked up in the colormap and again can result in a NoSuchElementException. This
// setter incidentally uses the set property syntax we covered in the slides.
//
// We can also ask the ColorPair instance for it's color pair, which is returned as a tuple of the
// two colors represented. Right now the class returns those colors whatever they are, even if the
// main color and the highlight are the same.
//
// In order to pass the tests below, you will need to add the following contracts to the class.
//
// 1. require that the color name passed in is in the color map (will cause an illegal argument exception if not)
// 2. assert that the highlight color, when set, is also in the color map - will cause an assert error if not
// 3. ensure (ensuring) that when returning the color pair property, the colors are different, if they are the
// same this will also cause an assertion error.

import java.awt.Color

object ColorPair {
  val colorMap = Map("Red" -> Color.RED, "Green" -> Color.GREEN, "Blue" -> Color.BLUE,
                   "Grey" -> Color.GRAY, "Yellow" -> Color.YELLOW, "Cyan" -> Color.CYAN)
}

class ColorPair(colorName: String) {
  val color = ColorPair.colorMap(colorName)
  private[this] var highlight: Color = _

  // property syntax for highlightColor
  def highlightColor_=(colorName: String) { highlight = ColorPair.colorMap(colorName) }
  def highlightColor = highlight

  // read only property syntax for getting the color pair tuple
  def colorPair = (color, highlight)
}

class Flight11DesignByContract extends FunSuite with Matchers with SeveredStackTraces {

  test("Scala asserts and requires") {

    // bad color should be caught by the require
    intercept[IllegalArgumentException] {
      val cm1 = new ColorPair("ELEPHANT")
    }

    // good color should be OK
    val cm2 = new ColorPair("Red")
    cm2.color should be (Color.RED)

    // now try pairing it with a valid color
    cm2.highlightColor = "Green"
    cm2.colorPair should be (Color.RED, Color.GREEN)

    // Pair it with the same color
    cm2.highlightColor = "Red"

    intercept[AssertionError] {
      cm2.colorPair should be (Color.RED, Color.RED) // no it shouldn't - this should fail the ensuring
    }

    // and with a nonsense color
    intercept[AssertionError] {
      cm2.highlightColor = "Goldenrod"
    }
  }
}
