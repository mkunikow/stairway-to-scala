
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package labsoln {
  package awesome {
    package laser {
      class Gun(val wattage: Int) {
        class Beam(val lumens: Int)
        def shoot() = new Beam(wattage * 10)
      }
    }
  }
}