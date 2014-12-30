package exercises

/**
 * Created by michal on 12/28/14.
 */
object e3 extends App {

  //  3. In the interpreter, define a function that takes a string and an Int, and prints the string the Int number of times.
  def printNTimes(str: String, n: Int): Unit = (1 to n).foreach(x => println(str))
  printNTimes("print 3 times", 3)
}
