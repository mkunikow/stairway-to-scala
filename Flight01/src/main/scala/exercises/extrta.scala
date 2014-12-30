package exercises

/**
 * Created by michal on 12/28/14.
 */
object extrta extends App {
  //  extra credit
  def fib(v : Int) : Int = v match {
    case 0 => 0
    case 1 => 1
    case n => fib(n-1) + fib(n-2)
  }
  println(s"fib(3): ${fib(3)}")
}
