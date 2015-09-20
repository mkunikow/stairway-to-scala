
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.BlankValues._
import support.StopOnFirstFailure

class Flight14 extends FunSuite with Matchers with StopOnFirstFailure with SeveredStackTraces {

  // fun example drawn from real experience. From these sequences of DNA representations, we are going to
  // build up a list of sets containing the possible mutations at each position in the strings, like this:
  //
  // "GTAAGCTTAC"
  // "GACAGCT-AC"
  // "G-AACCTAAC"
  //
  // should end up with the following list of sets (short hand notation used for brevity)
  // ((G),(T,A,-),(A,C),(A),(G,C),(C),(T,-,A),(A),(C))
  // in other words, for these three strings, at each location, list the possibilities that can occur there.
  // We will build this up step by step
  //
  // First, define a method that takes two strings and zips them together
  //
  // if you need help converting a String to a list - have a look at the options in the REPL

  val seq1 = "GTAAGCTTAC"
  val seq2 = "GACAGCT-AC"
  val seq3 = "G-AACCTAAC"
  val seq4 = "C-AACCTAAC"

  val listOfSeqs = List(seq1, seq2, seq3, seq4)

  // can you find an easy way to convert a String to a list of Chars?
  def stringToChars(s1: String): List[Char] = s1.toCharArray.toList

  test("Convert a string to a list of chars") {
    stringToChars(seq1) should be (List('G','T','A','A','G','C','T','T','A','C'))
  }

  // Using the above knowledge, create a method that instead of just giving back individual chars in a list,
  // map the list of chars to a list of Sets of chars, each set having a single char in it (for now)
  def stringToSetOfChars(s1: String): List[Set[Char]] = stringToChars(s1) map(Set(_))

  test("Convert a string to a list of set of chars (each set with one char in it)") {
    stringToSetOfChars(seq1).toString should be ("List(Set(G), Set(T), Set(A), Set(A), Set(G), Set(C), Set(T), Set(T), Set(A), Set(C))")
  }

  // using what you know about converting a string to a list, and knowing that if you add something to a Set that
  // is already there, it doesn't add a new version, use zip to create a list of tuples out of a list of set of chars
  // (like that given above) and a list of chars from the string, and add those chars to the set so that all
  // variations are captured, but no duplicates.
  def combineZippedSetsAndString(s1: List[Set[Char]], seq: String): List[Set[Char]]
    = (s1 zip seq) map Function.tupled((s, c) => s + c)

  test("Combine list of sets with list of new chars") {
    val startSet = stringToSetOfChars(seq1)
    val nextSet = combineZippedSetsAndString(startSet, seq2)
    val finalSet = combineZippedSetsAndString(nextSet, seq3)
    finalSet.toString should be ("List(Set(G), Set(T, A, -), Set(A, C), Set(A), Set(G, C), Set(C), Set(T), Set(T, -, A), Set(A), Set(C))")
  }

  // let's put these together so that, given a list of sequence strings, we return a list of sets of chars with
  // every possible letter at that position, combining the stringToSetOfChars method for the head of the list
  // and the combineZippedSetsAndChars for the remaining elements of the list in turn. You can do this using the
  // foldLeft method demonstrated in the slides.
  def comboSetsForSequences(sequences: List[String]): List[Set[Char]]
    = (sequences foldLeft(stringToSetOfChars(sequences.head)))  ((l, s) => combineZippedSetsAndString(l,s))

  test("Find all combinations from sequences") {
    val allCombos = comboSetsForSequences(listOfSeqs)
    allCombos.toString should be ("List(Set(G, C), Set(T, A, -), Set(A, C), Set(A), Set(G, C), Set(C), Set(T), Set(T, -, A), Set(A), Set(C))")
  }


  // And now for something completely different
  val numbers = List(10, 3, 21, 7, 9, 13, 15, 10, 16, 2, 1, 8, 12)

  // first up, using what you know, write a method partitionByFirst that takes the first value of a list
  // of Ints, and partitions the rest of the list into two new lists, one with values lower than the first number,
  // and one with the rest. It should return three values, the first number, the list lower, and the remaining list
  // to satisfy the test below
  def partitionByFirst(numbers: List[Int]): (Int, List[Int], List[Int]) = {
    val (a, b) =  numbers.tail.partition(_ < numbers.head)
    (numbers.head, a, b)
  }

  test("Partition by first") {
    val (head, lower, remaining) = partitionByFirst(numbers)
    head should be (10)
    lower should be (List(3, 7, 9, 2, 1, 8))
    remaining should be (List(21, 13, 15, 10, 16, 12))
  }

  // now, using pattern matching, write a method called mysteryFunction that will
  // detect the following cases with the following outcomes:
  // empty list (Nil) => Nil
  // List of Ints => split list into head, lower and remaining using the technique above, then using recursion,
  // return the result of mysteryFunction(lower) ::: List(head) ::: mysteryFunction(remaining)
  // What have you just created? Check that it works in the test below

  def mysteryFunction(numbers: List[Int]): List[Int] = numbers match {
    case Nil => Nil
    case t :: h => {
      val (head, lower, remaining) = partitionByFirst(numbers)
      mysteryFunction(lower) ::: List(head) ::: mysteryFunction(remaining)
    }
  }

  test("Can you tell what it is yet") {
    val newList = mysteryFunction(numbers)
    newList should be (List(1, 2, 3, 7, 8, 9, 10, 10, 12, 13, 15, 16, 21))
  }

  // Extra credit exercises:
  //
  // Rename the mystery function and it's test with the real name for that algorithm, if you recognize it
  // The choice of the first number as pivot is not a good choice for a list that may already have some kind
  // of sort order, so if you have time, implement a new version that takes a number out of the center of the list
  // and uses that number to partition the rest of the list (without that chosen pivot). Write the function and a
  // suitable test to make sure it still works
  //
  // Going back to the previous example, where you used the foldLeft to create the combination of sequences, try
  // converting to the alternative notation of /: instead. Use the slides or the book for reference
}
