
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans.akka.solutions

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

  // you need to add the act method here to handle the messages
  def receive = {
    case s: String => log = s :: log
    case Reset => log = List.empty[String]
    case ListMessages => sender ! log
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

    def getCurrentResults(): List[String] =
      Await.result(logger ? ListMessages, timeout.duration) match {
        case l: List[String @unchecked] => l
        case _ => Nil
      }

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

  // you need to define the act method here to handle the buys and sells, and other stuff
  def receive = {
    case trade @ Buy(requested) =>
      if (quant >= requested) {
        quant -= requested
        log(trade)
      }
      else {
       logger ! "Insufficient Beans to sell"
      }
    case trade @ Sell(q) =>
      quant += q
      log(trade)
    case Quantity =>
      sender ! quant
  }
}

class TraderSpec extends FunSpec with Matchers with StopOnFirstFailure with SeveredStackTraces {
  describe("Trader") {
    val system = ActorSystem("Flight13Trader")
    val logger = system.actorOf(Props[Logger], name = "logger")
    val beanTrader = system.actorOf(Props(new Trader("Beans", 100, logger)))
    val porkBelliesTrader = system.actorOf(Props(new Trader("Pork Bellies", 100, logger)))
    implicit val timeout = Timeout(5 seconds)

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

  // Extra credit
  // Alter the logger so that instead of getting the log entries direct from the object it also uses an actor
  // case to request and receive the log. Add the request case class, and the case handler to the logger

  // Even more credit
  // We are using loop/react based actors for this, but we could use thread/receive based ones instead. If
  // you have time, convert the actors to use threads and receive instead, the tests should all pass unaltered.
  // How hard was that?

  // Super-mega-credit - doubtful that you will have time for this, but perhaps you will treat it as an
  // off-line exercise. The previous flight had a banking example, which used pattern-matching throughout. This
  // makes it ready (easy even) to adapt to use actors. You can change the implementation of the Account object
  // to be an actor, and adapt the tests to use message passing instead of direct calls to the methods.

}
