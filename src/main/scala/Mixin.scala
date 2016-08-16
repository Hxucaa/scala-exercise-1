/**
  * Created by Lance on 2016-08-14.
  */

private sealed class CircularRefChecker(private val seq: Seq[String]) {

  def this(element: String) {
    this(Seq(element))
  }

  def hasDuplicate(element: String) = seq.contains(element)

  def notHaveDuplicate(element: String) = !hasDuplicate(element)

  def add(element: String) = new CircularRefChecker(seq = seq :+ element)
}

object Mixin {

  private type OuterKey = String
  private type InnerKey = String
  private type InnerValue = String
  type Definitions = Seq[(OuterKey, Seq[(InnerKey, InnerValue)])]

  private val Mixin = "mixin"

  def expand(d: Definitions): Definitions = {

    for ((outerKey, sequence) <- d)
      yield (outerKey, expand2(d, new CircularRefChecker(element = outerKey), sequence))

//    d
//      // map values of outer sequence
//      .map { case (outerKey, seq) =>
//      val newSeq = expand2(d, new CircularRefChecker(element = outerKey), seq)
//
//      (outerKey, newSeq)
//    }
  }


  private def expand2(d: Definitions, outerKeyAcc: CircularRefChecker, seq: Seq[(InnerKey, InnerValue)]): Seq[(InnerKey, InnerValue)] = {
    seq
      .flatMap { case (innerKey, innerValue) =>

        innerKey match {
          case Mixin =>

//            val matchedSeq = for {
//              (outerKey, innerSeq) <- d
//              if outerKey == innerValue
//            }
//            yield {
//              require(outerKeyAcc.notHaveDuplicate(outerKey), "Circular reference")
//              expand2(d, outerKeyAcc.add(outerKey), innerSeq)
//            }
//
//            require(matchedSeq.nonEmpty, s"Improper definition. Must contain the key $innerValue")
//
//            matchedSeq.flatten

            val matchedSeq = d
              .filter(_._1 == innerValue)

            require(matchedSeq.nonEmpty, s"Improper definition. Must contain the key $innerValue")
            require(outerKeyAcc.notHaveDuplicate(innerValue), "Circular reference")

            matchedSeq
              .flatMap { case (o, s) =>
                expand2(d, outerKeyAcc.add(innerValue), s)
              }

          case _ => Seq(innerKey -> innerValue)
        }
      }
  }
}
