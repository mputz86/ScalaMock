// Copyright (c) 2011-2012 Paul Butcher
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.paulbutcher.test

import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec

class MockFunctionTest extends FreeSpec with MockFactory {
  
  autoVerify = false
  
  def repeat(n: Int)(what: => Unit) {
    for (i <- 0 until n)
      what
  }
  
  "Mock functions should" - {
    "have a sensible default name" in {
      val m = mockFunction[String]
      assertResult("unnamed MockFunction0"){ m.toString }
    }
    
    "have the name we gave them" - {
      "where we use a symbol" in {
        val m1 = mockFunction[String](Symbol("a mock function"))
        assertResult("a mock function"){ m1.toString }
      }

      "where we use a string" in {
        val m2 = mockFunction[String]("another mock function")
        assertResult("another mock function"){ m2.toString }
      }
    }
    
    "resolve ambiguity when taking a symbol argument" - {
      "with no name specified" in {
        val m1 = mockFunction[Symbol, String]
        assertResult("unnamed MockFunction1"){ m1.toString }
      }

      "with a name specified" in {
        val m2 = mockFunction[Symbol, String](functionName("a named mock"))
        assertResult("a named mock"){ m2.toString }
      }
    }

    "match literal arguments" in {
      withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects("foo", 42)
        m("foo", 42)
      }
    }
    
    "match wildcard arguments" in {
      withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects(*, 42)
        m("foo", 42)
      }
    }
    
    "match epsilon arguments" in {
      withExpectations {
        val m = mockFunction[String, Double, Int]
        m.expects("foo", ~1.0)
        m("foo", 1.0001)
      }
    }
    
    "fail if an expectation is not met" in {
      intercept[ExpectationException](withExpectations {
        val m = mockFunction[String, Int, Int]
        m.expects("foo", 42)
      })
    }
    
    "match arguments" in {
      withExpectations {
        val m = mockFunction[Int, Int, String]
        m.expects(where { _ < _ }).returning("less")
        m.expects(where { _ > _ }).returning("more")
        assertResult("less"){ m(1, 2) }
        assertResult("more"){ m(2, 1) }
      }
    }
  }
}
