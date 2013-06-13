package com.github.jedesah.codesheet.api

import org.specs2.mutable._

class ScalaCodeSheetSpec extends Specification {
	"ScalaCodeSheet" should {
		"expressions" in {
			"literal" in {
				ScalaCodeSheet.computeResults("1") ==== List("1")
			}
			"complex" in {
				ScalaCodeSheet.computeResults("1 + 1") ==== List("2")
			}
			"two" in {
				"no newline" in {
					val code = """1 + 1
						| 4 * 4""".stripMargin
					ScalaCodeSheet.computeResults(code) ==== List("2", "16")
				}
				"with newline" in {
					val code = """1 + 1
						|
						| 4 * 4""".stripMargin
					ScalaCodeSheet.computeResults(code) ==== List("2", "", "16")
				}
			}
		}

		"funtion" in {
			"simple definition" in {
				val code = """def hello = "Hello!" """
				ScalaCodeSheet.computeResults(code) ==== List("hello => Hello!")
			}
			"complex definition" in {
				val code = "def foo = 10 * 4 - 2"
				ScalaCodeSheet.computeResults(code) ==== List("foo => 38")
			}
			"with use" in {
				val code = """def hello = "Hello!"
							|
							| hello""".stripMargin
				ScalaCodeSheet.computeResults(code) ==== List("hello => Hello!", "", "Hello!")
			}
			"with params" in {
				val code = "def foo(a: Int, b: Int, c: Int) = a + b - c"
				ScalaCodeSheet.computeResults(code) ==== List("foo(a = 3, b = 5, c = 7) => 1")
			}
		}
		
		"value definition" in {
			"simple" in {
				val code = "val a = 34"
				ScalaCodeSheet.computeResults(code) ==== List("a = 34")
			}
			"complex" in {
				val code = "val a = 2 * 3 - 2"
				ScalaCodeSheet.computeResults(code) ==== List("a = 4")
			}
			"with use" in {
				val code = """val a = 34
							| a + 10""".stripMargin
				ScalaCodeSheet.computeResults(code) ==== List("a = 34", "44")
			}
		}

		"class definition" in {
			"simple" in {
				val code = "class Car(model: String, year: Int)"
				ScalaCodeSheet.computeResults(code) ==== List("")
			}
			"with instantiation" in {
				val code = """case class Car(model: String, year: Int)
							| val test = Car("Fiat", 1999)""".stripMargin
				ScalaCodeSheet.computeResults(code) ==== List("", "test = Car(Fiat,1999)")
			}
			"with use" in {
				val code = """case class Car(model: String, year: Int)
							| val test = Car("Fiat", 1999)
							| test.model
							| test.year""".stripMargin
				ScalaCodeSheet.computeResults(code) ==== List("", "test = Car(Fiat,1999)", "Fiat", "1999")
			}
			"complex" in {
				val code = """case class Car(model: String, year: Int) {
							| 	def drive { println("vroum vroum")}
							| }
							| val a = new Car("BMW", 2013)
							| a.drive""".stripMargin
				ScalaCodeSheet.computeResults(code) ==== List("", "", "", "a = Car(BMW,2013)", "")
			}
		}
	}
}