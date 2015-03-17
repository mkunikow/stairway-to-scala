
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans
import java.util.{UUID, Date}
import org.scalatest.Matchers
import scala.collection._
import support.BlankValues._
import support.KoanSuite
import org.scalatest.SeveredStackTraces

class Flight09Solutions extends KoanSuite with Matchers with SeveredStackTraces {

  // Below is a fake database stateful entity called StatefulEntity that has two methods
  // on it, save and cancel that just print up what they do, (e.g. print a string Save or
  // Cancel) and always return true. It's not hard to stretch your imagination to where these
  // really use a database session to commit or abort, etc.
  // There is also a companion object which holds on to a map of IDs to stateful entities that
  // simulates the actual database. The save operation updates the entry for that ID in the
  // "database" and you can retrieve an object by its UUID.
  //
  // You could even think of this as a simple database mock object

  object StatefulEntity {
    val storedEntities = new mutable.HashMap[UUID, StatefulEntity]

    def store(entity: StatefulEntity) {
      storedEntities(entity.id) = entity
    }

    def findById(id: UUID): Option[StatefulEntity] = {
      storedEntities.get(id)
    }
  }

  class StatefulEntity {
    val id = UUID.randomUUID

    def save() = {
      println("Save")
      StatefulEntity.store(this)
      true
    }

    def cancel() = {
      println("Cancel")
      true
    }
  }


  // Fix the tests below, first of all
  test ("Stateful entity contract") {
    val se1 = new StatefulEntity
    val se2 = new StatefulEntity

    se1.id should equal (se1.id)
    se1.id should not equal (se2.id)

    StatefulEntity.findById(se1.id) should be (None)
    StatefulEntity.findById(se2.id) should be (None)

    se1.save() should be (true)
    se2.save() should be (true)

    StatefulEntity.findById(se1.id) should be (Some(se1))
    StatefulEntity.findById(se2.id) should be (Some(se2))

    se1.cancel() should be (true)
    se2.cancel() should be (true)
  }

  // Add a trait called CreatedUpdated which extends StatefulEntity but adds two new
  // date fields, one for the created date, and the other for the updated date (just use java.util.Date
  // for now). The behavior should be as follows:
  // Override the save() method so that the createdDate is set to now if it is not already set, or
  // left alone if it is set. The save method should update the updatedDate field any time save is called
  // whether it is set or not. The save method should still call the save() on StatefulEntity when it
  // has done it's work
  // cancel() does not need to update either date, hence doesn't need to be overridden
  // Also, the two date fields should be private, but exposed through a couple of accessor methods called
  // whenCreated and lastUpdated.
  //
  // Uncomment the tests below to make sure they work

  trait CreatedUpdated extends StatefulEntity {
    private var createdDate: Option[Date] = None
    private var updatedDate: Option[Date] = None

    override def save() = {
      val saved = super.save()
      if (saved) {
        val newDate = new Date()
        if (createdDate == None) createdDate = Some(newDate)
        updatedDate = Some(newDate)
      }
      saved
    }

    def whenCreated: Option[Date] = createdDate
    def lastUpdated: Option[Date] = updatedDate
  }

  test ("StatefulEntity with CreatedUpdated contract") {
    val se = new StatefulEntity with CreatedUpdated

    se.whenCreated should be (None)
    se.lastUpdated should be (None)

    se.cancel()

    se.whenCreated should be (None)
    se.lastUpdated should be (None)

    se.save()

    se.whenCreated should not be (None)
    se.lastUpdated should be (se.whenCreated)
    val created = se.whenCreated

    Thread.sleep(1001) // yuk - but it's easy
    se.save()
    se.whenCreated should be (created)
    se.lastUpdated should not be (created)

    val updated = se.lastUpdated

    Thread.sleep(1001)
    se.cancel()
    se.whenCreated should be (created)
    se.lastUpdated should be (updated)
  }

  // Add another trait, this time it should be called CreateOnly, and should override the save operation
  // to only save if the object has never been saved before - if the object has already been saved it
  // should not save the object, and should return false instead of true.
  //
  // Uncomment the tests to make sure it works

  trait CreateOnly extends StatefulEntity {
    var alreadySaved = false

    override def save() = {
      if (alreadySaved) false else {
        alreadySaved = true
        super.save()
      }
    }
  }


  test ("StatefulEntity with CreatedUpdated with CreateOnly") {
    val se = new StatefulEntity with CreateOnly with CreatedUpdated

    se.whenCreated should be (None)
    se.lastUpdated should be (None)

    se.save() should be (true)

    se.whenCreated should not be (None)
    se.lastUpdated should be (se.whenCreated)
    val created = se.whenCreated

    Thread.sleep(1001)

    se.save() should be (false)

    se.whenCreated should be (created)
    se.lastUpdated should be (created)
  }

  // Finally, let's create a new class for convenience, called StatefulEntityWithDateCreateOnly that
  // is based on the StatefulEntity and adds the two traits, so that they don't need to be composed
  // each time - the following tests should pass.
  class StatefulEntityWithDateCreateOnly extends StatefulEntity with CreatedUpdated with CreateOnly

  test ("StatefulEntityWithDateCreateOnly test") {
    val se = new StatefulEntityWithDateCreateOnly

    se.whenCreated should be (None)
    se.lastUpdated should be (None)

    se.save() should be (true)

    se.whenCreated should not be (None)
    se.lastUpdated should be (se.whenCreated)
    val created = se.whenCreated

    Thread.sleep(1001)

    se.save() should be (false)

    se.whenCreated should be (created)
    se.lastUpdated should be (created)
  }

  // Extra credit - if you alter the order in which the traits are applied in the above
  // StatefulEntityWithDateCreateOnly so that CreateOnly is first and CreatedUpdated second in the
  // class definition, the above unit test fails. Try it. Can you work out where it fails and why?
  // If you have time, fix the logic of the traits so the unit tests pass no matter what order the
  // traits are applied in.

}
