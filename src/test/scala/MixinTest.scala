/**
  * Created by Lance Zhu on 2016-08-15.
  */
import org.scalatest.{Matchers, WordSpec}


class MixinTest extends WordSpec with Matchers {

  "A Mapping" when {
    "mixing A -> B points to missing key" should {
      "throw IllegalArgumentException" in {
        val defs: Mixin.Definitions = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "User"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang")
        )

        a [IllegalArgumentException] should be thrownBy {
          Mixin.expand(defs)
        }
      }
    }

    "mixing A -> B" should {
      "produce correct result" in {
        val defs = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "Address"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang"),
          "Favourites" -> Seq("Food" -> "sushi", "drink" -> "vodka", "mixin" -> "Movie"),
          "Movie" -> Seq("SciFi" -> "Star Trek")
        )

        val result = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "Province" -> "Shanghai", "District" -> "Minhang"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang"),
          "Favourites" -> Seq("Food" -> "sushi", "drink" -> "vodka", "SciFi" -> "Star Trek"),
          "Movie" -> Seq("SciFi" -> "Star Trek")
        )
        Mixin.expand(defs) should equal(result)
      }
    }

    "mixing A -> C and B -> C" should {
      "produce correct result" in {
        val defs: Mixin.Definitions = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "Address"),
          "Home" -> Seq("about" -> "lol", "mixin" -> "Address"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang")
        )

        val result = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "Province" -> "Shanghai", "District" -> "Minhang"),
          "Home" -> Seq("about" -> "lol", "Province" -> "Shanghai", "District" -> "Minhang"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang")
        )

        Mixin.expand(defs) should equal(result)
      }
    }

    "recursive mixing A -> B and B -> C" should {
      "produce correct result" in {
        val defs: Mixin.Definitions = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "Address"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang", "mixin" -> "Favourites"),
          "Favourites" -> Seq("haha" -> "meme")
        )

        val result = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "Province" -> "Shanghai", "District" -> "Minhang", "haha" -> "meme"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang", "haha" -> "meme"),
          "Favourites" -> Seq("haha" -> "meme")
        )

        Mixin.expand(defs) should equal(result)
      }
    }

    "circular mixing" when {
      "referencing self" should {

        "throw IllegalArgumentException" in {
          val defs: Mixin.Definitions = Seq(
            "User" -> Seq("name" -> "oldpig", "gender" -> "male"),
            "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang", "mixin" -> "Address")
          )

          a [IllegalArgumentException] should be thrownBy {
            Mixin.expand(defs)
          }
        }
      }

      "A -> B and B -> A" should {
        "throw IllegalArgumentException" in {
          val defs: Mixin.Definitions = Seq(
            "User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "Address"),
            "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang", "mixin" -> "User")
          )

          a [IllegalArgumentException] should be thrownBy {
            Mixin.expand(defs)
          }
        }
      }

      "A -> B, B -> C and C -> A" should {
        "throw IllegalArgumentException" in {
          val defs: Mixin.Definitions = Seq(
            "User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "Address"),
            "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang", "mixin" -> "Favourites"),
            "Favourites" -> Seq("mixin" -> "User")
          )

          a [IllegalArgumentException] should be thrownBy {
            Mixin.expand(defs)
          }
        }
      }
    }

    "duplicate keys exist" should {
      "combine results or duplicating keys" in {

        val defs: Mixin.Definitions = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "mixin" -> "Address"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang"),
          "Address" -> Seq("Province" -> "Jiangsu", "District" -> "su")
        )

        val result = Seq(
          "User" -> Seq("name" -> "oldpig", "gender" -> "male", "Province" -> "Shanghai", "District" -> "Minhang", "Province" -> "Jiangsu", "District" -> "su"),
          "Address" -> Seq("Province" -> "Shanghai", "District" -> "Minhang"),
          "Address" -> Seq("Province" -> "Jiangsu", "District" -> "su")
        )

        Mixin.expand(defs) should equal(result)
      }
    }
  }
}

