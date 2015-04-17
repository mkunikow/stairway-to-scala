
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans.akka

/**
 * Created with IntelliJ IDEA.
 * User: dick
 * Date: 3/21/13
 * Time: 8:25 PM
 */

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import org.scalatest.FunSpec
import koans.support.BlankValues._
import koans.support.StopOnFirstFailure
import scala.collection._
import org.scalatest.SeveredStackTraces
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.Matchers
import akka.actor._
import scala.concurrent.duration._
import scala.concurrent.Await

// First we are going to create a Logger actor that takes a String message
// and adds it to a running list of log messages - starting out easy
// also make it clear the log on receipt of the Reset case class below

case object Reset
case object ListMessages

class Logger extends Actor {
  private[this] var log = List.empty[String]

  // you need to fill out the receive method to handle the messages
  // for receiving and logging a String message, resetting the log on a Reset,
  // and returning a list of messages on a ListMessage request
  def receive = {
    // put the implementation here and replace the line below
    case _ =>
  }
}


class LoggerSpec extends FunSpec with Matchers with StopOnFirstFailure with SeveredStackTraces {
  describe("Logger") {
    val system = ActorSystem("Flight13Logger")
    val logger = system.actorOf(Props[Logger], name = "logger")
    implicit val timeout = Timeout(5 seconds)

    it("should log messages passed in as Strings") {
      logger !"Hello, world!"
      logger ! "It appears to work"
    }


    // Fill in the method below to send a request to logger to get the
    // current messages and return them as a list of Strings, replacing the Nil
    // place holder here
    def getCurrentResults(): List[String] = Nil

    it("should contain the messages logged so far") {
      val results = getCurrentResults()
      results should contain ("Hello, world!")
      results should contain ("It appears to work")
    }

    it("should not contain other stuff") {
      val results = getCurrentResults()
      results.size should be (2)
      results.contains("Random crap") should be (false)
    }

    it("should reset when requested") {
      logger ! Reset
      Thread.sleep(20)  // let things settle
      getCurrentResults() should be ('empty)
    }

    it("should exit gracefully") {
      system.shutdown()
    }
  }
}

// Now, let's have some fun

// Below are some tests for a Trader actor, that trades something specified in the constructor and has a
// quantity also specified in the constructor, you can buy and sell a certain amount, but the buy
// will only be successful if there is a sufficient quantity (no short-selling on our trader).
// Uncomment the tests, and provide the case classes and trader actor to satisfy the tests below. Each trade
// should be logged with the Logger actor so we can check everything afterwards.
// Use a loop/react in the trader for now, and you can get the right format for the log entries in the specs
// (item + " " + Case class standard toString should be fine - you just need to log the errors in trading)

case class Buy(quantity: Int)
case class Sell(quantity: Int)
case object Quantity

class Trader(val item: String, q: Int, logger: ActorRef) extends Actor {
  private[this] var quant = q
  def quantity = quant
  implicit val timeout = Timeout(5 seconds)

  private def log(trade: Any) = logger ! (item + " " + trade)

  // you need to define the receive method here to handle the buys and sells, and other stuff
  def receive = {
    // fill in the receive method here.
    case _ =>
  }
}


// Uncomment the following test suite and make it run. You will need to
// create an actor system (use the name Flight13Trader) and a new actor for
// Logger with the name logger, as well as traders for Beans and Pork Bellies
// (these will need the constructor form of Akka actor properties to create
// them with the items they are trading (Beans and Pork Bellies) and their
// initial amounts (100 each), and also the loggers they will use (logger).
// Look at the actor system initializer for logger above for clues.
// You should also establish a 5 second timeout implicit for operations
// in order to get all this to compile.

/*class TraderSpec extends FunSpec with Matchers with StopOnFirstFailure with SeveredStackTraces {
  describe("Trader") {

    // establish the actor system, logger, Beans and PorkBellies traders here,
    // also the timeout

    def getCurrentMessages(): List[String] =
      Await.result(logger ? ListMessages, timeout.duration) match {
        case l: List[String @unchecked] => l
        case _ => Nil
      }

    def getQuantity(item: ActorRef): Int =
      Await.result(item ? Quantity, timeout.duration) match {
        case i: Int => i
        case _ => fail("didn't get an integer response")
      }

    it("should log all trades") {

      logger ! Reset
      beanTrader ! Buy(20)
      porkBelliesTrader ! Buy(30)
      porkBelliesTrader ! Sell(20)

      Thread.sleep(20)  // let things settle

      val messages = getCurrentMessages()
      messages should contain ("Beans Buy(20)")
      messages should contain ("Pork Bellies Buy(30)")
      messages should contain ("Pork Bellies Sell(20)")
    }

    it ("should keep a track of the item quantities") {
      getQuantity(beanTrader) should be (80)
      getQuantity(porkBelliesTrader) should be (90)
    }

    it ("should not sell more than it has") {
      logger ! Reset
      beanTrader ! Buy(200)

      Thread.sleep(20)  // let things settle

      getCurrentMessages() should contain ("Insufficient Beans to sell")

      // Quantities should be unaffected
      getQuantity(beanTrader) should be (80)
      getQuantity(porkBelliesTrader) should be (90)
    }

    // Instead of getting the quantity directly, which is not very actor-like, to prevent our actor being thrown
    // out of the actors guild, use the Quantity case object in a tuple with self to respond to a Quantity
    // request, so that the test below passes. Note how we request the result to the query
    it("should answer the quantity when requested") {
      val quantity = Await.result(beanTrader ? Quantity, timeout.duration)

      quantity should be (80)
    }

    it("should shut down properly") {
      system.shutdown()
    }
  }
}*/
