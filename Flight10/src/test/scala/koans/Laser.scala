
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */
package lab.awesome.laser

class Gun(wattage: Int) {
  def shoot(): Beam = new Beam(10 * wattage)
  class Beam(val lumens: Int)
}

